package com.ruoyi.disease.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备与病害关联对象 device_disease
 */
public class DeviceDisease extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String deviceId;

    /** 病害ID */
    @Excel(name = "病害ID")
    private Long diseaseId;

    /** 是否处于预测期 */
    @Excel(name = "是否处于预测期")
    private Integer isPredictionPeriod;

    /** 病害名称 */
    @Excel(name = "病害名称")
    private String diseaseName;

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

    public Long getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Long diseaseId) {
        this.diseaseId = diseaseId;
    }

    public Integer getIsPredictionPeriod() {
        return isPredictionPeriod;
    }

    public void setIsPredictionPeriod(Integer isPredictionPeriod) {
        this.isPredictionPeriod = isPredictionPeriod;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("deviceId", getDeviceId())
                .append("diseaseId", getDiseaseId())
                .append("isPredictionPeriod", getIsPredictionPeriod())
                .append("diseaseName", getDiseaseName())
                .toString();
    }
}