package com.mscode.project.manufacture.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mscode.project.manufacture.mapper.AlldataMapper;
import com.mscode.project.manufacture.domain.Alldata;
import com.mscode.project.manufacture.service.IAlldataService;

/**
 * 基础数据Service业务层处理
 * 
 * @author mscode
 * @date 2020-11-05
 */
@Service
public class AlldataServiceImpl implements IAlldataService 
{
    @Autowired
    private AlldataMapper alldataMapper;

    /**
     * 查询基础数据
     * 
     * @param id 基础数据ID
     * @return 基础数据
     */
    @Override
    public Alldata selectAlldataById(Long id)
    {
        return alldataMapper.selectAlldataById(id);
    }

    /**
     * 查询基础数据列表
     * 
     * @param alldata 基础数据
     * @return 基础数据
     */
    @Override
    public List<Alldata> selectAlldataList(Alldata alldata)
    {
        return alldataMapper.selectAlldataList(alldata);
    }

    /**
     * 新增基础数据
     * 
     * @param alldata 基础数据
     * @return 结果
     */
    @Override
    public int insertAlldata(Alldata alldata)
    {
        return alldataMapper.insertAlldata(alldata);
    }

    /**
     * 修改基础数据
     * 
     * @param alldata 基础数据
     * @return 结果
     */
    @Override
    public int updateAlldata(Alldata alldata)
    {
        return alldataMapper.updateAlldata(alldata);
    }

    /**
     * 批量删除基础数据
     * 
     * @param ids 需要删除的基础数据ID
     * @return 结果
     */
    @Override
    public int deleteAlldataByIds(Long[] ids)
    {
        return alldataMapper.deleteAlldataByIds(ids);
    }

    /**
     * 删除基础数据信息
     * 
     * @param id 基础数据ID
     * @return 结果
     */
    @Override
    public int deleteAlldataById(Long id)
    {
        return alldataMapper.deleteAlldataById(id);
    }
}
