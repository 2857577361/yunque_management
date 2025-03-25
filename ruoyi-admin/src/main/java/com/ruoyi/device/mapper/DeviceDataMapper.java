package com.ruoyi.device.mapper;

import java.util.List;
import com.ruoyi.device.domain.DeviceData;
import org.apache.ibatis.annotations.Param;

/**
 * 设备列表Mapper接口
 * 
 * @author yhy
 * @date 2025-03-18
 */
public interface DeviceDataMapper 
{
    /**
     * 查询设备列表
     * 
     * @param id 设备列表主键
     * @return 设备列表
     */
    public DeviceData selectDeviceDataById(Long id);

    /**
     * 查询设备列表列表
     * 
     * @param deviceData 设备列表
     * @return 设备列表集合
     */
    public List<DeviceData> selectDeviceDataList(DeviceData deviceData);

    /**
     * 查询所有设备列表
     */
    List<DeviceData> selectAllDeviceData(@Param("deviceData")DeviceData deviceData);

    /**
     * 根据 owner_id 查询设备列表
     */
    List<DeviceData> selectDeviceDataByOwnerId(Long ownerId);

    /**
     * 根据省份和 isOpened 查询设备列表
     */
    List<DeviceData> selectDeviceDataByProvinceAndIsOpened(@Param("province") String province, @Param("isOpened") Boolean isOpened, @Param("deviceData")DeviceData deviceData);
    List<DeviceData> selectDeviceDataByCityAndIsOpened(@Param("city") String city, @Param("isOpened") Boolean isOpened, @Param("deviceData")DeviceData deviceData);
    List<DeviceData> selectDeviceDataByDistrictAndIsOpened(@Param("district") String district, @Param("isOpened") Boolean isOpened, @Param("deviceData")DeviceData deviceData);

    /**
     * 新增设备列表
     * 
     * @param deviceData 设备列表
     * @return 结果
     */
    public int insertDeviceData(DeviceData deviceData);

    /**
     * 修改设备列表
     * 
     * @param deviceData 设备列表
     * @return 结果
     */
    public int updateDeviceData(DeviceData deviceData);

    /**
     * 删除设备列表
     * 
     * @param id 设备列表主键
     * @return 结果
     */
    public int deleteDeviceDataById(Long id);

    /**
     * 批量删除设备列表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceDataByIds(Long[] ids);
}
