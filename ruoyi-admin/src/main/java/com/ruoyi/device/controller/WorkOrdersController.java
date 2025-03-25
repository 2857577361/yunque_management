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
import com.ruoyi.device.domain.WorkOrders;
import com.ruoyi.device.service.IWorkOrdersService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 工单Controller
 * 
 * @author ruoyi
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/device/orders")
public class WorkOrdersController extends BaseController
{
    @Autowired
    private IWorkOrdersService workOrdersService;

    /**
     * 查询工单列表
     */
    @PreAuthorize("@ss.hasPermi('device:orders:list')")
    @GetMapping("/list")
    public TableDataInfo list(WorkOrders workOrders)
    {
        startPage();
        List<WorkOrders> list = workOrdersService.selectWorkOrdersList(workOrders);
        return getDataTable(list);
    }

    /**
     * 导出工单列表
     */
    @PreAuthorize("@ss.hasPermi('device:orders:export')")
    @Log(title = "工单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WorkOrders workOrders)
    {
        List<WorkOrders> list = workOrdersService.selectWorkOrdersList(workOrders);
        ExcelUtil<WorkOrders> util = new ExcelUtil<WorkOrders>(WorkOrders.class);
        util.exportExcel(response, list, "工单数据");
    }

    /**
     * 获取工单详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:orders:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(workOrdersService.selectWorkOrdersById(id));
    }

    /**
     * 新增工单
     */
    @PreAuthorize("@ss.hasPermi('device:orders:add')")
    @Log(title = "工单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WorkOrders workOrders)
    {
        return toAjax(workOrdersService.insertWorkOrders(workOrders));
    }

    /**
     * 修改工单
     */
    @PreAuthorize("@ss.hasPermi('device:orders:edit')")
    @Log(title = "工单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WorkOrders workOrders)
    {
        return toAjax(workOrdersService.updateWorkOrders(workOrders));
    }

    /**
     * 删除工单
     */
    @PreAuthorize("@ss.hasPermi('device:orders:remove')")
    @Log(title = "工单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(workOrdersService.deleteWorkOrdersByIds(ids));
    }

    /**
     * 上传维修信息
     */
    @PreAuthorize("@ss.hasPermi('device:orders:edit')")
    @Log(title = "工单", businessType = BusinessType.UPDATE)
    @PutMapping("/uploadRepairInfo")
    public AjaxResult uploadRepairInfo(@RequestBody WorkOrders workOrders) {
        return toAjax(workOrdersService.uploadRepairInfo(workOrders));
    }

    /**
     * 查询维修信息
     */
    @PreAuthorize("@ss.hasPermi('device:orders:query')")
    @GetMapping("/getRepairInfo/{id}")
    public AjaxResult getRepairInfo(@PathVariable("id") Long id) {
        return success(workOrdersService.selectRepairInfoById(id));
    }
}
