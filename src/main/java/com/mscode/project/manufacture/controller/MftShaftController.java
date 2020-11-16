package com.mscode.project.manufacture.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mscode.project.manufacture.domain.Alldata;
import com.mscode.project.manufacture.domain.MacMachine;
import com.mscode.project.manufacture.domain.MftShift;
import com.mscode.project.manufacture.service.IAlldataService;
import com.mscode.project.manufacture.service.IMacMachineService;
import com.mscode.project.manufacture.service.IMftShiftService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mscode.framework.aspectj.lang.annotation.Log;
import com.mscode.framework.aspectj.lang.enums.BusinessType;
import com.mscode.project.manufacture.domain.MftShaft;
import com.mscode.project.manufacture.service.IMftShaftService;
import com.mscode.framework.web.controller.BaseController;
import com.mscode.framework.web.domain.AjaxResult;
import com.mscode.common.utils.poi.ExcelUtil;
import com.mscode.framework.web.page.TableDataInfo;

/**
 * 织轴列表Controller
 * 
 * @author MS
 * @date 2020-09-21
 */
@RestController
@RequestMapping("/manufacture/shaft")
public class MftShaftController extends BaseController
{
    @Autowired
    private IMftShaftService mftShaftService;
    @Autowired
    private IMacMachineService macMachineService;
    @Autowired
    private IMftShiftService mftShiftService;
    @Autowired
    private IAlldataService alldataService;
    /**
     * 查询织轴列表列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shaft:list')")
    @GetMapping("/list")
    public TableDataInfo list(MftShaft mftShaft, @RequestParam Map<String,Object> params)
    {
        startPage();
        mftShaft.setParams(params);
        List<MftShaft> list = mftShaftService.selectMftShaftList(mftShaft);
        return getDataTable(list);
    }

    /**
     * 导出织轴列表列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shaft:export')")
    @Log(title = "织轴列表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MftShaft mftShaft)
    {
        List<MftShaft> list = mftShaftService.selectMftShaftList(mftShaft);
        ExcelUtil<MftShaft> util = new ExcelUtil<MftShaft>(MftShaft.class);
        return util.exportExcel(list, "shaft");
    }

    /**
     * 获取织轴列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shaft:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(mftShaftService.selectMftShaftById(id));
    }

    /**
     * 新增织轴列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shaft:add')")
    @Log(title = "织轴列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MftShaft mftShaft)
    {
        mftShaft.setCreatetime(new Date());
        mftShaft.setUpdatetime(new Date());
        return toAjax(mftShaftService.insertMftShaft(mftShaft));
    }

    /**
     * 修改织轴列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shaft:edit')")
    @Log(title = "织轴列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MftShaft mftShaft)
    {
        return toAjax(mftShaftService.updateMftShaft(mftShaft));
    }

    /**
     * 织轴上轴
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shaft:edit')")
    @Log(title = "织轴列表", businessType = BusinessType.UPDATE)
    @PutMapping("/doShangZhouAll")
    public AjaxResult doShangZhouAll(@RequestBody MftShaft mftShaft)
    {
        //TODO 对实际上机时间范围的一个判断 一定是以前的时间 不能是未来的 原因是无法计算剩余长度 最后判断


        mftShaft.setCreatetime(new Date());
        mftShaft.setUpdatetime(new Date());
        // 查询是否有保存了
        MftShaft mftshaftQuery = new MftShaft();
        mftshaftQuery.setShaftcode(mftShaft.getShaftcode());
        List<MftShaft> mftShafts = mftShaftService.selectMftShaftList(mftshaftQuery);
        if (mftShafts.size()==0){// 有了就不用保存了
            mftShaftService.insertMftShaft(mftShaft);
        }
        //之所以重新查一次是因为有默认值
        mftShafts = mftShaftService.selectMftShaftList(mftshaftQuery);
        if (mftShafts.size()==1){
            MftShaft shaft =mftShafts.get(0);
            shaft.setShaftstatus("已上轴");
            shaft.setUpdatetime(new Date());
            shaft.setActstart(mftShaft.getActstart());
            shaft.setActmaccode(mftShaft.getActmaccode());

            // 更新织机信息
            MacMachine machine = new MacMachine();
            machine.setMaccode(mftShaft.getActmaccode());
            List<MacMachine> macMachines = macMachineService.selectMacMachineList(machine);
            if (macMachines.size()==1){
                machine = macMachines.get(0);
                // 查询这段时间这个织机织了多长  查询打纬数
                Alldata alldataQuery = new Alldata();
                Map<String,Object> params = new HashMap<>();
                params.put("beginTime", mftShaft.getActstart().getTime()/1000);
                params.put("endTime", new Date().getTime()/1000);
                alldataQuery.setMiddleno(machine.getMiddleno());
                alldataQuery.setStationno(machine.getStationno());
                alldataQuery.setParams(params);
                List<Alldata> alldataList = alldataService.selectAlldataList(alldataQuery);
                Long pickNum = 0L;// 遍历记录打纬数  得到这段时间打了多少纬  换算成织了多长
                for (Alldata alldata : alldataList) {
                    if (alldata.getStartdate().getTime() <=mftShaft.getActstart().getTime() && alldata.getEnddate().getTime()>=mftShaft.getActstart().getTime()){
                        Long dusec = alldata.getEnddate().getTime()-mftShaft.getActstart().getTime();
                        pickNum = pickNum+new Double(dusec/(alldata.getEnddate().getTime()-alldata.getStartdate().getTime()+0.0001)).longValue();
                    }else if(alldata.getStartdate().getTime() <=new Date().getTime() && alldata.getEnddate().getTime()>=new Date().getTime()){
                        Long dusec = alldata.getEnddate().getTime()-new Date().getTime();
                        pickNum = pickNum+new Double(dusec/(alldata.getEnddate().getTime()-alldata.getStartdate().getTime()+0.0001)).longValue();
                    }else{
                        pickNum = pickNum + alldata.getOutput();
                    }
                }
                // 计算新的织轴的情况
                if (pickNum>0){
                    double density = shaft.getPdtweftdensity().doubleValue();
                    double weavingLength = pickNum / ((density * 10)+0.0000001);// 这里算的是布的长度 如果需要织轴剩余长度需要借助经缩
                    if (shaft.getPdtshrinkage().doubleValue()-100>=0){
                        shaft.setPdtshrinkage(new BigDecimal(3));// 如果没有经缩率 增加上默认值3
                    }
                    double remainLength = shaft.getShaftlength().doubleValue()-(weavingLength/(1-shaft.getPdtshrinkage().doubleValue()/100));//总长减去 织这么长的布需要多长织轴；
                    shaft.setClothlength(new BigDecimal(weavingLength).setScale(2, BigDecimal.ROUND_HALF_UP));
                    shaft.setShaftremainlength(new BigDecimal(remainLength).setScale(2, BigDecimal.ROUND_HALF_UP));
                    Long during = new Date().getTime()-shaft.getActstart().getTime();
                    double planTime = during * (shaft.getShaftlength().doubleValue() / weavingLength);
                    Date planDate = new Date(shaft.getActstart().getTime()+new Double(planTime).longValue());
                    shaft.setPlanend(planDate);
                }else{
                    shaft.setClothlength(new BigDecimal(0));
                    shaft.setShaftremainlength(shaft.getShaftlength());
                    shaft.setPlanend(null);//如果一点都没指出来先把预计了机设为null
                }

                // 把旧的织机设置为已了机  并且重新计算那个织轴的已织长度
                mftshaftQuery = new MftShaft();
                mftshaftQuery.setShaftcode(machine.getShaftcode());
                List<MftShaft> mftShafts2 = mftShaftService.selectMftShaftList(mftshaftQuery);
                if (mftShafts2.size()==1){// 之前那个织机必定只有一个织轴
                    MftShaft mftShaft2= mftShafts2.get(0);
                    mftShaft2.setShaftstatus("已了机");
                    mftShaft2.setActend(new Date());
                    // 查询之前织轴这段时间这个织机织了多长  查询打纬数
                    alldataQuery = new Alldata();
                    params = new HashMap<>();
                    params.put("beginTime", mftShaft2.getActstart().getTime()/1000);
                    params.put("endTime", mftShaft.getActstart().getTime()/1000);
                    alldataQuery.setMiddleno(machine.getMiddleno());
                    alldataQuery.setStationno(machine.getStationno());
                    alldataQuery.setParams(params);
                    alldataList = alldataService.selectAlldataList(alldataQuery);
                    pickNum = 0L;// 遍历记录打纬数  得到这段时间打了多少纬  换算成织了多长
                    for (Alldata alldata : alldataList) {
                        if (alldata.getStartdate().getTime() <=mftShaft2.getActstart().getTime() && alldata.getEnddate().getTime()>=mftShaft2.getActstart().getTime()){
                            Long dusec = alldata.getEnddate().getTime()-mftShaft2.getActstart().getTime();
                            pickNum = pickNum + new Double(dusec/(alldata.getEnddate().getTime()-alldata.getStartdate().getTime()+0.0001)).longValue();
                        }else if(alldata.getStartdate().getTime() <=mftShaft.getActstart().getTime() && alldata.getEnddate().getTime()>=mftShaft.getActstart().getTime()){
                            Long dusec = alldata.getEnddate().getTime()-mftShaft.getActstart().getTime();
                            pickNum = pickNum+new Double(dusec/(alldata.getEnddate().getTime()-alldata.getStartdate().getTime()+0.0001)).longValue();
                        }else{
                            pickNum = pickNum + alldata.getOutput();
                        }
                    }
                    // 计算旧的织轴的情况  算是内容的完善
                    if (pickNum>0){
                        double density = mftShaft2.getPdtweftdensity().doubleValue();
                        double weavingLength = pickNum / ((density * 10)+0.0000001);// 这里算的是布的长度 如果需要织轴剩余长度需要借助经缩
                        if (mftShaft2.getPdtshrinkage().doubleValue()-100>=0){
                            mftShaft2.setPdtshrinkage(new BigDecimal(3));// 如果没有经缩率 增加上默认值3
                        }
                        double remainLength = mftShaft2.getShaftlength().doubleValue()-(weavingLength/(1-mftShaft2.getPdtshrinkage().doubleValue()/100));//总长减去 织这么长的布需要多长织轴；
                        mftShaft2.setClothlength(new BigDecimal(weavingLength).setScale(2, BigDecimal.ROUND_HALF_UP));
                        mftShaft2.setShaftremainlength(new BigDecimal(remainLength).setScale(2, BigDecimal.ROUND_HALF_UP));
                        Long during = new Date().getTime()-shaft.getActstart().getTime();
                        double planTime = during * (mftShaft2.getShaftlength().doubleValue() / weavingLength);
                        Date planDate = new Date(mftShaft2.getActstart().getTime()+new Double(planTime).longValue());
                        mftShaft2.setPlanend(planDate);
                    }else{
                        mftShaft2.setClothlength(new BigDecimal(0));
                        mftShaft2.setShaftremainlength(mftShaft2.getShaftlength());
                        mftShaft2.setPlanend(null);//如果一点都没指出来先把预计了机设为null
                    }
                    mftShaftService.updateMftShaft(mftShaft2);
                }
                //更新织机信息
                machine.setPdtcode(shaft.getPdtcode());
                machine.setOrdercode(shaft.getOrdercode());
                machine.setShaftcode(shaft.getShaftcode());
                machine.setWeftdensity(shaft.getPdtweftdensity());
                machine.setWeavinglength(shaft.getClothlength());
                machine.setUpdatetime(new Date());
                // 更新班次信息   TODO 查阅跨过了多少班？
                // TODO 分开处理，先处理当前的班次，再处理之后的班次，当前班次与更新任意时刻班次数据的 已织长度计算要重新想方法计算

                // 先查询当前的班次 把当前班次的织造情况给修改了，
                MftShift shift = new MftShift();
                shift.setShiftnow(1L);
                shift.setMaccode(mftShaft.getActmaccode());
                List<MftShift> mftShifts = mftShiftService.selectMftShiftList(shift);
                if (mftShifts.size()==1){
                    shift = mftShifts.get(0);
                    if (shift.getPdtcodes()!=null){
                        shift.setPdtcodes(shift.getPdtcodes()+","+shaft.getPdtcode());
                    }else {
                        shift.setPdtcodes(shaft.getPdtcode());
                    }
                    if (shift.getShaftcodes()!=null){
                        shift.setShaftcodes(shift.getShaftcodes()+","+shaft.getShaftcode());
                    }else {
                        shift.setShaftcodes(shaft.getShaftcode());
                    }
                }
                mftShiftService.updateMftShift(shift);
                macMachineService.updateMacMachine(machine);
                mftShaftService.updateMftShaft(shaft);
            }
            return AjaxResult.success();
        }else {
            return AjaxResult.error();
        }
    }
    /**
     * 织轴上轴 /doShangZhou 二者合并了
     */

    /**
     * 删除织轴列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shaft:remove')")
    @Log(title = "织轴列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mftShaftService.deleteMftShaftByIds(ids));
    }
}
