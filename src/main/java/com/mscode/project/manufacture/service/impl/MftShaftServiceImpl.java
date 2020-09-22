package com.mscode.project.manufacture.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mscode.project.manufacture.mapper.MftShaftMapper;
import com.mscode.project.manufacture.domain.MftShaft;
import com.mscode.project.manufacture.service.IMftShaftService;

/**
 * 织轴列表Service业务层处理
 * 
 * @author MS
 * @date 2020-09-21
 */
@Service
public class MftShaftServiceImpl implements IMftShaftService 
{
    @Autowired
    private MftShaftMapper mftShaftMapper;

    /**
     * 查询织轴列表
     * 
     * @param id 织轴列表ID
     * @return 织轴列表
     */
    @Override
    public MftShaft selectMftShaftById(Long id)
    {
        return mftShaftMapper.selectMftShaftById(id);
    }

    /**
     * 查询织轴列表列表
     * 
     * @param mftShaft 织轴列表
     * @return 织轴列表
     */
    @Override
    public List<MftShaft> selectMftShaftList(MftShaft mftShaft)
    {
        return mftShaftMapper.selectMftShaftList(mftShaft);
    }

    /**
     * 新增织轴列表
     * 
     * @param mftShaft 织轴列表
     * @return 结果
     */
    @Override
    public int insertMftShaft(MftShaft mftShaft)
    {
        return mftShaftMapper.insertMftShaft(mftShaft);
    }

    /**
     * 修改织轴列表
     * 
     * @param mftShaft 织轴列表
     * @return 结果
     */
    @Override
    public int updateMftShaft(MftShaft mftShaft)
    {
        return mftShaftMapper.updateMftShaft(mftShaft);
    }

    /**
     * 批量删除织轴列表
     * 
     * @param ids 需要删除的织轴列表ID
     * @return 结果
     */
    @Override
    public int deleteMftShaftByIds(Long[] ids)
    {
        return mftShaftMapper.deleteMftShaftByIds(ids);
    }

    /**
     * 删除织轴列表信息
     * 
     * @param id 织轴列表ID
     * @return 结果
     */
    @Override
    public int deleteMftShaftById(Long id)
    {
        return mftShaftMapper.deleteMftShaftById(id);
    }
}
