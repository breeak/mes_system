-- # 开启定时任务设置
-- SET GLOBAL event_scheduler = ON;
-- # 查看是否开启定时任务
-- SHOW VARIABLES LIKE 'event_scheduler';
-- declare 变量不能@开头
-- 如果存在这个就删掉
DROP PROCEDURE IF EXISTS createShift ;

# 创建存储过程  一批班次
DELIMITER //
CREATE PROCEDURE createShift(IN _shiftStartTime DATETIME, IN _shiftEndTime DATETIME, IN _shiftDate DATE, IN _shiftType VARCHAR(255), IN _shiftNow INT)
BEGIN

  DECLARE s INT DEFAULT 0;

  DECLARE _id INT;

  DECLARE _middleno VARCHAR(255);

  DECLARE _stationno INT;

  DECLARE _macCode VARCHAR(255);

  BEGIN

    -- 查询所有织机
    DECLARE mac_list CURSOR FOR SELECT id, macCode,middleno,stationno FROM mac_machine;

    -- 声明当游标遍历完后将标志变量置成某个值
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET s=1;

    -- 打开第一个游标
    OPEN mac_list;
    -- 将游标中的值赋值给变量，注意：变量名不要和返回的列名同名，变量顺序要和sql结果列的顺序一致
    FETCH mac_list INTO _id, _macCode,_middleno,_stationno;
    -- 当s不等于1，也就是未遍历完时，会一直循环
    WHILE s<>1 DO
    -- 执行业务逻辑  每台织机创建一条数据
    INSERT INTO mft_shift(macCode,shiftType,shiftStartTime,shiftEndTime,shiftDate,shiftNow,remark,updateTime,middleno,stationno)
    VALUES (_macCode,_shiftType,_shiftStartTime,_shiftEndTime,_shiftDate,_shiftNow,"SQL更新",now(),_middleno,_stationno);

    -- 将游标中的值再赋值给变量，供下次循环使用
    FETCH mac_list INTO _id, _macCode,_middleno,_stationno;
    -- 当s等于1时表明遍历以完成，退出循环
    END WHILE;
    -- 关闭游标
    CLOSE mac_list;
  END;
END; //
DELIMITER ;

-- --------------------------------
DROP PROCEDURE IF EXISTS piciShift;

# 创建存储过程  生成某一天的班次
DELIMITER //
CREATE PROCEDURE piciShift()
BEGIN

  DECLARE s INT DEFAULT 0;

  DECLARE _dict_label VARCHAR(255);
  DECLARE _dict_value VARCHAR(255);
  DECLARE _dict_value1 VARCHAR(255);
  DECLARE _shiftDate DATE;
  DECLARE _shiftDateStr VARCHAR(255);
  DECLARE _shiftDate2 DATE;
  DECLARE _shiftDateStr2 VARCHAR(255);
  DECLARE _shiftType VARCHAR(255);
  DECLARE _shiftStartTime DATETIME;
  DECLARE _shiftEndTime DATETIME;

  -- 先查询 班次字典安排
  -- 查询班次字典安排
  DECLARE shift_list CURSOR FOR SELECT dict_label, dict_value FROM sys_dict_data WHERE  dict_type = "mac_common_shift" ORDER BY dict_sort;
  -- 声明当游标遍历完后将标志变量置成某个值
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET s=1;

  SET _shiftDate =DATE_ADD(NOW(), INTERVAL 1 DAY);
  SET _shiftDateStr =  DATE_FORMAT(_shiftDate,'%Y-%m-%d');
  SET _shiftDate2 =DATE_ADD(NOW(), INTERVAL 2 DAY);
  SET _shiftDateStr2 =  DATE_FORMAT(_shiftDate2,'%Y-%m-%d');
  -- 打开第一个游标
  OPEN shift_list;
  -- 将游标中的值赋值给变量，注意：变量名不要和返回的列名同名，变量顺序要和sql结果列的顺序一致
  FETCH shift_list INTO _dict_label, _dict_value;
  -- 执行业务逻辑  第一次更新
  SET _dict_value1 = _dict_value;
  SET _shiftType = _dict_label;
  SET _shiftStartTime = STR_TO_DATE(CONCAT(_shiftDateStr," ",_dict_value),'%Y-%m-%d %H:%i:%s');
  -- 第二次循环
  FETCH shift_list INTO _dict_label, _dict_value;
  SET _shiftEndTime = STR_TO_DATE(CONCAT(_shiftDateStr," ",_dict_value),'%Y-%m-%d %H:%i:%s');
  CALL createShift(_shiftStartTime, _shiftEndTime,_shiftDate, _shiftType,2) ;

  SET _shiftType = _dict_label;
  SET _shiftStartTime = STR_TO_DATE(CONCAT(_shiftDateStr," ",_dict_value),'%Y-%m-%d %H:%i:%s');

  -- 第三次取值 取不出来了 那就是两班倒
  IF s != 1 THEN
    -- 第三次循环
    FETCH shift_list INTO _dict_label, _dict_value;
    SET _shiftEndTime = STR_TO_DATE(CONCAT(_shiftDateStr," ",_dict_value),'%Y-%m-%d %H:%i:%s');
    CALL createShift(_shiftStartTime, _shiftEndTime,_shiftDate, _shiftType,2) ;
    -- 增加 最后一个班次
    SET _shiftType = _dict_label;
    SET _shiftStartTime = STR_TO_DATE(CONCAT(_shiftDateStr," ",_dict_value),'%Y-%m-%d %H:%i:%s');
  END IF;
  -- 计算结束时间
  SET _shiftEndTime = STR_TO_DATE(CONCAT(_shiftDateStr2," ",_dict_value1),'%Y-%m-%d %H:%i:%s');
  CALL createShift(_shiftStartTime, _shiftEndTime,_shiftDate, _shiftType,2) ;
  -- 关闭游标
  CLOSE shift_list;
END; //
DELIMITER ;

-- --------------------------------
DROP EVENT IF EXISTS doScheduleShift;
# 创建 定时任务 调用存储过程

CREATE EVENT doScheduleShift
  ON SCHEDULE EVERY 1 DAY STARTS DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 1 DAY), INTERVAL 23 HOUR)
  ON COMPLETION PRESERVE
  DO CALL piciShift();