package com.mscode.project.manufacture.controller;

import java.util.Date;
import java.util.List;

import com.mscode.project.manufacture.domain.MacMachine;
import com.mscode.project.manufacture.domain.MftShift;
import com.mscode.project.manufacture.service.IMacMachineService;
import com.mscode.project.manufacture.service.IMftShiftService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    /**
     * 查询织轴列表列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shaft:list')")
    @GetMapping("/list")
    public TableDataInfo list(MftShaft mftShaft)
    {
        startPage();
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
    @PutMapping("/doShangZhou")
    public AjaxResult doShangZhou(@RequestBody MftShaft mftShaft)
    {
        MftShaft mftshaftQuery = new MftShaft();
        mftshaftQuery.setShaftcode(mftShaft.getShaftcode());
        List<MftShaft> mftShafts = mftShaftService.selectMftShaftList(mftshaftQuery);
        if (mftShafts.size()==1){
            MftShaft shaft =mftShafts.get(0);
            shaft.setShaftstatus("1");
            shaft.setUpdatetime(new Date());
            shaft.setActstart(mftShaft.getActstart());
            shaft.setActmaccode(mftShaft.getActmaccode());

            // 更新织机信息
            MacMachine machine = new MacMachine();
            machine.setMaccode(mftShaft.getActmaccode());
            List<MacMachine> macMachines = macMachineService.selectMacMachineList(machine);
            if (macMachines.size()==1){
                machine = macMachines.get(0);
                machine.setPdtcode(shaft.getPdtcode());
                machine.setOrdercode(shaft.getOrdercode());
                machine.setShaftcode(shaft.getShaftcode());
                machine.setUpdatetime(new Date());

                // 更新班次信息
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
