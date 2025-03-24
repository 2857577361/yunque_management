package com.ruoyi.device.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工单对象 work_orders
 * 
 * @author ruoyi
 * @date 2025-03-22
 */
public class WorkOrders extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 自增ID */
    private Long id;

    /** 工单ID */
    @Excel(name = "工单ID")
    private String workOrderId;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String deviceId;

    /** 检修人员 */
    @Excel(name = "检修人员")
    private String repairPerson;

    /** 检修地点 */
    @Excel(name = "检修地点")
    private String repairLocation;

    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    /** 质保时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "质保时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date warrantyTime;

    /** 检修内容 */
    @Excel(name = "检修内容")
    private String repairContent;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setWorkOrderId(String workOrderId) 
    {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderId() 
    {
        return workOrderId;
    }
    public void setDeviceId(String deviceId) 
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId() 
    {
        return deviceId;
    }
    public void setRepairPerson(String repairPerson) 
    {
        this.repairPerson = repairPerson;
    }

    public String getRepairPerson() 
    {
        return repairPerson;
    }
    public void setRepairLocation(String repairLocation) 
    {
        this.repairLocation = repairLocation;
    }

    public String getRepairLocation() 
    {
        return repairLocation;
    }
    public void setCreateDate(Date createDate) 
    {
        this.createDate = createDate;
    }

    public Date getCreateDate() 
    {
        return createDate;
    }
    public void setWarrantyTime(Date warrantyTime) 
    {
        this.warrantyTime = warrantyTime;
    }

    public Date getWarrantyTime() 
    {
        return warrantyTime;
    }
    public void setRepairContent(String repairContent) 
    {
        this.repairContent = repairContent;
    }

    public String getRepairContent() 
    {
        return repairContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("workOrderId", getWorkOrderId())
            .append("deviceId", getDeviceId())
            .append("repairPerson", getRepairPerson())
            .append("repairLocation", getRepairLocation())
            .append("createDate", getCreateDate())
            .append("warrantyTime", getWarrantyTime())
            .append("repairContent", getRepairContent())
            .toString();
    }
}
