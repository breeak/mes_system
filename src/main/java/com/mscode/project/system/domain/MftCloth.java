package com.mscode.project.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mscode.framework.aspectj.lang.annotation.Excel;
import com.mscode.framework.web.domain.BaseEntity;

/**
 * 落布对象 mft_cloth
 * 
 * @author mscode
 * @date 2020-09-13
 */
public class MftCloth extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键自增 */
    private Long id;

    /** 布匹编号 */
    @Excel(name = "布匹编号")
    private String clothcode;

    /** 织轴编号 */
    @Excel(name = "织轴编号")
    private String shiftcode;

    /** 品种编号 */
    @Excel(name = "品种编号")
    private String pdtcode;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String ordercode;

    /** 织机编号 */
    @Excel(name = "织机编号")
    private String maccode;

    /** 布匹长度 */
    @Excel(name = "布匹长度")
    private Long clothlength;

    /** 经纱批次 */
    @Excel(name = "经纱批次")
    private String warpBatch;

    /** 纬纱批次 */
    @Excel(name = "纬纱批次")
    private String weftBatch;

    /** 落布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "落布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setClothcode(String clothcode) 
    {
        this.clothcode = clothcode;
    }

    public String getClothcode() 
    {
        return clothcode;
    }
    public void setShiftcode(String shiftcode) 
    {
        this.shiftcode = shiftcode;
    }

    public String getShiftcode() 
    {
        return shiftcode;
    }
    public void setPdtcode(String pdtcode) 
    {
        this.pdtcode = pdtcode;
    }

    public String getPdtcode() 
    {
        return pdtcode;
    }
    public void setOrdercode(String ordercode) 
    {
        this.ordercode = ordercode;
    }

    public String getOrdercode() 
    {
        return ordercode;
    }
    public void setMaccode(String maccode) 
    {
        this.maccode = maccode;
    }

    public String getMaccode() 
    {
        return maccode;
    }
    public void setClothlength(Long clothlength) 
    {
        this.clothlength = clothlength;
    }

    public Long getClothlength() 
    {
        return clothlength;
    }
    public void setWarpBatch(String warpBatch) 
    {
        this.warpBatch = warpBatch;
    }

    public String getWarpBatch() 
    {
        return warpBatch;
    }
    public void setWeftBatch(String weftBatch) 
    {
        this.weftBatch = weftBatch;
    }

    public String getWeftBatch() 
    {
        return weftBatch;
    }
    public void setCreatedate(Date createdate) 
    {
        this.createdate = createdate;
    }

    public Date getCreatedate() 
    {
        return createdate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("clothcode", getClothcode())
            .append("shiftcode", getShiftcode())
            .append("pdtcode", getPdtcode())
            .append("ordercode", getOrdercode())
            .append("maccode", getMaccode())
            .append("clothlength", getClothlength())
            .append("warpBatch", getWarpBatch())
            .append("weftBatch", getWeftBatch())
            .append("createdate", getCreatedate())
            .append("remark", getRemark())
            .toString();
    }
}
