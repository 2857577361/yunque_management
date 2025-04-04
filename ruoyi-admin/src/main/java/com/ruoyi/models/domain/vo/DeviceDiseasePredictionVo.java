package com.ruoyi.models.domain.vo;

public class DeviceDiseasePredictionVo {
    private String deviceId;
    private Integer diseaseId;
    private String diseaseName;
    private Boolean isPredictionPeriod;
    private Double predictedValue;
    private Double actualValue;
    private String predictionDataTime;
    private String predictedValueDescription;
    private Integer selectedModelId;
    private String area;

    // getters and setters


    public Boolean getPredictionPeriod() {
        return isPredictionPeriod;
    }

    public void setPredictionPeriod(Boolean predictionPeriod) {
        isPredictionPeriod = predictionPeriod;
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