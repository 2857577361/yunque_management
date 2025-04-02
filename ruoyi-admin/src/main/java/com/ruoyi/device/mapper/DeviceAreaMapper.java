package com.ruoyi.device.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.device.domain.DeviceArea;

public interface DeviceAreaMapper {
    /**
     * 查询设备监测区域信息列表
     */
    List<DeviceArea> selectDeviceAreaList(
            @Param("deviceId") String deviceId,
            @Param("cropType") String cropType,
            @Param("province") String province,
            @Param("city") String city,
            @Param("district") String district);

    /**
     * 根据设备ID查询监测区域信息
     */
    DeviceArea selectDeviceAreaByDeviceId(String deviceId);
}