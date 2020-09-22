package com.mscode.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mscode.project.system.mapper.MftClothMapper;
import com.mscode.project.system.domain.MftCloth;
import com.mscode.project.system.service.IMftClothService;

/**
 * 落布Service业务层处理
 * 
 * @author mscode
 * @date 2020-09-13
 */
@Service
public class MftClothServiceImpl implements IMftClothService 
{
    @Autowired
    private MftClothMapper mftClothMapper;

    /**
     * 查询落布
     * 
     * @param id 落布ID
     * @return 落布
     */
    @Override
    public MftCloth selectMftClothById(Long id)
    {
        return mftClothMapper.selectMftClothById(id);
    }

    /**
     * 查询落布列表
     * 
     * @param mftCloth 落布
     * @return 落布
     */
    @Override
    public List<MftCloth> selectMftClothList(MftCloth mftCloth)
    {
        return mftClothMapper.selectMftClothList(mftCloth);
    }

    /**
     * 新增落布
     * 
     * @param mftCloth 落布
     * @return 结果
     */
    @Override
    public int insertMftCloth(MftCloth mftCloth)
    {
        return mftClothMapper.insertMftCloth(mftCloth);
    }

    /**
     * 修改落布
     * 
     * @param mftCloth 落布
     * @return 结果
     */
    @Override
    public int updateMftCloth(MftCloth mftCloth)
    {
        return mftClothMapper.updateMftCloth(mftCloth);
    }

    /**
     * 批量删除落布
     * 
     * @param ids 需要删除的落布ID
     * @return 结果
     */
    @Override
    public int deleteMftClothByIds(Long[] ids)
    {
        return mftClothMapper.deleteMftClothByIds(ids);
    }

    /**
     * 删除落布信息
     * 
     * @param id 落布ID
     * @return 结果
     */
    @Override
    public int deleteMftClothById(Long id)
    {
        return mftClothMapper.deleteMftClothById(id);
    }
}
