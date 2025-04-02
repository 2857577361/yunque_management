package com.ruoyi.models.service;

import java.util.List;
import com.ruoyi.models.domain.vo.DeviceDiseasePredictionVo;
import com.ruoyi.models.domain.vo.DeviceDiseaseModelVo;
import com.ruoyi.models.domain.DeviceDiseaseModelRelation;

public interface IDeviceDiseaseModelService {

    /**
     * 查询设备病害预测数据列表
     *
     * @return 设备病害预测数据列表
     */
    List<DeviceDiseasePredictionVo> selectDeviceDiseasePredictionList();

    /**
     * 根据设备ID查询关联的病害和模型信息
     *
     * @param deviceId 设备ID
     * @return 病害和模型信息列表
     */
    List<DeviceDiseaseModelVo> selectDeviceDiseaseModels(String deviceId);

    /**
     * 更新设备病害模型关联关系
     *
     * @param relation 关联关系对象
     * @return 结果
     */
    int updateDeviceDiseaseModelRelations(DeviceDiseaseModelRelation relation);

    /**
     * 删除设备病害模型关联
     *
     * @param relation 关联关系对象
     * @return 结果
     */
    int deleteDeviceDiseaseModelRelations(DeviceDiseaseModelRelation relation);
}