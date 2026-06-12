package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    SysUser findByUsername(@Param("username") String username);

    SysUser findById(@Param("id") Long id);

    List<SysUser> findAll();

    void insert(SysUser user);

    void updateById(SysUser user);

    void updatePassword(SysUser user);

    void deleteById(@Param("id") Long id);
}
