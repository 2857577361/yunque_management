package com.ruoyi.models.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.models.mapper.DeviceDiseaseModelMapper;
import com.ruoyi.models.domain.DeviceDiseasePrediction;
import com.ruoyi.models.domain.vo.DeviceDiseasePredictionVo;
import com.ruoyi.models.domain.vo.DeviceDiseaseModelVo;
import com.ruoyi.models.domain.DeviceDiseaseModelRelation;
import com.ruoyi.models.service.IDeviceDiseaseModelService;

@Service
public class DeviceDiseaseModelServiceImpl implements IDeviceDiseaseModelService {

    @Autowired
    private DeviceDiseaseModelMapper deviceDiseaseModelMapper;

    @Override
    public List<DeviceDiseasePredictionVo> selectDeviceDiseasePredictionList(String deviceId, Integer diseaseId) {
        List<DeviceDiseasePredictionVo> list = deviceDiseaseModelMapper.selectDeviceDiseasePredictionList(deviceId, diseaseId);
        // 判断是否是预测期
        list.forEach(item -> {
            boolean isPredictionPeriod = deviceDiseaseModelMapper.checkIsPredictionPeriod(
                    item.getDeviceId(), item.getDiseaseId());
            item.setIsPredictionPeriod(isPredictionPeriod);
        });
        return list;
    }

    @Override
    public DeviceDiseasePrediction selectPredictionDetail(String deviceId, Integer diseaseId) {
        return deviceDiseaseModelMapper.selectPredictionDetail(deviceId, diseaseId);
    }

    @Override
    public int insertPrediction(DeviceDiseasePrediction prediction) {
        prediction.setCreateTime(new Date());
        return deviceDiseaseModelMapper.insertPrediction(prediction);
    }

    @Override
    public int updatePrediction(DeviceDiseasePrediction prediction) {
        // Set update time
        prediction.setUpdateTime(new Date());

        // First check if the prediction record exists
        DeviceDiseasePrediction existing = deviceDiseaseModelMapper.selectPredictionDetail(
                prediction.getDeviceId(),
                prediction.getDiseaseId()
        );

        if (existing != null) {
            // If record exists, perform update
            return deviceDiseaseModelMapper.updatePrediction(prediction);
        } else {
            // If record doesn't exist, perform insert
            prediction.setCreateTime(new Date());
            return deviceDiseaseModelMapper.insertPrediction(prediction);
        }
    }

    @Override
    public List<DeviceDiseaseModelVo> selectDeviceDiseaseModels(String deviceId) {
        return deviceDiseaseModelMapper.selectDeviceDiseaseModels(deviceId);
    }

//    @Override
//    public int updateDeviceDiseaseModelRelations(DeviceDiseaseModelRelation relation) {
//        // 先删除该设备的所有关联关系
//        deviceDiseaseModelMapper.deleteByDeviceId(relation.getDeviceId());
//        // 批量插入新的关联关系
//        if (relation.getDiseaseModels() != null && !relation.getDiseaseModels().isEmpty()) {
//            return deviceDiseaseModelMapper.batchInsertDeviceDiseaseModels(relation);
//        }
//        return 1;
//    }
//    @Override
//    public int updateDeviceDiseaseModelRelations(DeviceDiseaseModelRelation relation) {
//        if (relation.getDiseaseModels() == null || relation.getDiseaseModels().isEmpty()) {
//            return 1;
//        }
//
//        int result = 0;
//        for (DeviceDiseaseModelVo model : relation.getDiseaseModels()) {
//            // 检查是否已存在
//            boolean exists = deviceDiseaseModelMapper.checkExists(
//                    relation.getDeviceId(),
//                    model.getDiseaseId(),
//                    model.getModelId()
//            );
//
//            if (exists) {
//                // 更新现有记录
//                result += deviceDiseaseModelMapper.updateDeviceDiseaseModel(
//                        relation.getDeviceId(),
//                        model.getDiseaseId(),
//                        model.getModelId(),
//                        model
//                );
//            } else {
//                // 插入新记录
//                result += deviceDiseaseModelMapper.insertDeviceDiseaseModel(
//                        relation.getDeviceId(),
//                        model.getDiseaseId(),
//                        model.getModelId(),
//                        model.getIsPredictionEnabled(),
//                        model.getPredictionStartTime(),
//                        model.getPredictionEndTime(),
//                        model.getArea()
//                );
//            }
//        }
//        return result > 0 ? 1 : 0;
//    }

    @Override
    public int updateDeviceDiseaseModelRelations(DeviceDiseaseModelRelation relation) {
        if (relation.getDiseaseModels() == null || relation.getDiseaseModels().isEmpty()) {
            return 1;
        }

        int result = 0;
        for (DeviceDiseaseModelVo model : relation.getDiseaseModels()) {
            // 检查是否已存在
            boolean exists = deviceDiseaseModelMapper.existsDeviceDiseaseModel(
                    relation.getDeviceId(),
                    model.getDiseaseId(),
                    model.getModelId()
            );

            if (exists) {
                // 选择性更新现有记录（不更新空值）
                result += deviceDiseaseModelMapper.updateDeviceDiseaseModelSelective(
                        relation.getDeviceId(),
                        model.getDiseaseId(),
                        model.getModelId(),
                        model
                );
            } else {
                // 插入新记录
                result += deviceDiseaseModelMapper.batchInsertDeviceDiseaseModels(
                        new DeviceDiseaseModelRelation() {{
                            setDeviceId(relation.getDeviceId());
                            setDiseaseModels(Collections.singletonList(model));
                        }}
                );
            }
        }
        return result > 0 ? 1 : 0;
    }
    @Override
    public int deleteDeviceDiseaseModelRelations(DeviceDiseaseModelRelation relation) {
        if (relation.getDiseaseModels() == null || relation.getDiseaseModels().isEmpty()) {
            // 删除设备所有关联
            return deviceDiseaseModelMapper.deleteByDeviceId(relation.getDeviceId());
        } else {
            // 获取第一个病害模型关联信息
            DeviceDiseaseModelVo firstModel = relation.getDiseaseModels().get(0);

            if (firstModel.getModelId() != null) {
                // 删除设备+病害+模型关联
                return deviceDiseaseModelMapper.deleteByDeviceDiseaseModel(
                        relation.getDeviceId(),
                        firstModel.getDiseaseId(),
                        firstModel.getModelId());
            } else {
                // 删除设备+病害关联
                return deviceDiseaseModelMapper.deleteByDeviceDisease(
                        relation.getDeviceId(),
                        firstModel.getDiseaseId());
            }
        }
    }
}