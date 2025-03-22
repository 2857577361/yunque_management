package com.ruoyi.device.mapper;

import com.ruoyi.device.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UserMapper {
    User selectUserByUsername(String username);
    int insertUser(User user);
    User selectUserByDeptNameAndType(@Param("deptName") String deptName, @Param("deptType") String deptType);
}