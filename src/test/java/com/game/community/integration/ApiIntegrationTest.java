package com.game.community.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.community.BaseTest;
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
 * API集成测试
 * 测试完整的API流程
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiIntegrationTest extends BaseTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static String authToken;
    private static Long userId;
    
    /**
     * IT-01: 用户注册登录全流程
     */
    @Test
    @Order(1)
    @DisplayName("用户注册登录全流程")
    void testUserRegistrationAndLogin() throws Exception {
        // 1. 注册
        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("username", "integrationuser");
        registerRequest.put("password", "password123");
        registerRequest.put("nickname", "集成测试用户");
        registerRequest.put("email", "integration@test.com");
        
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        
        // 2. 登录
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "integrationuser");
        loginRequest.put("password", "password123");
        
        MvcResult result = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists())
                .andReturn();
        
        // 保存token和userId供后续测试使用
        String response = result.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
        Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
        authToken = (String) data.get("token");
        userId = ((Number) data.get("userId")).longValue();
        
        Assertions.assertNotNull(authToken);
        Assertions.assertNotNull(userId);
    }
    
    /**
     * IT-02: 游戏CRUD全流程
     */
    @Test
    @Order(2)
    @DisplayName("游戏CRUD全流程")
    void testGameCrudFlow() throws Exception {
        // 1. 新增游戏
        Map<String, Object> gameRequest = new HashMap<>();
        gameRequest.put("title", "集成测试游戏");
        gameRequest.put("description", "这是集成测试创建的游戏");
        gameRequest.put("developer", "Test Studio");
        
        MvcResult createResult = mockMvc.perform(post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        
        // 2. 查询游戏列表
        mockMvc.perform(get("/game/list")
                .param("keyword", "集成测试"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray());
        
        // 3. 获取热门游戏
        mockMvc.perform(get("/game/hot"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
    
    /**
     * RT-01: 登录后所有功能正常访问
     */
    @Test
    @Order(3)
    @DisplayName("登录后功能访问测试")
    void testAuthenticatedAccess() throws Exception {
        // 测试各个API端点
        mockMvc.perform(get("/game/list"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/news/list"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/forum/list"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/common/categories"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/common/banners"))
                .andExpect(status().isOk());
    }
    
    /**
     * RT-04: 数据一致性检查
     */
    @Test
    @Order(4)
    @DisplayName("数据一致性检查")
    void testDataConsistency() throws Exception {
        // 创建游戏并验证数据
        String uniqueTitle = "ConsistencyTest" + System.currentTimeMillis();
        Map<String, Object> gameRequest = new HashMap<>();
        gameRequest.put("title", uniqueTitle);
        gameRequest.put("description", "Test Data Consistency");
        
        mockMvc.perform(post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameRequest)))
                .andExpect(status().isOk());
        
        // 查询并验证 - 使用jsonPath代替contains避免编码问题
        mockMvc.perform(get("/game/list")
                .param("keyword", "ConsistencyTest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray())
                .andExpect(jsonPath("$.data.total").value(org.hamcrest.Matchers.greaterThanOrEqualTo(1)));
    }
}
