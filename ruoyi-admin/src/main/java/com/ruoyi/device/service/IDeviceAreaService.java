package com.ruoyi.device.service;

import java.util.List;
import com.ruoyi.device.domain.DeviceArea;

public interface IDeviceAreaService {
    /**
     * 查询设备监测区域信息列表
     * @param deviceId 设备ID
     * @param cropType 农作物种类
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @return 设备监测区域信息列表
     */
    List<DeviceArea> selectDeviceAreaList(String deviceId, String cropType, String province, String city, String district);

    /**
     * 根据设备ID查询监测区域信息
     * @param deviceId 设备ID
     * @return 设备监测区域信息
     */
    DeviceArea selectDeviceAreaByDeviceId(String deviceId);
}