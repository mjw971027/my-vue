package com.mjw.vueback.demos.web.mapper;

import com.mjw.vueback.demos.web.entity.UserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserPermissionMapper {

    /**
     * 查询用户的全部权限 key
     */
    List<String> findPermissionKeysByUserId(@Param("userId") Long userId);

    /**
     * 批量插入权限
     */
    void batchInsert(List<UserPermission> permissions);

    /**
     * 删除用户的所有权限
     */
    void deleteByUserId(@Param("userId") Long userId);
}
