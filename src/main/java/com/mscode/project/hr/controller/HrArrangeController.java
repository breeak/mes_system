package com.mscode.project.hr.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import com.mscode.common.utils.DateUtils;
import com.mscode.common.utils.StringUtils;
import com.mscode.project.hr.domain.HrMacWorker;
import com.mscode.project.hr.service.IHrMacWorkerService;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mscode.framework.aspectj.lang.annotation.Log;
import com.mscode.framework.aspectj.lang.enums.BusinessType;
import com.mscode.project.hr.domain.HrArrange;
import com.mscode.project.hr.service.IHrArrangeService;
import com.mscode.framework.web.controller.BaseController;
import com.mscode.framework.web.domain.AjaxResult;
import com.mscode.common.utils.poi.ExcelUtil;
import com.mscode.framework.web.page.TableDataInfo;

/**
 * 排班信息Controller
 * 
 * @author mscode
 * @date 2020-11-17
 */
@RestController
@RequestMapping("/hr/arrange")
public class HrArrangeController extends BaseController
{
    @Autowired
    private IHrArrangeService hrArrangeService;
    @Autowired
    private IHrMacWorkerService hrMacWorkerService;
    /**
     * 查询排班信息列表
     */
    @PreAuthorize("@ss.hasPermi('hr:arrange:list')")
    @GetMapping("/list")
    public TableDataInfo list(HrArrange hrArrange, @RequestParam Map<String,Object> params)
    {
        startPage();
        hrArrange.setParams(params);
        List<HrArrange> list = hrArrangeService.selectHrArrangeList(hrArrange);
        return getDataTable(list);
    }

