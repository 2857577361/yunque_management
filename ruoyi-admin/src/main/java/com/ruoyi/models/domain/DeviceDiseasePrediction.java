package com.ruoyi.models.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备病害预测数据对象
 */
public class DeviceDiseasePrediction extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String deviceId;

    /** 病害ID */
    @Excel(name = "病害ID")
    private Integer diseaseId;

    /** 预测值 */
    @Excel(name = "预测值")
    private Double predictedValue;

    /** 实际值 */
    @Excel(name = "实际值")
    private Double actualValue;

    /** 偏差值 */
    @Excel(name = "偏差值")
    private Double deviationValue;

    /** 预测说明 */
    @Excel(name = "预测说明")
    private String predictedValueDescription;

    /** 选用模型ID */
    @Excel(name = "选用模型ID")
    private Integer selectedModelId;

    /** 区域 */
    @Excel(name = "区域")
    private String area;

    /** 预测数据时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "预测数据时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date predictionDataTime;

    /** 是否预测期 */
    @Excel(name = "是否预测期")
    private Boolean isPredictionPeriod;

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

    public Integer getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Integer diseaseId) {
        this.diseaseId = diseaseId;
    }

    public Double getPredictedValue() {
        return predictedValue;
    }

    public void setPredictedValue(Double predictedValue) {
        this.predictedValue = predictedValue;
    }

    public Double getActualValue() {
        return actualValue;
    }

    public void setActualValue(Double actualValue) {
        this.actualValue = actualValue;
    }

    public Double getDeviationValue() {
        return deviationValue;
    }

    public void setDeviationValue(Double deviationValue) {
        this.deviationValue = deviationValue;
    }

    public String getPredictedValueDescription() {
        return predictedValueDescription;
    }

    public void setPredictedValueDescription(String predictedValueDescription) {
        this.predictedValueDescription = predictedValueDescription;
    }

    public Integer getSelectedModelId() {
        return selectedModelId;
    }

    public void setSelectedModelId(Integer selectedModelId) {
        this.selectedModelId = selectedModelId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getPredictionDataTime() {
        return predictionDataTime;
    }

    public void setPredictionDataTime(Date predictionDataTime) {
        this.predictionDataTime = predictionDataTime;
    }

    public Boolean getIsPredictionPeriod() {
        return isPredictionPeriod;
    }

    public void setIsPredictionPeriod(Boolean predictionPeriod) {
        isPredictionPeriod = predictionPeriod;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("deviceId", getDeviceId())
                .append("diseaseId", getDiseaseId())
                .append("predictedValue", getPredictedValue())
                .append("actualValue", getActualValue())
                .append("deviationValue", getDeviationValue())
                .append("predictedValueDescription", getPredictedValueDescription())
                .append("selectedModelId", getSelectedModelId())
                .append("area", getArea())
                .append("predictionDataTime", getPredictionDataTime())
                .append("isPredictionPeriod", getIsPredictionPeriod())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}