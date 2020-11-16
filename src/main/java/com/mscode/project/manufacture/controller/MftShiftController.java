package com.mscode.project.manufacture.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mscode.common.utils.DateUtils;
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
     * 查询班次效率列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shift:update')")
    @GetMapping("/updateShiftAny")
    public AjaxResult updateShiftAny(MftShift mftShift, @RequestParam Map<String,Object> params) throws Exception
    {
        mftShift.setParams(params);
        // 获取当前班次 是哪一班  如果是最近三班的话 要设为还是最近三班  先看有没这一班次的数据 没有的话shiftNow 设为0 自己建立 有的话就查询放进params里
        MftShift mftShift1 = new MftShift();
        String shiftDateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, mftShift.getShiftdate());
        mftShift1.setShiftdate(DateUtils.parseDate(shiftDateStr,DateUtils.YYYY_MM_DD));
        mftShift1.setShifttype(mftShift.getShifttype());
        List<MftShift> mftShifts = mftShiftService.selectMftShiftList(mftShift1);
        Long shiftNow = 0l;
        if (mftShifts.size()>0){
            shiftNow=mftShifts.get(0).getShiftnow();
        }
        try {
            mftShiftService.checkNow(mftShift.getShifttype(),shiftNow,DateUtils.parseDate(shiftDateStr,DateUtils.YYYY_MM_DD));
            return AjaxResult.success("请刷新页面");
        }catch (Exception e){
            return AjaxResult.error("更新失败");
        }
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
