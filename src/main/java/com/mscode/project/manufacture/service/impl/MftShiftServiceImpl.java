package com.mscode.project.manufacture.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.mscode.common.utils.DateUtils;
import com.mscode.project.system.domain.SysDictData;
import com.mscode.project.system.mapper.SysDictDataMapper;
import com.mscode.project.system.mapper.SysDictTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mscode.project.manufacture.mapper.MftShiftMapper;
import com.mscode.project.manufacture.domain.MftShift;
import com.mscode.project.manufacture.service.IMftShiftService;

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
                lengthArray[i] = stats.getSum();
                speedArray[i] = stats2.getAverage();
                efficiencyArray[i] = stats3.getAverage();
                runtimeArray[i] = stats4.getAverage();
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
}
