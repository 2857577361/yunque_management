package com.ruoyi.system.controller;

import com.ruoyi.system.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/system/dept")
public class DeptController {

    @Autowired
    private IDeptService deptService;

    @GetMapping("/names")
    public List<String> getDeptNamesByParentIdOrAncestors(@RequestParam Long parentId) {
        return deptService.getDeptNamesByParentIdOrAncestors(parentId);
    }
}