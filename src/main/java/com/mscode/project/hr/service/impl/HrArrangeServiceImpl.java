package com.mscode.project.hr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mscode.common.utils.DateUtils;
import com.mscode.project.hr.domain.HrMacWorker;
import com.mscode.project.hr.domain.HrWorker;
import com.mscode.project.hr.mapper.HrMacWorkerMapper;
import com.mscode.project.hr.mapper.HrWorkerMapper;
import com.mscode.project.manufacture.domain.MacMachine;
import com.mscode.project.manufacture.mapper.MacMachineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mscode.project.hr.mapper.HrArrangeMapper;
import com.mscode.project.hr.domain.HrArrange;
import com.mscode.project.hr.service.IHrArrangeService;

/**
 * 排班信息Service业务层处理
 * 
 * @author mscode
 * @date 2020-11-17
 */
@Service
public class HrArrangeServiceImpl implements IHrArrangeService 
{
    @Autowired
    private HrArrangeMapper hrArrangeMapper;
    @Autowired
    private HrMacWorkerMapper hrMacWorkerMapper;
    @Autowired
    private MacMachineMapper macMachineMapper;
    @Autowired
    private HrWorkerMapper hrWorkerMapper;
    /**
     * 查询排班信息
     * 
     * @param id 排班信息ID
     * @return 排班信息
     */
    @Override
    public HrArrange selectHrArrangeById(Long id)
    {
        return hrArrangeMapper.selectHrArrangeById(id);
    }

    /**
     * 查询排班信息列表
     * 
     * @param hrArrange 排班信息
     * @return 排班信息
     */
    @Override
    public List<HrArrange> selectHrArrangeList(HrArrange hrArrange)
    {
        List<HrArrange> hrArranges = hrArrangeMapper.selectHrArrangeList(hrArrange);
        for (HrArrange arrange : hrArranges) {
            String macworkerids = arrange.getMacworkerids();
            String[] ids = macworkerids.split(",");
            List<HrMacWorker> macworkerList = new ArrayList<>();
            Map<Long, List<HrWorker>> macworkerMap = new HashMap<>();
            for (String id : ids) {
                Long macworkerId = Long.parseLong(id);
                HrMacWorker macWorker = hrMacWorkerMapper.selectHrMacWorkerById(macworkerId);
                if (macWorker!=null){
                    Long machineid = macWorker.getMachineid();
                    Long workerid = macWorker.getWorkerid();
                    MacMachine macMachine = macMachineMapper.selectMacMachineById(machineid);
                    HrWorker hrWorker = hrWorkerMapper.selectHrWorkerById(workerid);
                    //判断是否有了这个machineId
                    if (macworkerMap.containsKey(machineid)){
                        macworkerMap.get(machineid).add(hrWorker);
                    }else {
                        ArrayList<HrWorker> workerList = new ArrayList<>();
                        workerList.add(hrWorker);
                        macworkerMap.put(machineid, workerList);
                    }
                    macWorker.setMachine(macMachine);
                    macWorker.setWorker(hrWorker);
                    macworkerList.add(macWorker);
                }
            }
            arrange.setMacworkerMap(macworkerMap);
            arrange.setMacworkerList(macworkerList);
        }
        return hrArranges;
    }

    /**
     * 新增排班信息
     * 
     * @param hrArrange 排班信息
     * @return 结果
     */
    @Override
    public int insertHrArrange(HrArrange hrArrange)
    {
        hrArrange.setCreateTime(DateUtils.getNowDate());
        return hrArrangeMapper.insertHrArrange(hrArrange);
    }

    /**
     * 修改排班信息
     * 
     * @param hrArrange 排班信息
     * @return 结果
     */
    @Override
    public int updateHrArrange(HrArrange hrArrange)
    {
        hrArrange.setUpdateTime(DateUtils.getNowDate());
        return hrArrangeMapper.updateHrArrange(hrArrange);
    }

    /**
     * 批量删除排班信息
     * 
     * @param ids 需要删除的排班信息ID
     * @return 结果
     */
    @Override
    public int deleteHrArrangeByIds(Long[] ids)
    {
        return hrArrangeMapper.deleteHrArrangeByIds(ids);
    }

    /**
     * 删除排班信息信息
     * 
     * @param id 排班信息ID
     * @return 结果
     */
    @Override
    public int deleteHrArrangeById(Long id)
    {
        return hrArrangeMapper.deleteHrArrangeById(id);
    }
}
