package com.ruoyi.climate.controller;

import com.ruoyi.climate.service.ClimateWebSocketService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruoyi.climate.task.ClimateDataSimulator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/climate/ws")
public class ClimateWebSocketController extends BaseController {

    @Autowired
    private ClimateWebSocketService climateWebSocketService;

    @Autowired
    private ClimateDataSimulator climateDataSimulator;

    /**
     * 测试发送气候警报
     */
    @PostMapping("/sendAlert")
    public AjaxResult sendAlert(@RequestParam String deviceId, @RequestParam String message) {
        climateWebSocketService.sendClimateAlert(deviceId, message);
        return success("警报发送成功");
    }

    /**
     * 测试发送气候数据
     */
    @PostMapping("/sendData")
    public AjaxResult sendData(@RequestBody String jsonData) {
        climateWebSocketService.broadcastClimateData(jsonData);
        return success("数据发送成功");
    }


    /**
     * 手动触发气候数据检查
     */
    @PostMapping("/manualCheck")
    public AjaxResult manualCheck() {
        try {
            climateDataSimulator.checkSensorDataViolations();
            return success("手动检查已触发");
        } catch (Exception e) {
            return error("触发失败: " + e.getMessage());
        }
    }

}