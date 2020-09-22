package com.mscode.project.manufacture.controller;

import java.util.List;
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
import com.mscode.project.manufacture.domain.MacMachine;
import com.mscode.project.manufacture.service.IMacMachineService;
import com.mscode.framework.web.controller.BaseController;
import com.mscode.framework.web.domain.AjaxResult;
import com.mscode.common.utils.poi.ExcelUtil;
import com.mscode.framework.web.page.TableDataInfo;

/**
 * 织机列表Controller
 * 
 * @author MS
 * @date 2020-09-16
 */
@RestController
@RequestMapping("/manufacture/machine")
public class MacMachineController extends BaseController
{
    @Autowired
    private IMacMachineService macMachineService;

    /**
     * 查询织机列表列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:machine:list')")
    @GetMapping("/list")
    public TableDataInfo list(MacMachine macMachine)
    {
        startPage();
        List<MacMachine> list = macMachineService.selectMacMachineList(macMachine);
        return getDataTable(list);
    }

    /**
     * 导出织机列表列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:machine:export')")
    @Log(title = "织机列表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MacMachine macMachine)
    {
        List<MacMachine> list = macMachineService.selectMacMachineList(macMachine);
        ExcelUtil<MacMachine> util = new ExcelUtil<MacMachine>(MacMachine.class);
        return util.exportExcel(list, "machine");
    }

    /**
     * 获取织机列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('manufacture:machine:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(macMachineService.selectMacMachineById(id));
    }

    /**
     * 新增织机列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:machine:add')")
    @Log(title = "织机列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MacMachine macMachine)
    {
        return toAjax(macMachineService.insertMacMachine(macMachine));
    }

    /**
     * 修改织机列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:machine:edit')")
    @Log(title = "织机列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MacMachine macMachine)
    {
        return toAjax(macMachineService.updateMacMachine(macMachine));
    }

    /**
     * 删除织机列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:machine:remove')")
    @Log(title = "织机列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(macMachineService.deleteMacMachineByIds(ids));
    }
}
