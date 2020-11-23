package com.mscode.project.manufacture.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mscode.framework.aspectj.lang.annotation.Excel;
import com.mscode.framework.web.domain.BaseEntity;

/**
 * 班次效率对象 mft_shift
 * 
 * @author MS
 * @date 2020-09-20
 */
public class MftShift extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键自增 */
    private Long id;

    /** 织机编号 */
    @Excel(name = "织机编号")
    private String maccode;

    /** 班次类别(早中晚) */
    @Excel(name = "班次类别(早中晚)")
    private String shifttype;

    /** 班次开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "班次开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date shiftstarttime;

    /** 班次结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "班次结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date shiftendtime;

    /** 品种编号列表 */
    @Excel(name = "品种编号列表")
    private String pdtcodes;

    /** 织轴编号列表 */
    @Excel(name = "织轴编号列表")
    private String shaftcodes;

    /** 织机平均速度 */
    @Excel(name = "织机平均速度")
    private BigDecimal macspeed;

    /** 织机平均效率 */
    @Excel(name = "织机平均效率")
    private BigDecimal macefficiency;

    /** 运行时长s */
    @Excel(name = "运行时长s")
    private Long runtime;

    /** 停机时长 */
    @Excel(name = "停机时长")
    private Long stoptime;

    /** 经停时长s */
    @Excel(name = "经停时长s")
    private Long warpstoptime;

    /** 纬停时长s */
    @Excel(name = "纬停时长s")
    private Long weftstoptime;

    /** 离线时长s */
    @Excel(name = "离线时长s")
    private Long offlinetime;

    /** 其他停时长s */
    @Excel(name = "其他停时长s")
    private Long otherstoptime;

    /** 经停次数 */
    @Excel(name = "经停次数")
    private Long warpstopnum;

    /** 纬停次数 */
    @Excel(name = "纬停次数")
    private Long weftstopnum;

    /** 其他停次数 */
    @Excel(name = "其他停次数")
    private Long otherstopnum;

    /** 打纬次数 */
    @Excel(name = "打纬次数")
    private Long picknum;

    /** 信号上传次数 */
    private Long uploadnum;

    /** 信号改变次数 */
    private Long statuschangenum;

    /** 班次产量 */
    @Excel(name = "班次产量")
    private BigDecimal shiftlength;

    /** 织轴起始产量 */
    @Excel(name = "织轴起始产量")
    private BigDecimal startlength;

    private String middleno;
    private Long stationno;

    public String getMiddleno() {
        return middleno;
    }

    public void setMiddleno(String middleno) {
        this.middleno = middleno;
    }

    public Long getStationno() {
        return stationno;
    }

    public void setStationno(Long stationno) {
        this.stationno = stationno;
    }

    /** 班次日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "班次日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date shiftdate;

    /** 是否当前班次 */
    @Excel(name = "是否当前班次")
    private Long shiftnow;

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
    public void setShifttype(String shifttype) 
    {
        this.shifttype = shifttype;
    }

    public String getShifttype() 
    {
        return shifttype;
    }
    public void setShiftstarttime(Date shiftstarttime) 
    {
        this.shiftstarttime = shiftstarttime;
    }

    public Date getShiftstarttime() 
    {
        return shiftstarttime;
    }
    public void setShiftendtime(Date shiftendtime) 
    {
        this.shiftendtime = shiftendtime;
    }

    public Date getShiftendtime() 
    {
        return shiftendtime;
    }
    public void setPdtcodes(String pdtcodes) 
    {
        this.pdtcodes = pdtcodes;
    }

    public String getPdtcodes() 
    {
        return pdtcodes;
    }
    public void setShaftcodes(String shaftcodes) 
    {
        this.shaftcodes = shaftcodes;
    }

    public String getShaftcodes() 
    {
        return shaftcodes;
    }
    public void setMacspeed(BigDecimal macspeed) 
    {
        this.macspeed = macspeed;
    }

    public BigDecimal getMacspeed() 
    {
        return macspeed;
    }
    public void setMacefficiency(BigDecimal macefficiency) 
    {
        this.macefficiency = macefficiency;
    }

    public BigDecimal getMacefficiency() 
    {
        return macefficiency;
    }
    public void setRuntime(Long runtime) 
    {
        this.runtime = runtime;
    }

    public Long getRuntime() 
    {
        return runtime;
    }
    public void setStoptime(Long stoptime) 
    {
        this.stoptime = stoptime;
    }

    public Long getStoptime() 
    {
        return stoptime;
    }
    public void setWarpstoptime(Long warpstoptime) 
    {
        this.warpstoptime = warpstoptime;
    }

    public Long getWarpstoptime() 
    {
        return warpstoptime;
    }
    public void setWeftstoptime(Long weftstoptime) 
    {
        this.weftstoptime = weftstoptime;
    }

    public Long getWeftstoptime() 
    {
        return weftstoptime;
    }
    public void setOfflinetime(Long offlinetime) 
    {
        this.offlinetime = offlinetime;
    }

    public Long getOfflinetime() 
    {
        return offlinetime;
    }
    public void setOtherstoptime(Long otherstoptime) 
    {
        this.otherstoptime = otherstoptime;
    }

    public Long getOtherstoptime() 
    {
        return otherstoptime;
    }
    public void setWarpstopnum(Long warpstopnum) 
    {
        this.warpstopnum = warpstopnum;
    }

    public Long getWarpstopnum() 
    {
        return warpstopnum;
    }
    public void setWeftstopnum(Long weftstopnum) 
    {
        this.weftstopnum = weftstopnum;
    }

    public Long getWeftstopnum() 
    {
        return weftstopnum;
    }
    public void setOtherstopnum(Long otherstopnum) 
    {
        this.otherstopnum = otherstopnum;
    }

    public Long getOtherstopnum() 
    {
        return otherstopnum;
    }
    public void setPicknum(Long picknum) 
    {
        this.picknum = picknum;
    }

    public Long getPicknum() 
    {
        return picknum;
    }
    public void setUploadnum(Long uploadnum) 
    {
        this.uploadnum = uploadnum;
    }

    public Long getUploadnum() 
    {
        return uploadnum;
    }
    public void setStatuschangenum(Long statuschangenum) 
    {
        this.statuschangenum = statuschangenum;
    }

    public Long getStatuschangenum() 
    {
        return statuschangenum;
    }
    public void setShiftlength(BigDecimal shiftlength) 
    {
        this.shiftlength = shiftlength;
    }

    public BigDecimal getShiftlength() 
    {
        return shiftlength;
    }
    public void setShiftdate(Date shiftdate) 
    {
        this.shiftdate = shiftdate;
    }

    public Date getShiftdate() 
    {
        return shiftdate;
    }
    public void setShiftnow(Long shiftnow) 
    {
        this.shiftnow = shiftnow;
    }

    public Long getShiftnow() 
    {
        return shiftnow;
    }

    public BigDecimal getStartlength() {
        return startlength;
    }

    public void setStartlength(BigDecimal startlength) {
        this.startlength = startlength;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("maccode", getMaccode())
            .append("shifttype", getShifttype())
            .append("shiftstarttime", getShiftstarttime())
            .append("shiftendtime", getShiftendtime())
            .append("pdtcodes", getPdtcodes())
            .append("shaftcodes", getShaftcodes())
            .append("macspeed", getMacspeed())
            .append("macefficiency", getMacefficiency())
            .append("runtime", getRuntime())
            .append("stoptime", getStoptime())
            .append("warpstoptime", getWarpstoptime())
            .append("weftstoptime", getWeftstoptime())
            .append("offlinetime", getOfflinetime())
            .append("otherstoptime", getOtherstoptime())
            .append("warpstopnum", getWarpstopnum())
            .append("weftstopnum", getWeftstopnum())
            .append("otherstopnum", getOtherstopnum())
            .append("picknum", getPicknum())
            .append("uploadnum", getUploadnum())
            .append("statuschangenum", getStatuschangenum())
            .append("shiftlength", getShiftlength())
            .append("startlength", getStartlength())
            .append("shiftdate", getShiftdate())
            .append("shiftnow", getShiftnow())
            .append("updatetime", getUpdatetime())
            .append("remark", getRemark())
            .toString();
    }
}
