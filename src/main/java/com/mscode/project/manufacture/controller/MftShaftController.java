package com.mscode.project.manufacture.controller;

import java.math.BigDecimal;
import java.util.*;

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
        // 查询是否有保存了
        MftShaft mftshaftQuery = new MftShaft();
        mftshaftQuery.setShaftcode(mftShaft.getShaftcode());
        List<MftShaft> mftShafts = mftShaftService.selectMftShaftList(mftshaftQuery);
        if (mftShafts.size()==0){// 有了就不用保存了
            mftShaft.setCreatetime(new Date());
            mftShaftService.insertMftShaft(mftShaft);
        }
        //之所以重新查一次是因为有默认值
        mftShafts = mftShaftService.selectMftShaftList(mftshaftQuery);
        if (mftShafts.size()==1){
            MftShaft shaft = mftShafts.get(0);
            shaft.setShaftstatus("已上轴");
            shaft.setUpdatetime(new Date());
            shaft.setActstart(mftShaft.getActstart());
            shaft.setActmaccode(mftShaft.getActmaccode());
            if (shaft.getPdtshrinkage().doubleValue()-100>=0){
                shaft.setPdtshrinkage(new BigDecimal(3));// 如果没有经缩率 增加上默认值3
            }
            // 更新织机信息
            MacMachine machine = new MacMachine();
            machine.setMaccode(mftShaft.getActmaccode());
            List<MacMachine> macMachines = macMachineService.selectMacMachineList(machine);
            if (macMachines.size()==1){
                machine = macMachines.get(0);
                // 查询跨过了几个班次  条件是 shiftEndTime>actStart，且shiftNow!=2
                MftShift mftShift = new MftShift();
                Map<String,Object> params = new HashMap<>();
                params.put("actTime", mftShaft.getActstart());
                mftShift.setParams(params);
                List<MftShift> mftShiftList = mftShiftService.selectMftShiftList(mftShift); //应该必定生成过班次，不可能没有班次，排序是按照startTime倒叙的
                Collections.reverse(mftShiftList);//改成按照时间来排序
                Double weavingLength = 0.;
                for (MftShift shift : mftShiftList) {
                    MftShift updateShift = new MftShift();
                    //这个班次中间刚好分开的话 就新生成一个班次在之前  应该第一个就是开始的班次 第一个就会中间分开
                    if (shift.getShiftstarttime().getTime()< mftShaft.getActstart().getTime()){
                        //先新生成 把旧的班次给闭合，旧的织轴一起闭合
                        mftshaftQuery = new MftShaft();
                        mftshaftQuery.setShaftcode(machine.getShaftcode());
                        List<MftShaft> mftShafts2 = mftShaftService.selectMftShaftList(mftshaftQuery);
                        if (mftShafts2.size()==1){// 之前那个织机
                            mftshaftQuery = mftShafts2.get(0);
                        }
                        MftShift newshift = alldataService.getShift(machine.getMaccode(), shift.getShiftstarttime(), mftShaft.getActstart(), mftshaftQuery);
                        newshift.setShifttype(shift.getShifttype());
                        newshift.setStartlength(shift.getStartlength());
                        newshift.setShiftdate(shift.getShiftdate());
                        newshift.setShiftnow(0L); //过去的统一设为0
                        newshift.setUpdatetime(new Date());
                        newshift.setRemark("织轴上轴分割班次，新增");
                        mftShiftService.insertMftShift(newshift);
                        //旧的织轴闭合  长度重新算了  startLength + shiftLength
                        if (mftshaftQuery.getId()!=null){//有织轴 才处理
                            mftshaftQuery.setClothlength(newshift.getShiftlength().add(newshift.getStartlength()));
                            double remainLength = mftshaftQuery.getShaftlength().doubleValue()-(mftshaftQuery.getClothlength().doubleValue()/(1-mftshaftQuery.getPdtshrinkage().doubleValue()/100));//总长减去 织这么长的布需要多长织轴；
                            mftshaftQuery.setShaftremainlength(new BigDecimal(remainLength).setScale(2,BigDecimal.ROUND_HALF_UP));
                            mftshaftQuery.setActend(mftShaft.getActstart());
                            mftshaftQuery.setShaftstatus("已了机");
                            mftshaftQuery.setUpdatetime(new Date());
                            mftshaftQuery.setRemark("下一个上轴对此织轴了机");
                            mftShaftService.updateMftShaft(mftshaftQuery);
                        }
                        //如果是在中间的话是这么计算 updateShift的 updateShift只的是新的织轴了
                        updateShift = alldataService.getShift(machine.getMaccode(), mftShaft.getActstart(),shift.getShiftendtime(), shaft);
                    }else{
                        updateShift = alldataService.getShift(machine.getMaccode(), shift.getShiftstarttime(),shift.getShiftendtime(), shaft);
                    }
                    // 更改之前shift
                    updateShift.setShifttype(shift.getShifttype());
                    updateShift.setStartlength(new BigDecimal(weavingLength).setScale(2,BigDecimal.ROUND_HALF_UP));
                    updateShift.setShiftdate(shift.getShiftdate());
                    updateShift.setShiftnow(shift.getShiftnow()); //保留目前的状态
                    updateShift.setUpdatetime(new Date());
                    updateShift.setRemark("织轴上轴分割班次，更新");
                    updateShift.setId(shift.getId());
                    mftShiftService.updateMftShift(updateShift);
                    weavingLength = weavingLength +updateShift.getShiftlength().doubleValue();
                }
                //更新当前织轴信息
                shaft.setClothlength(new BigDecimal(weavingLength).setScale(2,BigDecimal.ROUND_HALF_UP));
                double remainLength = shaft.getShaftlength().doubleValue()-(weavingLength/(1-shaft.getPdtshrinkage().doubleValue()/100));//总长减去 织这么长的布需要多长织轴；
                shaft.setShaftremainlength(new BigDecimal(remainLength).setScale(2, BigDecimal.ROUND_HALF_UP));
                Long during = new Date().getTime()-shaft.getActstart().getTime();
                if (weavingLength!=0){
                    double planTime = during * (remainLength / weavingLength);
                    Date planDate = new Date(new Date().getTime() + new Double(planTime).longValue());
                    shaft.setPlanend(planDate);
                }
                mftShaftService.updateMftShaft(shaft);
                //更新织机信息
                machine.setPdtcode(shaft.getPdtcode());
                machine.setOrdercode(shaft.getOrdercode());
                machine.setShaftcode(shaft.getShaftcode());
                machine.setWeftdensity(shaft.getPdtweftdensity());
                machine.setWeavinglength(shaft.getClothlength());
                machine.setUpdatetime(new Date());
                macMachineService.updateMacMachine(machine);
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
