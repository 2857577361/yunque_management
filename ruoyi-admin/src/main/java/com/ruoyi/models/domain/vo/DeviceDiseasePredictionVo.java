package com.ruoyi.models.domain.vo;

public class DeviceDiseasePredictionVo {
    private String deviceId;
    private Integer diseaseId;
    private String diseaseName;
    private Boolean isPredictionPeriod;
    private Double predictedValue;
    private Double actualValue;
    private String predictionDataTime;

    // getters and setters

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

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public Boolean getIsPredictionPeriod() {
        return isPredictionPeriod;
    }

    public void setIsPredictionPeriod(Boolean predictionPeriod) {
        isPredictionPeriod = predictionPeriod;
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

    public String getPredictionDataTime() {
        return predictionDataTime;
    }

    public void setPredictionDataTime(String predictionDataTime) {
        this.predictionDataTime = predictionDataTime;
    }
}