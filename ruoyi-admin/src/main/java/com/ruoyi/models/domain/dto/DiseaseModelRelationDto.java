package com.ruoyi.models.domain.dto;

import java.util.List;

public class DiseaseModelRelationDto {
    private Long diseaseId;
    private List<Long> modelIds;

    // getters and setters
    public Long getDiseaseId() { return diseaseId; }
    public void setDiseaseId(Long diseaseId) { this.diseaseId = diseaseId; }

    public List<Long> getModelIds() { return modelIds; }
    public void setModelIds(List<Long> modelIds) { this.modelIds = modelIds; }
}