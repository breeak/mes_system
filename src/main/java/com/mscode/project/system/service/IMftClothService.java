package com.mscode.project.system.service;

import java.util.List;
import com.mscode.project.system.domain.MftCloth;

/**
 * 落布Service接口
 * 
 * @author mscode
 * @date 2020-09-13
 */
public interface IMftClothService 
{
    /**
     * 查询落布
     * 
     * @param id 落布ID
     * @return 落布
     */
    public MftCloth selectMftClothById(Long id);

    /**
     * 查询落布列表
     * 
     * @param mftCloth 落布
     * @return 落布集合
     */
    public List<MftCloth> selectMftClothList(MftCloth mftCloth);

    /**
     * 新增落布
     * 
     * @param mftCloth 落布
     * @return 结果
     */
    public int insertMftCloth(MftCloth mftCloth);

    /**
     * 修改落布
     * 
     * @param mftCloth 落布
     * @return 结果
     */
    public int updateMftCloth(MftCloth mftCloth);

    /**
     * 批量删除落布
     * 
     * @param ids 需要删除的落布ID
     * @return 结果
     */
    public int deleteMftClothByIds(Long[] ids);

    /**
     * 删除落布信息
     * 
     * @param id 落布ID
     * @return 结果
     */
    public int deleteMftClothById(Long id);
}
