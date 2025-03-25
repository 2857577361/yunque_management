package com.ruoyi.device.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.device.mapper.WorkOrdersMapper;
import com.ruoyi.device.domain.WorkOrders;
import com.ruoyi.device.service.IWorkOrdersService;

/**
 * 工单Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-24
 */
@Service
public class WorkOrdersServiceImpl implements IWorkOrdersService 
{
    @Autowired
    private WorkOrdersMapper workOrdersMapper;

    /**
     * 查询工单
     * 
     * @param id 工单主键
     * @return 工单
     */
    @Override
    public WorkOrders selectWorkOrdersById(Long id)
    {
        return workOrdersMapper.selectWorkOrdersById(id);
    }

    /**
     * 查询工单列表
     * 
     * @param workOrders 工单
     * @return 工单
     */
    @Override
    public List<WorkOrders> selectWorkOrdersList(WorkOrders workOrders)
    {
        return workOrdersMapper.selectWorkOrdersList(workOrders);
    }

    /**
     * 新增工单
     * 
     * @param workOrders 工单
     * @return 结果
     */
    @Override
    public int insertWorkOrders(WorkOrders workOrders)
    {
        return workOrdersMapper.insertWorkOrders(workOrders);
    }

    /**
     * 修改工单
     * 
     * @param workOrders 工单
     * @return 结果
     */
    @Override
    public int updateWorkOrders(WorkOrders workOrders)
    {
        return workOrdersMapper.updateWorkOrders(workOrders);
    }

    /**
     * 批量删除工单
     * 
     * @param ids 需要删除的工单主键
     * @return 结果
     */
    @Override
    public int deleteWorkOrdersByIds(Long[] ids)
    {
        return workOrdersMapper.deleteWorkOrdersByIds(ids);
    }

    /**
     * 删除工单信息
     * 
     * @param id 工单主键
     * @return 结果
     */
    @Override
    public int deleteWorkOrdersById(Long id)
    {
        return workOrdersMapper.deleteWorkOrdersById(id);
    }
}
