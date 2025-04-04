package com.ruoyi.models.service;

import java.util.List;
import com.ruoyi.models.domain.DeviceDiseasePrediction;
import com.ruoyi.models.domain.vo.DeviceDiseasePredictionVo;
import com.ruoyi.models.domain.vo.DeviceDiseaseModelVo;
import com.ruoyi.models.domain.DeviceDiseaseModelRelation;

public interface IDeviceDiseaseModelService {

    /**
     * 查询设备病害预测数据列表
     */
    List<DeviceDiseasePredictionVo> selectDeviceDiseasePredictionList(String deviceId, Integer diseaseId);

    /**
     * 查询预测数据详情
     */
    DeviceDiseasePrediction selectPredictionDetail(String deviceId, Integer diseaseId);

    /**
     * 新增预测数据
     */
    int insertPrediction(DeviceDiseasePrediction prediction);

    /**
     * 修改预测数据
     */
    int updatePrediction(DeviceDiseasePrediction prediction);

    /**
     * 根据设备ID查询关联的病害和模型信息
     */
    List<DeviceDiseaseModelVo> selectDeviceDiseaseModels(String deviceId);

    /**
     * 更新设备病害模型关联关系
     */
    int updateDeviceDiseaseModelRelations(DeviceDiseaseModelRelation relation);

    /**
     * 删除设备病害模型关联
     */
    int deleteDeviceDiseaseModelRelations(DeviceDiseaseModelRelation relation);
}