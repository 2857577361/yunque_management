package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Dept;
import com.ruoyi.system.mapper.DeptMapper;
import com.ruoyi.system.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeptServiceImpl implements IDeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<String> getDeptNamesByParentIdOrAncestors(Long parentId) {
        List<Dept> depts = deptMapper.selectDeptsByParentIdOrAncestors(parentId);
        System.out.println(depts);
        System.out.println("depts");
        return depts.stream()
                .map(Dept::getDeptName)
                .collect(Collectors.toList());
    }
}