package com.ruoyi.device.service;

import java.util.Date;
import java.util.List;
import com.ruoyi.device.domain.SensorData;

/**
 * 传感器数据 Service 接口
 *
 * @author ruoyi
 * @date 2025-03-24
 */
public interface ISensorDataService {
    /**
     * 根据设备 ID 列表查询气候数据
     */
    List<SensorData> selectClimateDataByDeviceIds(
            List<String> deviceIds,
            Date startCollectTime,
            Date endCollectTime,
            Date startUploadTime,
            Date endUploadTime
    );
}