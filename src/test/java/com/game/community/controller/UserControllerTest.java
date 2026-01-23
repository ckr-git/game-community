package com.game.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.community.BaseTest;
import com.game.community.entity.User;
import com.game.community.mapper.UserMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户控制器单元测试
 * 测试用户注册、登录、信息获取等功能
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest extends BaseTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "123456";
    
    /**
     * FT-01: 用户注册正常流程
     */
    @Test
    @Order(1)
    @DisplayName("用户注册正常流程")
    void testRegisterSuccess() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", TEST_USERNAME);
        request.put("password", TEST_PASSWORD);
        request.put("nickname", "测试用户");
        request.put("email", "test@example.com");
        
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"));
    }
    
    /**
     * FT-02: 用户注册-重复用户名
     */
    @Test
    @Order(2)
    @DisplayName("用户注册-重复用户名")
    void testRegisterDuplicateUsername() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", TEST_USERNAME);
        request.put("password", TEST_PASSWORD);
        request.put("nickname", "测试用户2");
        request.put("email", "test2@example.com");
        
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("用户名已存在"));
    }
    
    /**
     * FT-03: 用户登录正常流程
     */
    @Test
    @Order(3)
    @DisplayName("用户登录正常流程")
    void testLoginSuccess() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", TEST_USERNAME);
        request.put("password", TEST_PASSWORD);
        
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.username").value(TEST_USERNAME));
    }
    
    /**
     * FT-04: 用户登录-密码错误
     */
    @Test
    @Order(4)
    @DisplayName("用户登录-密码错误")
    void testLoginWrongPassword() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", TEST_USERNAME);
        request.put("password", "wrongpassword");
        
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("密码错误"));
    }
    
    /**
     * FT-05: 用户登录-账号禁用
     */
    @Test
    @Order(5)
    @DisplayName("用户登录-账号禁用")
    void testLoginDisabledAccount() throws Exception {
        // 先禁用账号
        User user = userMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                .eq("username", TEST_USERNAME));
        if (user != null) {
            user.setStatus(0);
            userMapper.updateById(user);
        }
        
        Map<String, String> request = new HashMap<>();
        request.put("username", TEST_USERNAME);
        request.put("password", TEST_PASSWORD);
        
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("账号已被禁用"));
        
        // 恢复账号状态
        if (user != null) {
            user.setStatus(1);
            userMapper.updateById(user);
        }
    }
    
    /**
     * FT-06: 获取用户信息
     */
    @Test
    @Order(6)
    @DisplayName("获取用户信息")
    void testGetUserInfo() throws Exception {
        User user = userMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                .eq("username", TEST_USERNAME));
        
        if (user != null) {
            mockMvc.perform(get("/user/" + user.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.username").value(TEST_USERNAME))
                    .andExpect(jsonPath("$.data.password").doesNotExist()); // 密码不应返回
        }
    }
    
    /**
     * FT-07: 更新用户信息
     */
    @Test
    @Order(7)
    @DisplayName("更新用户信息")
    void testUpdateUser() throws Exception {
        User user = userMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                .eq("username", TEST_USERNAME));
        
        if (user != null) {
            Map<String, String> request = new HashMap<>();
            request.put("nickname", "更新后的昵称");
            request.put("email", "updated@example.com");
            
            mockMvc.perform(put("/user/" + user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("操作成功"));
        }
    }
    
    /**
     * 用户登录-用户不存在
     */
    @Test
    @DisplayName("用户登录-用户不存在")
    void testLoginUserNotFound() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "nonexistentuser");
        request.put("password", TEST_PASSWORD);
        
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("用户不存在"));
    }
}
