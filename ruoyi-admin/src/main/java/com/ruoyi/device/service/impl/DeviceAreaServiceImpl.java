package com.ruoyi.device.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.device.domain.DeviceArea;
import com.ruoyi.device.mapper.DeviceAreaMapper;
import com.ruoyi.device.service.IDeviceAreaService;

@Service
public class DeviceAreaServiceImpl implements IDeviceAreaService {

    @Autowired
    private DeviceAreaMapper deviceAreaMapper;

    @Override
    public List<DeviceArea> selectDeviceAreaList(String deviceId, String cropType, String province, String city, String district) {
        return deviceAreaMapper.selectDeviceAreaList(deviceId, cropType, province, city, district);
    }

    @Override
    public DeviceArea selectDeviceAreaByDeviceId(String deviceId) {
        return deviceAreaMapper.selectDeviceAreaByDeviceId(deviceId);
    }
}