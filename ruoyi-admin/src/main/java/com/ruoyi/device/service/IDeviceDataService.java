package com.ruoyi.device.service;

import java.util.List;
import com.ruoyi.device.domain.DeviceData;
import com.ruoyi.device.domain.dto.DeviceDataUpdateDTO;
/**
 * 设备列表Service接口
 * 
 * @author yhy
 * @date 2025-03-18
 */
public interface IDeviceDataService 
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
            * 根据角色和部门查询设备列表
     */
    List<DeviceData> selectDeviceDataByRoleAndDept(String roleName, String deptName, DeviceData deviceData);

    /**
     * 新增设备列表
     * 
     * @param deviceDataUpdateDTO 设备列表
     * @return 结果
     */
    public int insertDeviceData(DeviceDataUpdateDTO deviceDataUpdateDTO);

    /**
     * 修改设备列表
     * 
     * @param deviceDataUpdateDTO 设备列表
     * @return 结果
     */
    public int updateDeviceData(DeviceDataUpdateDTO deviceDataUpdateDTO);

    /**
     * 批量删除设备列表
     * 
     * @param ids 需要删除的设备列表主键集合
     * @return 结果
     */
    public int deleteDeviceDataByIds(Long[] ids);

    /**
     * 删除设备列表信息
     * 
     * @param id 设备列表主键
     * @return 结果
     */
    public int deleteDeviceDataById(Long id);
}
