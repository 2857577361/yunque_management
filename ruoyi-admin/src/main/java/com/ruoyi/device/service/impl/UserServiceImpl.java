package com.ruoyi.device.service.impl;

import com.ruoyi.device.domain.User;
import com.ruoyi.device.mapper.UserMapper;
import com.ruoyi.device.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Override
    public Long createUser(User user) {
        userMapper.insertUser(user);
        return user.getId();
    }

    @Override
    public User getUserByDeptNameAndType(String deptName, String deptType) {
        return userMapper.selectUserByDeptNameAndType(deptName, deptType);
    }


    /**
     * 根据部门名称和类型查询用户ID
     */
    @Override
    public Long getUserIdByDeptNameAndType(String deptName, String deptType) {
        return userMapper.getUserIdByDeptNameAndType(deptName, deptType);
    }
}