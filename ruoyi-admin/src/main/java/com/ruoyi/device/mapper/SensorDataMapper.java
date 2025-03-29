package com.ruoyi.device.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.device.domain.SensorData;

/**
 * 传感器数据 Mapper 接口
 *
 * @author ruoyi
 * @date 2025-03-24
 */
public interface SensorDataMapper {
    /**
     * 根据设备 ID 列表查询气候数据
     */
    List<SensorData> selectClimateDataByDeviceIds(
            @Param("deviceIds") List<String> deviceIds,
            @Param("startCollectTime") Date startCollectTime,
            @Param("endCollectTime") Date endCollectTime,
            @Param("startUploadTime") Date startUploadTime,
            @Param("endUploadTime") Date endUploadTime
    );
}