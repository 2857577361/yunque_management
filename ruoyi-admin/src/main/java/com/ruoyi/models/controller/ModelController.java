package com.ruoyi.models.controller;

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
import com.ruoyi.models.domain.Model;
import com.ruoyi.models.service.IModelService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 模型管理Controller
 *
 * @author ruoyi
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/models/model")
public class ModelController extends BaseController
{
    @Autowired
    private IModelService modelService;

    /**
     * 查询模型列表（包含关联设备ID列表）
     */
    @PreAuthorize("@ss.hasPermi('models:model:list')")
    @GetMapping("/list")
    public TableDataInfo list(Model model)
    {
        startPage();
        List<Model> list = modelService.selectModelListWithDevices(model);
        return getDataTable(list);
    }

    /**
     * 导出模型列表
     */
    @PreAuthorize("@ss.hasPermi('models:model:export')")
    @Log(title = "模型管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Model model)
    {
        List<Model> list = modelService.selectModelList(model);
        ExcelUtil<Model> util = new ExcelUtil<Model>(Model.class);
        util.exportExcel(response, list, "模型数据");
    }

    /**
     * 获取模型详细信息（包含关联设备ID列表）
     */
    @PreAuthorize("@ss.hasPermi('models:model:query')")
    @GetMapping(value = "/{modelId}")
    public AjaxResult getInfo(@PathVariable("modelId") Long modelId)
    {
        return success(modelService.selectModelWithDevicesById(modelId));
    }

    /**
     * 新增模型
     */
    @PreAuthorize("@ss.hasPermi('models:model:add')")
    @Log(title = "模型管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Model model)
    {
        return toAjax(modelService.insertModel(model));
    }

    /**
     * 修改模型
     */
    @PreAuthorize("@ss.hasPermi('models:model:edit')")
    @Log(title = "模型管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Model model)
    {
        return toAjax(modelService.updateModel(model));
    }

    /**
     * 删除模型
     */
    @PreAuthorize("@ss.hasPermi('models:model:remove')")
    @Log(title = "模型管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{modelIds}")
    public AjaxResult remove(@PathVariable Long[] modelIds)
    {
        return toAjax(modelService.deleteModelByIds(modelIds));
    }
}