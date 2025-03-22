package com.ruoyi.device.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备列表对象 device_data
 * 
 * @author yhy
 * @date 2025-03-18
 */
public class DeviceData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String deviceId;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String address;

    /** 部门 */
    @Excel(name = "部门")
    private String department;

    /** 负责人 */
    @Excel(name = "负责人")
    private String responsiblePerson;

    /** 告警手机号 */
    @Excel(name = "告警手机号")
    private String alarmPhone;

    /** 经纬度 */
    @Excel(name = "经纬度")
    private String coordinates;

    /** 质保时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "质保时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date warrantyTime;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 国别 */
    private String country;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 县 */
    private String district;

    /** 拥有者ID */
    private Long ownerId;

    /** 是否公开 */
    private Boolean isOpened;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDeviceId(String deviceId) 
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId() 
    {
        return deviceId;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setDepartment(String department) 
    {
        this.department = department;
    }

    public String getDepartment() 
    {
        return department;
    }
    public void setResponsiblePerson(String responsiblePerson) 
    {
        this.responsiblePerson = responsiblePerson;
    }

    public String getResponsiblePerson() 
    {
        return responsiblePerson;
    }
    public void setAlarmPhone(String alarmPhone) 
    {
        this.alarmPhone = alarmPhone;
    }

    public String getAlarmPhone() 
    {
        return alarmPhone;
    }
    public void setCoordinates(String coordinates) 
    {
        this.coordinates = coordinates;
    }

    public String getCoordinates() 
    {
        return coordinates;
    }
    public void setWarrantyTime(Date warrantyTime) 
    {
        this.warrantyTime = warrantyTime;
    }

    public Date getWarrantyTime() 
    {
        return warrantyTime;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setCountry(String country) 
    {
        this.country = country;
    }

    public String getCountry() 
    {
        return country;
    }
    public void setProvince(String province) 
    {
        this.province = province;
    }

    public String getProvince() 
    {
        return province;
    }
    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }
    public void setDistrict(String district) 
    {
        this.district = district;
    }

    public String getDistrict() 
    {
        return district;
    }
    public void setOwnerId(Long ownerId) 
    {
        this.ownerId = ownerId;
    }

    public Long getOwnerId() 
    {
        return ownerId;
    }
    public void setIsOpened(Boolean isOpened)
    {
        this.isOpened = isOpened;
    }

    public Boolean getIsOpened()
    {
        return isOpened;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("address", getAddress())
            .append("department", getDepartment())
            .append("responsiblePerson", getResponsiblePerson())
            .append("alarmPhone", getAlarmPhone())
            .append("coordinates", getCoordinates())
            .append("updateTime", getUpdateTime())
            .append("warrantyTime", getWarrantyTime())
            .append("status", getStatus())
            .append("country", getCountry())
            .append("province", getProvince())
            .append("city", getCity())
            .append("district", getDistrict())
            .append("ownerId", getOwnerId())
            .append("isOpened", getIsOpened())
            .toString();
    }
}
