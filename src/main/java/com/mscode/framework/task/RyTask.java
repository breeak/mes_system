package com.mscode.framework.task;

import com.mscode.common.constant.ScheduleConstants;
import com.mscode.common.utils.DateUtils;
import com.mscode.framework.config.MsCodeConfig;
import com.mscode.project.hr.domain.HrArrange;
import com.mscode.project.hr.domain.HrShiftArrange;
import com.mscode.project.hr.service.IHrArrangeService;
import com.mscode.project.hr.service.IHrShiftArrangeService;
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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

    @Autowired
    private IHrArrangeService hrArrangeService;
    @Autowired
    private IHrShiftArrangeService hrShiftArrangeService;


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

    /**
     * 生成班次与人员排班的对应关系  具体就是 哪个班次 使用的是哪个排班
     * 生成班次与人员排班一定也是那天 不要出现跨天
     * 异常不处理  全部抛出 直接执行失败 看日志
     */
    public void updateShiftArrange() throws Exception
    {

    }


    /**
     * 生成班次的方法 ，每次到点会执行这个任务，建立正在执行的班次
     * @param shiftType
     * @throws Exception
     */
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
        //排班的生成
        Date date = new Date();     // 获取当前时刻
        List<SysDictData> hr_worker_rule = dictTypeService.selectDictDataByType("hr_worker_rule");
        ArrayList<String> orders = new ArrayList<>();
        Long during = 5L; //先给个默认值
        Date startDate = new Date();
        String shiftStartType = "";
        String shiftGroup = "";
        Long current = 1L; //先给个默认值
        Long offdays = 0L;
        // 解析规则
        for (SysDictData arrangeruleDict : hr_worker_rule) {
            String dictLabel = arrangeruleDict.getDictLabel();
            if (dictLabel.equals("轮班顺序")){
                String value = arrangeruleDict.getDictValue();
                String[] orderArray = value.split("，");//中文逗号
                for (String s : orderArray) {
                    orders.add(s);
                }
            }else if(dictLabel.equals("轮班周期")){
                String value = arrangeruleDict.getDictValue();
                during = Long.parseLong(value);
            }else if(dictLabel.equals("轮班起始日期")){
                String value = arrangeruleDict.getDictValue();
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(value);
            }else if (dictLabel.equals("轮班起始班次")){
                shiftStartType = arrangeruleDict.getDictValue();
            }else if (dictLabel.equals("轮班起始分组")){
                shiftGroup = arrangeruleDict.getDictValue();
            }else if (dictLabel.equals("轮班第几天")){
                current = Long.parseLong(arrangeruleDict.getDictValue());
            }else if (dictLabel.equals("共休息几天")){
                offdays = Long.parseLong(arrangeruleDict.getDictValue());
            }
        }
        //判断经过的天数  距离起始那天
        Long datePoorDay = DateUtils.getDatePoorDay(date, startDate);
        datePoorDay = datePoorDay - offdays;// 减去休息的天数 这些天数不参与排班；
        Long aLoop = orders.size() * during; //一共几个组；多久换一次班；乘起来就是一个大循环的长度
        Long yuDay = datePoorDay % aLoop;// 取余数就是当前在这个大循环的第几天
        int startGroupIndex = -1;//判断开始的那个排第几 index
        int startGroupRange = -1;//落在哪个范围里
        for (int i = 0; i <= orders.size(); i++) {//要大一个
            if ((during*i-current+1)<=yuDay && yuDay<(during*(i+1)-current+1)){// 判断在哪个区间里
                startGroupRange = i;
            }
            if (i==orders.size()){
                break;
            }
            String orderGroup = orders.get(i);
            if (orderGroup.equals(shiftGroup)){// 判断开始的那个排第几
                startGroupIndex = i;
            }
        }
        int actStartIndex = (startGroupRange + startGroupIndex) % orders.size();//得到当前实际的一个班次
        //获取实际的班次对应的数据
        List<SysDictData> mac_common_shift = dictTypeService.selectDictDataByType("mac_common_shift");
        int shiftIndex = -1; //获取起始的班次的排序
        int currentIndex = -1; //获取当前班次的排序
        for (int i = 0; i < mac_common_shift.size(); i++) {
            SysDictData sysDictData = mac_common_shift.get(i);
            if (sysDictData.getDictLabel().equals(shiftStartType)){
                shiftIndex = i;
            }
            if (sysDictData.getDictLabel().equals(shiftType)){
                currentIndex = i;
            }
        }
        int actShiftIndex =(actStartIndex-(shiftIndex -currentIndex)+orders.size())%orders.size();
        String actCurrentGroup = orders.get(actShiftIndex);

        // 根据shiftType 与 actCurrentGroup 与 当前时刻 查找班次即可；然后保存一条记录即可
        HrArrange hrArrange = new HrArrange();
        hrArrange.setArrangeclass(actCurrentGroup);
        Map<String, Object> params = new HashMap<>();
        params.put("beginTime",date);
        params.put("endTime",date);//两个date一样
        hrArrange.setParams(params);
        List<HrArrange> hrArranges = hrArrangeService.selectHrArrangeList(hrArrange);
        if (hrArranges.size()==1){//应该只有一个排班的
            HrArrange arrange = hrArranges.get(0);
            HrShiftArrange hrShiftArrange = new HrShiftArrange();
            hrShiftArrange.setArrangeid(arrange.getId());
            hrShiftArrange.setArrangeno(arrange.getArrangeno());
            hrShiftArrange.setShiftdate(DateUtils.parseDate(shiftDate,DateUtils.YYYY_MM_DD));
            hrShiftArrange.setShifttype(shiftType);
            hrShiftArrange.setCreateTime(new Date());
            hrShiftArrange.setUpdateTime(new Date());
            hrShiftArrange.setStatus("1");
            hrShiftArrange.setCreateBy("admin");
            hrShiftArrange.setUpdateBy("admin");
            hrShiftArrange.setRemark("定时生成备注");
            hrShiftArrangeService.insertHrShiftArrange(hrShiftArrange);
        }else{
            throw new Exception("此时间段未排班");
        }
        System.out.println(actCurrentGroup);
    }

    /**
     * 按照对应的班次顺序生成产生班次的定时任务；
     * 每次修改mac_common_shift 都应该运行一次这个任务，这个任务的状态不应启用，但生成的定时任务应该手动启用
     * @param dictType  排班日期与时间
     * @throws Exception
     */
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
