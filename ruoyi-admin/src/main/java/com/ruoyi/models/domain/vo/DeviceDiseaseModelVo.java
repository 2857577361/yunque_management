package com.ruoyi.models.domain.vo;

public class DeviceDiseaseModelVo {
    private Integer diseaseId;
    private String diseaseName;
    private Integer modelId;
    private String modelName;
    private Boolean isPredictionEnabled;

    private String area;

    // getters and setters


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public Boolean getPredictionEnabled() {
        return isPredictionEnabled;
    }

    public void setPredictionEnabled(Boolean predictionEnabled) {
        isPredictionEnabled = predictionEnabled;
    }
}