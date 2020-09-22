package com.mscode.project.manufacture.mapper;

import java.util.List;
import com.mscode.project.manufacture.domain.MacMachine;

/**
 * 织机列表Mapper接口
 * 
 * @author MS
 * @date 2020-09-16
 */
public interface MacMachineMapper 
{
    /**
     * 查询织机列表
     * 
     * @param id 织机列表ID
     * @return 织机列表
     */
    public MacMachine selectMacMachineById(Long id);

    /**
     * 查询织机列表列表
     * 
     * @param macMachine 织机列表
     * @return 织机列表集合
     */
    public List<MacMachine> selectMacMachineList(MacMachine macMachine);

    /**
     * 新增织机列表
     * 
     * @param macMachine 织机列表
     * @return 结果
     */
    public int insertMacMachine(MacMachine macMachine);

    /**
     * 修改织机列表
     * 
     * @param macMachine 织机列表
     * @return 结果
     */
    public int updateMacMachine(MacMachine macMachine);

    /**
     * 删除织机列表
     * 
     * @param id 织机列表ID
     * @return 结果
     */
    public int deleteMacMachineById(Long id);

    /**
     * 批量删除织机列表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMacMachineByIds(Long[] ids);
}
