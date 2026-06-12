package com.mjw.vueback.demos.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // 放行接口register
                .antMatchers("/error").permitAll()  // 放行错误页面

                .antMatchers("/api/user/register").permitAll()  // 放行认证接口（登录、注册等）
                .antMatchers("/api/auth/**").permitAll()  // 放行认证接口（登录、注册等）
                .antMatchers("/api/components/fileDownload").permitAll()  // 放行文件下载（window.open 无法携带请求头）
                .antMatchers("/api/components/printPdf").permitAll()  // 放行PDF打印（window.open 无法携带请求头）
//                .antMatchers("/components/**").permitAll()  // 放行工装申请接口
//                .antMatchers("/api/settings/**").permitAll()
//                .antMatchers("/api/stats/**").permitAll()
                // 其他接口需要认证
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
