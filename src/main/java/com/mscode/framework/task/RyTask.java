package com.mscode.framework.task;

import com.mscode.common.constant.ScheduleConstants;
import com.mscode.common.utils.DateUtils;
import com.mscode.project.manufacture.domain.MacMachine;
import com.mscode.project.manufacture.domain.MftShift;
import com.mscode.project.manufacture.service.IAlldataService;
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

    @Autowired
    private IAlldataService alldataService;

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


    public void generateShift(String shiftType) throws Exception
    {
        //shiftNow, 只有 1当前 2未来 0已完成
        //获取当前班次对应的时间点
        Map<String, Object> startEnd = mftShiftService.getStartEnd(shiftType, new Date());
        Date shiftStartTime = (Date) startEnd.get("shiftStartTime");
        Date shiftEndTime = (Date) startEnd.get("shiftEndTime");
        String shiftDate = (String)startEnd.get("shiftDate");
        //获取旧的当前班次，把所有当前班次状态给闭合掉
        MftShift mftShift1 = new MftShift();
        mftShift1.setShiftnow(1L);
        List<MftShift> mftShifts = mftShiftService.selectMftShiftList(mftShift1);
        if (mftShifts.size()>0){//表示生成了当前班次 存在当前班次  计算相关数据并闭合掉
            for (MftShift mftShift : mftShifts) {
                MftShift updateShift = alldataService.getShift(mftShift.getMaccode(), mftShift.getShiftstarttime(), mftShift.getShiftendtime(), null);//使用null 自动寻找合适的shaft
                updateShift.setShifttype(mftShift.getShifttype());
                updateShift.setStartlength(mftShift.getStartlength());
                updateShift.setShiftdate(mftShift.getShiftdate());
                updateShift.setShiftnow(0L); //过去的统一设为0
                updateShift.setUpdatetime(new Date());
                updateShift.setRemark("班次到点自动闭合");
                updateShift.setId(mftShift.getId());
                mftShiftService.updateMftShift(updateShift);
                updateShift=mftShiftService.selectMftShiftById(mftShift.getId());
                // 对于新的班次 查找有没有
                MftShift shiftQuery = new MftShift();
                shiftQuery.setShiftdate(DateUtils.parseDate(shiftDate,DateUtils.YYYY_MM_DD));
                shiftQuery.setShifttype(shiftType);
                shiftQuery.setMaccode(mftShift.getMaccode());
                List<MftShift> mftShifts1 = mftShiftService.selectMftShiftList(shiftQuery);
                if (mftShifts1.size()==1){
                    mftShifts1.get(0).setShiftnow(1L);//设为当前班次
                    mftShifts1.get(0).setStartlength(updateShift.getShiftlength().add(updateShift.getStartlength()));
                    mftShifts1.get(0).setPdtcodes(updateShift.getPdtcodes());
                    mftShifts1.get(0).setShaftcodes(updateShift.getShaftcodes());
                    mftShifts1.get(0).setMiddleno(updateShift.getMiddleno());
                    mftShifts1.get(0).setStationno(updateShift.getStationno());
                    mftShifts1.get(0).setUpdatetime(new Date());
                    mftShifts1.get(0).setRemark("前端当前班次更新");
                    mftShiftService.updateMftShift(mftShifts1.get(0));
                }else {
                    shiftQuery.setShiftnow(1L);//设为当前班次
                    shiftQuery.setShiftstarttime(shiftStartTime);
                    shiftQuery.setShiftendtime(shiftEndTime);
                    shiftQuery.setStartlength(updateShift.getShiftlength().add(updateShift.getStartlength()));
                    shiftQuery.setPdtcodes(updateShift.getPdtcodes());
                    shiftQuery.setShaftcodes(updateShift.getShaftcodes());
                    shiftQuery.setMiddleno(updateShift.getMiddleno());
                    shiftQuery.setStationno(updateShift.getStationno());
                    shiftQuery.setUpdatetime(new Date());
                    shiftQuery.setRemark("前端当前班次新建");
                    mftShiftService.insertMftShift(shiftQuery);
                }
            }
        }else{//旧当前班次没有生成，不用管它，只生成新的当前班次即可
            List<MacMachine> macMachines = macMachineService.selectMacMachineList(new MacMachine());
            for (MacMachine macMachine : macMachines) {
                MftShift mftShift = new MftShift();
                mftShift.setMaccode(macMachine.getMaccode());
                mftShift.setShifttype(shiftType);
                mftShift.setShiftstarttime(shiftStartTime);
                mftShift.setShiftendtime(shiftEndTime);
                mftShift.setPdtcodes(macMachine.getPdtcode());
                mftShift.setShaftcodes(macMachine.getShaftcode());
                mftShift.setStartlength(macMachine.getWeavinglength());
                mftShift.setShiftdate(DateUtils.parseDate(shiftDate,DateUtils.YYYY_MM_DD));
                mftShift.setShiftnow(1L);//设为当前
                mftShift.setUpdatetime(new Date());
                mftShift.setRemark("前端直接生成当前班次");
                mftShift.setMiddleno(macMachine.getMiddleno());
                mftShift.setStationno(macMachine.getStationno());
                mftShiftService.insertMftShift(mftShift);
            }
        }
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
