package com.mscode.project.hr.service.impl;

import java.util.List;
import com.mscode.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mscode.project.hr.mapper.HrShiftArrangeMapper;
import com.mscode.project.hr.domain.HrShiftArrange;
import com.mscode.project.hr.service.IHrShiftArrangeService;

/**
 * 班次排班Service业务层处理
 * 
 * @author mscode
 * @date 2020-11-17
 */
@Service
public class HrShiftArrangeServiceImpl implements IHrShiftArrangeService 
{
    @Autowired
    private HrShiftArrangeMapper hrShiftArrangeMapper;

    /**
     * 查询班次排班
     * 
     * @param id 班次排班ID
     * @return 班次排班
     */
    @Override
    public HrShiftArrange selectHrShiftArrangeById(Long id)
    {
        return hrShiftArrangeMapper.selectHrShiftArrangeById(id);
    }

    /**
     * 查询班次排班列表
     * 
     * @param hrShiftArrange 班次排班
     * @return 班次排班
     */
    @Override
    public List<HrShiftArrange> selectHrShiftArrangeList(HrShiftArrange hrShiftArrange)
    {
        return hrShiftArrangeMapper.selectHrShiftArrangeList(hrShiftArrange);
    }

    /**
     * 新增班次排班
     * 
     * @param hrShiftArrange 班次排班
     * @return 结果
     */
    @Override
    public int insertHrShiftArrange(HrShiftArrange hrShiftArrange)
    {
        hrShiftArrange.setCreateTime(DateUtils.getNowDate());
        return hrShiftArrangeMapper.insertHrShiftArrange(hrShiftArrange);
    }

    /**
     * 修改班次排班
     * 
     * @param hrShiftArrange 班次排班
     * @return 结果
     */
    @Override
    public int updateHrShiftArrange(HrShiftArrange hrShiftArrange)
    {
        hrShiftArrange.setUpdateTime(DateUtils.getNowDate());
        return hrShiftArrangeMapper.updateHrShiftArrange(hrShiftArrange);
    }

    /**
     * 批量删除班次排班
     * 
     * @param ids 需要删除的班次排班ID
     * @return 结果
     */
    @Override
    public int deleteHrShiftArrangeByIds(Long[] ids)
    {
        return hrShiftArrangeMapper.deleteHrShiftArrangeByIds(ids);
    }

    /**
     * 删除班次排班信息
     * 
     * @param id 班次排班ID
     * @return 结果
     */
    @Override
    public int deleteHrShiftArrangeById(Long id)
    {
        return hrShiftArrangeMapper.deleteHrShiftArrangeById(id);
    }
}
