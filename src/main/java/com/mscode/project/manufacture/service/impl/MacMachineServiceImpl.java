package com.mscode.project.manufacture.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mscode.project.manufacture.mapper.MacMachineMapper;
import com.mscode.project.manufacture.domain.MacMachine;
import com.mscode.project.manufacture.service.IMacMachineService;

/**
 * 织机列表Service业务层处理
 * 
 * @author MS
 * @date 2020-09-16
 */
@Service
public class MacMachineServiceImpl implements IMacMachineService 
{
    @Autowired
    private MacMachineMapper macMachineMapper;

    /**
     * 查询织机列表
     * 
     * @param id 织机列表ID
     * @return 织机列表
     */
    @Override
    public MacMachine selectMacMachineById(Long id)
    {
        return macMachineMapper.selectMacMachineById(id);
    }

    /**
     * 查询织机列表列表
     * 
     * @param macMachine 织机列表
     * @return 织机列表
     */
    @Override
    public List<MacMachine> selectMacMachineList(MacMachine macMachine)
    {
        return macMachineMapper.selectMacMachineList(macMachine);
    }

    /**
     * 新增织机列表
     * 
     * @param macMachine 织机列表
     * @return 结果
     */
    @Override
    public int insertMacMachine(MacMachine macMachine)
    {
        return macMachineMapper.insertMacMachine(macMachine);
    }

    /**
     * 修改织机列表
     * 
     * @param macMachine 织机列表
     * @return 结果
     */
    @Override
    public int updateMacMachine(MacMachine macMachine)
    {
        return macMachineMapper.updateMacMachine(macMachine);
    }

    /**
     * 批量删除织机列表
     * 
     * @param ids 需要删除的织机列表ID
     * @return 结果
     */
    @Override
    public int deleteMacMachineByIds(Long[] ids)
    {
        return macMachineMapper.deleteMacMachineByIds(ids);
    }

    /**
     * 删除织机列表信息
     * 
     * @param id 织机列表ID
     * @return 结果
     */
    @Override
    public int deleteMacMachineById(Long id)
    {
        return macMachineMapper.deleteMacMachineById(id);
    }
}
