package com.ruoyi.models.mapper;

import java.util.List;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.models.domain.DeviceDiseasePrediction;
import com.ruoyi.models.domain.vo.DeviceDiseasePredictionVo;
import com.ruoyi.models.domain.vo.DeviceDiseaseModelVo;
import com.ruoyi.models.domain.DeviceDiseaseModelRelation;

public interface DeviceDiseaseModelMapper {

    List<DeviceDiseasePredictionVo> selectDeviceDiseasePredictionList(
            @Param("deviceId") String deviceId,
            @Param("diseaseId") Integer diseaseId);

    DeviceDiseasePrediction selectPredictionDetail(
            @Param("deviceId") String deviceId,
            @Param("diseaseId") Integer diseaseId);

    int insertPrediction(DeviceDiseasePrediction prediction);

    int updatePrediction(DeviceDiseasePrediction prediction);

    boolean checkIsPredictionPeriod(
            @Param("deviceId") String deviceId,
            @Param("diseaseId") Integer diseaseId);

    List<DeviceDiseaseModelVo> selectDeviceDiseaseModels(String deviceId);

    int deleteByDeviceId(String deviceId);

    int batchInsertDeviceDiseaseModels(DeviceDiseaseModelRelation relation);

    boolean existsDeviceDiseaseModel(
            @Param("deviceId") String deviceId,
            @Param("diseaseId") Integer diseaseId,
            @Param("modelId") Integer modelId);

    int updateDeviceDiseaseModelSelective(
            @Param("deviceId") String deviceId,
            @Param("diseaseId") Integer diseaseId,
            @Param("modelId") Integer modelId,
            @Param("vo") DeviceDiseaseModelVo vo);
    int deleteByDeviceDisease(
            @Param("deviceId") String deviceId,
            @Param("diseaseId") Integer diseaseId);

    int deleteByDeviceDiseaseModel(
            @Param("deviceId") String deviceId,
            @Param("diseaseId") Integer diseaseId,
            @Param("modelId") Integer modelId);
}