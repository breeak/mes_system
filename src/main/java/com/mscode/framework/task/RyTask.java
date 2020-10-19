package com.mscode.framework.task;

import com.mscode.common.constant.ScheduleConstants;
import com.mscode.common.utils.DateUtils;
import com.mscode.project.manufacture.domain.MacMachine;
import com.mscode.project.manufacture.domain.MftShift;
import com.mscode.project.manufacture.service.IMacMachineService;
import com.mscode.project.manufacture.service.IMftShiftService;
import com.mscode.project.monitor.domain.SysJob;
import com.mscode.project.monitor.service.ISysJobService;
import com.mscode.project.system.domain.SysDictData;
import com.mscode.project.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mscode.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 定时任务调度测试
 * 
 * @author mscode  官方网址：www.mscode.vip
 */
@Component("ryTask")
public class RyTask
{
    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private IMftShiftService mftShiftService;

    @Autowired
    private IMacMachineService macMachineService;

    @Autowired
    private ISysJobService jobService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }


    public void generateShift(String shiftType)
    {
        //倒数第三班去掉
        MftShift mftShift1 = new MftShift();
        mftShift1.setShiftnow(-2L);
        List<MftShift> mftShifts = mftShiftService.selectMftShiftList(mftShift1);
        for (MftShift mftShift : mftShifts) {
            mftShift.setShiftnow(0L); //设为不是当前班次了
            mftShift.setUpdatetime(new Date());
            mftShiftService.updateMftShift(mftShift);//更新数据
        }
        //倒数第二班设为倒数第三班
        mftShift1 = new MftShift();
        mftShift1.setShiftnow(-1L);
        mftShifts = mftShiftService.selectMftShiftList(mftShift1);
        for (MftShift mftShift : mftShifts) {
            mftShift.setShiftnow(-2L); //设为不是当前班次了
            mftShift.setUpdatetime(new Date());
            mftShiftService.updateMftShift(mftShift);//更新数据
        }


        // 把旧的当前班次给设为倒数第一班
        mftShift1 = new MftShift();
        mftShift1.setShiftnow(1L);
        mftShifts = mftShiftService.selectMftShiftList(mftShift1);
        for (MftShift mftShift : mftShifts) {
            mftShift.setShiftnow(-1L); //设为不是当前班次了
            mftShift.setShiftendtime(new Date()); // 结束设为当前时间
            MacMachine macMachine = new MacMachine();
            macMachine.setMaccode(mftShift.getMaccode());
            List<MacMachine> macMachines = macMachineService.selectMacMachineList(macMachine); // 查找班次对应的机台 应该只有1台 当前状态下
            Long duringSecond = (new Date().getTime()-mftShift.getUpdatetime().getTime())/1000; // 这里是算的 班次到点了 还有数据没有传完 原因=》       //TODO 更新时间只有上传数据才更新 结束状态改变才更新
            if (macMachines.size()==1){
                Long macstatus = macMachines.get(0).getMacstatus();
                if (macstatus==30){//运行
                    mftShift.setRuntime(mftShift.getRuntime()+duringSecond);
                    // 存储过程中要调整runTime，pickNum的计算 要与开始时间比较 如果 是切换班次的时间要分开 只需加上
                    // 下一次更新 picknum 要拆分 runTime 只算一半
                }else if(macstatus==15){//经停
                    mftShift.setStoptime(mftShift.getStoptime()+duringSecond);
                    mftShift.setWarpstoptime(mftShift.getWarpstoptime()+duringSecond);
                }else if(macstatus==23){//纬停
                    mftShift.setStoptime(mftShift.getStoptime()+duringSecond);
                    mftShift.setWeftstoptime(mftShift.getWeftstoptime()+duringSecond);
                }else if(macstatus==31){//其他停
                    mftShift.setStoptime(mftShift.getStoptime()+duringSecond);
                    mftShift.setOtherstoptime(mftShift.getOtherstoptime()+duringSecond);
                }
                //更新效率
                if ((mftShift.getRuntime()+mftShift.getStoptime())>0){
                    mftShift.setMacefficiency(new BigDecimal(mftShift.getRuntime().doubleValue()*100/(mftShift.getRuntime()+mftShift.getStoptime())).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                }else {
                    mftShift.setMacefficiency(new BigDecimal(0));
                }
            }
            mftShift.setUpdatetime(new Date());
            mftShiftService.updateMftShift(mftShift);//更新数据
        }

        //生成新的班次
        List<MacMachine> macMachines = macMachineService.selectMacMachineList(new MacMachine());
        for (MacMachine macMachine : macMachines) {
            MftShift mftShift = new MftShift();
            mftShift.setMaccode(macMachine.getMaccode());
            mftShift.setMiddleno(macMachine.getMiddleno());
            mftShift.setStationno(macMachine.getStationno());
            mftShift.setShifttype(shiftType);
            mftShift.setShiftstarttime(new Date());
            mftShift.setPdtcodes(macMachine.getPdtcode());
            mftShift.setShaftcodes(macMachine.getShaftcode());
            mftShift.setMacspeed(new BigDecimal(macMachine.getMacspeed()));
            mftShift.setMacefficiency(new BigDecimal(100));//班次开始的默认效率都是100
            mftShift.setShiftdate(new Date());
            mftShift.setShiftnow(1L);//当前班次
            mftShift.setUpdatetime(new Date());
            mftShiftService.insertMftShift(mftShift);
        }
        System.out.println("更新班次一次");
    }

    //班次表任务开启
    public void updateShift(String dictType) throws Exception{
        if (dictType==null || "".equals(dictType)){
            dictType = "mac_common_shift";
        }
        List<SysDictData> shiftStartTimes = dictTypeService.selectDictDataByType(dictType);
        if (shiftStartTimes.size()>0){// 根据班次时间进行处理  获取自定义的班次  并且要进行排序
            Map<String,Date> dateMap =new  HashMap<String,Date>();
            for (SysDictData shiftStartTime : shiftStartTimes) {
                if(shiftStartTime!=null && !"".equals(shiftStartTime)){
                    String shiftStartJutiTime = DateUtils.getDate() + " " +shiftStartTime.getDictValue();
                    Date shiftStartDateTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, shiftStartJutiTime);
                    dateMap.put(shiftStartTime.getDictLabel(),shiftStartDateTime);
                }
            }
            List<Map.Entry<String,Date>> dateMapList =
                    new ArrayList<Map.Entry<String,Date>>(dateMap.entrySet());
            Collections.sort(dateMapList, new Comparator<Map.Entry<String, Date>>() {
                @Override
                public int compare(Map.Entry<String, Date> o1, Map.Entry<String, Date> o2) {

                    return o1.getValue().compareTo(o2.getValue());
                }
            });
            //关闭之前开启的shift任务
            SysJob sysJob = new SysJob();
            sysJob.setJobGroup("SHIFT");
            List<SysJob> sysJobs = jobService.selectJobList(sysJob);
            for (SysJob job : sysJobs) {
                job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
                jobService.changeStatus(job);
            }
            //对于自定义的班次生成 job任务 并且启动
            for (Map.Entry<String, Date> dateEntry : dateMap.entrySet()) {

                String cronbyDate = DateUtils.getCronbyDate(dateEntry.getValue());
                String substring = cronbyDate.substring(0, 8);// 获取对应的corn表达式
                substring = substring + " * * ?";
                sysJob = new SysJob();
                sysJob.setStatus(ScheduleConstants.Status.NORMAL.getValue());// 创建即执行
                sysJob.setJobName(dateEntry.getKey()+"定时任务");
                sysJob.setJobGroup("SHIFT");
                sysJob.setInvokeTarget("ryTask.generateShift('"+dateEntry.getKey()+"')");// 需要的参数
                sysJob.setCronExpression(substring);
                sysJob.setMisfirePolicy("3");
                sysJob.setConcurrent("1");
                //sysJob.setCreateBy(SecurityUtils.getUsername());
                jobService.insertJob(sysJob);

            }
        }
    }

    public static void main(String[] args) {
        ArrayList<String> shiftStartTimes = new ArrayList<>();
        shiftStartTimes.add("00:00:00");
        shiftStartTimes.add("15:59:00");
        shiftStartTimes.add("07:59:00");


        ArrayList<Date> dateList = new ArrayList<>();
        for (String shiftStartTime : shiftStartTimes) {
            String shiftStartJutiTime = DateUtils.getDate() + " " +shiftStartTime;
            Date shiftStartDateTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, shiftStartJutiTime);
            String cronbyDate = DateUtils.getCronbyDate(shiftStartDateTime);
            String substring = cronbyDate.substring(0, 8);
            substring = substring + " * * ?";


            System.out.println(substring);
            dateList.add(shiftStartDateTime);
        }
        Collections.sort(dateList);
        System.out.println("执行无参方法");

    }

}
