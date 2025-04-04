package com.ruoyi.models.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class DeviceDiseaseModelVo {
    private Integer diseaseId;
    private String diseaseName;
    private String diseaseDescription;
    private String diseaseSeries;
    private Integer modelId;
    private String modelName;
    private Boolean isPredictionEnabled;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date predictionStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date predictionEndTime;
    private String area;

    // Getters and Setters
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

    public String getDiseaseDescription() {
        return diseaseDescription;
    }

    public void setDiseaseDescription(String diseaseDescription) {
        this.diseaseDescription = diseaseDescription;
    }

    public String getDiseaseSeries() {
        return diseaseSeries;
    }

    public void setDiseaseSeries(String diseaseSeries) {
        this.diseaseSeries = diseaseSeries;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Boolean getIsPredictionEnabled() {
        return isPredictionEnabled;
    }

    public void setIsPredictionEnabled(Boolean predictionEnabled) {
        isPredictionEnabled = predictionEnabled;
    }

    public Date getPredictionStartTime() {
        return predictionStartTime;
    }

    public void setPredictionStartTime(Date predictionStartTime) {
        this.predictionStartTime = predictionStartTime;
    }

    public Date getPredictionEndTime() {
        return predictionEndTime;
    }

    public void setPredictionEndTime(Date predictionEndTime) {
        this.predictionEndTime = predictionEndTime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}