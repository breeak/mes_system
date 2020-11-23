package com.mscode.project.hr.service.impl;

import java.util.List;
import com.mscode.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mscode.project.hr.mapper.HrWorkerMapper;
import com.mscode.project.hr.domain.HrWorker;
import com.mscode.project.hr.service.IHrWorkerService;

/**
 * 员工列表Service业务层处理
 * 
 * @author mscode
 * @date 2020-11-17
 */
@Service
public class HrWorkerServiceImpl implements IHrWorkerService 
{
    @Autowired
    private HrWorkerMapper hrWorkerMapper;

    /**
     * 查询员工列表
     * 
     * @param id 员工列表ID
     * @return 员工列表
     */
    @Override
    public HrWorker selectHrWorkerById(Long id)
    {
        return hrWorkerMapper.selectHrWorkerById(id);
    }

    /**
     * 查询员工列表列表
     * 
     * @param hrWorker 员工列表
     * @return 员工列表
     */
    @Override
    public List<HrWorker> selectHrWorkerList(HrWorker hrWorker)
    {
        return hrWorkerMapper.selectHrWorkerList(hrWorker);
    }

    /**
     * 新增员工列表
     * 
     * @param hrWorker 员工列表
     * @return 结果
     */
    @Override
    public int insertHrWorker(HrWorker hrWorker)
    {
        hrWorker.setCreateTime(DateUtils.getNowDate());
        return hrWorkerMapper.insertHrWorker(hrWorker);
    }

    /**
     * 修改员工列表
     * 
     * @param hrWorker 员工列表
     * @return 结果
     */
    @Override
    public int updateHrWorker(HrWorker hrWorker)
    {
        hrWorker.setUpdateTime(DateUtils.getNowDate());
        return hrWorkerMapper.updateHrWorker(hrWorker);
    }

    /**
     * 批量删除员工列表
     * 
     * @param ids 需要删除的员工列表ID
     * @return 结果
     */
    @Override
    public int deleteHrWorkerByIds(Long[] ids)
    {
        return hrWorkerMapper.deleteHrWorkerByIds(ids);
    }

    /**
     * 删除员工列表信息
     * 
     * @param id 员工列表ID
     * @return 结果
     */
    @Override
    public int deleteHrWorkerById(Long id)
    {
        return hrWorkerMapper.deleteHrWorkerById(id);
    }
}
