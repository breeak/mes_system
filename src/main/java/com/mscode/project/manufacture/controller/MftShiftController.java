package com.mscode.project.manufacture.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mscode.common.utils.DateUtils;
import com.mscode.project.manufacture.domain.Alldata;
import com.mscode.project.manufacture.domain.MftShaft;
import com.mscode.project.manufacture.service.IAlldataService;
import com.mscode.project.manufacture.service.IMftShaftService;
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
    @Autowired
    private IAlldataService alldataService;
    @Autowired
    private IMftShaftService mftShaftService;
    /**
     * 查询班次效率列表
     * 如果要查询合并 请使用groupby
     */
    @PreAuthorize("@ss.hasPermi('manufacture:shift:list')")
    @GetMapping("/list")
    public TableDataInfo list(MftShift mftShift, @RequestParam Map<String,Object> params)
    {
        startPage();
        mftShift.setParams(params);
        if (params!=null){
            // 这样是按照班次和织机类型查询的
            if (params.get("shiftdate")!=null && params.get("maccode")!=null && params.get("groupby")!=null){
                List<MftShift> list = mftShiftService.selectMftShiftList(mftShift);//只有一天的三个班 或者不到三个班；
                List<Alldata> alldataList = new ArrayList<>();
                params.put("groupby", null);//先不分组查 查出所有的shift;
                mftShift.setParams(params);
                List<MftShift> allList = mftShiftService.selectMftShiftList(mftShift);// 这里应该有按上轴时间分割的班次数据
                for (MftShift shift : allList) {//只有一个品种了 为的是得到织轴长度 每一个的开始结束时间
                    MftShaft mftShaft = new MftShaft();
                    mftShaft.setShaftcode(shift.getShaftcodes());
                    List<MftShaft> mftShafts = mftShaftService.selectMftShaftList(mftShaft);
                    if (mftShafts.size()==1){
                        mftShaft = mftShafts.get(0);
                    }else{
                        mftShaft.setPdtweftdensity(new BigDecimal(100));//TODO 给一个默认的纬密值 先默认是100
                        mftShaft.setPdtcode("未上轴");
                    }
                    alldataList = alldataService.getShiftDetails(shift.getMiddleno(),shift.getStationno(), shift.getShiftstarttime(), shift.getShiftendtime(), mftShaft);
                    for (MftShift newshift : list) {//只有一天的三个班 或者不到三个班；可能没有
                        if (shift.getShifttype().equals(newshift.getShifttype())){//相等的放到一起
                            if (newshift.getAlldataList()==null){
                                newshift.setAlldataList(alldataList);
                            }else{
                                alldataList.addAll(newshift.getAlldataList() );
                                newshift.setAlldataList(alldataList);
                            }
                        }
                    }
                }
                return getDataTable(list);
            }
        }
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
    @GetMapping("/recent/{days}/{maccode}")
    public AjaxResult shiftRecent(@PathVariable Integer days,@PathVariable String maccode)
    {
        return AjaxResult.success(mftShiftService.listRecentDays(days, maccode));
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
