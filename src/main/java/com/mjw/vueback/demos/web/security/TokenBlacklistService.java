package com.mjw.vueback.demos.web.security;

import com.mjw.vueback.demos.web.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * Token 黑名单服务
 * 用于实现退出登录功能
 */
@Service
public class TokenBlacklistService {

    @Autowired
    private RedisUtil redisUtil;

    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    /**
     * 将 Token 加入黑名单
     * @param token JWT Token
     * @param expiration Token 的剩余过期时间（秒）
     */
    public void addToBlacklist(String token, long expiration) {
        String tokenHash = getTokenHash(token);
        String key = BLACKLIST_PREFIX + tokenHash;
        
        System.out.println("=== 添加 Token 到黑名单 ===");
        System.out.println("Token Hash: " + tokenHash);
        System.out.println("Redis Key: " + key);
        System.out.println("过期时间（秒）: " + expiration);
        
        // 将 Token 存入 Redis，过期时间设置为 Token 的剩余有效期
        if (expiration > 0) {
            redisUtil.set(key, "1", expiration, TimeUnit.SECONDS);
            System.out.println("Token 已添加到黑名单");
        } else {
            // 如果过期时间小于等于0，设置默认30分钟
            redisUtil.set(key, "1", 30, TimeUnit.MINUTES);
            System.out.println("Token 已添加到黑名单（使用默认过期时间30分钟）");
        }
    }

    /**
     * 检查 Token 是否在黑名单中
     * @param token JWT Token
     * @return true 表示在黑名单中（已退出登录）
     */
    public boolean isBlacklisted(String token) {
        String tokenHash = getTokenHash(token);
        String key = BLACKLIST_PREFIX + tokenHash;
        boolean result = redisUtil.hasKey(key);
        System.out.println("检查 Token 是否在黑名单: " + tokenHash + " -> " + result);
        return result;
    }

    /**
     * 从黑名单中移除 Token（可选，用于重新激活 Token）
     * @param token JWT Token
     */
    public void removeFromBlacklist(String token) {
        String tokenHash = getTokenHash(token);
        String key = BLACKLIST_PREFIX + tokenHash;
        redisUtil.delete(key);
        System.out.println("Token 已从黑名单移除: " + tokenHash);
    }

    /**
     * 计算 Token 的 MD5 哈希值（作为 Redis key）
     */
    private String getTokenHash(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(token.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // 如果 MD5 不可用，使用简单的哈希值
            return String.valueOf(token.hashCode());
        }
    }
}
