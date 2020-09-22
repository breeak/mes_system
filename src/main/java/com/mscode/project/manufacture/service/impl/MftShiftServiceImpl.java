package com.mscode.project.manufacture.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mscode.project.manufacture.mapper.MftShiftMapper;
import com.mscode.project.manufacture.domain.MftShift;
import com.mscode.project.manufacture.service.IMftShiftService;

/**
 * 班次效率Service业务层处理
 * 
 * @author MS
 * @date 2020-09-20
 */
@Service
public class MftShiftServiceImpl implements IMftShiftService 
{
    @Autowired
    private MftShiftMapper mftShiftMapper;

    /**
     * 查询班次效率
     * 
     * @param id 班次效率ID
     * @return 班次效率
     */
    @Override
    public MftShift selectMftShiftById(Long id)
    {
        return mftShiftMapper.selectMftShiftById(id);
    }

    /**
     * 查询班次效率列表
     * 
     * @param mftShift 班次效率
     * @return 班次效率
     */
    @Override
    public List<MftShift> selectMftShiftList(MftShift mftShift)
    {
        return mftShiftMapper.selectMftShiftList(mftShift);
    }

    /**
     * 新增班次效率
     * 
     * @param mftShift 班次效率
     * @return 结果
     */
    @Override
    public int insertMftShift(MftShift mftShift)
    {
        return mftShiftMapper.insertMftShift(mftShift);
    }

    /**
     * 修改班次效率
     * 
     * @param mftShift 班次效率
     * @return 结果
     */
    @Override
    public int updateMftShift(MftShift mftShift)
    {
        return mftShiftMapper.updateMftShift(mftShift);
    }

    /**
     * 批量删除班次效率
     * 
     * @param ids 需要删除的班次效率ID
     * @return 结果
     */
    @Override
    public int deleteMftShiftByIds(Long[] ids)
    {
        return mftShiftMapper.deleteMftShiftByIds(ids);
    }

    /**
     * 删除班次效率信息
     * 
     * @param id 班次效率ID
     * @return 结果
     */
    @Override
    public int deleteMftShiftById(Long id)
    {
        return mftShiftMapper.deleteMftShiftById(id);
    }
}
