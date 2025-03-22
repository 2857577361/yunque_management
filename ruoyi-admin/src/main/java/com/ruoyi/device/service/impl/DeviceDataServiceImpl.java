package com.ruoyi.device.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.device.mapper.DeviceDataMapper;
import com.ruoyi.device.domain.DeviceData;
import com.ruoyi.device.service.IDeviceDataService;
import com.ruoyi.device.domain.User;
import com.ruoyi.device.service.IUserService;
import com.ruoyi.device.domain.dto.DeviceDataUpdateDTO;
/**
 * 设备列表Service业务层处理
 * 
 * @author yhy
 * @date 2025-03-18
 */
@Service
public class DeviceDataServiceImpl implements IDeviceDataService 
{
    @Autowired
    private DeviceDataMapper deviceDataMapper;
    @Autowired
    private IUserService userService;

    /**
     * 查询设备列表
     * 
     * @param id 设备列表主键
     * @return 设备列表
     */
    @Override
    public DeviceData selectDeviceDataById(Long id)
    {
        return deviceDataMapper.selectDeviceDataById(id);
    }

    /**
     * 查询设备列表列表
     * 
     * @param deviceData 设备列表
     * @return 设备列表
     */
    @Override
    public List<DeviceData> selectDeviceDataList(DeviceData deviceData)
    {
        return deviceDataMapper.selectDeviceDataList(deviceData);
    }

    /**
     * 新增设备列表
     * 
     * @param deviceData 设备列表
     * @return 结果
     */
    @Override
    public int insertDeviceData(DeviceData deviceData)
    {
        return deviceDataMapper.insertDeviceData(deviceData);
    }

    /**
     * 修改设备列表
     * 
     * @param deviceData 设备列表
     * @return 结果
     */
    @Override
    public int updateDeviceData(DeviceDataUpdateDTO deviceDataUpdateDTO) {
        DeviceData deviceData = new DeviceData();
        deviceData.setId(deviceDataUpdateDTO.getId());
        deviceData.setDeviceId(deviceDataUpdateDTO.getDeviceId());
        deviceData.setAddress(deviceDataUpdateDTO.getAddress());
        deviceData.setDepartment(deviceDataUpdateDTO.getDepartment());
        deviceData.setResponsiblePerson(deviceDataUpdateDTO.getResponsiblePerson());
        deviceData.setAlarmPhone(deviceDataUpdateDTO.getAlarmPhone());
        deviceData.setCoordinates(deviceDataUpdateDTO.getCoordinates());
        deviceData.setWarrantyTime(deviceDataUpdateDTO.getWarrantyTime());
        deviceData.setStatus(deviceDataUpdateDTO.getStatus());
        deviceData.setCountry(deviceDataUpdateDTO.getCountry());
        deviceData.setProvince(deviceDataUpdateDTO.getProvince());
        deviceData.setCity(deviceDataUpdateDTO.getCity());
        deviceData.setDistrict(deviceDataUpdateDTO.getDistrict());
        deviceData.setIsOpened(deviceDataUpdateDTO.getIsOpened());

        // 根据传入的参数查询 user 表
//        String username = deviceDataUpdateDTO.getResponsiblePerson(); // 假设负责人字段对应 username
        String deptName = deviceDataUpdateDTO.getDeptName(); // 假设负责人字段对应 username
        String deptType = deviceDataUpdateDTO.getDeptType();
//        User user = userService.getUserByUsername(username);
        User user = userService.getUserByDeptNameAndType(deptName, deptType);

        System.out.println(deptName + " " + deptType);

        if (user == null) {
            // 如果 user 表中没有记录，则插入一条新记录
            User newUser = new User();
            newUser.setDeptName(deptName); // 假设部门字段对应 dept_name
            newUser.setDeptType(deptType); // 默认类型
            newUser.setUsername("default");
            Long userId = userService.createUser(newUser);
            deviceData.setOwnerId(userId); // 设置 owner_id
        } else {
            // 如果 user 表中有记录，则直接使用其 id
            deviceData.setOwnerId(user.getId());
        }

        // 更新设备信息
        deviceData.setUpdateTime(DateUtils.getNowDate());
        return deviceDataMapper.updateDeviceData(deviceData);
    }

    /**
     * 批量删除设备列表
     * 
     * @param ids 需要删除的设备列表主键
     * @return 结果
     */
    @Override
    public int deleteDeviceDataByIds(Long[] ids)
    {
        return deviceDataMapper.deleteDeviceDataByIds(ids);
    }

    /**
     * 删除设备列表信息
     * 
     * @param id 设备列表主键
     * @return 结果
     */
    @Override
    public int deleteDeviceDataById(Long id)
    {
        return deviceDataMapper.deleteDeviceDataById(id);
    }
}
