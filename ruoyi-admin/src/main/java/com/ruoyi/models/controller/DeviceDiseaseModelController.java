package com.ruoyi.models.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.models.domain.DeviceDiseasePrediction;
import com.ruoyi.models.domain.vo.DeviceDiseasePredictionVo;
import com.ruoyi.models.domain.vo.DeviceDiseaseModelVo;
import com.ruoyi.models.domain.DeviceDiseaseModelRelation;
import com.ruoyi.models.service.IDeviceDiseaseModelService;

/**
 * 设备病害模型关联 Controller
 */
@RestController
@RequestMapping("/models/relation")
public class DeviceDiseaseModelController extends BaseController {

    @Autowired
    private IDeviceDiseaseModelService deviceDiseaseModelService;

    /**
     * 查询设备病害预测数据列表(改进版)
     */
    @PreAuthorize("@ss.hasPermi('models:relation:list')")
    @GetMapping("/prediction/list")
    public TableDataInfo listPredictionData(
            @RequestParam(value = "deviceId", required = false) String deviceId,
            @RequestParam(value = "diseaseId", required = false) Integer diseaseId) {
        startPage();
        List<DeviceDiseasePredictionVo> list = deviceDiseaseModelService.selectDeviceDiseasePredictionList(deviceId, diseaseId);
        return getDataTable(list);
    }

    /**
     * 根据设备ID和病害ID查询预测数据详情
     */
    @PreAuthorize("@ss.hasPermi('models:relation:query')")
    @GetMapping("/prediction/detail")
    public AjaxResult getPredictionDetail(
            @RequestParam("deviceId") String deviceId,
            @RequestParam("diseaseId") Integer diseaseId) {
        return success(deviceDiseaseModelService.selectPredictionDetail(deviceId, diseaseId));
    }

    /**
     * 新增预测数据
     */
    @PreAuthorize("@ss.hasPermi('models:relation:add')")
    @PostMapping("/prediction")
    public AjaxResult addPrediction(@RequestBody DeviceDiseasePrediction prediction) {
        return toAjax(deviceDiseaseModelService.insertPrediction(prediction));
    }

    /**
     * 修改预测数据
     */
    @PreAuthorize("@ss.hasPermi('models:relation:edit')")
    @PutMapping("/prediction")
    public AjaxResult updatePrediction(@RequestBody DeviceDiseasePrediction prediction) {
        return toAjax(deviceDiseaseModelService.updatePrediction(prediction));
    }

    /**
     * 根据设备ID查询关联的病害和模型信息(改进版)
     */
    @PreAuthorize("@ss.hasPermi('models:relation:query')")
    @GetMapping("/device/{deviceId}")
    public AjaxResult getDeviceDiseaseModels(@PathVariable("deviceId") String deviceId) {
        List<DeviceDiseaseModelVo> result = deviceDiseaseModelService.selectDeviceDiseaseModels(deviceId);
        return success(result);
    }

    /**
     * 更新设备病害模型关联关系(改进版)
     */
    @PreAuthorize("@ss.hasPermi('models:relation:edit')")
    @PostMapping("/update")
    public AjaxResult updateRelations(@RequestBody DeviceDiseaseModelRelation relation) {
        return toAjax(deviceDiseaseModelService.updateDeviceDiseaseModelRelations(relation));
    }

    /**
     * 删除设备病害模型关联
     */
    @PreAuthorize("@ss.hasPermi('models:relation:remove')")
    @DeleteMapping("/delete")
    public AjaxResult deleteRelations(@RequestBody DeviceDiseaseModelRelation relation) {
        return toAjax(deviceDiseaseModelService.deleteDeviceDiseaseModelRelations(relation));
    }
}