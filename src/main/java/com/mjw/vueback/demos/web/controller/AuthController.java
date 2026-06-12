package com.mjw.vueback.demos.web.controller;

import com.mjw.vueback.demos.web.common.ApiResponse;
import com.mjw.vueback.demos.web.entity.SysUser;
import com.mjw.vueback.demos.web.security.JwtTokenUtil;
import com.mjw.vueback.demos.web.security.TokenBlacklistService;
import com.mjw.vueback.demos.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private com.mjw.vueback.demos.web.service.OnlineUserService onlineUserService;

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

            // 查询用户完整信息（获取 role）
            SysUser sysUser = userService.findByUsername(loginRequest.getUsername());
            String role = sysUser != null && sysUser.getRole() != null ? sysUser.getRole() : "USER";

            // 生成JWT Token（携带角色信息）
            String token = jwtTokenUtil.generateToken(authentication, role);

            // 标记用户在线（24h 过期自动离线）
            onlineUserService.setUserOnline(loginRequest.getUsername());

            // 返回Token和用户信息（匹配前端期望的格式）
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("tokenType", "Bearer");
            result.put("expiresIn", jwtExpiration / 1000); // 秒
            result.put("username", loginRequest.getUsername());
            result.put("role", role);

            logger.info("用户登录成功: {}", loginRequest.getUsername());
            return ApiResponse.success(result);
        } catch (Exception e) {
            logger.warn("登录失败, username={}: {}", loginRequest.getUsername(), e.getMessage());
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
            logger.info("用户注册成功: {}", registerRequest.getUsername());
            return ApiResponse.success("注册成功");
        } catch (RuntimeException e) {
            logger.warn("注册失败, username={}: {}", registerRequest.getUsername(), e.getMessage());
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
    public ApiResponse<String> logout(HttpServletRequest request) {
        try {
            logger.info("收到退出登录请求");

            // 获取请求中的 Token
            String token = extractTokenFromRequest(request);

            if (token != null) {
                // 获取 Token 的剩余过期时间（秒）
                long expiration = jwtTokenUtil.getExpirationFromToken(token);
                logger.debug("Token剩余过期时间: {}s", expiration);

                // 将 Token 加入黑名单
                tokenBlacklistService.addToBlacklist(token, expiration);

                // 从 Token 中获取用户名并标记离线
                String username = jwtTokenUtil.getUsernameFromToken(token);
                onlineUserService.setUserOffline(username);
                logger.info("用户已退出登录: {}", username);

                return ApiResponse.success("退出成功");
            }
            return ApiResponse.error(400, "未找到 Token");
        } catch (Exception e) {
            logger.error("退出登录异常", e);
            return ApiResponse.error(500, "退出失败: " + e.getMessage());
        }
    }

    /**
     * 从请求中提取 Token
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
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

            // 查询用户完整信息（获取 role）
            SysUser sysUser = userService.findByUsername(username);
            String role = sysUser != null && sysUser.getRole() != null ? sysUser.getRole() : "USER";

            // 生成新 Token（携带角色信息）
            UserDetails userDetails = userService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            String newToken = jwtTokenUtil.generateToken(authentication, role);

            // 刷新在线标记（延长24h）
            onlineUserService.setUserOnline(username);

            // 返回新 Token
            Map<String, Object> result = new HashMap<>();
            result.put("token", newToken);
            result.put("tokenType", "Bearer");
            result.put("expiresIn", jwtExpiration / 1000);

            logger.info("Token 刷新成功, username={}", username);
            return ApiResponse.success(result);
        } catch (Exception e) {
            logger.warn("刷新 Token 失败: {}", e.getMessage());
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

            logger.info("用户修改密码成功: {}", username);
            return ApiResponse.success("密码修改成功");
        } catch (Exception e) {
            logger.error("修改密码失败", e);
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
