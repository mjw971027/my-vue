package com.mjw.vueback.demos.web.controller;

import com.mjw.vueback.demos.web.common.ApiResponse;
import com.mjw.vueback.demos.web.entity.SysUser;
import com.mjw.vueback.demos.web.security.JwtTokenUtil;
import com.mjw.vueback.demos.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    // 登录接口
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 验证用户名密码
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // 生成JWT Token
            String token = jwtTokenUtil.generateToken(authentication);

            // 返回Token和用户信息（匹配前端期望的格式）
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("tokenType", "Bearer");
            result.put("expiresIn", jwtExpiration / 1000); // 秒
            result.put("username", loginRequest.getUsername());

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(401, "用户名或密码错误");
        }
    }

    // 注册接口
    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.register(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    registerRequest.getEmail()
            );
            return ApiResponse.success("注册成功");
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    // 获取当前用户信息
    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ApiResponse.error(401, "未登录");
        }

        String username = authentication.getName();
        SysUser user = userService.findByUsername(username);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());

        return ApiResponse.success(userInfo);
    }

    // 退出登录接口
    @PostMapping("/logout")
    public ApiResponse<String> logout() {
        // JWT 是无状态的，后端不需要做特殊处理
        // 前端会清除本地存储的 Token
        return ApiResponse.success("退出成功");
    }

    // 刷新 Token 接口
    @PostMapping("/refresh")
    public ApiResponse<Map<String, Object>> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String oldToken = request.getRefreshToken();
            
            // 验证旧 Token
            if (!jwtTokenUtil.validateToken(oldToken)) {
                return ApiResponse.error(401, "Token 无效或已过期");
            }

            // 获取用户名
            String username = jwtTokenUtil.getUsernameFromToken(oldToken);

            // 生成新 Token
            UserDetails userDetails = userService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            String newToken = jwtTokenUtil.generateToken(authentication);

            // 返回新 Token
            Map<String, Object> result = new HashMap<>();
            result.put("token", newToken);
            result.put("tokenType", "Bearer");
            result.put("expiresIn", jwtExpiration / 1000);

            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(401, "刷新 Token 失败");
        }
    }

    // 修改密码接口
    @PostMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestBody ChangePasswordRequest request, 
                                             Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ApiResponse.error(401, "未登录");
            }

            String username = authentication.getName();
            SysUser user = userService.findByUsername(username);

            // 验证旧密码
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                return ApiResponse.error(400, "旧密码错误");
            }

            // 更新密码
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userService.updatePassword(user);

            return ApiResponse.success("密码修改成功");
        } catch (Exception e) {
            return ApiResponse.error(500, "密码修改失败");
        }
    }

    // 内部类：登录请求
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // 内部类：注册请求
    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    // 内部类：刷新 Token 请求
    public static class RefreshTokenRequest {
        private String refreshToken;

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }

    // 内部类：修改密码请求
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;

        public String getOldPassword() { return oldPassword; }
        public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
