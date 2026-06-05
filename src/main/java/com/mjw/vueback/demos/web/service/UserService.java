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

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = userMapper.findByUsername(username);
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
    }

    public SysUser findByUsername(String username) {
        return userMapper.findByUsername(username);
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
    }
}
