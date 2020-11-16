-- # 开启定时任务设置
-- SET GLOBAL event_scheduler = ON;
-- # 查看是否开启定时任务
-- SHOW VARIABLES LIKE 'event_scheduler';

-- declare 变量不能@开头
-- 如果存在这个就删掉
DROP PROCEDURE if exists updateShaft;

# 创建存储过程
DELIMITER //
CREATE PROCEDURE updateShaft()
BEGIN

  DECLARE s int DEFAULT 0;
  DECLARE _id INT;
  DECLARE _shaftLength NUMERIC(10,2);
  DECLARE _shaftRemainLength NUMERIC(10,2);
  DECLARE _clothLength NUMERIC(10,2);
  DECLARE _actStart datetime;
  DECLARE _planEnd datetime;
  DECLARE _planTime BIGINT;
  DECLARE _during BIGINT;

  -- 定义游标，并将sql结果集赋值到游标中
  DECLARE shaft_list CURSOR FOR SELECT shaftLength, shaftRemainLength, clothLength, actStart, planEnd, id FROM mft_shaft WHERE shaftStatus ="已上轴";
  -- 声明当游标遍历完后将标志变量置成某个值
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET s=1;

  open shaft_list;
  -- 将游标中的值赋值给变量，注意：变量名不要和返回的列名同名，变量顺序要和sql结果列的顺序一致
  fetch shaft_list into _shaftLength, _shaftRemainLength, _clothLength,_actStart, _planEnd, _id;
  -- 当s不等于1，也就是未遍历完时，会一直循环
  while s<>1 do
  -- 执行业务逻辑

    -- 计算已经进行了多久
    Set _during = (unix_timestamp(now())-(unix_timestamp(_actStart)));

    Set _shaftRemainLength = _shaftLength - _clothLength;

    Set _planTime = (_during*_shaftLength)/_clothLength;

    Set _planEnd = from_unixtime(unix_timestamp(_actStart) + _planTime);

    update mft_shaft set shaftRemainLength = _shaftRemainLength,planEnd=_planEnd, updateTime =now() where id = _id;


    -- 将游标中的值再赋值给变量，供下次循环使用
  fetch shaft_list into _shaftLength, _shaftRemainLength, _clothLength,_actStart, _planEnd, _id;
  -- 当s等于1时表明遍历以完成，退出循环
  end while;

  -- 关闭游标
  close shaft_list;

END //
DELIMITER ;

DROP EVENT if exists doScheduleShaft;
# 创建 定时任务 调用存储过程
CREATE EVENT doScheduleShaft ON SCHEDULE EVERY 181 SECOND
  ON COMPLETION PRESERVE
  DO CALL updateShaft();


# 临时关闭事件
# ALTER EVENT doSchedule DISABLE;

# 删除创建的事件
# DROP EVENT doSchedule;
