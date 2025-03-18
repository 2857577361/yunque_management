package com.ruoyi.disease.service;

import java.util.List;
import com.ruoyi.disease.domain.DeviceDisease;

/**
 * 设备与病害关联Service接口
 */
public interface IDeviceDiseaseService {
    /**
     * 查询设备与病害关联列表（支持模糊搜索）
     *
     * @param deviceDisease 查询条件
     * @return 设备与病害关联集合
     */
    List<DeviceDisease> selectDeviceDiseaseList(DeviceDisease deviceDisease);
}