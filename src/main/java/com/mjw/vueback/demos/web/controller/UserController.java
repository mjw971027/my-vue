package com.mjw.vueback.demos.web.controller;

import com.mjw.vueback.demos.web.common.ApiResponse;
import com.mjw.vueback.demos.web.entity.SysUser;
import com.mjw.vueback.demos.web.service.OnlineUserService;
import com.mjw.vueback.demos.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理 Controller
 * 对应前端接口：
 *   - src/api/user.ts        → /info（获取当前用户信息）
 *   - src/api/userManage.ts  → /list, /register, /{id}, /{id}
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private com.mjw.vueback.demos.web.mapper.UserMapper userMapper;

    // ==================== 当前用户信息 ====================

    /**
     * GET /api/user/info
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getUserInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ApiResponse.error(401, "未登录");
        }

        SysUser user = userService.getCurrentUser();
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail() != null ? user.getEmail() : "");
        userInfo.put("role", user.getRole() != null ? user.getRole() : "USER");
        // 动态判断在线状态
        userInfo.put("status", onlineUserService.getOnlineStatusText(user.getUsername()));

        return ApiResponse.success(userInfo);
    }

    // ==================== 用户管理 ====================

    /**
     * GET /api/user/list
     * 获取用户列表（密码不返回）
     */
    @GetMapping("/list")
    public ApiResponse<List<Map<String, Object>>> getUserList() {
        List<SysUser> users = userService.findAllUsers();
        List<Map<String, Object>> result = new ArrayList<>();

        for (SysUser user : users) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", user.getId());
            item.put("username", user.getUsername());
            item.put("email", user.getEmail() != null ? user.getEmail() : "");
            item.put("role", user.getRole() != null ? user.getRole() : "USER");
            item.put("status", onlineUserService.getOnlineStatusText(user.getUsername()));
            item.put("createdAt", user.getCreatedAt());
            item.put("updatedAt", user.getUpdatedAt());
            result.add(item);
        }

        return ApiResponse.success(result);
    }

    /**
     * POST /api/user/register
     * 管理员新增用户
     */
    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> registerUser(@RequestBody RegisterUserRequest request) {
        // 检查用户名是否已存在
        if (userService.findByUsername(request.getUsername()) != null) {
            return ApiResponse.error(400, "用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail() != null ? request.getEmail() : "");
        user.setRole(request.getRole() != null ? request.getRole() : "USER");
        user.setStatus("在线");

        try {
            userMapper.insert(user);
            // insert 后 user.getId() 已被自动回填

            Map<String, Object> result = new HashMap<>();
            result.put("id", user.getId());
            result.put("username", user.getUsername());
            result.put("email", user.getEmail());
            result.put("role", user.getRole());
            result.put("status", "在线");

            return ApiResponse.success("创建成功", result);
        } catch (Exception e) {
            return ApiResponse.error(500, "创建用户失败: " + e.getMessage());
        }
    }

    /**
     * PUT /api/user/{id}
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ApiResponse<Map<String, Object>> updateUser(@PathVariable Long id,
                                                       @RequestBody UpdateUserRequest request) {
        // 检查用户是否存在
        SysUser existing = userService.findById(id);
        if (existing == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        SysUser updateUser = new SysUser();
        updateUser.setId(id);

        if (request.getEmail() != null) {
            updateUser.setEmail(request.getEmail());
        }
        if (request.getRole() != null) {
            updateUser.setRole(request.getRole());
        }
        if (request.getStatus() != null) {
            updateUser.setStatus(request.getStatus());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            updateUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        try {
            userService.updateUser(updateUser);
            // 重新查询返回完整信息
            SysUser updated = userService.findById(id);

            Map<String, Object> result = new HashMap<>();
            result.put("id", updated.getId());
            result.put("username", updated.getUsername());
            result.put("email", updated.getEmail() != null ? updated.getEmail() : "");
            result.put("role", updated.getRole() != null ? updated.getRole() : "USER");
            result.put("status", onlineUserService.getOnlineStatusText(updated.getUsername()));
            result.put("createdAt", updated.getCreatedAt());
            result.put("updatedAt", updated.getUpdatedAt());

            return ApiResponse.success("更新成功", result);
        } catch (RuntimeException e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * DELETE /api/user/{id}
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        // 检查用户是否存在
        SysUser existing = userService.findById(id);
        if (existing == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        try {
            userService.deleteUser(id);
            // 清除该用户的在线状态
            onlineUserService.setUserOffline(existing.getUsername());
            return ApiResponse.success("删除成功");
        } catch (RuntimeException e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    // ==================== 页面权限 ====================

    /**
     * GET /api/user/permissions
     * 获取当前登录用户的页面权限
     */
    @GetMapping("/permissions")
    public ApiResponse<List<String>> getMyPermissions(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ApiResponse.error(401, "未登录");
        }
        SysUser user = userService.getCurrentUser();
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }
        List<String> perms = userService.getUserPermissions(user.getId());
        return ApiResponse.success(perms);
    }

    /**
     * GET /api/user/permissions/{userId}
     * 获取指定用户的页面权限
     */
    @GetMapping("/permissions/{userId}")
    public ApiResponse<List<String>> getUserPermissions(@PathVariable Long userId) {
        SysUser user = userService.findById(userId);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }
        List<String> perms = userService.getUserPermissions(userId);
        return ApiResponse.success(perms);
    }

    /**
     * PUT /api/user/permissions/{userId}
     * 设置指定用户的页面权限（全量覆盖）
     * body: { "permissions": ["page1", "page2"] }
     */
    @PutMapping("/permissions/{userId}")
    public ApiResponse<Void> setUserPermissions(
            @PathVariable Long userId,
            @RequestBody Map<String, List<String>> body) {
        SysUser user = userService.findById(userId);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }
        List<String> permissions = body.get("permissions");
        userService.setUserPermissions(userId, permissions);
        return ApiResponse.success();
    }

    // ==================== 请求体 ====================

    public static class RegisterUserRequest {
        private String username;
        private String password;
        private String email;
        private String role;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    public static class UpdateUserRequest {
        private String email;
        private String role;
        private String status;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
