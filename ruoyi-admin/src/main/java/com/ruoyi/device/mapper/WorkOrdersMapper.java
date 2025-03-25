package com.ruoyi.device.mapper;

import java.util.List;
import com.ruoyi.device.domain.WorkOrders;

/**
 * 工单Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-24
 */
public interface WorkOrdersMapper 
{
    /**
     * 查询工单
     * 
     * @param id 工单主键
     * @return 工单
     */
    public WorkOrders selectWorkOrdersById(Long id);

    /**
     * 查询工单列表
     * 
     * @param workOrders 工单
     * @return 工单集合
     */
    public List<WorkOrders> selectWorkOrdersList(WorkOrders workOrders);

    /**
     * 新增工单
     * 
     * @param workOrders 工单
     * @return 结果
     */
    public int insertWorkOrders(WorkOrders workOrders);

    /**
     * 修改工单
     * 
     * @param workOrders 工单
     * @return 结果
     */
    public int updateWorkOrders(WorkOrders workOrders);

    /**
     * 删除工单
     * 
     * @param id 工单主键
     * @return 结果
     */
    public int deleteWorkOrdersById(Long id);

    /**
     * 批量删除工单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWorkOrdersByIds(Long[] ids);
}
