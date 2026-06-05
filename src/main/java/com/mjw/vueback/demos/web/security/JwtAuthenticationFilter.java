package com.mjw.vueback.demos.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final ApplicationContext applicationContext;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, ApplicationContext applicationContext) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.applicationContext = applicationContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request);
            String ipAddress = getClientIpAddress(request);
            String requestURI = request.getRequestURI();

            // 获取客户端端口
            int remotePort = request.getRemotePort();
            String clientIpAndPort = ipAddress + ":" + remotePort;

            // 打印请求信息
            logger.info("=== 收到请求 ===");
            logger.info("请求路径: {}", requestURI);
            logger.info("客户端IP: {}", ipAddress);
            logger.info("客户端端口: {}", remotePort);
            logger.info("客户端IP:端口: {}", clientIpAndPort);

            if (token != null) {
                logger.info("传入的Token: {}", token);

                if (jwtTokenUtil.validateToken(token)) {
                    String username = jwtTokenUtil.getUsernameFromToken(token);
                    logger.info("Token验证通过，用户名: {}", username);

                    // 从 ApplicationContext 获取 UserDetailsService，避免循环依赖
                    org.springframework.security.core.userdetails.UserDetailsService userDetailsService =
                            applicationContext.getBean(org.springframework.security.core.userdetails.UserDetailsService.class);

                    org.springframework.security.core.userdetails.UserDetails userDetails =
                            userDetailsService.loadUserByUsername(username);

                    org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication =
                            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(request));
                    org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.warn("Token验证失败");
                }
            } else {
                logger.info("未传入Token");
            }
        } catch (Exception e) {
            logger.error("JWT认证失败: {}", e.getMessage());
            org.springframework.security.core.context.SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0];  // 可能有多个IP，取第一个
            }
        }

        return request.getRemoteAddr();
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
