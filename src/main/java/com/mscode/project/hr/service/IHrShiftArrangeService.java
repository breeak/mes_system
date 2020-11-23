package com.mscode.project.hr.service;

import java.util.List;
import com.mscode.project.hr.domain.HrShiftArrange;

/**
 * 班次排班Service接口
 * 
 * @author mscode
 * @date 2020-11-17
 */
public interface IHrShiftArrangeService 
{
    /**
     * 查询班次排班
     * 
     * @param id 班次排班ID
     * @return 班次排班
     */
    public HrShiftArrange selectHrShiftArrangeById(Long id);

    /**
     * 查询班次排班列表
     * 
     * @param hrShiftArrange 班次排班
     * @return 班次排班集合
     */
    public List<HrShiftArrange> selectHrShiftArrangeList(HrShiftArrange hrShiftArrange);

    /**
     * 新增班次排班
     * 
     * @param hrShiftArrange 班次排班
     * @return 结果
     */
    public int insertHrShiftArrange(HrShiftArrange hrShiftArrange);

    /**
     * 修改班次排班
     * 
     * @param hrShiftArrange 班次排班
     * @return 结果
     */
    public int updateHrShiftArrange(HrShiftArrange hrShiftArrange);

    /**
     * 批量删除班次排班
     * 
     * @param ids 需要删除的班次排班ID
     * @return 结果
     */
    public int deleteHrShiftArrangeByIds(Long[] ids);

    /**
     * 删除班次排班信息
     * 
     * @param id 班次排班ID
     * @return 结果
     */
    public int deleteHrShiftArrangeById(Long id);
}
