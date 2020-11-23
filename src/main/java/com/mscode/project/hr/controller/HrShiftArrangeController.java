package com.mscode.project.hr.controller;

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
import com.mscode.project.hr.domain.HrShiftArrange;
import com.mscode.project.hr.service.IHrShiftArrangeService;
import com.mscode.framework.web.controller.BaseController;
import com.mscode.framework.web.domain.AjaxResult;
import com.mscode.common.utils.poi.ExcelUtil;
import com.mscode.framework.web.page.TableDataInfo;

/**
 * 班次排班Controller
 * 
 * @author mscode
 * @date 2020-11-17
 */
@RestController
@RequestMapping("/hr/shiftarrange")
public class HrShiftArrangeController extends BaseController
{
    @Autowired
    private IHrShiftArrangeService hrShiftArrangeService;

    /**
     * 查询班次排班列表
     */
    @PreAuthorize("@ss.hasPermi('hr:shiftarrange:list')")
    @GetMapping("/list")
    public TableDataInfo list(HrShiftArrange hrShiftArrange)
    {
        startPage();
        List<HrShiftArrange> list = hrShiftArrangeService.selectHrShiftArrangeList(hrShiftArrange);
        return getDataTable(list);
    }

    /**
     * 导出班次排班列表
     */
    @PreAuthorize("@ss.hasPermi('hr:shiftarrange:export')")
    @Log(title = "班次排班", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HrShiftArrange hrShiftArrange)
    {
        List<HrShiftArrange> list = hrShiftArrangeService.selectHrShiftArrangeList(hrShiftArrange);
        ExcelUtil<HrShiftArrange> util = new ExcelUtil<HrShiftArrange>(HrShiftArrange.class);
        return util.exportExcel(list, "shiftarrange");
    }

    /**
     * 获取班次排班详细信息
     */
    @PreAuthorize("@ss.hasPermi('hr:shiftarrange:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(hrShiftArrangeService.selectHrShiftArrangeById(id));
    }

    /**
     * 新增班次排班
     */
    @PreAuthorize("@ss.hasPermi('hr:shiftarrange:add')")
    @Log(title = "班次排班", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrShiftArrange hrShiftArrange)
    {
        return toAjax(hrShiftArrangeService.insertHrShiftArrange(hrShiftArrange));
    }

    /**
     * 修改班次排班
     */
    @PreAuthorize("@ss.hasPermi('hr:shiftarrange:edit')")
    @Log(title = "班次排班", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrShiftArrange hrShiftArrange)
    {
        return toAjax(hrShiftArrangeService.updateHrShiftArrange(hrShiftArrange));
    }

    /**
     * 删除班次排班
     */
    @PreAuthorize("@ss.hasPermi('hr:shiftarrange:remove')")
    @Log(title = "班次排班", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hrShiftArrangeService.deleteHrShiftArrangeByIds(ids));
    }
}
