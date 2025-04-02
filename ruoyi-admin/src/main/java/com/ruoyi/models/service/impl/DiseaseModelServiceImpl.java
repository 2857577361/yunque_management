package com.ruoyi.models.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.models.mapper.DiseaseMapper;
import com.ruoyi.models.mapper.DiseaseModelRelationMapper;
import com.ruoyi.models.domain.vo.DiseaseModelVo;
import com.ruoyi.models.domain.dto.DiseaseModelRelationDto;
import com.ruoyi.models.service.IDiseaseModelService;

@Service
public class DiseaseModelServiceImpl implements IDiseaseModelService {

    @Autowired
    private DiseaseMapper diseaseMapper;

    @Autowired
    private DiseaseModelRelationMapper diseaseModelRelationMapper;

    @Override
    public List<DiseaseModelVo> selectDiseaseModelList() {
        return diseaseMapper.selectDiseaseModelList();
    }

    @Override
    public int updateDiseaseModelRelation(DiseaseModelRelationDto relationDto) {
        // 1. 删除该病害所有现有关系
        diseaseModelRelationMapper.deleteByDiseaseId(relationDto.getDiseaseId());

        // 2. 插入新的关系
        int count = 0;
        for (Long modelId : relationDto.getModelIds()) {
            count += diseaseModelRelationMapper.insert(
                    relationDto.getDiseaseId(),
                    modelId
            );
        }
        return count;
    }

    @Override
    public int deleteDiseaseModelRelation(Long diseaseId) {
        return diseaseModelRelationMapper.deleteByDiseaseId(diseaseId);
    }
}