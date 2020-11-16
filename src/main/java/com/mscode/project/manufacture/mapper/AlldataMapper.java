package com.mscode.project.manufacture.mapper;

import java.util.List;
import com.mscode.project.manufacture.domain.Alldata;

/**
 * 基础数据Mapper接口
 * 
 * @author mscode
 * @date 2020-11-05
 */
public interface AlldataMapper 
{
    /**
     * 查询基础数据
     * 
     * @param id 基础数据ID
     * @return 基础数据
     */
    public Alldata selectAlldataById(Long id);

    /**
     * 查询基础数据列表
     * 
     * @param alldata 基础数据
     * @return 基础数据集合
     */
    public List<Alldata> selectAlldataList(Alldata alldata);

    /**
     * 新增基础数据
     * 
     * @param alldata 基础数据
     * @return 结果
     */
    public int insertAlldata(Alldata alldata);

    /**
     * 修改基础数据
     * 
     * @param alldata 基础数据
     * @return 结果
     */
    public int updateAlldata(Alldata alldata);

    /**
     * 删除基础数据
     * 
     * @param id 基础数据ID
     * @return 结果
     */
    public int deleteAlldataById(Long id);

    /**
     * 批量删除基础数据
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAlldataByIds(Long[] ids);
}
