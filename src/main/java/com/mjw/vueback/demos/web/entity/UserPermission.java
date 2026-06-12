package com.mjw.vueback.demos.web.entity;

import java.io.Serializable;

/**
 * 用户页面权限实体
 * 对应表 user_permissions
 */
public class UserPermission implements Serializable {

    private Long id;
    private Long userId;
    private String permissionKey;

    public UserPermission() {
    }

    public UserPermission(Long userId, String permissionKey) {
        this.userId = userId;
        this.permissionKey = permissionKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPermissionKey() {
        return permissionKey;
    }

    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
    }
}
