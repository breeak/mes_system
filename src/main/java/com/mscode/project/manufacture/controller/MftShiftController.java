package com.mscode.project.manufacture.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mscode.framework.aspectj.lang.annotation.Log;
import com.mscode.framework.aspectj.lang.enums.BusinessType;
import com.mscode.project.manufacture.domain.MftShift;
import com.mscode.project.manufacture.service.IMftShiftService;
import com.mscode.framework.web.controller.BaseController;
import com.mscode.framework.web.domain.AjaxResult;
import com.mscode.common.utils.poi.ExcelUtil;
import com.mscode.framework.web.page.TableDataInfo;

/**
 * 班次效率Controller
 * 
 * @author MS
 * @date 2020-09-20
 */
@RestController
@RequestMapping("/manufacture/shift")
public class MftShiftController extends BaseController
{
    @Autowired
    private IMftShiftService mftShiftService;

    /**
     * 查询班次效率列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shift:list')")
    @GetMapping("/list")
    public TableDataInfo list(MftShift mftShift, @RequestParam Map<String,Object> params)
    {
        startPage();
        mftShift.setParams(params);
        List<MftShift> list = mftShiftService.selectMftShiftList(mftShift);
        return getDataTable(list);
    }

    /**
     * 查询班次最近days生产情况
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shift:list')")
    @GetMapping("/recent/{days}")
    public AjaxResult shiftRecent(@PathVariable Integer days)
    {
        return AjaxResult.success(mftShiftService.listRecentDays(days));
    }

    /**
     * 导出班次效率列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shift:export')")
    @Log(title = "班次效率", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MftShift mftShift)
    {
        List<MftShift> list = mftShiftService.selectMftShiftList(mftShift);
        ExcelUtil<MftShift> util = new ExcelUtil<MftShift>(MftShift.class);
        return util.exportExcel(list, "shift");
    }

    /**
     * 获取班次效率详细信息
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shift:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(mftShiftService.selectMftShiftById(id));
    }

    /**
     * 新增班次效率
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shift:add')")
    @Log(title = "班次效率", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MftShift mftShift)
    {
        return toAjax(mftShiftService.insertMftShift(mftShift));
    }

    /**
     * 修改班次效率
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shift:edit')")
    @Log(title = "班次效率", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MftShift mftShift)
    {
        return toAjax(mftShiftService.updateMftShift(mftShift));
    }

    /**
     * 删除班次效率
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shift:remove')")
    @Log(title = "班次效率", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mftShiftService.deleteMftShiftByIds(ids));
    }
}
