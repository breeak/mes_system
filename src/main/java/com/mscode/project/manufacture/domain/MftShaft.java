package com.mscode.project.manufacture.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mscode.framework.aspectj.lang.annotation.Excel;
import com.mscode.framework.web.domain.BaseEntity;

/**
 * 织轴列表对象 mft_shaft
 * 
 * @author MS
 * @date 2020-09-21
 */
public class MftShaft extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键自增 */
    private Long id;

    /** 织轴卡号(开卡用唯一) */
    @Excel(name = "织轴卡号(开卡用唯一)")
    private String shaftcode;

    /** 织轴号 */
    @Excel(name = "织轴号")
    private String shaftno;

    /** 织轴长度 */
    @Excel(name = "织轴长度")
    private BigDecimal shaftlength;

    /** 品种编号 */
    @Excel(name = "品种编号")
    private String pdtcode;

    /** 品种纬密 */
    @Excel(name = "品种纬密")
    private BigDecimal pdtweftshrinkage;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String ordercode;

    /** 织轴状态 */
    @Excel(name = "织轴状态")
    private String shaftstatus;

    /** 余轴长度 */
    @Excel(name = "余轴长度")
    private BigDecimal shaftremainlength;

    /** 已织长度 */
    @Excel(name = "已织长度")
    private BigDecimal clothlength;

    /** 操作员 */
    @Excel(name = "操作员")
    private String shaftworker;

    /** 经纱批次 */
    @Excel(name = "经纱批次")
    private String warpbacth;

    /** 纬纱批次 */
    @Excel(name = "纬纱批次")
    private String weftbacth;

    /** 计划上机时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划上机时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planstart;

    /** 计划下机时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划下机时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planend;

    /** 实际上机时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际上机时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actstart;

    /** 实际下机时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际下机时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actend;

    /** 计划上机机台编号 */
    @Excel(name = "计划上机机台编号")
    private String planmaccode;

    /** 实际上机机台编号 */
    @Excel(name = "实际上机机台编号")
    private String actmaccode;


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

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date createtime;

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setShaftcode(String shaftcode) 
    {
        this.shaftcode = shaftcode;
    }

    public String getShaftcode() 
    {
        return shaftcode;
    }
    public void setShaftno(String shaftno) 
    {
        this.shaftno = shaftno;
    }

    public String getShaftno() 
    {
        return shaftno;
    }
    public void setShaftlength(BigDecimal shaftlength) 
    {
        this.shaftlength = shaftlength;
    }

    public BigDecimal getShaftlength() 
    {
        return shaftlength;
    }
    public void setPdtcode(String pdtcode) 
    {
        this.pdtcode = pdtcode;
    }

    public String getPdtcode() 
    {
        return pdtcode;
    }
    public void setPdtweftshrinkage(BigDecimal pdtweftshrinkage) 
    {
        this.pdtweftshrinkage = pdtweftshrinkage;
    }

    public BigDecimal getPdtweftshrinkage() 
    {
        return pdtweftshrinkage;
    }
    public void setOrdercode(String ordercode) 
    {
        this.ordercode = ordercode;
    }

    public String getOrdercode() 
    {
        return ordercode;
    }
    public void setShaftstatus(String shaftstatus) 
    {
        this.shaftstatus = shaftstatus;
    }

    public String getShaftstatus() 
    {
        return shaftstatus;
    }
    public void setShaftremainlength(BigDecimal shaftremainlength) 
    {
        this.shaftremainlength = shaftremainlength;
    }

    public BigDecimal getShaftremainlength() 
    {
        return shaftremainlength;
    }
    public void setClothlength(BigDecimal clothlength) 
    {
        this.clothlength = clothlength;
    }

    public BigDecimal getClothlength() 
    {
        return clothlength;
    }
    public void setShaftworker(String shaftworker) 
    {
        this.shaftworker = shaftworker;
    }

    public String getShaftworker() 
    {
        return shaftworker;
    }
    public void setWarpbacth(String warpbacth) 
    {
        this.warpbacth = warpbacth;
    }

    public String getWarpbacth() 
    {
        return warpbacth;
    }
    public void setWeftbacth(String weftbacth) 
    {
        this.weftbacth = weftbacth;
    }

    public String getWeftbacth() 
    {
        return weftbacth;
    }
    public void setPlanstart(Date planstart) 
    {
        this.planstart = planstart;
    }

    public Date getPlanstart() 
    {
        return planstart;
    }
    public void setPlanend(Date planend) 
    {
        this.planend = planend;
    }

    public Date getPlanend() 
    {
        return planend;
    }
    public void setActstart(Date actstart) 
    {
        this.actstart = actstart;
    }

    public Date getActstart() 
    {
        return actstart;
    }
    public void setActend(Date actend) 
    {
        this.actend = actend;
    }

    public Date getActend() 
    {
        return actend;
    }
    public void setPlanmaccode(String planmaccode) 
    {
        this.planmaccode = planmaccode;
    }

    public String getPlanmaccode() 
    {
        return planmaccode;
    }
    public void setActmaccode(String actmaccode) 
    {
        this.actmaccode = actmaccode;
    }

    public String getActmaccode() 
    {
        return actmaccode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shaftcode", getShaftcode())
            .append("shaftno", getShaftno())
            .append("shaftlength", getShaftlength())
            .append("pdtcode", getPdtcode())
            .append("pdtweftshrinkage", getPdtweftshrinkage())
            .append("ordercode", getOrdercode())
            .append("shaftstatus", getShaftstatus())
            .append("shaftremainlength", getShaftremainlength())
            .append("clothlength", getClothlength())
            .append("shaftworker", getShaftworker())
            .append("warpbacth", getWarpbacth())
            .append("weftbacth", getWeftbacth())
            .append("createtime", getCreatetime())
            .append("updatetime", getUpdatetime())
            .append("planstart", getPlanstart())
            .append("planend", getPlanend())
            .append("actstart", getActstart())
            .append("actend", getActend())
            .append("planmaccode", getPlanmaccode())
            .append("actmaccode", getActmaccode())
            .append("remark", getRemark())
            .toString();
    }
}
