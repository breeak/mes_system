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
import com.mscode.project.manufacture.domain.Alldata;
import com.mscode.project.manufacture.service.IAlldataService;
import com.mscode.framework.web.controller.BaseController;
import com.mscode.framework.web.domain.AjaxResult;
import com.mscode.common.utils.poi.ExcelUtil;
import com.mscode.framework.web.page.TableDataInfo;

/**
 * 基础数据Controller
 * 
 * @author mscode
 * @date 2020-11-05
 */
@RestController
@RequestMapping("/manufacture/alldata")
public class AlldataController extends BaseController
{
    @Autowired
    private IAlldataService alldataService;

    /**
     * 查询基础数据列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:alldata:list')")
    @GetMapping("/list")
    public TableDataInfo list(Alldata alldata)
    {
        startPage();
        List<Alldata> list = alldataService.selectAlldataList(alldata);
        return getDataTable(list);
    }

    /**
     * 导出基础数据列表
     */
    @PreAuthorize("@ss.hasPermi('manufacture:alldata:export')")
    @Log(title = "基础数据", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Alldata alldata)
    {
        List<Alldata> list = alldataService.selectAlldataList(alldata);
        ExcelUtil<Alldata> util = new ExcelUtil<Alldata>(Alldata.class);
        return util.exportExcel(list, "alldata");
    }

    /**
     * 获取基础数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('manufacture:alldata:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(alldataService.selectAlldataById(id));
    }

    /**
     * 新增基础数据
     */
    @PreAuthorize("@ss.hasPermi('manufacture:alldata:add')")
    @Log(title = "基础数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Alldata alldata)
    {
        return toAjax(alldataService.insertAlldata(alldata));
    }

    /**
     * 修改基础数据
     */
    @PreAuthorize("@ss.hasPermi('manufacture:alldata:edit')")
    @Log(title = "基础数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Alldata alldata)
    {
        return toAjax(alldataService.updateAlldata(alldata));
    }

    /**
     * 删除基础数据
     */
    @PreAuthorize("@ss.hasPermi('manufacture:alldata:remove')")
    @Log(title = "基础数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(alldataService.deleteAlldataByIds(ids));
    }
}
