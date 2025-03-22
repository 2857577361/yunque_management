package com.ruoyi.device.service;

import com.ruoyi.device.domain.User;

public interface IUserService {
    User getUserByUsername(String username);
    Long createUser(User user);
    User getUserByDeptNameAndType(String deptName, String deptType);
}