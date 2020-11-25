package com.mscode.project.manufacture.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mscode.project.manufacture.domain.MacMachine;
import com.mscode.project.manufacture.domain.MftShaft;
import com.mscode.project.manufacture.domain.MftShift;
import com.mscode.project.manufacture.mapper.MacMachineMapper;
import com.mscode.project.manufacture.mapper.MftShaftMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mscode.project.manufacture.mapper.AlldataMapper;
import com.mscode.project.manufacture.domain.Alldata;
import com.mscode.project.manufacture.service.IAlldataService;

/**
 * 基础数据Service业务层处理
 * 
 * @author mscode
 * @date 2020-11-05
 */
@Service
public class AlldataServiceImpl implements IAlldataService 
{
    @Autowired
    private AlldataMapper alldataMapper;
    @Autowired
    private MacMachineMapper macMachineMapper;
    @Autowired
    private MftShaftMapper mftShaftMapper;
    /**
     * 查询基础数据
     * 
     * @param id 基础数据ID
     * @return 基础数据
     */
    @Override
    public Alldata selectAlldataById(Long id)
    {
        return alldataMapper.selectAlldataById(id);
    }

    /**
     * 查询基础数据列表
     * 
     * @param alldata 基础数据
     * @return 基础数据
     */
    @Override
    public List<Alldata> selectAlldataList(Alldata alldata)
    {
        return alldataMapper.selectAlldataList(alldata);
    }

    /**
     * 新增基础数据
     * 
     * @param alldata 基础数据
     * @return 结果
     */
    @Override
    public int insertAlldata(Alldata alldata)
    {
        return alldataMapper.insertAlldata(alldata);
    }

    /**
     * 修改基础数据
     * 
     * @param alldata 基础数据
     * @return 结果
     */
    @Override
    public int updateAlldata(Alldata alldata)
    {
        return alldataMapper.updateAlldata(alldata);
    }

    /**
     * 批量删除基础数据
     * 
     * @param ids 需要删除的基础数据ID
     * @return 结果
     */
    @Override
    public int deleteAlldataByIds(Long[] ids)
    {
        return alldataMapper.deleteAlldataByIds(ids);
    }

    /**
     * 删除基础数据信息
     * 
     * @param id 基础数据ID
     * @return 结果
     */
    @Override
    public int deleteAlldataById(Long id)
    {
        return alldataMapper.deleteAlldataById(id);
    }

