package com.mjw.vueback.demos.web.service;

import com.mjw.vueback.demos.web.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 在线用户服务
 * 基于 Redis 实现实时在线状态判断
 * 原理：
 *   - 登录时，标记用户在线（Redis key TTL = 24h，过期自动离线）
 *   - 刷新Token时，刷新在线标记（延长24h）
 *   - 退出登录时，删除 Redis 记录（立即离线）
 * 注意：Filter 中不再标记在线，确保 logout 请求不会被 Filter 重新设成在线
 */
@Service
public class OnlineUserService {

    @Autowired
    private RedisUtil redisUtil;

    private static final String ONLINE_PREFIX = "online:user:";

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    /**
     * 标记用户在线
     * 在 JWT Filter 验证通过后调用
     */
    public void setUserOnline(String username) {
        String key = ONLINE_PREFIX + username;
        // 过期时间与 Token 一致，确保 Token 过期后自动标记为离线
        redisUtil.set(key, "1", jwtExpiration, TimeUnit.MILLISECONDS);
    }

    /**
     * 标记用户离线（退出登录时调用）
     */
    public void setUserOffline(String username) {
        String key = ONLINE_PREFIX + username;
        redisUtil.delete(key);
    }

    /**
     * 判断用户是否在线
     * @return true 在线 / false 离线
     */
    public boolean isUserOnline(String username) {
        String key = ONLINE_PREFIX + username;
        return redisUtil.hasKey(key);
    }

    /**
     * 获取用户的在线状态文本
     * @return "在线" 或 "离线"
     */
    public String getOnlineStatusText(String username) {
        return isUserOnline(username) ? "在线" : "离线";
    }
}
