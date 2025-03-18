package com.ruoyi.disease.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.disease.domain.DeviceDisease;
import com.ruoyi.disease.service.IDeviceDiseaseService;

/**
 * 设备与病害关联Controller
 */
@RestController
@RequestMapping("/system/deviceDisease")
public class DeviceDiseaseController extends BaseController {
    @Autowired
    private IDeviceDiseaseService deviceDiseaseService;

    /**
     * 查询设备与病害关联列表（支持模糊搜索）
     */
    @PreAuthorize("@ss.hasPermi('system:deviceDisease:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceDisease deviceDisease) {
        startPage();
        List<DeviceDisease> list = deviceDiseaseService.selectDeviceDiseaseList(deviceDisease);
        return getDataTable(list);
    }
}