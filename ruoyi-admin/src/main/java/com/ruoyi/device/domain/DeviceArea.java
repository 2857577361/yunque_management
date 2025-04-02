package com.ruoyi.device.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备监测区域信息对象 device_area
 *
 * @author ruoyi
 * @date 2025-04-02
 */
public class DeviceArea extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String deviceId;

    /** 总监测面积(亩) */
    @Excel(name = "总监测面积")
    private Double totalArea;

    /** 监测农作物种类(逗号分隔) */
    @Excel(name = "监测农作物种类")
    private String cropTypes;

    /** 农作物详情(JSON格式) */
    @Excel(name = "农作物详情")
    private String cropDetails;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(Double totalArea) {
        this.totalArea = totalArea;
    }

    public String getCropTypes() {
        return cropTypes;
    }

    public void setCropTypes(String cropTypes) {
        this.cropTypes = cropTypes;
    }

    public String getCropDetails() {
        return cropDetails;
    }

    public void setCropDetails(String cropDetails) {
        this.cropDetails = cropDetails;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("deviceId", getDeviceId())
                .append("totalArea", getTotalArea())
                .append("cropTypes", getCropTypes())
                .append("cropDetails", getCropDetails())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}