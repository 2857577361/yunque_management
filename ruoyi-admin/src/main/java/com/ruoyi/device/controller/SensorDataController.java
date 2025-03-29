package com.ruoyi.device.controller;

import java.util.Date;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.device.domain.SensorData;
import com.ruoyi.device.service.ISensorDataService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 传感器数据 Controller
 *
 * @author ruoyi
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/device/sensor")
public class SensorDataController extends BaseController {

    @Autowired
    private ISensorDataService sensorDataService;

    /**
     * 根据设备 ID 列表查询气候数据
     *
     * @param deviceIds 设备 ID 列表
     * @param startCollectTime 采集时间范围开始
     * @param endCollectTime 采集时间范围结束
     * @param startUploadTime 上传时间范围开始
     * @param endUploadTime 上传时间范围结束
     * @return 气候数据列表
     */
    @PostMapping("/queryClimateDataByDeviceIds")
    public TableDataInfo queryClimateDataByDeviceIds(
            @RequestBody List<String> deviceIds,
            @RequestParam(required = false) Date startCollectTime,
            @RequestParam(required = false) Date endCollectTime,
            @RequestParam(required = false) Date startUploadTime,
            @RequestParam(required = false) Date endUploadTime
    ) {
        startPage();
        List<SensorData> result = sensorDataService.selectClimateDataByDeviceIds(
                deviceIds,
                startCollectTime,
                endCollectTime,
                startUploadTime,
                endUploadTime
        );
        return getDataTable(result);
    }
}