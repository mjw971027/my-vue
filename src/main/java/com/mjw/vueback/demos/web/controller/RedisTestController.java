package com.mjw.vueback.demos.web.controller;

import com.mjw.vueback.demos.web.common.ApiResponse;
import com.mjw.vueback.demos.web.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/redis-test")
public class RedisTestController {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 测试设置缓存
     * GET http://localhost:8081/api/redis-test/set?key=test&value=hello
     */
    @GetMapping("/set")
    public ApiResponse setCache(@RequestParam String key, @RequestParam String value) {
        try {
            redisUtil.set(key, value);
            return ApiResponse.success("缓存设置成功", null);
        } catch (Exception e) {
            return ApiResponse.error("缓存设置失败: " + e.getMessage());
        }
    }

    /**
     * 测试设置缓存并指定过期时间
     * GET http://localhost:8081/api/redis-test/set-with-expire?key=test&value=hello&seconds=60
     */
    @GetMapping("/set-with-expire")
    public ApiResponse setCacheWithExpire(
            @RequestParam String key,
            @RequestParam String value,
            @RequestParam long seconds) {
        try {
            redisUtil.set(key, value, seconds, TimeUnit.SECONDS);
            return ApiResponse.success("缓存设置成功（过期时间：" + seconds + "秒）", null);
        } catch (Exception e) {
            return ApiResponse.error("缓存设置失败: " + e.getMessage());
        }
    }

    /**
     * 测试获取缓存
     * GET http://localhost:8081/api/redis-test/get?key=test
     */
    @GetMapping("/get")
    public ApiResponse getCache(@RequestParam String key) {
        try {
            Object value = redisUtil.get(key);
            if (value == null) {
                return ApiResponse.success("缓存不存在", null);
            }
            return ApiResponse.success("获取缓存成功", value);
        } catch (Exception e) {
            return ApiResponse.error("获取缓存失败: " + e.getMessage());
        }
    }

    /**
     * 测试删除缓存
     * DELETE http://localhost:8081/api/redis-test/delete?key=test
     */
    @DeleteMapping("/delete")
    public ApiResponse deleteCache(@RequestParam String key) {
        try {
            boolean result = redisUtil.delete(key);
            if (result) {
                return ApiResponse.success("缓存删除成功", null);
            } else {
                return ApiResponse.error("缓存删除失败");
            }
        } catch (Exception e) {
            return ApiResponse.error("缓存删除失败: " + e.getMessage());
        }
    }

    /**
     * 测试判断缓存是否存在
     * GET http://localhost:8081/api/redis-test/has?key=test
     */
    @GetMapping("/has")
    public ApiResponse hasCache(@RequestParam String key) {
        try {
            boolean exists = redisUtil.hasKey(key);
            return ApiResponse.success("查询成功", exists);
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 测试获取缓存过期时间
     * GET http://localhost:8081/api/redis-test/expire?key=test
     */
    @GetMapping("/expire")
    public ApiResponse getExpire(@RequestParam String key) {
        try {
            long expire = redisUtil.getExpire(key, TimeUnit.SECONDS);
            if (expire == -2) {
                return ApiResponse.success("缓存不存在", null);
            } else if (expire == -1) {
                return ApiResponse.success("缓存永不过期", null);
            } else {
                return ApiResponse.success("获取成功", "剩余过期时间：" + expire + "秒");
            }
        } catch (Exception e) {
            return ApiResponse.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 测试缓存对象
     * POST http://localhost:8081/api/redis-test/set-object
     */
    @PostMapping("/set-object")
    public ApiResponse setObjectCache(@RequestBody TestUser user) {
        try {
            redisUtil.set("user:" + user.getId(), user);
            return ApiResponse.success("对象缓存设置成功", null);
        } catch (Exception e) {
            return ApiResponse.error("对象缓存设置失败: " + e.getMessage());
        }
    }

    /**
     * 测试获取缓存对象
     * GET http://localhost:8081/api/redis-test/get-object?id=1
     */
    @GetMapping("/get-object")
    public ApiResponse getObjectCache(@RequestParam String id) {
        try {
            TestUser user = (TestUser) redisUtil.get("user:" + id);
            if (user == null) {
                return ApiResponse.success("缓存不存在", null);
            }
            return ApiResponse.success("获取对象缓存成功", user);
        } catch (Exception e) {
            return ApiResponse.error("获取对象缓存失败: " + e.getMessage());
        }
    }

    /**
     * 测试内部类，用于测试对象缓存
     */
    public static class TestUser {
        private String id;
        private String name;
        private String email;

        // 必须有无参构造函数
        public TestUser() {
        }

        public TestUser(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
