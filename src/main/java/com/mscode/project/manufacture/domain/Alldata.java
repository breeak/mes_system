package com.mscode.project.manufacture.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mscode.framework.aspectj.lang.annotation.Excel;
import com.mscode.framework.web.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 基础数据对象 alldata
 * 
 * @author mscode
 * @date 2020-11-05
 */
public class Alldata extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String middleno;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long stationno;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long starttime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date startdate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long endtime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date enddate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long dusec;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long state;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long output;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMiddleno(String middleno) 
    {
        this.middleno = middleno;
    }

    public String getMiddleno() 
    {
        return middleno;
    }
    public void setStationno(Long stationno) 
    {
        this.stationno = stationno;
    }

    public Long getStationno() 
    {
        return stationno;
    }
    public void setStarttime(Long starttime) 
    {
        this.starttime = starttime;
    }

    public Long getStarttime() 
    {
        return starttime;
    }
    public void setStartdate(Date startdate) 
    {
        this.startdate = startdate;
    }

    public Date getStartdate() 
    {
        return startdate;
    }
    public void setEndtime(Long endtime) 
    {
        this.endtime = endtime;
    }

    public Long getEndtime() 
    {
        return endtime;
    }
    public void setEnddate(Date enddate) 
    {
        this.enddate = enddate;
    }

    public Date getEnddate() 
    {
        return enddate;
    }
    public void setDusec(Long dusec) 
    {
        this.dusec = dusec;
    }

    public Long getDusec() 
    {
        return dusec;
    }
    public void setState(Long state) 
    {
        this.state = state;
    }

    public Long getState() 
    {
        return state;
    }
    public void setOutput(Long output) 
    {
        this.output = output;
    }

    public Long getOutput() 
    {
        return output;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("middleno", getMiddleno())
            .append("stationno", getStationno())
            .append("starttime", getStarttime())
            .append("startdate", getStartdate())
            .append("endtime", getEndtime())
            .append("enddate", getEnddate())
            .append("dusec", getDusec())
            .append("state", getState())
            .append("output", getOutput())
            .toString();
    }
}
