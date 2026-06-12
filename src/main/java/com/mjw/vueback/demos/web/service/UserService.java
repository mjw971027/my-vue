package com.mjw.vueback.demos.web.service;

import com.mjw.vueback.demos.web.entity.SysUser;
import com.mjw.vueback.demos.web.entity.UserPermission;
import com.mjw.vueback.demos.web.mapper.UserMapper;
import com.mjw.vueback.demos.web.mapper.UserPermissionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPermissionMapper userPermissionMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = userMapper.findByUsername(username);

        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 根据用户角色构建权限列表
        String role = sysUser.getRole() != null ? sysUser.getRole() : "USER";
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role)
        );

        return new User(sysUser.getUsername(), sysUser.getPassword(), authorities);
    }

    // 注册用户
    public SysUser register(String username, String password, String email) {
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
        logger.info("用户注册成功: {}", username);
        return user;
    }

    public SysUser findByUsername(String username) {
        SysUser user = userMapper.findByUsername(username);
        if (user == null) {
            logger.warn("用户不存在: {}", username);
        }
        return user;
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
        userMapper.updatePassword(user);
        logger.info("用户密码已更新: {}", user.getUsername());
    }

    // 获取所有用户
    public List<SysUser> findAllUsers() {
        return userMapper.findAll();
    }

    // 根据ID查询用户
    public SysUser findById(Long id) {
        SysUser user = userMapper.findById(id);
        if (user == null) {
            logger.warn("用户ID不存在: {}", id);
        }
        return user;
    }

    // 更新用户信息
    public void updateUser(SysUser user) {
        userMapper.updateById(user);
        logger.info("用户信息已更新: id={}", user.getId());
    }

    // 删除用户
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
        logger.info("用户已删除: id={}", id);
    }

    // ==================== 页面权限 ====================

    /**
     * 获取用户的页面权限 key 列表
     */
    public List<String> getUserPermissions(Long userId) {
        return userPermissionMapper.findPermissionKeysByUserId(userId);
    }

    /**
     * 设置用户的页面权限（先清旧权限，再批量插入新权限）
     */
    public void setUserPermissions(Long userId, List<String> permissionKeys) {
        // 先删旧的
        userPermissionMapper.deleteByUserId(userId);

        // 再插入新的
        if (permissionKeys != null && !permissionKeys.isEmpty()) {
            List<UserPermission> list = permissionKeys.stream()
                    .map(key -> new UserPermission(userId, key))
                    .collect(Collectors.toList());
            userPermissionMapper.batchInsert(list);
        }
        logger.info("用户权限已更新: userId={}, permissions={}", userId, permissionKeys);
    }
}
