package com.ruoyi.models.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.models.domain.vo.DeviceDiseasePredictionVo;
import com.ruoyi.models.domain.vo.DeviceDiseaseModelVo;
import com.ruoyi.models.domain.DeviceDiseaseModelRelation;

public interface DeviceDiseaseModelMapper {

    List<DeviceDiseasePredictionVo> selectDeviceDiseasePredictionList();

    boolean checkIsPredictionPeriod(@Param("deviceId") String deviceId, @Param("diseaseId") Integer diseaseId);

    List<DeviceDiseaseModelVo> selectDeviceDiseaseModels(String deviceId);

    int deleteByDeviceId(String deviceId);

    int batchInsertDeviceDiseaseModels(DeviceDiseaseModelRelation relation);

    int deleteByDeviceDisease(@Param("deviceId") String deviceId,
                              @Param("diseaseId") Integer diseaseId);

    int deleteByDeviceDiseaseModel(@Param("deviceId") String deviceId,
                                   @Param("diseaseId") Integer diseaseId,
                                   @Param("modelId") Integer modelId);
}