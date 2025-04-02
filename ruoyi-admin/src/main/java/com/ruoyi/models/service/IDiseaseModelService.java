package com.ruoyi.models.service;

import com.ruoyi.models.domain.vo.DiseaseModelVo;
import com.ruoyi.models.domain.dto.DiseaseModelRelationDto;
import java.util.List;

public interface IDiseaseModelService {
    /**
     * 查询病害及其关联模型列表
     */
    List<DiseaseModelVo> selectDiseaseModelList();

    /**
     * 更新病害模型关系
     */
    int updateDiseaseModelRelation(DiseaseModelRelationDto relationDto);

    /**
     * 删除病害模型关系
     */
    int deleteDiseaseModelRelation(Long diseaseId);
}