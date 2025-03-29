package com.ruoyi.models.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.models.mapper.ModelMapper;
import com.ruoyi.models.domain.Model;
import com.ruoyi.models.service.IModelService;

/**
 * 模型管理Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-24
 */
@Service
public class ModelServiceImpl implements IModelService
{
    @Autowired
    private ModelMapper modelMapper;

    /**
     * 查询模型管理
     *
     * @param modelId 模型管理主键
     * @return 模型管理
     */
    @Override
    public Model selectModelById(Long modelId)
    {
        return modelMapper.selectModelById(modelId);
    }

    /**
     * 查询模型管理列表
     *
     * @param model 模型管理
     * @return 模型管理集合
     */
    @Override
    public List<Model> selectModelList(Model model)
    {
        return modelMapper.selectModelList(model);
    }

    /**
     * 查询模型列表（包含关联设备ID）
     */
    @Override
    public List<Model> selectModelListWithDevices(Model model)
    {
        List<Model> models = modelMapper.selectModelList(model);
        models.forEach(m -> {
            m.setDeviceIds(modelMapper.selectDeviceIdsByModelId(m.getModelId()));
        });
        return models;
    }

    /**
     * 查询模型详情（包含关联设备ID）
     */
    @Override
    public Model selectModelWithDevicesById(Long modelId)
    {
        Model model = modelMapper.selectModelById(modelId);
        if(model != null) {
            model.setDeviceIds(modelMapper.selectDeviceIdsByModelId(modelId));
        }
        return model;
    }

    /**
     * 新增模型管理
     *
     * @param model 模型管理
     * @return 结果
     */
    @Override
    public int insertModel(Model model)
    {
        return modelMapper.insertModel(model);
    }

    /**
     * 修改模型管理
     *
     * @param model 模型管理
     * @return 结果
     */
    @Override
    public int updateModel(Model model)
    {
        return modelMapper.updateModel(model);
    }

    /**
     * 批量删除模型管理
     *
     * @param modelIds 需要删除的模型管理主键集合
     * @return 结果
     */
    @Override
    public int deleteModelByIds(Long[] modelIds)
    {
        return modelMapper.deleteModelByIds(modelIds);
    }

    /**
     * 删除模型管理信息
     *
     * @param modelId 模型管理主键
     * @return 结果
     */
    @Override
    public int deleteModelById(Long modelId)
    {
        return modelMapper.deleteModelById(modelId);
    }
}