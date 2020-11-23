package com.mscode.project.hr.mapper;

import java.util.List;
import com.mscode.project.hr.domain.HrMacWorker;

/**
 * 员工划片Mapper接口
 * 
 * @author mscode
 * @date 2020-11-17
 */
public interface HrMacWorkerMapper 
{
    /**
     * 查询员工划片
     * 
     * @param id 员工划片ID
     * @return 员工划片
     */
    public HrMacWorker selectHrMacWorkerById(Long id);

    /**
     * 查询员工划片列表
     * 
     * @param hrMacWorker 员工划片
     * @return 员工划片集合
     */
    public List<HrMacWorker> selectHrMacWorkerList(HrMacWorker hrMacWorker);

    /**
     * 新增员工划片
     * 
     * @param hrMacWorker 员工划片
     * @return 结果
     */
    public int insertHrMacWorker(HrMacWorker hrMacWorker);

    /**
     * 修改员工划片
     * 
     * @param hrMacWorker 员工划片
     * @return 结果
     */
    public int updateHrMacWorker(HrMacWorker hrMacWorker);

    /**
     * 删除员工划片
     * 
     * @param id 员工划片ID
     * @return 结果
     */
    public int deleteHrMacWorkerById(Long id);

    /**
     * 批量删除员工划片
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrMacWorkerByIds(Long[] ids);
}
