-- # 开启定时任务设置
-- SET GLOBAL event_scheduler = ON;
-- # 查看是否开启定时任务
-- SHOW VARIABLES LIKE 'event_scheduler';

-- declare 变量不能@开头
-- 如果存在这个就删掉
DROP PROCEDURE if exists updateData;

# 创建存储过程
DELIMITER //
CREATE PROCEDURE updateData()
BEGIN

  DECLARE s int DEFAULT 0;

  DECLARE _id INT;

  DECLARE _middleno varchar(255);

  DECLARE _stationno INT;

  DECLARE _macSpeed INT;

  DECLARE _macStatus INT;

  DECLARE _starttime BIGINT;

  DECLARE _endtime BIGINT;

  DECLARE _dusec INT DEFAULT 0;

  DECLARE _state INT;

  DECLARE _output INT DEFAULT 0;

  DECLARE _weftDensity INT;

  DECLARE _updateTime DATETIME;

  DECLARE _updateTime2 DATETIME;

  DECLARE _shiftStartTime DATETIME;

  DECLARE _macCode varchar(255);

  DECLARE _macSpeedAverage NUMERIC(10,2) DEFAULT 0;

  DECLARE _macEfficiency NUMERIC(10,2) DEFAULT 0;

  DECLARE _runTime BIGINT DEFAULT 0;

  DECLARE _stopTime BIGINT DEFAULT 0;

  DECLARE _warpStopTime BIGINT DEFAULT 0;

  DECLARE _weftStopTime BIGINT DEFAULT 0;

  DECLARE _otherStopTime BIGINT DEFAULT 0;
  DECLARE _offLineTime BIGINT DEFAULT 0;

  DECLARE _warpStopNum INT DEFAULT 0;

  DECLARE _weftStopNum INT DEFAULT 0;

  DECLARE _otherStopNum INT DEFAULT 0;

  DECLARE _pickNum BIGINT DEFAULT 0;

  DECLARE _shiftLength NUMERIC(20,2) DEFAULT 0;

  DECLARE _weavingLength NUMERIC(20,2) DEFAULT 0;

  DECLARE _statusChangeNum BIGINT DEFAULT 0;

  DECLARE _uploadNum INT DEFAULT 0;

  DECLARE _shaftCode varchar(255);


  BEGIN

    -- 查询最新实时情况
    -- 定义游标，并将sql结果集赋值到游标中
    DECLARE mac_list CURSOR FOR SELECT id, rpm, state, lastdate,middleno,stationno FROM allrealtime;

    -- 定义第二个游标 把值设为变量即可
    DECLARE shift_list CURSOR FOR SELECT dusec, state, output, starttime, endtime FROM alldata WHERE  endtime >= UNIX_TIMESTAMP(_shiftStartTime) and middleno = _middleno and stationno = _stationno;

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
    SELECT macCode, weftDensity, weavingLength,shaftCode into _macCode, _weftDensity, _weavingLength, _shaftCode  FROM mac_machine where id = _id;

    SELECT   shiftStartTime
             INTO   _shiftStartTime
    FROM mft_shift
    WHERE shiftNow = 1 AND  macCode = _macCode;

    /*-- 查询当前班次情况
    SELECT   stopTime, runTime, warpStopTime, weftStopTime, otherStopTime, warpStopNum, weftStopNum, otherStopNum, pickNum, shiftLength, statusChangeNum, macSpeed, uploadNum, shiftStartTime,updateTime
             INTO   _stopTime, _runTime, _warpStopTime, _weftStopTime, _otherStopTime, _warpStopNum, _weftStopNum, _otherStopNum, _pickNum, _shiftLength, _statusChangeNum, _macSpeedAverage, _uploadNum, _shiftStartTime, _updateTime2
    FROM mft_shift
    WHERE shiftNow = 1 AND  macCode = _macCode;*/

    begin
      -- 查询最新班次情况 使用第二个游标

      open shift_list;
      -- 将游标中的值赋值给变量，注意：变量名不要和返回的列名同名，变量顺序要和sql结果列的顺序一致
      fetch shift_list into _dusec, _state, _output, _starttime, _endtime;
      -- 当s不等于1，也就是未遍历完时，会一直循环
      while s<>1 do

      -- 执行相关班次的更新
      -- 判断是否刚好在某个班次点 那么 dusec 要只算这半部分的 之前剩下的那部分由程序后台加上  次数算在这里
      if (_starttime<= UNIX_TIMESTAMP(_shiftStartTime) and UNIX_TIMESTAMP(_shiftStartTime)<=_endtime) then
        set _dusec = _endtime - UNIX_TIMESTAMP(_shiftStartTime);
        SET _output = _output * (_dusec/(_endtime-_starttime));
      end if;
      -- 判断传输过来的状态
      -- 运行
      if (_state = 30) THEN
        SET _runTime = _runTime + _dusec;
        SET _shiftLength = _shiftLength + _output/(_weftDensity*10);
        SET _weavingLength = _weavingLength + _output /(_weftDensity*10);
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

        SET _macEfficiency = CONVERT((_runTime*100), DECIMAL)/(_runTime + _stopTime);
      end if;




      fetch shift_list into _dusec, _state, _output, _starttime, _endtime;
      -- 当s等于1时表明遍历以完成，退出循环
      end while;
      -- 关闭游标
      close shift_list;

      -- 第二个游标结束后 一定要设置第一个游标的handler为0 要不然会直接退出第一个循环
      set s = 0;



    end;

    -- 计算平均车速  要根据织了多长来算
    if (_pickNum = 0) then
      Set _macSpeedAverage = 0;
    elseif (_pickNum > 0) then
      Set _macSpeedAverage =_pickNum/((_runTime + _stopTime)/60+0.000001);
    end if;

    -- 离线判断 如果上次更新距这次超过15分钟未有数据上传定义为离线
    if(unix_timestamp(_updateTime) < (unix_timestamp(now())-900)) THEN
      SET _macStatus = 11;
      SET _offLineTime = unix_timestamp(now()) - unix_timestamp(_updateTime);
    end if;

    -- 更新实时表
    UPDATE mac_machine set macSpeed= _macSpeed, macStatus = _macStatus, macEfficiency = _macEfficiency,weavingLength = _weavingLength, updateTime = _updateTime where middleno = _middleno and stationno=_stationno  ;  -- TODO  updateTime 要改

    -- 更新班次表

    UPDATE mft_shift SET

                       macEfficiency = _macEfficiency,runTime=_runTime, stopTime=_stopTime, warpStopTime=_warpStopTime, weftStopTime=_weftStopTime, otherStopTime=_otherStopTime, warpStopNum=_warpStopNum, weftStopNum=_weftStopNum, otherStopNum=_otherStopNum, pickNum=_pickNum, shiftLength=_shiftLength, statusChangeNum=_statusChangeNum, updateTime = now(),
                       macSpeed = _macSpeedAverage, uploadNum = _uploadNum,offLineTime = _offLineTime
    WHERE shiftNow = 1 and macCode = _macCode; -- 跟新当前班次的情况

    -- 更新织轴表
    UPDATE mft_shaft set clothLength= _weavingLength,updateTime=now() where shaftCode = _shaftCode  ;



    -- 将游标中的值再赋值给变量，供下次循环使用
    fetch mac_list into _id, _macSpeed, _macStatus, _updateTime, _middleno, _stationno;
    -- 共下次使用时把变量全部初始化

    set _macSpeedAverage = 0;
    set _macEfficiency = 0;
    set _runTime = 0;
    set _stopTime = 0;
    set _warpStopTime = 0;
    set _weftStopTime = 0;
    set _otherStopTime = 0;
    set _warpStopNum = 0;
    set _weftStopNum = 0;
    set _otherStopNum = 0;
    set _pickNum = 0;
    set _shiftLength = 0;
    set _weavingLength = 0;
    set _statusChangeNum = 0;
    set _uploadNum = 0;
    set _offLineTime = 0;

    -- 当s等于1时表明遍历以完成，退出循环

    end while;
    -- 关闭游标
    close mac_list;
  END;
END //
DELIMITER ;
--
DROP EVENT if exists doSchedule;
# 创建 定时任务 调用存储过程
CREATE EVENT doSchedule ON SCHEDULE EVERY 300 SECOND
  ON COMPLETION PRESERVE
  DO CALL updateData();

# 临时关闭事件
# ALTER EVENT doSchedule DISABLE;

# 删除创建的事件
# DROP EVENT doSchedule;
