package com.mscode.project.manufacture.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.mscode.common.utils.DateUtils;
import com.mscode.project.manufacture.domain.MacMachine;
import com.mscode.project.manufacture.mapper.MacMachineMapper;
import com.mscode.project.system.domain.SysDictData;
import com.mscode.project.system.mapper.SysDictDataMapper;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mscode.project.manufacture.mapper.MftShiftMapper;
import com.mscode.project.manufacture.domain.MftShift;
import com.mscode.project.manufacture.service.IMftShiftService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 班次效率Service业务层处理
 * 
 * @author MS
 * @date 2020-09-20
 */
@Service
public class MftShiftServiceImpl implements IMftShiftService 
{
    @Autowired
    private MftShiftMapper mftShiftMapper;
    @Autowired
    private MacMachineMapper  macMachineMapper;
    @Autowired
    private SysDictDataMapper dictDataMapper;
    /**
     * 查询班次效率
     * 
     * @param id 班次效率ID
     * @return 班次效率
     */
    @Override
    public MftShift selectMftShiftById(Long id)
    {
        return mftShiftMapper.selectMftShiftById(id);
    }

    /**
     * 查询班次效率列表
     * 
     * @param mftShift 班次效率
     * @return 班次效率
     */
    @Override
    public List<MftShift> selectMftShiftList(MftShift mftShift)
    {
        return mftShiftMapper.selectMftShiftList(mftShift);
    }

    /**
     * 新增班次效率
     * 
     * @param mftShift 班次效率
     * @return 结果
     */
    @Override
    public int insertMftShift(MftShift mftShift)
    {
        return mftShiftMapper.insertMftShift(mftShift);
    }

    /**
     * 修改班次效率
     * 
     * @param mftShift 班次效率
     * @return 结果
     */
    @Override
    public int updateMftShift(MftShift mftShift)
    {
        return mftShiftMapper.updateMftShift(mftShift);
    }

    /**
     * 批量删除班次效率
     * 
     * @param ids 需要删除的班次效率ID
     * @return 结果
     */
    @Override
    public int deleteMftShiftByIds(Long[] ids)
    {
        return mftShiftMapper.deleteMftShiftByIds(ids);
    }

    /**
     * 删除班次效率信息
     * 
     * @param id 班次效率ID
     * @return 结果
     */
    @Override
    public int deleteMftShiftById(Long id)
    {
        return mftShiftMapper.deleteMftShiftById(id);
    }

