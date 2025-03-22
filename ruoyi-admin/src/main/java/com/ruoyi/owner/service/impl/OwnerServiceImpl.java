package com.ruoyi.owner.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.owner.mapper.OwnerMapper;
import com.ruoyi.owner.domain.Owner;
import com.ruoyi.owner.service.IOwnerService;

/**
 * 拥有者Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-22
 */
@Service
public class OwnerServiceImpl implements IOwnerService 
{
    @Autowired
    private OwnerMapper ownerMapper;

    /**
     * 查询拥有者
     * 
     * @param id 拥有者主键
     * @return 拥有者
     */
    @Override
    public Owner selectOwnerById(Long id)
    {
        return ownerMapper.selectOwnerById(id);
    }

    /**
     * 查询拥有者列表
     * 
     * @param owner 拥有者
     * @return 拥有者
     */
    @Override
    public List<Owner> selectOwnerList(Owner owner)
    {
        return ownerMapper.selectOwnerList(owner);
    }

    /**
     * 新增拥有者
     * 
     * @param owner 拥有者
     * @return 结果
     */
    @Override
    public int insertOwner(Owner owner)
    {
        return ownerMapper.insertOwner(owner);
    }

    /**
     * 修改拥有者
     * 
     * @param owner 拥有者
     * @return 结果
     */
    @Override
    public int updateOwner(Owner owner)
    {
        return ownerMapper.updateOwner(owner);
    }

    /**
     * 批量删除拥有者
     * 
     * @param ids 需要删除的拥有者主键
     * @return 结果
     */
    @Override
    public int deleteOwnerByIds(Long[] ids)
    {
        return ownerMapper.deleteOwnerByIds(ids);
    }

    /**
     * 删除拥有者信息
     * 
     * @param id 拥有者主键
     * @return 结果
     */
    @Override
    public int deleteOwnerById(Long id)
    {
        return ownerMapper.deleteOwnerById(id);
    }
}
