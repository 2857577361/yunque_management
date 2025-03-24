package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Dept;
import java.util.List;

public interface DeptMapper {
    List<Dept> selectDeptsByParentIdOrAncestors(Long parentId);
}