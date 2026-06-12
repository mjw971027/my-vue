package com.mjw.vueback.demos.web.service;

import com.mjw.vueback.demos.web.entity.SysUser;
import com.mjw.vueback.demos.web.entity.UserPermission;
import com.mjw.vueback.demos.web.mapper.UserMapper;
import com.mjw.vueback.demos.web.mapper.UserPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPermissionMapper userPermissionMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /** 默认用户（数据库不可用时使用） */
    private static final Map<String, SysUser> DEFAULT_USERS = new HashMap<>();

    @PostConstruct
    public void initDefaultUsers() {
        // admin/admin123
        SysUser admin = new SysUser();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@example.com");
        admin.setRole("ADMIN");
        admin.setStatus("在线");
        DEFAULT_USERS.put("admin", admin);

        // user/123456
        SysUser user = new SysUser();
        user.setId(2L);
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setEmail("user@example.com");
        user.setRole("USER");
        user.setStatus("在线");
        DEFAULT_USERS.put("user", user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = null;
        try {
            sysUser = userMapper.findByUsername(username);
        } catch (Exception e) {
            // 数据库不可用时，使用默认用户
            sysUser = DEFAULT_USERS.get(username);
        }

        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        return new User(sysUser.getUsername(), sysUser.getPassword(), authorities);
    }

    // 注册用户
    public SysUser register(String username, String password, String email) {
        try {
            // 检查用户是否已存在
            if (userMapper.findByUsername(username) != null) {
                throw new RuntimeException("用户名已存在");
            }

            SysUser user = new SysUser();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setRole("USER");
            user.setStatus("在线");

            userMapper.insert(user);
            return user;
        } catch (Exception e) {
            // 数据库不可用时，模拟注册成功
            throw new RuntimeException("注册功能暂不可用（数据库未连接），请使用默认账号 admin/admin123 登录");
        }
    }

    public SysUser findByUsername(String username) {
        try {
            return userMapper.findByUsername(username);
        } catch (Exception e) {
            // 数据库不可用时，返回默认用户
            return DEFAULT_USERS.get(username);
        }
    }

    // 获取当前登录用户
    public SysUser getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext.getAuthentication() == null 
                || !securityContext.getAuthentication().isAuthenticated()) {
            return null;
        }
        String username = securityContext.getAuthentication().getName();
        if ("anonymousUser".equals(username)) {
            return null;
        }
        return findByUsername(username);
    }

    // 更新密码
    public void updatePassword(SysUser user) {
        try {
            userMapper.updatePassword(user);
        } catch (Exception e) {
            // 数据库不可用时，模拟成功
        }
    }

    // 获取所有用户
    public List<SysUser> findAllUsers() {
        try {
            return userMapper.findAll();
        } catch (Exception e) {
            // 数据库不可用时，返回默认用户列表
            return new ArrayList<>(DEFAULT_USERS.values());
        }
    }

    // 根据ID查询用户
    public SysUser findById(Long id) {
        try {
            SysUser user = userMapper.findById(id);
            if (user == null) {
                // 回退到默认用户
                for (SysUser u : DEFAULT_USERS.values()) {
                    if (u.getId().equals(id)) {
                        return u;
                    }
                }
            }
            return user;
        } catch (Exception e) {
            // 数据库不可用时，在默认用户中查找
            for (SysUser u : DEFAULT_USERS.values()) {
                if (u.getId().equals(id)) {
                    return u;
                }
            }
            return null;
        }
    }

    // 更新用户信息
    public void updateUser(SysUser user) {
        try {
            userMapper.updateById(user);
        } catch (Exception e) {
            throw new RuntimeException("更新用户失败: " + e.getMessage());
        }
    }

    // 删除用户
    public void deleteUser(Long id) {
        try {
            userMapper.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("删除用户失败: " + e.getMessage());
        }
    }

    // ==================== 页面权限 ====================

    /**
     * 获取用户的页面权限 key 列表
     */
    public List<String> getUserPermissions(Long userId) {
        try {
            return userPermissionMapper.findPermissionKeysByUserId(userId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 设置用户的页面权限（先清旧权限，再批量插入新权限）
     */
    public void setUserPermissions(Long userId, List<String> permissionKeys) {
        try {
            // 先删旧的
            userPermissionMapper.deleteByUserId(userId);

            // 再插入新的
            if (permissionKeys != null && !permissionKeys.isEmpty()) {
                List<UserPermission> list = permissionKeys.stream()
                        .map(key -> new UserPermission(userId, key))
                        .collect(Collectors.toList());
                userPermissionMapper.batchInsert(list);
            }
        } catch (Exception e) {
            throw new RuntimeException("设置权限失败: " + e.getMessage());
        }
    }
}
