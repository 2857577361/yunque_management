package com.ruoyi.models.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.models.domain.vo.DiseaseModelVo;
import com.ruoyi.models.domain.dto.DiseaseModelRelationDto;
import com.ruoyi.models.service.IDiseaseModelService;

/**
 * 病害模型管理 Controller
 *
 * @author ruoyi
 * @date 2023-06-15
 */
@RestController
@RequestMapping("/models/disease-model")
public class DiseaseModelController extends BaseController {

    @Autowired
    private IDiseaseModelService diseaseModelService;

    /**
     * 查询病害及其关联模型列表
     */
    @PreAuthorize("@ss.hasPermi('models:disease-model:list')")
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<DiseaseModelVo> list = diseaseModelService.selectDiseaseModelList();
        return getDataTable(list);
    }

    /**
     * 导出病害模型关联列表
     */
    @PreAuthorize("@ss.hasPermi('models:disease-model:export')")
    @Log(title = "病害模型关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response) {
        List<DiseaseModelVo> list = diseaseModelService.selectDiseaseModelList();
        ExcelUtil<DiseaseModelVo> util = new ExcelUtil<>(DiseaseModelVo.class);
        util.exportExcel(response, list, "病害模型关联数据");
    }

    /**
     * 更新病害模型关系
     */
    @PreAuthorize("@ss.hasPermi('models:disease-model:edit')")
    @Log(title = "病害模型关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody DiseaseModelRelationDto relationDto) {
        return toAjax(diseaseModelService.updateDiseaseModelRelation(relationDto));
    }

    /**
     * 删除病害模型关系
     */
    @PreAuthorize("@ss.hasPermi('models:disease-model:remove')")
    @Log(title = "病害模型关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{diseaseId}")
    public AjaxResult remove(@PathVariable Long diseaseId) {
        return toAjax(diseaseModelService.deleteDiseaseModelRelation(diseaseId));
    }
}