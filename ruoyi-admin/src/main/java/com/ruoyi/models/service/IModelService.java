package com.ruoyi.models.service;

import java.util.List;
import com.ruoyi.models.domain.Model;

/**
 * 模型管理Service接口
 *
 * @author ruoyi
 * @date 2025-03-24
 */
public interface IModelService
{
    /**
     * 查询模型管理
     *
     * @param modelId 模型管理主键
     * @return 模型管理
     */
    public Model selectModelById(Long modelId);

    /**
     * 查询模型管理列表
     *
     * @param model 模型管理
     * @return 模型管理集合
     */
    public List<Model> selectModelList(Model model);

    /**
     * 查询模型列表（包含关联设备ID）
     */
    public List<Model> selectModelListWithDevices(Model model);

    /**
     * 查询模型详情（包含关联设备ID）
     */
    public Model selectModelWithDevicesById(Long modelId);

    /**
     * 新增模型管理
     *
     * @param model 模型管理
     * @return 结果
     */
    public int insertModel(Model model);

    /**
     * 修改模型管理
     *
     * @param model 模型管理
     * @return 结果
     */
    public int updateModel(Model model);

    /**
     * 批量删除模型管理
     *
     * @param modelIds 需要删除的模型管理主键集合
     * @return 结果
     */
    public int deleteModelByIds(Long[] modelIds);

    /**
     * 删除模型管理信息
     *
     * @param modelId 模型管理主键
     * @return 结果
     */
    public int deleteModelById(Long modelId);
}