package com.ruoyi.owner.controller;

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
import com.ruoyi.owner.domain.Owner;
import com.ruoyi.owner.service.IOwnerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 拥有者Controller
 * 
 * @author ruoyi
 * @date 2025-03-22
 */
@RestController
@RequestMapping("/owner/owner")
public class OwnerController extends BaseController
{
    @Autowired
    private IOwnerService ownerService;

    /**
     * 查询拥有者列表
     */
    @PreAuthorize("@ss.hasPermi('owner:owner:list')")
    @GetMapping("/list")
    public TableDataInfo list(Owner owner)
    {
        startPage();
        List<Owner> list = ownerService.selectOwnerList(owner);
        return getDataTable(list);
    }

    /**
     * 导出拥有者列表
     */
    @PreAuthorize("@ss.hasPermi('owner:owner:export')")
    @Log(title = "拥有者", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Owner owner)
    {
        List<Owner> list = ownerService.selectOwnerList(owner);
        ExcelUtil<Owner> util = new ExcelUtil<Owner>(Owner.class);
        util.exportExcel(response, list, "拥有者数据");
    }

    /**
     * 获取拥有者详细信息
     */
    @PreAuthorize("@ss.hasPermi('owner:owner:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(ownerService.selectOwnerById(id));
    }

    /**
     * 新增拥有者
     */
    @PreAuthorize("@ss.hasPermi('owner:owner:add')")
    @Log(title = "拥有者", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Owner owner)
    {
        return toAjax(ownerService.insertOwner(owner));
    }

    /**
     * 修改拥有者
     */
    @PreAuthorize("@ss.hasPermi('owner:owner:edit')")
    @Log(title = "拥有者", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Owner owner)
    {
        return toAjax(ownerService.updateOwner(owner));
    }

    /**
     * 删除拥有者
     */
    @PreAuthorize("@ss.hasPermi('owner:owner:remove')")
    @Log(title = "拥有者", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(ownerService.deleteOwnerByIds(ids));
    }
}
