package com.mscode.project.hr.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mscode.project.manufacture.domain.MacMachine;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mscode.framework.aspectj.lang.annotation.Excel;
import com.mscode.framework.web.domain.BaseEntity;

/**
 * 员工划片对象 hr_mac_worker
 * 
 * @author mscode
 * @date 2020-11-17
 */
public class HrMacWorker extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 员工id */
    @Excel(name = "员工id")
    private Long workerid;

    /** 织机id */
    @Excel(name = "织机id")
    private Long machineid;

    /** 员工姓名 */
    @Excel(name = "员工姓名")
    private String workername;

    /** 织机编号 */
    @Excel(name = "织机编号")
    private String maccode;

    private MacMachine machine;
    private HrWorker worker;

    /** 状态码 */
    @Excel(name = "状态码")
    private String status;

    public MacMachine getMachine() {
        return machine;
    }

    public void setMachine(MacMachine machine) {
        this.machine = machine;
    }

    public HrWorker getWorker() {
        return worker;
    }

    public void setWorker(HrWorker worker) {
        this.worker = worker;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setWorkerid(Long workerid) 
    {
        this.workerid = workerid;
    }

    public Long getWorkerid() 
    {
        return workerid;
    }
    public void setMachineid(Long machineid) 
    {
        this.machineid = machineid;
    }

    public Long getMachineid() 
    {
        return machineid;
    }
    public void setWorkername(String workername) 
    {
        this.workername = workername;
    }

    public String getWorkername() 
    {
        return workername;
    }
    public void setMaccode(String maccode) 
    {
        this.maccode = maccode;
    }

    public String getMaccode() 
    {
        return maccode;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("workerid", getWorkerid())
            .append("machineid", getMachineid())
            .append("workername", getWorkername())
            .append("maccode", getMaccode())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
