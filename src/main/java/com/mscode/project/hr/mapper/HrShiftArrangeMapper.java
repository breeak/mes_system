package com.mscode.project.hr.mapper;

import java.util.List;
import com.mscode.project.hr.domain.HrShiftArrange;

/**
 * 班次排班Mapper接口
 * 
 * @author mscode
 * @date 2020-11-17
 */
public interface HrShiftArrangeMapper 
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
     * 删除班次排班
     * 
     * @param id 班次排班ID
     * @return 结果
     */
    public int deleteHrShiftArrangeById(Long id);

    /**
     * 批量删除班次排班
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrShiftArrangeByIds(Long[] ids);
}
