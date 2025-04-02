package com.ruoyi.models.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.models.mapper.DeviceDiseaseModelMapper;
import com.ruoyi.models.domain.vo.DeviceDiseasePredictionVo;
import com.ruoyi.models.domain.vo.DeviceDiseaseModelVo;
import com.ruoyi.models.domain.DeviceDiseaseModelRelation;
import com.ruoyi.models.service.IDeviceDiseaseModelService;

@Service
public class DeviceDiseaseModelServiceImpl implements IDeviceDiseaseModelService {

    @Autowired
    private DeviceDiseaseModelMapper deviceDiseaseModelMapper;

    @Override
    public List<DeviceDiseasePredictionVo> selectDeviceDiseasePredictionList() {
        List<DeviceDiseasePredictionVo> list = deviceDiseaseModelMapper.selectDeviceDiseasePredictionList();
        // 判断是否是预测期
        list.forEach(item -> {
            boolean isPredictionPeriod = deviceDiseaseModelMapper.checkIsPredictionPeriod(
                    item.getDeviceId(), item.getDiseaseId());
            item.setIsPredictionPeriod(isPredictionPeriod);
        });
        return list;
    }

    @Override
    public List<DeviceDiseaseModelVo> selectDeviceDiseaseModels(String deviceId) {
        return deviceDiseaseModelMapper.selectDeviceDiseaseModels(deviceId);
    }

    @Override
    public int updateDeviceDiseaseModelRelations(DeviceDiseaseModelRelation relation) {
        // 先删除该设备的所有关联关系
        deviceDiseaseModelMapper.deleteByDeviceId(relation.getDeviceId());

        // 批量插入新的关联关系
        if (relation.getDiseaseModels() != null && !relation.getDiseaseModels().isEmpty()) {
            return deviceDiseaseModelMapper.batchInsertDeviceDiseaseModels(relation);
        }
        return 1;
    }

    @Override
    public int deleteDeviceDiseaseModelRelations(DeviceDiseaseModelRelation relation) {
        // 如果传入了完整的三个ID，则精确删除
        if (relation.getDiseaseModels() != null &&
                !relation.getDiseaseModels().isEmpty() &&
                relation.getDiseaseModels().get(0).containsKey("modelId")) {

            return deviceDiseaseModelMapper.deleteByDeviceDiseaseModel(
                    relation.getDeviceId(),
                    relation.getDiseaseModels().get(0).get("diseaseId"),
                    relation.getDiseaseModels().get(0).get("modelId"));
        }
        // 如果传入了设备ID和病害ID，则删除匹配的
        else if (relation.getDiseaseModels() != null &&
                !relation.getDiseaseModels().isEmpty()) {

            return deviceDiseaseModelMapper.deleteByDeviceDisease(
                    relation.getDeviceId(),
                    relation.getDiseaseModels().get(0).get("diseaseId"));
        }
        // 如果只传入了设备ID，则删除该设备所有关联
        else {
            return deviceDiseaseModelMapper.deleteByDeviceId(relation.getDeviceId());
        }
    }
}