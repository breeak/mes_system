package com.mscode.project.manufacture.service;

import java.util.Date;
import java.util.List;
import com.mscode.project.manufacture.domain.Alldata;
import com.mscode.project.manufacture.domain.MftShaft;
import com.mscode.project.manufacture.domain.MftShift;

/**
 * 基础数据Service接口
 * 
 * @author mscode
 * @date 2020-11-05
 */
public interface IAlldataService 
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
     * 批量删除基础数据
     * 
     * @param ids 需要删除的基础数据ID
     * @return 结果
     */
    public int deleteAlldataByIds(Long[] ids);

    /**
     * 删除基础数据信息
     * 
     * @param id 基础数据ID
     * @return 结果
     */
    public int deleteAlldataById(Long id);

    MftShift getShift(String macCode, Date beginTime, Date endTime, MftShaft mftShaft);
}
