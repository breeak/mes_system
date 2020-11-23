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
import com.mscode.project.hr.domain.HrWorker;
import com.mscode.project.hr.service.IHrWorkerService;
import com.mscode.framework.web.controller.BaseController;
import com.mscode.framework.web.domain.AjaxResult;
import com.mscode.common.utils.poi.ExcelUtil;
import com.mscode.framework.web.page.TableDataInfo;

/**
 * 员工列表Controller
 * 
 * @author mscode
 * @date 2020-11-17
 */
@RestController
@RequestMapping("/hr/worker")
public class HrWorkerController extends BaseController
{
    @Autowired
    private IHrWorkerService hrWorkerService;

    /**
     * 查询员工列表列表
     */
    @PreAuthorize("@ss.hasPermi('hr:worker:list')")
    @GetMapping("/list")
    public TableDataInfo list(HrWorker hrWorker)
    {
        startPage();
        List<HrWorker> list = hrWorkerService.selectHrWorkerList(hrWorker);
        return getDataTable(list);
    }

    /**
     * 导出员工列表列表
     */
    @PreAuthorize("@ss.hasPermi('hr:worker:export')")
    @Log(title = "员工列表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HrWorker hrWorker)
    {
        List<HrWorker> list = hrWorkerService.selectHrWorkerList(hrWorker);
        ExcelUtil<HrWorker> util = new ExcelUtil<HrWorker>(HrWorker.class);
        return util.exportExcel(list, "worker");
    }

    /**
     * 获取员工列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('hr:worker:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(hrWorkerService.selectHrWorkerById(id));
    }

    /**
     * 新增员工列表
     */
    @PreAuthorize("@ss.hasPermi('hr:worker:add')")
    @Log(title = "员工列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrWorker hrWorker)
    {
        return toAjax(hrWorkerService.insertHrWorker(hrWorker));
    }

    /**
     * 修改员工列表
     */
    @PreAuthorize("@ss.hasPermi('hr:worker:edit')")
    @Log(title = "员工列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrWorker hrWorker)
    {
        return toAjax(hrWorkerService.updateHrWorker(hrWorker));
    }

    /**
     * 删除员工列表
     */
    @PreAuthorize("@ss.hasPermi('hr:worker:remove')")
    @Log(title = "员工列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hrWorkerService.deleteHrWorkerByIds(ids));
    }
}