    /**
     * 导出排班信息列表
     */
    @PreAuthorize("@ss.hasPermi('hr:arrange:export')")
    @Log(title = "排班信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HrArrange hrArrange)
    {
        List<HrArrange> list = hrArrangeService.selectHrArrangeList(hrArrange);
        ExcelUtil<HrArrange> util = new ExcelUtil<HrArrange>(HrArrange.class);
        return util.exportExcel(list, "arrange");
    }

    /**
     * 获取排班信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('hr:arrange:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(hrArrangeService.selectHrArrangeById(id));
    }

    /**
     * 新增排班信息
     */
    @PreAuthorize("@ss.hasPermi('hr:arrange:add')")
    @Log(title = "排班信息", businessType = BusinessType.INSERT)
    @PostMapping("/part")
    public AjaxResult addpart(@RequestBody HrArrange hrArrange)
    {
        //TODO 测试新增  先做上轴等的测试 暂停 并提交了
        List<HrMacWorker> macworkerList = hrArrange.getMacworkerList();//必定存在macworkerList 有一个；
        Long workerid = macworkerList.get(0).getWorkerid();//获取安排的是哪一个员工id
        Date arrangestart = hrArrange.getArrangestart();
        Date arrangeend = hrArrange.getArrangeend();
        //params 必不能空
        Map<String, Object> params = hrArrange.getParams();
        Object isever = params.get("isever");
        Object type = params.get("type");
        Object oldclass = params.get("oldclass"); // 这是这个人本来的班次
        Object otherclass = params.get("otherclass");
        Object otherworkerid = params.get("otherworkerid");
        if (isever!=null && "true".equals(isever)){// 永久排班 设定好最大日期；
            arrangestart = new Date();//TODO 永久排班目前不能同时改变已经发生的排班；目前不能,所以都设置为当前日期开始；可以考虑同时增加目前排班；
            arrangeend = DateUtils.MAX_DATE;
        }
        hrArrange.setArrangeno("排班"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        //仅仅是安排区域，那么只需要查出他这个类别的当前状态 对当前状态下的负责织机信息改变
        // 判断是否有这个人的了，有了的话就吧原来负责的重复区域删除掉，重新生成，
        // 使用的是oldclass; oldclass应该必定有才行；
        if (oldclass!=null){
            // 查询这个新的班次类别； 按班次类别来保存
            // 查询与要新增的状态有重合的所有状态；查询状态应该是 beginTime<arrangeEnd; endTime>arrangeStart;oldclass;
            HrArrange arrangeQuery = new HrArrange();
            arrangeQuery.setArrangeclass(oldclass.toString()); // 这是这个人本来的班次
            Map<String, Object> params1 = new HashMap<>();
            params1.put("beginTime", arrangestart);
            params1.put("endTime",arrangeend);
            arrangeQuery.setParams(params1);
            List<HrArrange> hrArranges = hrArrangeService.selectHrArrangeList(arrangeQuery);
            if (hrArranges.size()==0){// 这一时间区段这个类别没有安排 直接增加一个arrange即可
                hrArrange.setArrangestart(arrangestart);
                hrArrange.setArrangeend(arrangeend);
                hrArrange.setArrangeclass(oldclass.toString());
                ArrayList<Long> ids = new ArrayList<>();
                for (HrMacWorker hrMacWorker : macworkerList) {
                    hrMacWorkerService.insertHrMacWorker(hrMacWorker);//要先存起来才能得到id
                    ids.add(hrMacWorker.getId());
                }
                String idstrs = StringUtils.join(ids,",");
                hrArrange.setMacworkerids(idstrs);
                hrArrange.setStatus("1");
                hrArrangeService.insertHrArrange(hrArrange);
            }
            List<HrArrange> hrArrangesOther = new ArrayList<>();
            if(otherclass!=null){// 只有代班或者换班才会出现。 代班 原来的不处理（新增加在另一个类别中） 换班两者都处理 两个类别都处理
                arrangeQuery.setArrangeclass(otherclass.toString()); // params 未改变 未初始化
                hrArrangesOther = hrArrangeService.selectHrArrangeList(arrangeQuery);
            }
            // 安排区域（复制某个人区域或新定义区域）、代班（替换某个人排班）、换班（二者交换）
            // 安排区域 换班 都需要调整这个人原有的安排
            // 对重合的所有状态的时间进行分割处理； 时间要弄好；
            if (type!=null && !"代班".equals(type)) { // 代班原有的不需要做任何处理
                for (HrArrange currentArrange : hrArranges) {// 这都是这个人所在类别 或者说要安排的类别
                    Date arrangestartc = currentArrange.getArrangestart();
                    Date arrangeendc = currentArrange.getArrangeend();
                            // 先处理中间 新增 两边直接Copy
                            HrArrange hrArrange1 = new HrArrange();
                            if (arrangestartc.getTime() < arrangestart.getTime()) {
                                hrArrange1.setArrangestart(arrangestart);
                            }else{
                                hrArrange1.setArrangestart(arrangestartc);
                            }
                            if (arrangeendc.getTime() > arrangeend.getTime()) {//这是原来的两边都多出来  小大小
                                hrArrange1.setArrangeend(arrangeend);
                            }else{
                                hrArrange1.setArrangeend(arrangeendc);
                            }
                            hrArrange1.setArrangeclass(oldclass.toString());
                            hrArrange1.setArrangeno(hrArrange.getArrangeno());// 这个no 可以自动生成 再说 其他先再说
                            // 处理重合的员工状态 macworkerList
                            List<HrMacWorker> currentMacworkerList = currentArrange.getMacworkerList();
                            for (HrMacWorker hrMacWorker : currentMacworkerList) {
                                if (type != null && "安排区域".equals(type)) {
                                    //当前状态里 那么删掉这个人的原有的重合状态 重新生成
                                    if (hrMacWorker.getWorkerid().equals(workerid)) {//把安排的员工织机中  这个员工的排班全部删除；
                                        hrMacWorkerService.deleteHrMacWorkerById(hrMacWorker.getWorkerid());
                                    }
                                } else if (type != null && "换班".equals(type)) {
                                    //当前状态里 那么删掉那个人的原有的状态  在 另一个类别里 增加这个人的
                                    if (hrMacWorker.getWorkerid().equals(otherworkerid)) {//
                                        hrMacWorkerService.deleteHrMacWorkerById(hrMacWorker.getWorkerid());
                                    }
                                }
                            }
                            // 把这个人要新增的状态增加 返回自增的id 存到
                            ArrayList<Long> ids = new ArrayList<>();
                            for (HrMacWorker newMacWorker : macworkerList) {
                                hrMacWorkerService.insertHrMacWorker(newMacWorker);
                                ids.add(newMacWorker.getId());
                            }
                            String idstr = StringUtils.join(ids, ",");
                            hrArrange1.setMacworkerids(idstr);
                            hrArrangeService.insertHrArrange(hrArrange1);
                            // 处理两边的状态
                            if (arrangeendc.getTime() > arrangeend.getTime()) {
                                // 两边的状态 新增 右侧的一个
                                currentArrange.setArrangestart(arrangeendc);
                                currentArrange.setArrangeend(arrangeend);
                                hrArrangeService.updateHrArrange(currentArrange);
                            }else{
                                // 两边的状态 新增 右侧的一个
                                hrArrange1.setArrangestart(arrangeendc);
                                hrArrange1.setArrangeend(arrangeend);
                                hrArrangeService.insertHrArrange(hrArrange1);
                            }
                            if (arrangestartc.getTime() < arrangestart.getTime()) {
                                // 两边的状态 修改 左侧的状态
                                currentArrange.setArrangestart(arrangestartc);
                                currentArrange.setArrangeend(arrangestart);
                                hrArrangeService.updateHrArrange(currentArrange);
                            }else{
                                // 两边的状态 新增 左侧的状态
                                hrArrange1.setArrangestart(arrangestart);
                                hrArrange1.setArrangeend(arrangestartc);
                                hrArrangeService.insertHrArrange(hrArrange1);
                            }
                }
            }
            if (type!=null && !"安排区域".equals(type)) { // 代班原有的不需要做任何处理
                for (HrArrange currentArrange : hrArrangesOther) {// 这都是这个人所在类别 或者说要安排的类别
                    Date arrangestartc = currentArrange.getArrangestart();
                    Date arrangeendc = currentArrange.getArrangeend();
                    // 先处理中间 新增 两边直接Copy
                    HrArrange hrArrange1 = new HrArrange();
                    if (arrangestartc.getTime() < arrangestart.getTime()) {
                        hrArrange1.setArrangestart(arrangestart);
                    }else{
                        hrArrange1.setArrangestart(arrangestartc);
                    }
                    if (arrangeendc.getTime() > arrangeend.getTime()) {//这是原来的两边都多出来  小大小
                        hrArrange1.setArrangeend(arrangeend);
                    }else{
                        hrArrange1.setArrangeend(arrangeendc);
                    }
                    hrArrange1.setArrangeclass(otherclass.toString());
                    hrArrange1.setArrangeno(hrArrange.getArrangeno());// 这个no 可以自动生成 再说 其他先再说
                    // 处理重合的员工状态 macworkerList
                    List<HrMacWorker> currentMacworkerList = currentArrange.getMacworkerList();
                    for (HrMacWorker hrMacWorker : currentMacworkerList) {
                        if (type != null && "换班".equals(type)) {
                            //那个人的原有的重合状态 换成这个人 不用删除啊
                            if (hrMacWorker.getWorkerid().equals(otherworkerid)) {
                                hrMacWorker.setWorkerid((Long)workerid);
                                hrMacWorkerService.updateHrMacWorker(hrMacWorker);
                            }
                        } else if (type != null && "代班".equals(type)) {
                            //当前状态里 那么删掉那个人的原有的状态  在 另一个类别里 增加这个人的
                            if (hrMacWorker.getWorkerid().equals(otherworkerid)) {//
                                hrMacWorkerService.deleteHrMacWorkerById(hrMacWorker.getWorkerid());
                            }
                            // 把这个人要新增的状态增加 返回自增的id 存到
                            ArrayList<Long> ids = new ArrayList<>();
                            for (HrMacWorker newMacWorker : macworkerList) {
                                hrMacWorkerService.insertHrMacWorker(newMacWorker);
                                ids.add(newMacWorker.getId());
                            }
                            String idstr = StringUtils.join(ids, ",");
                            hrArrange1.setMacworkerids(idstr);
                            hrArrangeService.insertHrArrange(hrArrange1);
                        }
                    }
                    // 处理两边的状态
                    if (arrangeendc.getTime() > arrangeend.getTime()) {
                        // 两边的状态 新增 右侧的一个
                        currentArrange.setArrangestart(arrangeendc);
                        currentArrange.setArrangeend(arrangeend);
                        hrArrangeService.updateHrArrange(currentArrange);
                    }else{
                        // 两边的状态 新增 右侧的一个
                        hrArrange1.setArrangestart(arrangeendc);
                        hrArrange1.setArrangeend(arrangeend);
                        hrArrangeService.insertHrArrange(hrArrange1);
                    }
                    if (arrangestartc.getTime() < arrangestart.getTime()) {
                        // 两边的状态 修改 左侧的状态
                        currentArrange.setArrangestart(arrangestartc);
                        currentArrange.setArrangeend(arrangestart);
                        hrArrangeService.updateHrArrange(currentArrange);
                    }else{
                        // 两边的状态 新增 左侧的状态
                        hrArrange1.setArrangestart(arrangestart);
                        hrArrange1.setArrangeend(arrangestartc);
                        hrArrangeService.insertHrArrange(hrArrange1);
                    }
                }
            }
        }

        // 查询重合的排班 arrangestart< end;
        HrArrange arrangeQuery = new HrArrange();
        arrangeQuery.setArrangeclass(oldclass.toString()); // 这是这个人本来的班次
        List<HrArrange> hrArranges = hrArrangeService.selectHrArrangeList(arrangeQuery);
        return AjaxResult.success(hrArranges);
    }

    /**
     * 修改排班信息
     */
    @PreAuthorize("@ss.hasPermi('hr:arrange:edit')")
    @Log(title = "排班信息", businessType = BusinessType.UPDATE)
    @PutMapping("/part")
    public AjaxResult editpart(@RequestBody HrArrange hrArrange)
    {
        return toAjax(hrArrangeService.updateHrArrange(hrArrange));
    }


    /**
     * 新增排班信息
     */
    @PreAuthorize("@ss.hasPermi('hr:arrange:add')")
    @Log(title = "排班信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrArrange hrArrange)
    {
        return toAjax(hrArrangeService.insertHrArrange(hrArrange));
    }

    /**
     * 修改排班信息
     */
    @PreAuthorize("@ss.hasPermi('hr:arrange:edit')")
    @Log(title = "排班信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrArrange hrArrange)
    {
        return toAjax(hrArrangeService.updateHrArrange(hrArrange));
    }

    /**
     * 删除排班信息
     */
    @PreAuthorize("@ss.hasPermi('hr:arrange:remove')")
    @Log(title = "排班信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hrArrangeService.deleteHrArrangeByIds(ids));
    }
}
