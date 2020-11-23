package com.mscode.project.hr.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mscode.framework.aspectj.lang.annotation.Excel;
import com.mscode.framework.web.domain.BaseEntity;

/**
 * 班次排班对象 hr_shift_arrange
 * 
 * @author mscode
 * @date 2020-11-17
 */
public class HrShiftArrange extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 班次日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "班次日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date shiftdate;

    /** 班次类别 */
    @Excel(name = "班次类别")
    private String shifttype;

    /** 排班id */
    @Excel(name = "排班id")
    private Long arrangeid;

    /** 排班编号 */
    @Excel(name = "排班编号")
    private String arrangeno;

    /** 排班状态 */
    @Excel(name = "排班状态")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setShiftdate(Date shiftdate) 
    {
        this.shiftdate = shiftdate;
    }

    public Date getShiftdate() 
    {
        return shiftdate;
    }
    public void setShifttype(String shifttype) 
    {
        this.shifttype = shifttype;
    }

    public String getShifttype() 
    {
        return shifttype;
    }
    public void setArrangeid(Long arrangeid) 
    {
        this.arrangeid = arrangeid;
    }

    public Long getArrangeid() 
    {
        return arrangeid;
    }
    public void setArrangeno(String arrangeno) 
    {
        this.arrangeno = arrangeno;
    }

    public String getArrangeno() 
    {
        return arrangeno;
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
            .append("shiftdate", getShiftdate())
            .append("shifttype", getShifttype())
            .append("arrangeid", getArrangeid())
            .append("arrangeno", getArrangeno())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .toString();
    }
}
