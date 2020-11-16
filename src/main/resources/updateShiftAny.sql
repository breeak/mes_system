-- # 开启定时任务设置
-- SET GLOBAL event_scheduler = ON;
-- # 查看是否开启定时任务
-- SHOW VARIABLES LIKE 'event_scheduler';

-- declare 变量不能@开头
-- 如果存在这个就删掉
DROP PROCEDURE if exists updateShiftAny;
# TODO 想查任意时间的产量的话 需要另写存储过程 对这个进行改进即可

# 创建存储过程  任意班次时刻的一个班次更新或者新建，必须要有班次  换班时更新 重新生成班次
DELIMITER //
CREATE PROCEDURE updateShiftAny(IN _shiftStartTime DATETIME, IN _shiftEndTime DATETIME, IN _shiftDate DATE, IN _shiftType VARCHAR(255), IN _shiftNow INT)
BEGIN

  DECLARE s int DEFAULT 0;

  DECLARE _id INT;

  DECLARE _middleno varchar(255);

  DECLARE _stationno INT;

  DECLARE _starttime BIGINT;

  DECLARE _endtime BIGINT;

  DECLARE _exist BIGINT;

  DECLARE _dusec INT DEFAULT 0;

  DECLARE _state INT;

  DECLARE _output INT DEFAULT 0;

  DECLARE _weftDensity INT;

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
  DECLARE _shaftCode VARCHAR(255);
  DECLARE _pdtCode VARCHAR(255);
  DECLARE _shaftCodes VARCHAR(255) DEFAULT "";
  DECLARE _pdtCodes VARCHAR(255) DEFAULT "";
  DECLARE _clothLength NUMERIC(20,2) DEFAULT 0;
  DECLARE _actStart DATETIME;
  DECLARE _actEnd DATETIME;


  BEGIN

    -- 查询最新实时情况
    -- 定义游标，并将sql结果集赋值到游标中
    DECLARE mac_list CURSOR FOR SELECT id, middleno, stationno,macCode FROM mac_machine;

    -- 定义第二个游标 天衡的数据 把值设为变量即可
    --  两边的都给取到了
    DECLARE shift_list CURSOR FOR SELECT dusec, state, output, starttime, endtime FROM alldata WHERE  endtime >= UNIX_TIMESTAMP(_shiftStartTime) and starttime<= UNIX_TIMESTAMP(_shiftEndTime) and middleno = _middleno and stationno = _stationno;

    -- 定义第三个游标  织轴的数据 计算产量
    --  两边的都给取到了
    DECLARE shaft_list CURSOR FOR SELECT pdtCode, shaftCode, clothLength, actStart, actEnd FROM mft_shaft WHERE actMacCode = _macCode and  ( (actEnd>=UNIX_TIMESTAMP(_shiftStartTime)) or (actEnd is null) ) and actStart <= UNIX_TIMESTAMP(_shiftEndTime);


    -- 声明当游标遍历完后将标志变量置成某个值
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET s=1;


    -- 打开第一个游标
    open mac_list;
    -- 将游标中的值赋值给变量，注意：变量名不要和返回的列名同名，变量顺序要和sql结果列的顺序一致
    fetch mac_list into _id, _middleno, _stationno,_macCode;
    -- 当s不等于1，也就是未遍历完时，会一直循环
    while s<>1 do
    -- 执行业务逻辑

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
      -- 上传数据落在前半部分 只取上传数据的后半 判断是否刚好在某个班次点 那么 dusec 要只算这半部分的
      if (_starttime <= UNIX_TIMESTAMP(_shiftStartTime) and UNIX_TIMESTAMP(_shiftStartTime)<=_endtime) then
        set _dusec = _endtime - UNIX_TIMESTAMP(_shiftStartTime);
        SET _output = _output * (_dusec/(_endtime-_starttime+0.0001)); -- 按比例折算产量  防止除零错误
      end if;

      -- 上传数据落在后半部分 只取上传数据的前半  判断是否刚好在某个班次点 那么 dusec 要只算这半部分的
      if (_starttime <= UNIX_TIMESTAMP(_shiftEndTime) and UNIX_TIMESTAMP(_shiftEndTime)<=_endtime) then
        set _dusec =  UNIX_TIMESTAMP(_shiftEndTime)-_starttime;
        SET _output = _output * (_dusec/(_endtime-_starttime+0.0001));
      end if;

      -- 判断传输过来的状态
      -- 运行
      if (_state = 30) THEN
        SET _runTime = _runTime + _dusec;
        -- SET _shiftLength = _shiftLength + _output/(_weftDensity*10);  -- 不能这么算 纬密没有了要取当时
        -- SET _weavingLength = _weavingLength + _output /(_weftDensity*10);
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

      -- 记录更新次数 上传次数
      SET _statusChangeNum =_statusChangeNum + 1;

      -- 将游标中的值再赋值给变量，供下次循环使用
      fetch shift_list into _dusec, _state, _output, _starttime, _endtime;
      -- 当s等于1时表明遍历以完成，退出循环
      end while;
      -- 关闭游标
      close shift_list;

      -- 第二个游标结束后 一定要设置第一个游标的handler为0 要不然会直接退出第一个循环
      set s = 0;

    end;
    -- ----------------------------------------------------------
    begin
      -- 查询织轴情况 使用第三个游标

      open shaft_list;
      -- 将游标中的值赋值给变量，注意：变量名不要和返回的列名同名，变量顺序要和sql结果列的顺序一致
      fetch shaft_list into _pdtCode, _shaftCode, _clothLength, _actStart, _actEnd;
      -- 当s不等于1，也就是未遍历完时，会一直循环
      while s<>1 do

      -- 执行相关织轴的更新
      -- 上传数据落在前半部分 只取上传数据的后半 判断是否刚好在某个班次点 那么 dusec 要只算这半部分的
      if (_actStart <= UNIX_TIMESTAMP(_shiftStartTime) and ( (UNIX_TIMESTAMP(_shiftStartTime)<=_actEnd))or(_actEnd is null) ) then
        if (_actEnd >= _shiftEndTime ) then
          set _weavingLength = _weavingLength +  (UNIX_TIMESTAMP(_shiftEndTime)-UNIX_TIMESTAMP(_shiftStartTime))*_clothLength;
        elseif (_actEnd < _shiftEndTime ) then
          set _weavingLength = _weavingLength +  (UNIX_TIMESTAMP(_actEnd)-UNIX_TIMESTAMP(_shiftStartTime))*_clothLength;
        elseif (now() < _shiftEndTime ) then
          set _weavingLength = _weavingLength +  (UNIX_TIMESTAMP(now())-UNIX_TIMESTAMP(_shiftStartTime))*_clothLength;
        elseif (now() >= _shiftEndTime ) then
          set _weavingLength = _weavingLength +  (UNIX_TIMESTAMP(_shiftEndTime)-UNIX_TIMESTAMP(_shiftStartTime))*_clothLength;
        end if;
      end if ;

      -- 上传数据落在后半部分 只取上传数据的前半  判断是否刚好在某个班次点 那么 dusec 要只算这半部分的
      if (_actStart <= UNIX_TIMESTAMP(_shiftEndTime) and ((UNIX_TIMESTAMP(_shiftEndTime)<=_actEnd) or (_actEnd is null))) then
        set _weavingLength = _weavingLength +  (UNIX_TIMESTAMP(_shiftEndTime)-UNIX_TIMESTAMP(_actStart))*_clothLength;
      end if;
      -- 上传数据落在中间
      if (_actStart <= UNIX_TIMESTAMP(_shiftEndTime) and (UNIX_TIMESTAMP(_shiftEndTime)>=_actEnd)) then
        set _weavingLength = _weavingLength +  (UNIX_TIMESTAMP(_shiftEndTime)-UNIX_TIMESTAMP(_shiftStartTime))*_clothLength;
      end if;

        -- 拼接品种编号  一般也就一台织机一个品种 如果有多个品种的话就留给前端处理
        if (_pdtCodes = "") then
          set _pdtCodes = _pdtCode;
          set _shaftCodes = _shaftCode;
        else
          set _pdtCodes = concat(_pdtCodes, "," ,_pdtCode);
          set _shaftCodes = concat(_shaftCodes, "," , _shaftCode);
        end if ;
      -- 将游标中的值再赋值给变量，供下次循环使用
      fetch shaft_list into _pdtCode, _shaftCode, _clothLength, _actStart, _actEnd;
      -- 当s等于1时表明遍历以完成，退出循环
      end while;
      -- 关闭游标
      close shaft_list;

      -- 第二个游标结束后 一定要设置第一个游标的handler为0 要不然会直接退出第一个循环
      set s = 0;
    end;


    -- 更新效率  这里已经不是实时效率了
    if((_runTime + _stopTime) > 0) THEN
      SET _macEfficiency = CAST(_runTime AS FLOAT)*100 /(_runTime + _stopTime);
    end if;
      -- 更新离线时间
    set _offLineTime =  UNIX_TIMESTAMP(_shiftEndTime)-UNIX_TIMESTAMP(_shiftStartTime)-_runTime-_stopTime;

    -- 计算平均车速  要根据织了多长来算
    if (_pickNum = 0) then
      Set _macSpeedAverage = 0;
    elseif (_pickNum > 0) then
      Set _macSpeedAverage =_pickNum/((_runTime + _stopTime)/60+0.000001);
    end if;

    Set _uploadNum = _uploadNum + 1;

    -- 更新班次表 判断是否有班次已生成   如果已经生成 就更新 否则 新建 插入
    -- 先查有没有
    select count(*) into _exist from mft_shift where  macCode=_macCode and shiftType=_shiftType and shiftDate=_shiftDate;
    if _exist > 0 then
        update mft_shift set  macCode=_macCode,shiftType=_shiftType,pdtcodes=_pdtcodes,shaftCodes=_shaftCodes, shiftStartTime=_shiftStartTime,shiftEndTime=_shiftEndTime, macEfficiency=_macEfficiency, runTime=_runTime, stopTime=_stopTime, warpStopTime=_warpStopTime, weftStopTime=_weftStopTime, otherStopTime=_otherStopTime,offLineTime=_offLineTime, warpStopNum=_warpStopNum,
                             weftStopNum=_weftStopNum, macspeed=_macSpeedAverage,otherStopNum=_otherStopNum, pickNum=_pickNum, shiftLength=_weavingLength, statusChangeNum=_statusChangeNum, updateTime=now(),shiftDate=_shiftDate,shiftNow=_shiftNow,remark="任意时刻更新"
        where  macCode=_macCode and shiftType=_shiftType and shiftDate=_shiftDate;
    else
      Insert into mft_shift (macCode,macSpeed, shiftType,pdtcodes,shaftCodes, shiftStartTime,shiftEndTime, macEfficiency, runTime, stopTime, warpStopTime, weftStopTime, otherStopTime,offLineTime, warpStopNum, weftStopNum, otherStopNum, pickNum, shiftLength, statusChangeNum, updateTime,shiftDate,shiftNow,remark,middleno,stationno)
      values (_macCode,_macSpeedAverage,_shiftType,_pdtcodes,_shaftCodes, _shiftStartTime,_shiftEndTime, _macEfficiency, _runTime, _stopTime, _warpStopTime, _weftStopTime, _otherStopTime,_offLineTime,  _warpStopNum, _weftStopNum, _otherStopNum, _pickNum, _weavingLength, _statusChangeNum, now(), _shiftDate,_shiftNow,"任意时刻创建",_middleno,_stationno);

    end if;
    -- 将游标中的值再赋值给变量，供下次循环使用
    fetch mac_list into _id,_middleno, _stationno,_macCode;
    -- 共下次使用时把变量全部初始化

    set _macSpeedAverage = 0;
    set _macEfficiency = 0;
    set _runTime = 0;
    set _stopTime = 0;
    set _warpStopTime = 0;
    set _weftStopTime = 0;
    set _otherStopTime = 0;
    set _offLineTime = 0;
    set _warpStopNum = 0;
    set _weftStopNum = 0;
    set _otherStopNum = 0;
    set _pickNum = 0;
    set _shiftLength = 0;
    set _weavingLength = 0;
    set _statusChangeNum = 0;
    set _uploadNum = 0;
    set _clothLength = 0;
    set _pdtCodes = "";
    set _shaftCodes = "";
    -- 当s等于1时表明遍历以完成，退出循环
    end while;
    -- 关闭游标
    close mac_list;




  END;
END //
DELIMITER ;

