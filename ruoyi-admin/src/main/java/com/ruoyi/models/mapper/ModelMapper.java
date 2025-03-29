package com.ruoyi.models.mapper;

import java.util.List;
import com.ruoyi.models.domain.Model;

/**
 * 模型管理Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-24
 */
public interface ModelMapper
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
     * 根据模型ID查询关联的设备ID列表
     */
    public List<String> selectDeviceIdsByModelId(Long modelId);

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
     * 删除模型管理
     *
     * @param modelId 模型管理主键
     * @return 结果
     */
    public int deleteModelById(Long modelId);

    /**
     * 批量删除模型管理
     *
     * @param modelIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteModelByIds(Long[] modelIds);
}