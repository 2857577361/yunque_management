package com.ruoyi.owner.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 拥有者对象 user
 * 
 * @author ruoyi
 * @date 2025-03-22
 */
public class Owner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long id;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 用户名 */
    @Excel(name = "用户名")
    private String username;

    /** 部门种类 */
    @Excel(name = "部门种类")
    private String deptType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }
    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }
    public void setDeptType(String deptType) 
    {
        this.deptType = deptType;
    }

    public String getDeptType() 
    {
        return deptType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deptName", getDeptName())
            .append("username", getUsername())
            .append("deptType", getDeptType())
            .toString();
    }
}
