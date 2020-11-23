package com.mscode.project.hr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mscode.project.hr.domain.HrWorker;
import com.mscode.project.hr.service.IHrWorkerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mscode.framework.aspectj.lang.annotation.Log;
import com.mscode.framework.aspectj.lang.enums.BusinessType;
import com.mscode.project.hr.domain.HrMacWorker;
import com.mscode.project.hr.service.IHrMacWorkerService;
import com.mscode.framework.web.controller.BaseController;
import com.mscode.framework.web.domain.AjaxResult;
import com.mscode.common.utils.poi.ExcelUtil;
import com.mscode.framework.web.page.TableDataInfo;

/**
 * 员工划片Controller
 * 
 * @author mscode
 * @date 2020-11-17
 */
@RestController
@RequestMapping("/hr/macworker")
public class HrMacWorkerController extends BaseController
{
    @Autowired
    private IHrMacWorkerService hrMacWorkerService;
    @Autowired
    private IHrWorkerService hrWorkerService;
    /**
     * 查询员工划片列表
     */
    @PreAuthorize("@ss.hasPermi('hr:macworker:list')")
    @GetMapping("/list")
    public TableDataInfo list(HrMacWorker hrMacWorker, @RequestParam Map<String,Object> params)
    {
        startPage();
        hrMacWorker.setParams(params);
        List<HrMacWorker> list = hrMacWorkerService.selectHrMacWorkerList(hrMacWorker);
        List<HrWorker> workers = new ArrayList<>();
        if (params!=null && params.get("groupby").equals("workerid")){
            for (HrMacWorker macWorker : list) {
                HrWorker hrWorker = hrWorkerService.selectHrWorkerById(macWorker.getWorkerid());
                workers.add(hrWorker);
            }
            return getDataTable(workers);
        }
        return getDataTable(list);
    }

    /**
     * 导出员工划片列表
     */
    @PreAuthorize("@ss.hasPermi('hr:macworker:export')")
    @Log(title = "员工划片", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HrMacWorker hrMacWorker)
    {
        List<HrMacWorker> list = hrMacWorkerService.selectHrMacWorkerList(hrMacWorker);
        ExcelUtil<HrMacWorker> util = new ExcelUtil<HrMacWorker>(HrMacWorker.class);
        return util.exportExcel(list, "macworker");
    }

    /**
     * 获取员工划片详细信息
     */
    @PreAuthorize("@ss.hasPermi('hr:macworker:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(hrMacWorkerService.selectHrMacWorkerById(id));
    }

    /**
     * 新增员工划片
     */
    @PreAuthorize("@ss.hasPermi('hr:macworker:add')")
    @Log(title = "员工划片", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrMacWorker hrMacWorker)
    {
        return toAjax(hrMacWorkerService.insertHrMacWorker(hrMacWorker));
    }

    /**
     * 修改员工划片
     */
    @PreAuthorize("@ss.hasPermi('hr:macworker:edit')")
    @Log(title = "员工划片", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrMacWorker hrMacWorker)
    {
        return toAjax(hrMacWorkerService.updateHrMacWorker(hrMacWorker));
    }

    /**
     * 删除员工划片
     */
    @PreAuthorize("@ss.hasPermi('hr:macworker:remove')")
    @Log(title = "员工划片", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hrMacWorkerService.deleteHrMacWorkerByIds(ids));
    }
}
