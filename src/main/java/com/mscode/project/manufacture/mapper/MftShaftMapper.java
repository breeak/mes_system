package com.mscode.project.manufacture.mapper;

import java.util.List;
import com.mscode.project.manufacture.domain.MftShaft;

/**
 * 织轴列表Mapper接口
 * 
 * @author MS
 * @date 2020-09-21
 */
public interface MftShaftMapper 
{
    /**
     * 查询织轴列表
     * 
     * @param id 织轴列表ID
     * @return 织轴列表
     */
    public MftShaft selectMftShaftById(Long id);

    /**
     * 查询织轴列表列表
     * 
     * @param mftShaft 织轴列表
     * @return 织轴列表集合
     */
    public List<MftShaft> selectMftShaftList(MftShaft mftShaft);

    /**
     * 新增织轴列表
     * 
     * @param mftShaft 织轴列表
     * @return 结果
     */
    public int insertMftShaft(MftShaft mftShaft);

    /**
     * 修改织轴列表
     * 
     * @param mftShaft 织轴列表
     * @return 结果
     */
    public int updateMftShaft(MftShaft mftShaft);

    /**
     * 删除织轴列表
     * 
     * @param id 织轴列表ID
     * @return 结果
     */
    public int deleteMftShaftById(Long id);

    /**
     * 批量删除织轴列表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMftShaftByIds(Long[] ids);
}
