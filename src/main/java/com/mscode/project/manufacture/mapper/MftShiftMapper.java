package com.mscode.project.manufacture.mapper;

import java.util.List;
import com.mscode.project.manufacture.domain.MftShift;

/**
 * 班次效率Mapper接口
 * 
 * @author MS
 * @date 2020-09-20
 */
public interface MftShiftMapper 
{
    /**
     * 查询班次效率
     * 
     * @param id 班次效率ID
     * @return 班次效率
     */
    public MftShift selectMftShiftById(Long id);

    /**
     * 查询班次效率列表
     * 
     * @param mftShift 班次效率
     * @return 班次效率集合
     */
    public List<MftShift> selectMftShiftList(MftShift mftShift);

    /**
     * 新增班次效率
     * 
     * @param mftShift 班次效率
     * @return 结果
     */
    public int insertMftShift(MftShift mftShift);

    /**
     * 修改班次效率
     * 
     * @param mftShift 班次效率
     * @return 结果
     */
    public int updateMftShift(MftShift mftShift);

    /**
     * 删除班次效率
     * 
     * @param id 班次效率ID
     * @return 结果
     */
    public int deleteMftShiftById(Long id);

    /**
     * 批量删除班次效率
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMftShiftByIds(Long[] ids);

    void createShift(MftShift mftShift);

    void updateShiftAny(MftShift mftShift);
}