    @Override
    public MftShift getShift(String macCode, Date beginTime, Date endTime,MftShaft mftShaft) {
        Alldata alldataQuery = new Alldata();
        MacMachine machine = new MacMachine();
        MftShift shift = new MftShift();
        shift.setShiftstarttime(beginTime);
        shift.setShiftendtime(endTime);
        shift.setMaccode(macCode);
        shift.setRemark("封装失败！");
        // 查找织机
        machine.setMaccode(macCode);
        List<MacMachine> macMachines = macMachineMapper.selectMacMachineList(machine);
        if (macMachines.size()==1){
            machine = macMachines.get(0);
            shift.setMiddleno(machine.getMiddleno());
            shift.setStationno(machine.getStationno());
        }else {
            shift.setRemark("无此织机");
            return shift;
        }
        //判断有无传过来织轴信息
        BigDecimal weftDensity = new BigDecimal(100L);
        if (mftShaft!=null){
            shift.setPdtcodes(mftShaft.getPdtcode());
            shift.setShaftcodes(mftShaft.getShaftcode());
            weftDensity = mftShaft.getPdtweftdensity();
        }else{
            // 查找这段时间对应的织轴   假定这段时间必定没有换轴,先不处理换轴的情况 条件需要 beginTime>=actStart,且actEnd=null或者endTime<actEnd,actmaccode,
            MftShaft shaft = new MftShaft();
            shaft.setActmaccode(macCode);
            Map<String,Object> params = new HashMap<>();
            params.put("shiftStartTime", beginTime);
            params.put("shiftEndTime", endTime);
            shaft.setParams(params);
            List<MftShaft> mftShafts = mftShaftMapper.selectMftShaftList(shaft);
            if (mftShafts.size()==1){
                shift.setPdtcodes(mftShafts.get(0).getPdtcode());
                shift.setShaftcodes(mftShafts.get(0).getShaftcode());
                weftDensity = mftShafts.get(0).getPdtweftdensity();
            }else {
                shift.setRemark("无此织轴，使用默认值100");
            }
        }
        // 查找班次的所有数据
        Map<String,Object> params = new HashMap<>();
        params.put("beginTime", beginTime.getTime()/1000);
        params.put("endTime", endTime.getTime()/1000);
        alldataQuery.setMiddleno(machine.getMiddleno());
        alldataQuery.setStationno(machine.getStationno());
        alldataQuery.setParams(params);
        List<Alldata> alldataList = alldataMapper.selectAlldataList(alldataQuery);
        if (alldataList.size()>0){
            // 遍历得到所有的班次数据并且封装好
            Long runTime = 0L, stopTime = 0L, warpStopTime = 0L, weftStopTime= 0L, otherStopTime= 0L,offLineTime= 0L, warpStopNum= 0L, weftStopNum = 0L, otherStopNum = 0L, pickNum = 0L;
            Double macSpeed = 0.,macEfficiency=0.,shiftLength=0.;
            for (Alldata alldata : alldataList) {
                Long state = alldata.getState();
                Long dusec = alldata.getDusec();
                Long output = alldata.getOutput();
                if (alldata.getStartdate().getTime() <=beginTime.getTime() && alldata.getEnddate().getTime()>=beginTime.getTime()){
                    dusec = alldata.getEnddate().getTime()-beginTime.getTime();
                    output = output * new Double(dusec/(alldata.getEnddate().getTime()-alldata.getStartdate().getTime()+0.000001)).longValue();
                }else if(alldata.getStartdate().getTime() <=endTime.getTime() && alldata.getEnddate().getTime()>=endTime.getTime()){
                    dusec = alldata.getEnddate().getTime()-endTime.getTime();
                    output = output * new Double(alldata.getDusec()/(alldata.getEnddate().getTime()-alldata.getStartdate().getTime()+0.000001)).longValue();
                }
                if (state==30){//运转
                    runTime = runTime + dusec;
                    pickNum = pickNum + output;
                    // 长度的计算
                    shiftLength = shiftLength + output/(weftDensity.doubleValue()*10);

                }else if (state==15){
                    //经停
                    stopTime = stopTime + dusec;
                    warpStopTime = warpStopTime + dusec;
                    warpStopNum = warpStopNum + 1;
                }else if (state==23){
                    //纬停
                    stopTime = stopTime + dusec;
                    weftStopTime = weftStopTime + dusec;
                    weftStopNum = weftStopNum + 1;
                }else if (state==31){
                    //其他停
                    stopTime = stopTime + dusec;
                    otherStopTime = otherStopTime + dusec;
                    otherStopNum = otherStopNum + 1;
                }
            }
            // 更新效率  这里已经不是实时效率了
            if((runTime + stopTime) > 0) {
                macEfficiency = runTime.doubleValue()*100 /(runTime + stopTime);
            }
            // 更新离线时间
            offLineTime =  endTime.getTime()/1000-beginTime.getTime()/1000 - runTime- stopTime;
            // 计算平均车速  要根据织了多长来算
            if ( pickNum > 0) {
                macSpeed =pickNum/((runTime + stopTime)/60+0.000001);
            }
            shift.setPicknum(pickNum);
            shift.setRuntime(runTime);
            shift.setStoptime(stopTime);
            shift.setWarpstoptime(warpStopTime);
            shift.setWeftstoptime(weftStopTime);
            shift.setWarpstopnum(warpStopNum);
            shift.setWeftstopnum(weftStopNum);
            shift.setMacefficiency(new BigDecimal(macEfficiency).setScale(2, BigDecimal.ROUND_HALF_UP));
            shift.setMacspeed(new BigDecimal(macSpeed).setScale(2, BigDecimal.ROUND_HALF_UP));
            shift.setOfflinetime(offLineTime);
            shift.setOtherstoptime(otherStopTime);
            shift.setOtherstopnum(otherStopNum);
            shift.setShiftlength(new BigDecimal(shiftLength).setScale(2, BigDecimal.ROUND_HALF_UP));
            shift.setUploadnum(new Integer(alldataList.size()).longValue());
            shift.setRemark("ok");//表示正确查找到了
        }else {
            shift.setRemark("无数据上传");
            return shift;
        }
        return shift;
    }

    @Override
    public List<Alldata> getShiftDetails(String middleno, Long stationno, Date beginTime, Date endTime, MftShaft mftShaft) {
        // 查找班次的所有数据
        Alldata alldataQuery = new Alldata();
        Map<String,Object> params = new HashMap<>();
        params.put("beginTime", beginTime.getTime()/1000);
        params.put("endTime", endTime.getTime()/1000);
        alldataQuery.setMiddleno(middleno);
        alldataQuery.setStationno(stationno);
        alldataQuery.setParams(params);
        List<Alldata> alldataList = alldataMapper.selectAlldataList(alldataQuery);
        // 遍历得到所有的班次数据并且封装好
        Double shiftLength=0.;
        BigDecimal weftDensity = mftShaft.getPdtweftdensity();
        for (Alldata alldata : alldataList) {
            Long output = alldata.getOutput();
            if (alldata.getStartdate().getTime() <= beginTime.getTime() && alldata.getEnddate().getTime() >= beginTime.getTime()) {
                Long dusec = alldata.getEnddate().getTime() - beginTime.getTime();
                output = output * new Double(dusec / (alldata.getEnddate().getTime() - alldata.getStartdate().getTime() + 0.000001)).longValue();
                alldata.setDusec(dusec);
                alldata.setOutput(output);
            } else if (alldata.getStartdate().getTime() <= endTime.getTime() && alldata.getEnddate().getTime() >= endTime.getTime()) {
                Long dusec = alldata.getEnddate().getTime() - endTime.getTime();
                output = output * new Double(alldata.getDusec() / (alldata.getEnddate().getTime() - alldata.getStartdate().getTime() + 0.000001)).longValue();
                alldata.setDusec(dusec);
                alldata.setOutput(output);
            }
            shiftLength = output / (weftDensity.doubleValue() * 10);
            alldata.setPdtcode(mftShaft.getPdtcode());
            alldata.setLength(new BigDecimal(shiftLength).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        return alldataList;
    }
}
