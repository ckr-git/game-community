package com.game.community.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.community.common.JwtUtil;
import com.game.community.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Autowired
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 非 Controller 方法直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 放行 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 放行所有 GET 请求（公开读取）
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 放行白名单路径（登录、注册、WebSocket）
        String uri = request.getRequestURI();
        log.debug("JWT拦截器 - method: {}, URI: {}, contextPath: {}, servletPath: {}",
                request.getMethod(), uri, request.getContextPath(), request.getServletPath());

        if (isWhiteListed(uri)) {
            return true;
        }

        // POST/PUT/DELETE 需要验证 token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                Long userId = jwtUtil.getUserIdFromToken(token);
                request.setAttribute("userId", userId);
                return true;
            }
        }

        // 校验失败返回 401
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Result.error("未登录或token已过期")));
        return false;
    }

    private boolean isWhiteListed(String uri) {
        // 兼容有无 context-path 的情况
        return uri.endsWith("/user/login")
                || uri.endsWith("/user/register")
                || uri.contains("/websocket/");
    }
}
