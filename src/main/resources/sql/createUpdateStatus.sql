-- # 开启定时任务设置
-- SET GLOBAL event_scheduler = ON;
-- # 查看是否开启定时任务
-- SHOW VARIABLES LIKE 'event_scheduler';

-- declare 变量不能@开头
-- 如果存在这个就删掉
DROP PROCEDURE if exists updateDataStatus;

# 创建存储过程
DELIMITER //
CREATE PROCEDURE updateDataStatus()
BEGIN

  DECLARE s int DEFAULT 0;

  DECLARE _id INT;

  DECLARE _macSpeed INT;

  DECLARE _macStatus INT;

  DECLARE _updateTime DATETIME;

  DECLARE _middleno varchar(255);

  DECLARE _stationno INT;


  BEGIN

    -- 查询最新实时情况
    -- 定义游标，并将sql结果集赋值到游标中
    DECLARE mac_list CURSOR FOR SELECT id, rpm, state, lastdate,middleno,stationno FROM allrealtime;

    -- 声明当游标遍历完后将标志变量置成某个值
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET s=1;


    -- 打开第一个游标
    open mac_list;
    -- 将游标中的值赋值给变量，注意：变量名不要和返回的列名同名，变量顺序要和sql结果列的顺序一致
    fetch mac_list into _id, _macSpeed, _macStatus, _updateTime, _middleno, _stationno;
    -- 当s不等于1，也就是未遍历完时，会一直循环
    while s<>1 do
    -- 执行业务逻辑

    -- 离线判断 如果上次更新距这次超过5分钟未有数据上传定义为离线
    if(unix_timestamp(_updateTime) < (unix_timestamp(now())-300)) THEN
      SET _macStatus = 11;
    end if;

    -- 更新实时表
    UPDATE mac_machine set macSpeed= _macSpeed, macStatus = _macStatus, updateTime = _updateTime where  middleno = _middleno and stationno=_stationno  ;


    -- 将游标中的值再赋值给变量，供下次循环使用
    fetch mac_list into _id, _macSpeed, _macStatus, _updateTime, _middleno, _stationno;
    -- 共下次使用时把变量全部初始化

    end while;
    -- 关闭游标
    close mac_list;
  END;
END //
DELIMITER ;
--
DROP EVENT if exists doScheduleStatus;
# 创建 定时任务 调用存储过程
CREATE EVENT doScheduleStatus ON SCHEDULE EVERY 60 SECOND
  ON COMPLETION PRESERVE
  DO CALL updateDataStatus();

# 临时关闭事件
# ALTER EVENT doSchedule DISABLE;

# 删除创建的事件
# DROP EVENT doSchedule;
