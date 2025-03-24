package com.ruoyi.system.service;

import com.ruoyi.system.domain.Dept;
import java.util.List;

public interface IDeptService {
    List<String> getDeptNamesByParentIdOrAncestors(Long parentId);
}