package com.ruoyi.models.mapper;

import com.ruoyi.models.domain.vo.DiseaseModelVo;
import java.util.List;

public interface DiseaseMapper {
    List<DiseaseModelVo> selectDiseaseModelList();
}