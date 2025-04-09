package com.ruoyi.climate.controller;

import com.ruoyi.climate.domain.ClimateAlert;
import com.ruoyi.climate.service.IClimateAlertService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/climate/alert")
public class ClimateAlertController extends BaseController {

    @Autowired
    private IClimateAlertService climateAlertService;

    /**
     * 查询异常数据列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ClimateAlert climateAlert,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        // 设置时间参数
        if (beginTime != null || endTime != null) {
            Map<String, Object> params = new HashMap<>();
            if (beginTime != null) {
                params.put("beginAlertTime", beginTime);
            }
            if (endTime != null) {
                params.put("endAlertTime", endTime);
            }
            climateAlert.setParams(params);
        }

        startPage();
        List<ClimateAlert> list = climateAlertService.selectClimateAlertList(climateAlert);
        return getDataTable(list);
    }

    /**
     * 获取异常数据详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(climateAlertService.selectClimateAlertById(id));
    }

    /**
     * 更新异常数据状态
     */
    @PutMapping
    public AjaxResult edit(@RequestBody ClimateAlert climateAlert) {
        return toAjax(climateAlertService.updateClimateAlert(climateAlert));
    }

    /**
     * 删除异常数据
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(climateAlertService.deleteClimateAlertByIds(ids));
    }

    /**
     * 获取未处理异常数量
     */
    @GetMapping("/unprocessedCount")
    public AjaxResult getUnprocessedCount() {
        return success(climateAlertService.countUnprocessedAlerts());
    }
}