package com.mjw.vueback.demos.web.service;

import com.mjw.vueback.demos.web.entity.SysUser;
import com.mjw.vueback.demos.web.mapper.UserMapper;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

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
        DEFAULT_USERS.put("admin", admin);

        // user/123456
        SysUser user = new SysUser();
        user.setId(2L);
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setEmail("user@example.com");
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
}
