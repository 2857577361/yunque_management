package com.ruoyi.device.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.device.domain.DeviceArea;
import com.ruoyi.device.service.IDeviceAreaService;

/**
 * 设备监测区域信息 Controller
 */
@RestController
@RequestMapping("/device/area")
public class DeviceAreaController extends BaseController {

    @Autowired
    private IDeviceAreaService deviceAreaService;

    /**
     * 综合查询设备监测区域信息
     * @param deviceId 设备ID(可选)
     * @param cropType 农作物种类(可选)
     * @param province 省份(可选)
     * @param city 城市(可选)
     * @param district 区县(可选)
     * @return 设备监测区域信息列表
     */
    @PreAuthorize("@ss.hasPermi('device:area:list')")
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(value = "deviceId", required = false) String deviceId,
            @RequestParam(value = "cropType", required = false) String cropType,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "district", required = false) String district) {
        startPage();
        List<DeviceArea> list = deviceAreaService.selectDeviceAreaList(deviceId, cropType, province, city, district);
        return getDataTable(list);
    }

    /**
     * 根据设备ID获取监测区域详细信息
     * @param deviceId 设备ID
     * @return 设备监测区域信息
     */
    @PreAuthorize("@ss.hasPermi('device:area:query')")
    @GetMapping("/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") String deviceId) {
        return success(deviceAreaService.selectDeviceAreaByDeviceId(deviceId));
    }
}