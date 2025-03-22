package com.ruoyi.device.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.device.domain.DeviceData;
import com.ruoyi.device.service.IDeviceDataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.device.domain.dto.DeviceDataUpdateDTO;

/**
 * 设备列表Controller
 * 
 * @author yhy
 * @date 2025-03-18
 */
@RestController
@RequestMapping("/device/device")
public class DeviceDataController extends BaseController
{
    @Autowired
    private IDeviceDataService deviceDataService;

    /**
     * 查询设备列表列表
     */
    @PreAuthorize("@ss.hasPermi('device:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceData deviceData)
    {
        startPage();
        List<DeviceData> list = deviceDataService.selectDeviceDataList(deviceData);
        return getDataTable(list);
    }

    /**
     * 导出设备列表列表
     */
    @PreAuthorize("@ss.hasPermi('device:device:export')")
    @Log(title = "设备列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceData deviceData)
    {
        List<DeviceData> list = deviceDataService.selectDeviceDataList(deviceData);
        ExcelUtil<DeviceData> util = new ExcelUtil<DeviceData>(DeviceData.class);
        util.exportExcel(response, list, "设备列表数据");
    }

    /**
     * 获取设备列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:device:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(deviceDataService.selectDeviceDataById(id));
    }

    /**
     * 新增设备列表
     */
    @PreAuthorize("@ss.hasPermi('device:device:add')")
    @Log(title = "设备列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceData deviceData)
    {
        return toAjax(deviceDataService.insertDeviceData(deviceData));
    }

    /**
     * 修改设备列表
     */
    @PreAuthorize("@ss.hasPermi('device:device:edit')")
    @Log(title = "设备列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceDataUpdateDTO deviceDataUpdateDTO)
    {
        return toAjax(deviceDataService.updateDeviceData(deviceDataUpdateDTO));
    }

    /**
     * 删除设备列表
     */
    @PreAuthorize("@ss.hasPermi('device:device:remove')")
    @Log(title = "设备列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(deviceDataService.deleteDeviceDataByIds(ids));
    }
}
