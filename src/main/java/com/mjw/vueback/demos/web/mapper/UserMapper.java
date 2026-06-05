package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    SysUser findByUsername(@Param("username") String username);

    void insert(SysUser user);

    void updatePassword(SysUser user);
}
