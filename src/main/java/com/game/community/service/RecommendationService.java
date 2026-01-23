package com.game.community.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.game.community.entity.Game;
import com.game.community.mapper.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 游戏推荐服务 - 基于协同过滤算法
 */
@Service
public class RecommendationService {
    
    @Autowired
    private GameMapper gameMapper;
    
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 基于用户协同过滤的游戏推荐
     * 通过分析用户的收藏行为，找到相似用户，推荐相似用户喜欢的游戏
     */
    public List<Game> recommendByUserBehavior(Long userId, int limit) {
        String cacheKey = "recommend:user:" + userId;
        
        // 尝试从缓存获取
        if (redisTemplate != null) {
            @SuppressWarnings("unchecked")
            List<Game> cachedGames = (List<Game>) redisTemplate.opsForValue().get(cacheKey);
            if (cachedGames != null && !cachedGames.isEmpty()) {
                return cachedGames.stream().limit(limit).collect(Collectors.toList());
            }
        }
        
        // TODO: 实现协同过滤算法
        // 1. 获取用户收藏的游戏列表
        // 2. 计算用户之间的相似度（余弦相似度或皮尔逊相关系数）
        // 3. 找到最相似的K个用户
        // 4. 推荐这些用户收藏但目标用户未收藏的游戏
        
        // 临时实现：返回热门游戏
        List<Game> recommendedGames = getHotGames(limit);
        
        // 缓存推荐结果（1小时）
        if (redisTemplate != null && !recommendedGames.isEmpty()) {
            redisTemplate.opsForValue().set(cacheKey, recommendedGames, 1, TimeUnit.HOURS);
        }
        
        return recommendedGames;
    }
    
    /**
     * 基于内容的推荐
     * 根据游戏的标签、分类等特征进行推荐
     */
    public List<Game> recommendBySimilarContent(Long gameId, int limit) {
        Game sourceGame = gameMapper.selectById(gameId);
        if (sourceGame == null) {
            return Collections.emptyList();
        }
        
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.ne("id", gameId);
        
        // 优先推荐同类游戏
        if (sourceGame.getCategoryId() != null) {
            queryWrapper.eq("category_id", sourceGame.getCategoryId());
        }
        
        queryWrapper.orderByDesc("collect_count", "view_count");
        queryWrapper.last("LIMIT " + limit);
        
        return gameMapper.selectList(queryWrapper);
    }
    
    /**
     * 热门游戏推荐
     * 基于浏览量、下载量、收藏量等指标
     */
    public List<Game> getHotGames(int limit) {
        String cacheKey = "recommend:hot:games";
        
        // 尝试从缓存获取
        if (redisTemplate != null) {
            @SuppressWarnings("unchecked")
            List<Game> cachedGames = (List<Game>) redisTemplate.opsForValue().get(cacheKey);
            if (cachedGames != null && !cachedGames.isEmpty()) {
                return cachedGames.stream().limit(limit).collect(Collectors.toList());
            }
        }
        
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        // 综合评分：下载量 * 2 + 收藏量 * 3 + 浏览量
        queryWrapper.orderByDesc("download_count * 2 + collect_count * 3 + view_count");
        queryWrapper.last("LIMIT " + limit);
        
        List<Game> hotGames = gameMapper.selectList(queryWrapper);
        
        // 缓存30分钟
        if (redisTemplate != null && !hotGames.isEmpty()) {
            redisTemplate.opsForValue().set(cacheKey, hotGames, 30, TimeUnit.MINUTES);
        }
        
        return hotGames;
    }
    
    /**
     * 个性化推荐
     * 结合用户行为和内容特征的混合推荐
     */
    public List<Game> getPersonalizedRecommendations(Long userId, int limit) {
        List<Game> recommendations = new ArrayList<>();
        
        // 获取基于用户行为的推荐（占60%）
        List<Game> userBasedGames = recommendByUserBehavior(userId, (int)(limit * 0.6));
        recommendations.addAll(userBasedGames);
        
        // 获取热门游戏（占40%）
        int remainingCount = limit - recommendations.size();
        if (remainingCount > 0) {
            List<Game> hotGames = getHotGames(remainingCount);
            // 去重
            Set<Long> existingIds = recommendations.stream()
                    .map(Game::getId)
                    .collect(Collectors.toSet());
            
            hotGames.stream()
                    .filter(game -> !existingIds.contains(game.getId()))
                    .limit(remainingCount)
                    .forEach(recommendations::add);
        }
        
        return recommendations;
    }
    
    /**
     * 计算余弦相似度
     * 用于用户-游戏矩阵的相似度计算
     */
    private double calculateCosineSimilarity(Map<Long, Double> vector1, Map<Long, Double> vector2) {
        Set<Long> commonItems = new HashSet<>(vector1.keySet());
        commonItems.retainAll(vector2.keySet());
        
        if (commonItems.isEmpty()) {
            return 0.0;
        }
        
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (Long itemId : commonItems) {
            dotProduct += vector1.get(itemId) * vector2.get(itemId);
        }
        
        for (double value : vector1.values()) {
            norm1 += value * value;
        }
        
        for (double value : vector2.values()) {
            norm2 += value * value;
        }
        
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
