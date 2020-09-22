package com.mscode.project.manufacture.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mscode.framework.aspectj.lang.annotation.Excel;
import com.mscode.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 织机列表对象 mac_machine
 * 
 * @author MS
 * @date 2020-09-16
 */
public class MacMachine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键自增 */
    private Long id;

    /** 织机编号 */
    @Excel(name = "织机编号")
    private String maccode;

    /** 织机类型 */
    @Excel(name = "织机类型")
    private String mactype;

    /** 织机状态 */
    @Excel(name = "织机状态")
    private Long macstatus;

    /** 织机速度 */
    @Excel(name = "织机速度")
    private Long macspeed;

    /** 织机效率 */
    @Excel(name = "织机效率")
    private Double macefficiency;

    /** 品种编号 */
    @Excel(name = "品种编号")
    private String pdtcode;

    /** 上机卡号 */
    @Excel(name = "上机卡号")
    private String shaftcode;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String ordercode;

    /** 位置列 */
    @Excel(name = "位置列")
    private Long maccolumn;

    /** 位置行 */
    @Excel(name = "位置行")
    private Long macrow;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date updatetime;


    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMaccode(String maccode) 
    {
        this.maccode = maccode;
    }

    public String getMaccode() 
    {
        return maccode;
    }
    public void setMactype(String mactype) 
    {
        this.mactype = mactype;
    }

    public String getMactype() 
    {
        return mactype;
    }
    public void setMacstatus(Long macstatus) 
    {
        this.macstatus = macstatus;
    }

    public Long getMacstatus() 
    {
        return macstatus;
    }
    public void setMacspeed(Long macspeed) 
    {
        this.macspeed = macspeed;
    }

    public Long getMacspeed() 
    {
        return macspeed;
    }
    public void setMacefficiency(Double macefficiency)
    {
        this.macefficiency = macefficiency;
    }

    public Double getMacefficiency()
    {
        return macefficiency;
    }
    public void setPdtcode(String pdtcode) 
    {
        this.pdtcode = pdtcode;
    }

    public String getPdtcode() 
    {
        return pdtcode;
    }
    public void setShaftcode(String shaftcode) 
    {
        this.shaftcode = shaftcode;
    }

    public String getShaftcode() 
    {
        return shaftcode;
    }
    public void setOrdercode(String ordercode) 
    {
        this.ordercode = ordercode;
    }

    public String getOrdercode() 
    {
        return ordercode;
    }
    public void setMaccolumn(Long maccolumn) 
    {
        this.maccolumn = maccolumn;
    }

    public Long getMaccolumn() 
    {
        return maccolumn;
    }
    public void setMacrow(Long macrow) 
    {
        this.macrow = macrow;
    }

    public Long getMacrow() 
    {
        return macrow;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("maccode", getMaccode())
            .append("mactype", getMactype())
            .append("macstatus", getMacstatus())
            .append("macspeed", getMacspeed())
            .append("macefficiency", getMacefficiency())
            .append("pdtcode", getPdtcode())
            .append("shaftcode", getShaftcode())
            .append("ordercode", getOrdercode())
            .append("updateTime", getUpdateTime())
            .append("maccolumn", getMaccolumn())
            .append("macrow", getMacrow())
            .append("remark", getRemark())
            .toString();
    }
}
