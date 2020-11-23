package com.mscode.project.hr.mapper;

import java.util.List;
import com.mscode.project.hr.domain.HrWorker;

/**
 * 员工列表Mapper接口
 * 
 * @author mscode
 * @date 2020-11-17
 */
public interface HrWorkerMapper 
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
     * 删除员工列表
     * 
     * @param id 员工列表ID
     * @return 结果
     */
    public int deleteHrWorkerById(Long id);

    /**
     * 批量删除员工列表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrWorkerByIds(Long[] ids);
}
