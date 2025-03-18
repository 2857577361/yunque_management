package com.ruoyi.disease.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.disease.mapper.DeviceDiseaseMapper;
import com.ruoyi.disease.domain.DeviceDisease;
import com.ruoyi.disease.service.IDeviceDiseaseService;

/**
 * 设备与病害关联Service业务层处理
 */
@Service
public class DeviceDiseaseServiceImpl implements IDeviceDiseaseService {
    @Autowired
    private DeviceDiseaseMapper deviceDiseaseMapper;

    @Override
    public List<DeviceDisease> selectDeviceDiseaseList(DeviceDisease deviceDisease) {
        return deviceDiseaseMapper.selectDeviceDiseaseList(deviceDisease);
    }
}