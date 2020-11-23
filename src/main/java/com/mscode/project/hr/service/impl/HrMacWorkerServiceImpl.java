package com.mscode.project.hr.service.impl;

import java.util.List;
import com.mscode.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mscode.project.hr.mapper.HrMacWorkerMapper;
import com.mscode.project.hr.domain.HrMacWorker;
import com.mscode.project.hr.service.IHrMacWorkerService;

/**
 * 员工划片Service业务层处理
 * 
 * @author mscode
 * @date 2020-11-17
 */
@Service
public class HrMacWorkerServiceImpl implements IHrMacWorkerService 
{
    @Autowired
    private HrMacWorkerMapper hrMacWorkerMapper;

    /**
     * 查询员工划片
     * 
     * @param id 员工划片ID
     * @return 员工划片
     */
    @Override
    public HrMacWorker selectHrMacWorkerById(Long id)
    {
        return hrMacWorkerMapper.selectHrMacWorkerById(id);
    }

    /**
     * 查询员工划片列表
     * 
     * @param hrMacWorker 员工划片
     * @return 员工划片
     */
    @Override
    public List<HrMacWorker> selectHrMacWorkerList(HrMacWorker hrMacWorker)
    {
        return hrMacWorkerMapper.selectHrMacWorkerList(hrMacWorker);
    }

    /**
     * 新增员工划片
     * 
     * @param hrMacWorker 员工划片
     * @return 结果
     */
    @Override
    public int insertHrMacWorker(HrMacWorker hrMacWorker)
    {
        hrMacWorker.setCreateTime(DateUtils.getNowDate());
        return hrMacWorkerMapper.insertHrMacWorker(hrMacWorker);
    }

    /**
     * 修改员工划片
     * 
     * @param hrMacWorker 员工划片
     * @return 结果
     */
    @Override
    public int updateHrMacWorker(HrMacWorker hrMacWorker)
    {
        hrMacWorker.setUpdateTime(DateUtils.getNowDate());
        return hrMacWorkerMapper.updateHrMacWorker(hrMacWorker);
    }

    /**
     * 批量删除员工划片
     * 
     * @param ids 需要删除的员工划片ID
     * @return 结果
     */
    @Override
    public int deleteHrMacWorkerByIds(Long[] ids)
    {
        return hrMacWorkerMapper.deleteHrMacWorkerByIds(ids);
    }

    /**
     * 删除员工划片信息
     * 
     * @param id 员工划片ID
     * @return 结果
     */
    @Override
    public int deleteHrMacWorkerById(Long id)
    {
        return hrMacWorkerMapper.deleteHrMacWorkerById(id);
    }
}
