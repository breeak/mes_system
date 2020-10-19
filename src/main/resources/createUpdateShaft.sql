-- # 开启定时任务设置
-- SET GLOBAL event_scheduler = ON;
-- # 查看是否开启定时任务
-- SHOW VARIABLES LIKE 'event_scheduler';

-- declare 变量不能@开头
-- 如果存在这个就删掉
DROP PROCEDURE if exists updateData;

# 创建存储过程
DELIMITER //
CREATE PROCEDURE updateData(In _during INT)
BEGIN

  DECLARE s int DEFAULT 0;

  DECLARE _id INT;

  DECLARE _middleno varchar(255);

  DECLARE _stationno INT;

  DECLARE _macSpeed INT;

  DECLARE _macStatus INT;

  DECLARE _starttime BIGINT;

  DECLARE _endtime BIGINT;

  DECLARE _dusec INT;

  DECLARE _state INT;

  DECLARE _output INT;

  DECLARE _weftDensity INT;

  DECLARE _updateTime DATETIME;

  DECLARE _shiftStartTime DATETIME;

  DECLARE _macCode varchar(255);

  DECLARE _macSpeedAverage NUMERIC(10,2);

  DECLARE _macEfficiency NUMERIC(10,2);

  DECLARE _runTime BIGINT ;

  DECLARE _stopTime BIGINT;

  DECLARE _warpStopTime BIGINT;

  DECLARE _weftStopTime BIGINT;

  DECLARE _otherStopTime BIGINT;

  DECLARE _warpStopNum INT;

  DECLARE _weftStopNum INT;

  DECLARE _otherStopNum INT;

  DECLARE _pickNum BIGINT;

  DECLARE _shiftLength NUMERIC(20,2);

  DECLARE _shaftCode varchar(255);

  DECLARE _weavingLength NUMERIC(20,2);

  DECLARE _statusChangeNum BIGINT;

  DECLARE _uploadNum INT;

  BEGIN

    -- 查询最新实时情况
    -- 定义游标，并将sql结果集赋值到游标中
    DECLARE mac_list CURSOR FOR SELECT id, rpm, state, lastdate,middleno,stationno FROM allrealtime WHERE lasttime > (unix_timestamp(now())-_during) ;	-- TODO 改成大于

    -- 定义第二个游标 把值设为变量即可
    DECLARE shift_list CURSOR FOR SELECT dusec, state, output, starttime, endtime FROM alldata WHERE  endtime > (unix_timestamp(now())-_during) and middleno = _middleno and stationno = _stationno; -- TODO 改成大于

    -- 声明当游标遍历完后将标志变量置成某个值
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET s=1;

    -- 打开第一个游标
    open mac_list;
    -- 将游标中的值赋值给变量，注意：变量名不要和返回的列名同名，变量顺序要和sql结果列的顺序一致
    fetch mac_list into _id, _macSpeed, _macStatus, _updateTime, _middleno, _stationno;
    -- 当s不等于1，也就是未遍历完时，会一直循环
    while s<>1 do
      -- 执行业务逻辑
      -- 查询当前实时情况
      SELECT macCode, weftDensity, weavingLength,shaftCode,macEfficiency into _macCode, _weftDensity, _weavingLength, _shaftCode, _macEfficiency  FROM mac_machine where id = _id;

      -- 查询当前班次情况
      SELECT   stopTime, runTime, warpStopTime, weftStopTime, otherStopTime, warpStopNum, weftStopNum, otherStopNum, pickNum, shiftLength, statusChangeNum, macSpeed, uploadNum, shiftStartTime
               INTO   _stopTime, _runTime, _warpStopTime, _weftStopTime, _otherStopTime, _warpStopNum, _weftStopNum, _otherStopNum, _pickNum, _shiftLength, _statusChangeNum, _macSpeedAverage, _uploadNum, _shiftStartTime
      FROM mft_shift
      WHERE shiftNow = 1 AND  macCode = _macCode;

      begin
        -- 查询最新班次情况 使用第二个游标

        open shift_list;
        -- 将游标中的值赋值给变量，注意：变量名不要和返回的列名同名，变量顺序要和sql结果列的顺序一致
        fetch shift_list into _dusec, _state, _output, _starttime, _endtime;
        -- 当s不等于1，也就是未遍历完时，会一直循环
        while s<>1 do
            -- 执行相关班次的更新
            -- 判断是否刚好在某个班次点 那么 dusec 要只算这半部分的 之前剩下的那部分由程序后台加上  次数算在这里
            if (_starttime< UNIX_TIMESTAMP(_shiftStartTime) and UNIX_TIMESTAMP(_shiftStartTime)<_endtime) then
              set _dusec = _endtime - UNIX_TIMESTAMP(_shiftStartTime);
            end if;
            -- 判断传输过来的状态
            -- 运行
            if (_state = 30) THEN
              SET _runTime = _runTime + _dusec;
              SET _shiftLength = _shiftLength + (_output * 10)/_weftDensity;
              SET _weavingLength = _weavingLength + (_output * 10)/_weftDensity;
              SET _pickNum = _pickNum + _output;
            end if;

            -- 经停
            if (_state = 15) THEN
              SET _stopTime = _stopTime + _dusec;
              SET _warpStopTime = _warpStopTime + _dusec;
              SET _warpStopNum = _warpStopNum + 1;
            end if;

            -- 纬停
            if (_state = 23) THEN
              SET _stopTime = _stopTime + _dusec;
              SET _weftStopTime = _weftStopTime + _dusec;
              SET _weftStopNum = _weftStopNum + 1;
            end if;

            -- 其他停
            if(_state = 31) THEN
              SET _stopTime = _stopTime + _dusec;
              SET _otherStopTime = _otherStopTime + _dusec;
              SET _otherStopNum = _otherStopNum + 1;
            end if;

            -- 记录更新次数
            SET _statusChangeNum =_statusChangeNum + 1;

            -- 更新效率

            if((_runTime + _stopTime) > 0) THEN

              SET _macEfficiency = CAST(_runTime AS FLOAT)*100 /(_runTime + _stopTime);

            end if;
            -- 将游标中的值再赋值给变量，供下次循环使用
            fetch shift_list into _dusec, _state, _output, _starttime, _endtime;
        -- 当s等于1时表明遍历以完成，退出循环
        end while;
        -- 关闭游标
        close shift_list;

        -- 第二个游标结束后 一定要设置第一个游标的handler为0 要不然会直接退出第一个循环
        set s = 0;

      end;
      -- 计算平均车速
      if (_uploadNum = 0) then
        Set _macSpeedAverage = _macSpeed;
      elseif (_uploadNum > 0) then
        Set _macSpeedAverage =( _macSpeed + _macSpeedAverage * _uploadNum)/(_uploadNum+1);
      end if;

      Set _uploadNum = _uploadNum + 1;

      -- 更新实时表
      UPDATE mac_machine set macSpeed= _macSpeed, macStatus = _macStatus, macEfficiency = _macEfficiency,weavingLength = _weavingLength, updateTime = now() where id = _id  ;  -- TODO  updateTime 要改

      -- 更新班次表

      UPDATE mft_shift SET

                         macEfficiency = _macEfficiency,runTime=_runTime, stopTime=_stopTime, warpStopTime=_warpStopTime, weftStopTime=_weftStopTime, otherStopTime=_otherStopTime, warpStopNum=_warpStopNum, weftStopNum=_weftStopNum, otherStopNum=_otherStopNum, pickNum=_pickNum, shiftLength=_shiftLength, statusChangeNum=_statusChangeNum, updateTime = now(),
                         macSpeed = _macSpeedAverage, uploadNum = _uploadNum
      WHERE shiftNow = 1 and macCode = _macCode; -- 跟新当前班次的情况

      -- 更新织轴表
      UPDATE mft_shaft set clothLength= _weavingLength where shaftCode = _shaftCode  ;



      -- 将游标中的值再赋值给变量，供下次循环使用
      fetch mac_list into _id, _macSpeed, _macStatus, _updateTime, _middleno, _stationno;
      -- 当s等于1时表明遍历以完成，退出循环
      end while;
      -- 关闭游标
      close mac_list;
    END;
END //
DELIMITER ;

DROP EVENT if exists doSchedule;
# 创建 定时任务 调用存储过程
CREATE EVENT doSchedule ON SCHEDULE EVERY 30 SECOND
  ON COMPLETION PRESERVE
  DO CALL updateData(30);

# 临时关闭事件
# ALTER EVENT doSchedule DISABLE;

# 删除创建的事件
# DROP EVENT doSchedule;
