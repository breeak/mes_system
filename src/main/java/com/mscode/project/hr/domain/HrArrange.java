package com.mscode.project.hr.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mscode.framework.aspectj.lang.annotation.Excel;
import com.mscode.framework.web.domain.BaseEntity;

/**
 * 排班信息对象 hr_arrange
 * 
 * @author mscode
 * @date 2020-11-17
 */
public class HrArrange extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 排班号 */
    @Excel(name = "排班号")
    private String arrangeno;

    /** 排班起始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "排班起始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date arrangestart;

    /** 排班结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "排班结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date arrangeend;

    /** 排班内容 */
    @Excel(name = "排班内容")
    private String macworkerids;

    private List<HrMacWorker> macworkerList;
    private Map<Long,List<HrWorker>> macworkerMap;

    /** 排班类别 */
    @Excel(name = "排班类别")
    private String arrangeclass;

    /** 排班状态 */
    @Excel(name = "排班状态")
    private String status;

    public Map<Long, List<HrWorker>> getMacworkerMap() {
        return macworkerMap;
    }

    public void setMacworkerMap(Map<Long, List<HrWorker>> macworkerMap) {
        this.macworkerMap = macworkerMap;
    }

    public List<HrMacWorker> getMacworkerList() {
        return macworkerList;
    }

    public void setMacworkerList(List<HrMacWorker> macworkerList) {
        this.macworkerList = macworkerList;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setArrangeno(String arrangeno) 
    {
        this.arrangeno = arrangeno;
    }

    public String getArrangeno() 
    {
        return arrangeno;
    }
    public void setArrangestart(Date arrangestart) 
    {
        this.arrangestart = arrangestart;
    }

    public Date getArrangestart() 
    {
        return arrangestart;
    }
    public void setArrangeend(Date arrangeend) 
    {
        this.arrangeend = arrangeend;
    }

    public Date getArrangeend() 
    {
        return arrangeend;
    }
    public void setMacworkerids(String macworkerids) 
    {
        this.macworkerids = macworkerids;
    }

    public String getMacworkerids() 
    {
        return macworkerids;
    }
    public void setArrangeclass(String arrangeclass) 
    {
        this.arrangeclass = arrangeclass;
    }

    public String getArrangeclass() 
    {
        return arrangeclass;
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
            .append("arrangeno", getArrangeno())
            .append("arrangestart", getArrangestart())
            .append("arrangeend", getArrangeend())
            .append("macworkerids", getMacworkerids())
            .append("arrangeclass", getArrangeclass())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .toString();
    }
}
