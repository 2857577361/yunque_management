package com.ruoyi.owner.mapper;

import java.util.List;
import com.ruoyi.owner.domain.Owner;

/**
 * 拥有者Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-22
 */
public interface OwnerMapper 
{
    /**
     * 查询拥有者
     * 
     * @param id 拥有者主键
     * @return 拥有者
     */
    public Owner selectOwnerById(Long id);

    /**
     * 查询拥有者列表
     * 
     * @param owner 拥有者
     * @return 拥有者集合
     */
    public List<Owner> selectOwnerList(Owner owner);

    /**
     * 新增拥有者
     * 
     * @param owner 拥有者
     * @return 结果
     */
    public int insertOwner(Owner owner);

    /**
     * 修改拥有者
     * 
     * @param owner 拥有者
     * @return 结果
     */
    public int updateOwner(Owner owner);

    /**
     * 删除拥有者
     * 
     * @param id 拥有者主键
     * @return 结果
     */
    public int deleteOwnerById(Long id);

    /**
     * 批量删除拥有者
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOwnerByIds(Long[] ids);
}
