package com.ruoyi.models.domain.vo;

import java.util.List;

public class DiseaseModelVo {
    private Long diseaseId;
    private String diseaseName;
    private List<ModelInfo> modelList;

    // getters and setters
    public Long getDiseaseId() { return diseaseId; }
    public void setDiseaseId(Long diseaseId) { this.diseaseId = diseaseId; }

    public String getDiseaseName() { return diseaseName; }
    public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName; }

    public List<ModelInfo> getModelList() { return modelList; }
    public void setModelList(List<ModelInfo> modelList) { this.modelList = modelList; }

    public static class ModelInfo {
        private Long modelId;
        private String modelName;

        // getters and setters
        public Long getModelId() { return modelId; }
        public void setModelId(Long modelId) { this.modelId = modelId; }

        public String getModelName() { return modelName; }
        public void setModelName(String modelName) { this.modelName = modelName; }
    }
}