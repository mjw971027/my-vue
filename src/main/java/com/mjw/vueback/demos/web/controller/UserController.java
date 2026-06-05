package com.mjw.vueback.demos.web.controller;

import com.mjw.vueback.demos.web.common.ApiResponse;
import com.mjw.vueback.demos.web.entity.SysUser;
import com.mjw.vueback.demos.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 用户相关 Controller
 * 对应前端：src/api/user.ts → getUserInfo()
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173")  // 允许前端开发服务器跨域
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * GET /api/user/info
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public ApiResponse<SysUser> getUserInfo(Authentication authentication) {
        // 检查是否登录
        if (authentication == null || !authentication.isAuthenticated()) {
            return ApiResponse.error(401, "未登录");
        }

        // 获取当前登录用户
        SysUser user = userService.getCurrentUser();
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        return ApiResponse.success(user);
    }
}
