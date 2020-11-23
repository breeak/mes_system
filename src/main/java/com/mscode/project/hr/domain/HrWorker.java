package com.mscode.project.hr.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mscode.framework.aspectj.lang.annotation.Excel;
import com.mscode.framework.web.domain.BaseEntity;

/**
 * 员工列表对象 hr_worker
 * 
 * @author mscode
 * @date 2020-11-17
 */
public class HrWorker extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 员工姓名 */
    @Excel(name = "员工姓名")
    private String workername;

    /** 员工编号 */
    @Excel(name = "员工编号")
    private String workerno;

    /** 卡号id */
    @Excel(name = "卡号id")
    private Long cardid;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userid;

    /** 员工组别 */
    @Excel(name = "员工组别")
    private String workergroup;

    /** 员工角色 */
    @Excel(name = "员工角色")
    private String workerrole;

    /** 员工状态 */
    @Excel(name = "员工状态")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setWorkername(String workername) 
    {
        this.workername = workername;
    }

    public String getWorkername() 
    {
        return workername;
    }
    public void setWorkerno(String workerno) 
    {
        this.workerno = workerno;
    }

    public String getWorkerno() 
    {
        return workerno;
    }
    public void setCardid(Long cardid) 
    {
        this.cardid = cardid;
    }

    public Long getCardid() 
    {
        return cardid;
    }
    public void setUserid(Long userid) 
    {
        this.userid = userid;
    }

    public Long getUserid() 
    {
        return userid;
    }
    public void setWorkergroup(String workergroup) 
    {
        this.workergroup = workergroup;
    }

    public String getWorkergroup() 
    {
        return workergroup;
    }
    public void setWorkerrole(String workerrole) 
    {
        this.workerrole = workerrole;
    }

    public String getWorkerrole() 
    {
        return workerrole;
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
            .append("workername", getWorkername())
            .append("workerno", getWorkerno())
            .append("cardid", getCardid())
            .append("userid", getUserid())
            .append("workergroup", getWorkergroup())
            .append("workerrole", getWorkerrole())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .toString();
    }
}