    @Override
    public Object listRecentDays(Integer days){
        // 获取对应的日期
        Date startDate = DateUtils.addDays(new Date(), days*-1);
        MftShift mftShift = new MftShift();
        mftShift.setBeginTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, startDate));
        List<MftShift> mftShifts = mftShiftMapper.selectMftShiftList(mftShift);
        if (mftShifts.size()==0){
            return null;
        }
        // 获取班次数据字典
        List<SysDictData> mac_common_shift = dictDataMapper.selectDictDataByType("mac_common_shift");
        Map<String,Map> allMap = new HashMap<>();
        Map<String, double[]> lengthMap = new HashMap<>();
        Map<String, double[]> speedMap = new HashMap<>();
        Map<String, double[]> efficiencyMap = new HashMap<>();
        Map<String, double[]> runtimeMap = new HashMap<>();
        Map<String, String[]> datelistMap = new HashMap<>();
        String[] dateList = new String[days];
        for (SysDictData sysDictData : mac_common_shift) {
            double[] lengthArray = new double[days];
            double[] speedArray = new double[days];
            double[] efficiencyArray = new double[days];
            double[] runtimeArray = new double[days];
            for(int i=0; i<days;i++){
                startDate = DateUtils.addDays(new Date(), (days-i)*-1);
                String startDateString = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, startDate);
                // 获取统计数据
                DoubleSummaryStatistics stats = mftShifts.stream().filter(mftShift1 -> (mftShift1.getShifttype().equals(sysDictData.getDictLabel()) && DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, mftShift1.getShiftdate()).equals(startDateString))).mapToDouble((mftShift1) -> {
                    return mftShift1.getShiftlength().doubleValue();
                }).summaryStatistics();
                DoubleSummaryStatistics stats2 = mftShifts.stream().filter(mftShift1 -> (mftShift1.getShifttype().equals(sysDictData.getDictLabel()) && DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, mftShift1.getShiftdate()).equals(startDateString))).mapToDouble((mftShift1) -> {
                    return mftShift1.getMacspeed().doubleValue();
                }).summaryStatistics();
                DoubleSummaryStatistics stats3 = mftShifts.stream().filter(mftShift1 -> (mftShift1.getShifttype().equals(sysDictData.getDictLabel()) && DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, mftShift1.getShiftdate()).equals(startDateString))).mapToDouble((mftShift1) -> {
                    return mftShift1.getMacefficiency().doubleValue();
                }).summaryStatistics();
                DoubleSummaryStatistics stats4 = mftShifts.stream().filter(mftShift1 -> (mftShift1.getShifttype().equals(sysDictData.getDictLabel()) && DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, mftShift1.getShiftdate()).equals(startDateString))).mapToDouble((mftShift1) -> {
                    return mftShift1.getRuntime().doubleValue();
                }).summaryStatistics();
                lengthArray[i] = Math.round(stats.getSum());
                speedArray[i] = Math.round(stats2.getAverage());
                efficiencyArray[i] = new BigDecimal(stats3.getAverage()).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                runtimeArray[i] = Math.round(stats4.getAverage());
                dateList[i] = startDateString;
            }
            lengthMap.put(sysDictData.getDictLabel(), lengthArray);
            speedMap.put(sysDictData.getDictLabel(), speedArray);
            efficiencyMap.put(sysDictData.getDictLabel(), efficiencyArray);
            runtimeMap.put(sysDictData.getDictLabel(), runtimeArray);
        }
        datelistMap.put("日期", dateList);
        double[] allList = new double[days];
        double[] allList1 = new double[days];
        double[] allList2 = new double[days];
        double[] allList3 = new double[days];
        for(int i=0; i<days;i++){
            for (int j=0; j<mac_common_shift.size();j++) {
                allList[i] = lengthMap.get(mac_common_shift.get(j).getDictLabel())[i] +allList[i];
                allList1[i] = speedMap.get(mac_common_shift.get(j).getDictLabel())[i] +allList1[i];
                allList2[i] = efficiencyMap.get(mac_common_shift.get(j).getDictLabel())[i] +allList2[i];
                allList3[i] = runtimeMap.get(mac_common_shift.get(j).getDictLabel())[i] +allList3[i];
            }
            allList1[i] = allList1[i]/mac_common_shift.size();
            allList2[i] = allList2[i]/mac_common_shift.size();
            allList3[i] = allList3[i]/mac_common_shift.size();
        }
        lengthMap.put("总计", allList);
        speedMap.put("总计", allList1);
        efficiencyMap.put("总计", allList2);
        runtimeMap.put("总计", allList3);
        allMap.put("时间列表", datelistMap);
        allMap.put("产量统计", lengthMap);
        allMap.put("速度统计", speedMap);
        allMap.put("效率统计", efficiencyMap);
        allMap.put("运行统计", runtimeMap);
        return allMap;
    }
    /**
     * 根据班次与日期获取具体的时间
     * @param shiftType
     * @param shiftDate
     * @return
     */
    public Map<String, Object> getStartEnd(String shiftType, Date shiftDate) throws Exception {
        List<SysDictData> mac_common_shift = dictDataMapper.selectDictDataByType("mac_common_shift");
        // 大于两班开始处理 计算对应的准确时刻下有没有数据
        // 获取对应班次的准确起止时间
        int flag = 0;
        for (int i=0; i<mac_common_shift.size();i++) {
            if (shiftType.equals(mac_common_shift.get(i).getDictLabel())){
                flag = i;
            }
        }
        Date shiftStartTime = null;
        Date shiftEndTime = null;
        // 第一个就是  按顺序来的 没有隔天
        String dictValue_start = mac_common_shift.get(flag).getDictValue();
        String shiftDateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, shiftDate);
        shiftStartTime = DateUtils.parseDate(shiftDateStr+" "+dictValue_start,DateUtils.YYYY_MM_DD_HH_MM_SS);
        Date endDate = DateUtils.addDays(shiftDate, 1);
        String shiftDateStr2 = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, endDate);
        if (flag==0){
            String dictValue_end = mac_common_shift.get(flag+1).getDictValue();
            shiftEndTime = DateUtils.parseDate(shiftDateStr+" "+dictValue_end,DateUtils.YYYY_MM_DD_HH_MM_SS);
        }else if (flag==1){
            // 如果两班隔天 否则不隔天
            // 分情况
            if (mac_common_shift.size()==2){
                String dictValue_end = mac_common_shift.get(0).getDictValue();
                shiftEndTime = DateUtils.parseDate(shiftDateStr2+" "+dictValue_end,DateUtils.YYYY_MM_DD_HH_MM_SS);
            }else if (mac_common_shift.size()==3){
                String dictValue_end = mac_common_shift.get(flag+1).getDictValue();
                shiftEndTime = DateUtils.parseDate(shiftDateStr+" "+dictValue_end,DateUtils.YYYY_MM_DD_HH_MM_SS);
            }
        }if (flag==2){//必定三班
            String dictValue_end = mac_common_shift.get(0).getDictValue();
            shiftEndTime = DateUtils.parseDate(shiftDateStr2+" "+dictValue_end,DateUtils.YYYY_MM_DD_HH_MM_SS);
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("shiftEndTime", shiftEndTime);
        params.put("shiftStartTime", shiftStartTime);
        params.put("shiftDate", shiftDateStr);
        params.put("shiftType", shiftType);
        return params;
    }

    /**
     * 判断是否要生成手动执行生成新的班次 如果已经生成了就不创建，
     * 找到当前班次，shiftNow设为1以及更新时刻
     *
     * @param shiftType
     */
    @Override
    public void checkNow(String shiftType, Long shiftNow,Date shiftDate) throws Exception {

        Map<String, Object> params = getStartEnd(shiftType, shiftDate);
        // 生成班次
        MftShift mftShift = new MftShift();
        mftShift.setShifttype(shiftType);
        mftShift.setShiftdate(DateUtils.parseDate((String) params.get(shiftDate),DateUtils.YYYY_MM_DD));
        List<MftShift> mftShifts = mftShiftMapper.selectMftShiftList(mftShift);
        params.put("shiftNow", shiftNow);
        mftShift.setParams(params);

        if (shiftNow == 1L){
            // 这个班次有数据  只需把这个班次设为当前班次  这个班次必定是sql定时生成的未来的任务
            if (mftShifts.size()>0 && mftShifts.get(0).getShiftnow()==2L){
                for (MftShift shift : mftShifts) {
                    shift.setShiftnow(shiftNow);//当前班次
                    shift.setUpdatetime(new Date());
                    mftShiftMapper.updateMftShift(shift);
                }
            }else{// 没有数据就 生成这个班次数据 利用mysql存储过程  必定要生成的
                mftShiftMapper.createShift(mftShift);
                //检查当前是否有当前时刻班次了 刚好是152台  如果没有 或数据不对 就 重新生成
                MftShift mfttest = new MftShift();
                mfttest.setShiftnow(1L);
                List<MftShift> mftShifts1 = mftShiftMapper.selectMftShiftList(mfttest);
                if (mftShifts1.size()!= macMachineMapper.selectMacMachineList(new MacMachine()).size()){
                    for (MftShift shift : mftShifts1) {
                        mftShiftMapper.deleteMftShiftById(shift.getId());//没有就全部删掉
                    }
                    mftShiftMapper.createShift(mftShift);
                }
            }
        }else{ // 这个是更新任意班次 会自动判断是否要更新还是新建
            mftShiftMapper.updateShiftAny(mftShift);
        }
    }
}
