package com.mscode.project.hr.service;

import java.util.List;
import com.mscode.project.hr.domain.HrWorker;

/**
 * 员工列表Service接口
 * 
 * @author mscode
 * @date 2020-11-17
 */
public interface IHrWorkerService 
{
    /**
     * 查询员工列表
     * 
     * @param id 员工列表ID
     * @return 员工列表
     */
    public HrWorker selectHrWorkerById(Long id);

    /**
     * 查询员工列表列表
     * 
     * @param hrWorker 员工列表
     * @return 员工列表集合
     */
    public List<HrWorker> selectHrWorkerList(HrWorker hrWorker);

    /**
     * 新增员工列表
     * 
     * @param hrWorker 员工列表
     * @return 结果
     */
    public int insertHrWorker(HrWorker hrWorker);

    /**
     * 修改员工列表
     * 
     * @param hrWorker 员工列表
     * @return 结果
     */
    public int updateHrWorker(HrWorker hrWorker);

    /**
     * 批量删除员工列表
     * 
     * @param ids 需要删除的员工列表ID
     * @return 结果
     */
    public int deleteHrWorkerByIds(Long[] ids);

    /**
     * 删除员工列表信息
     * 
     * @param id 员工列表ID
     * @return 结果
     */
    public int deleteHrWorkerById(Long id);
}
