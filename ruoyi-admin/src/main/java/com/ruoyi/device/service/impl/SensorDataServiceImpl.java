package com.ruoyi.device.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.device.mapper.SensorDataMapper;
import com.ruoyi.device.domain.SensorData;
import com.ruoyi.device.service.ISensorDataService;

/**
 * 传感器数据 Service 业务层处理
 *
 * @author ruoyi
 * @date 2025-03-24
 */
@Service
public class SensorDataServiceImpl implements ISensorDataService {

    @Autowired
    private SensorDataMapper sensorDataMapper;

    @Override
    public List<SensorData> selectClimateDataByDeviceIds(
            List<String> deviceIds,
            Date startCollectTime,
            Date endCollectTime,
            Date startUploadTime,
            Date endUploadTime
    ) {
        return sensorDataMapper.selectClimateDataByDeviceIds(
                deviceIds,
                startCollectTime,
                endCollectTime,
                startUploadTime,
                endUploadTime
        );
    }
}