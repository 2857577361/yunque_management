package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.Model;
import com.ruoyi.system.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/system/model")
public class ModelController extends BaseController {

    @Autowired
    private ModelService modelService;

    /**
     * 查询模型列表
     */
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String modelId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        startPage();
        List<Model> list = modelService.selectModelListWithDevices(modelId, startTime, endTime);
        return getDataTable(list);
    }

    /**
     * 根据模型ID查询模型详情
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable String id) {
        return success(modelService.selectModelById(id));
    }

    /**
     * 更新模型信息
     */
    @PutMapping
    public AjaxResult update(@RequestBody Model model) {
        return toAjax(modelService.updateModel(model));
    }

    /**
     * 删除模型
     */
    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable String id) {
        return toAjax(modelService.deleteModelById(id));
    }
}