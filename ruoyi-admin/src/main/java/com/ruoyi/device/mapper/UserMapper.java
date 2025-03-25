package com.ruoyi.device.mapper;

import com.ruoyi.device.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UserMapper {
    User selectUserByUsername(String username);
    int insertUser(User user);
    User selectUserByDeptNameAndType(@Param("deptName") String deptName, @Param("deptType") String deptType);

    /**
     * 根据部门名称和类型查询用户ID
     */
    Long getUserIdByDeptNameAndType(String deptName, String deptTyp);
}