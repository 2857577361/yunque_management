package com.ruoyi.models.mapper;

import org.apache.ibatis.annotations.Param;
public interface DiseaseModelRelationMapper {
    int deleteByDiseaseId(@Param("diseaseId")Long diseaseId);
    int insert(@Param("diseaseId")Long diseaseId, @Param("modelId")Long modelId);
}