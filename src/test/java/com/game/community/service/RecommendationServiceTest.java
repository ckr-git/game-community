package com.game.community.service;

import com.game.community.BaseTest;
import com.game.community.TestDataFactory;
import com.game.community.entity.Game;
import com.game.community.mapper.GameMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 推荐服务单元测试
 */
class RecommendationServiceTest extends BaseTest {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @Autowired
    private GameMapper gameMapper;
    
    @BeforeEach
    void setUp() {
        // 确保有测试数据
        if (gameMapper.selectCount(null) < 5) {
            for (int i = 1; i <= 5; i++) {
                Game game = TestDataFactory.createGame("推荐测试游戏" + i, "描述" + i);
                game.setViewCount(i * 100);
                game.setDownloadCount(i * 50);
                game.setCollectCount(i * 30);
                gameMapper.insert(game);
            }
        }
    }
    
    /**
     * FT-21: 热门游戏推荐
     */
    @Test
    @DisplayName("热门游戏推荐测试")
    void testGetHotGames() {
        List<Game> hotGames = recommendationService.getHotGames(5);
        
        assertNotNull(hotGames);
        assertTrue(hotGames.size() <= 5);
        
        // 验证所有游戏状态为上架
        for (Game game : hotGames) {
            assertEquals(1, game.getStatus());
        }
    }
    
    /**
     * FT-22: 内容相似推荐
     */
    @Test
    @DisplayName("内容相似推荐测试")
    void testRecommendBySimilarContent() {
        // 获取一个游戏
        Game sourceGame = gameMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Game>()
                .eq("status", 1)
                .last("LIMIT 1"));
        
        if (sourceGame != null) {
            List<Game> recommendations = recommendationService.recommendBySimilarContent(sourceGame.getId(), 5);
            
            assertNotNull(recommendations);
            // 推荐结果不应包含源游戏
            for (Game game : recommendations) {
                assertNotEquals(sourceGame.getId(), game.getId());
            }
        }
    }
    
    /**
     * FT-23: 个性化推荐
     */
    @Test
    @DisplayName("个性化推荐测试")
    void testGetPersonalizedRecommendations() {
        Long userId = 1L;
        List<Game> recommendations = recommendationService.getPersonalizedRecommendations(userId, 10);
        
        assertNotNull(recommendations);
        // 个性化推荐应返回结果（即使是降级为热门推荐）
    }
    
    /**
     * 基于用户行为推荐测试
     */
    @Test
    @DisplayName("基于用户行为推荐测试")
    void testRecommendByUserBehavior() {
        Long userId = 1L;
        List<Game> recommendations = recommendationService.recommendByUserBehavior(userId, 5);
        
        assertNotNull(recommendations);
        // 由于是新用户，应该降级返回热门游戏
    }
    
    /**
     * 空结果测试
     */
    @Test
    @DisplayName("推荐数量边界测试")
    void testRecommendationLimit() {
        // 请求0个推荐
        List<Game> zeroRecommendations = recommendationService.getHotGames(0);
        assertTrue(zeroRecommendations.isEmpty());
        
        // 请求大量推荐
        List<Game> manyRecommendations = recommendationService.getHotGames(100);
        assertNotNull(manyRecommendations);
    }
    
    /**
     * 内容相似推荐-游戏不存在
     */
    @Test
    @DisplayName("内容相似推荐-游戏不存在")
    void testRecommendBySimilarContentNotFound() {
        List<Game> recommendations = recommendationService.recommendBySimilarContent(99999L, 5);
        
        assertNotNull(recommendations);
        assertTrue(recommendations.isEmpty());
    }
}
