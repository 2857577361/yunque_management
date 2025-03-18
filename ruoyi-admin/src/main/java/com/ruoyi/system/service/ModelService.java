package com.ruoyi.system.service;

import com.ruoyi.system.domain.Model;
import java.util.Date;
import java.util.List;

public interface ModelService {
    /**
     * 查询模型列表
     *
     * @param modelId   模型ID（模糊查询）
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 模型列表
     */
    List<Model> selectModelListWithDevices(String modelId, Date startTime, Date endTime);

    /**
     * 根据模型ID查询模型详情
     *
     * @param id 模型ID
     * @return 模型详情
     */
    Model selectModelById(String id);

    /**
     * 更新模型信息
     *
     * @param model 模型信息
     * @return 更新结果
     */
    int updateModel(Model model);

    /**
     * 删除模型
     *
     * @param id 模型ID
     * @return 删除结果
     */
    int deleteModelById(String id);
}