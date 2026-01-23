package com.game.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.community.BaseTest;
import com.game.community.TestDataFactory;
import com.game.community.entity.Game;
import com.game.community.mapper.GameMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 游戏控制器单元测试
 * 测试游戏CRUD功能
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameControllerTest extends BaseTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private GameMapper gameMapper;
    
    private static Long testGameId;
    
    @BeforeEach
    void setUp() {
        // 确保有测试数据
        if (testGameId == null) {
            Game game = TestDataFactory.createGame("测试游戏", "这是一个测试游戏描述");
            gameMapper.insert(game);
            testGameId = game.getId();
        }
    }
    
    /**
     * FT-08: 游戏列表分页
     */
    @Test
    @Order(1)
    @DisplayName("游戏列表分页")
    void testGetGameList() throws Exception {
        mockMvc.perform(get("/game/list")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray());
    }
    
    /**
     * FT-09: 游戏分类筛选
     */
    @Test
    @Order(2)
    @DisplayName("游戏分类筛选")
    void testGetGameListByCategory() throws Exception {
        mockMvc.perform(get("/game/list")
                .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
    
    /**
     * FT-10: 游戏关键词搜索
     */
    @Test
    @Order(3)
    @DisplayName("游戏关键词搜索")
    void testSearchGames() throws Exception {
        mockMvc.perform(get("/game/list")
                .param("keyword", "测试"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
    
    /**
     * FT-11: 游戏详情获取
     */
    @Test
    @Order(4)
    @DisplayName("游戏详情获取")
    void testGetGameDetail() throws Exception {
        mockMvc.perform(get("/game/" + testGameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("测试游戏"));
    }
    
    /**
     * FT-12: 游戏浏览量自增
     */
    @Test
    @Order(5)
    @DisplayName("游戏浏览量自增")
    void testViewCountIncrement() throws Exception {
        // 获取初始浏览量
        Game gameBefore = gameMapper.selectById(testGameId);
        int initialViewCount = gameBefore.getViewCount();
        
        // 访问游戏详情
        mockMvc.perform(get("/game/" + testGameId))
                .andExpect(status().isOk());
        
        // 验证浏览量增加
        Game gameAfter = gameMapper.selectById(testGameId);
        Assertions.assertEquals(initialViewCount + 1, gameAfter.getViewCount());
    }
    
    /**
     * FT-13: 热门游戏接口
     */
    @Test
    @Order(6)
    @DisplayName("热门游戏接口")
    void testGetHotGames() throws Exception {
        mockMvc.perform(get("/game/hot")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    /**
     * FT-14: 游戏新增 (管理员)
     */
    @Test
    @Order(7)
    @DisplayName("游戏新增")
    void testAddGame() throws Exception {
        Game newGame = TestDataFactory.createGame("新增游戏", "新增游戏描述");
        
        mockMvc.perform(post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newGame)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("添加成功"));
    }
    
    /**
     * FT-15: 游戏更新 (管理员)
     */
    @Test
    @Order(8)
    @DisplayName("游戏更新")
    void testUpdateGame() throws Exception {
        Game updateGame = new Game();
        updateGame.setTitle("更新后的游戏名称");
        updateGame.setDescription("更新后的描述");
        
        mockMvc.perform(put("/game/" + testGameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGame)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("更新成功"));
    }
    
    /**
     * FT-16: 游戏删除 (管理员)
     */
    @Test
    @Order(9)
    @DisplayName("游戏删除")
    void testDeleteGame() throws Exception {
        // 创建一个用于删除的游戏
        Game deleteGame = TestDataFactory.createGame("待删除游戏", "待删除游戏描述");
        gameMapper.insert(deleteGame);
        
        mockMvc.perform(delete("/game/" + deleteGame.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("删除成功"));
    }
    
    /**
     * 游戏详情-游戏不存在
     */
    @Test
    @DisplayName("游戏详情-游戏不存在")
    void testGetGameDetailNotFound() throws Exception {
        mockMvc.perform(get("/game/99999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
