package com.game.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.game.community.common.Result;
import com.game.community.entity.*;
import com.game.community.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interaction")
public class InteractionController {
    
    @Autowired
    private GameCollectMapper gameCollectMapper;
    
    @Autowired
    private PostCollectMapper postCollectMapper;
    
    @Autowired
    private UserLikeMapper userLikeMapper;
    
    @Autowired
    private GameMapper gameMapper;
    
    @Autowired
    private ForumPostMapper forumPostMapper;
    
    // ========== 游戏收藏 ==========
    
    /**
     * 收藏游戏
     */
    @PostMapping("/game/collect")
    public Result<String> collectGame(@RequestParam Long userId, @RequestParam Long gameId) {
        // 检查是否已收藏
        QueryWrapper<GameCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("game_id", gameId);
        if (gameCollectMapper.selectOne(queryWrapper) != null) {
            return Result.error("已经收藏过了");
        }
        
        GameCollect collect = new GameCollect();
        collect.setUserId(userId);
        collect.setGameId(gameId);
        gameCollectMapper.insert(collect);
        
        // 更新游戏收藏数
        Game game = gameMapper.selectById(gameId);
        if (game != null) {
            game.setCollectCount(game.getCollectCount() + 1);
            gameMapper.updateById(game);
        }
        
        return Result.success("收藏成功");
    }
    
    /**
     * 取消收藏游戏
     */
    @DeleteMapping("/game/collect")
    public Result<String> uncollectGame(@RequestParam Long userId, @RequestParam Long gameId) {
        QueryWrapper<GameCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("game_id", gameId);
        gameCollectMapper.delete(queryWrapper);
        
        // 更新游戏收藏数
        Game game = gameMapper.selectById(gameId);
        if (game != null && game.getCollectCount() > 0) {
            game.setCollectCount(game.getCollectCount() - 1);
            gameMapper.updateById(game);
        }
        
        return Result.success("取消收藏成功");
    }
    
    /**
     * 检查是否已收藏游戏
     */
    @GetMapping("/game/collect/check")
    public Result<Boolean> checkGameCollected(@RequestParam Long userId, @RequestParam Long gameId) {
        QueryWrapper<GameCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("game_id", gameId);
        boolean collected = gameCollectMapper.selectOne(queryWrapper) != null;
        return Result.success(collected);
    }
    
    /**
     * 获取用户收藏的游戏列表
     */
    @GetMapping("/game/collect/list")
    public Result<Page<GameCollect>> getUserGameCollects(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Page<GameCollect> page = new Page<>(pageNum, pageSize);
        QueryWrapper<GameCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        
        gameCollectMapper.selectPage(page, queryWrapper);
        return Result.success(page);
    }
    
    // ========== 帖子收藏 ==========
    
    /**
     * 收藏帖子
     */
    @PostMapping("/post/collect")
    public Result<String> collectPost(@RequestParam Long userId, @RequestParam Long postId) {
        QueryWrapper<PostCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("post_id", postId);
        if (postCollectMapper.selectOne(queryWrapper) != null) {
            return Result.error("已经收藏过了");
        }
        
        PostCollect collect = new PostCollect();
        collect.setUserId(userId);
        collect.setPostId(postId);
        postCollectMapper.insert(collect);
        
        // 更新帖子收藏数
        ForumPost post = forumPostMapper.selectById(postId);
        if (post != null) {
            post.setCollectCount(post.getCollectCount() + 1);
            forumPostMapper.updateById(post);
        }
        
        return Result.success("收藏成功");
    }
    
    /**
     * 取消收藏帖子
     */
    @DeleteMapping("/post/collect")
    public Result<String> uncollectPost(@RequestParam Long userId, @RequestParam Long postId) {
        QueryWrapper<PostCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("post_id", postId);
        postCollectMapper.delete(queryWrapper);
        
        ForumPost post = forumPostMapper.selectById(postId);
        if (post != null && post.getCollectCount() > 0) {
            post.setCollectCount(post.getCollectCount() - 1);
            forumPostMapper.updateById(post);
        }
        
        return Result.success("取消收藏成功");
    }
    
    // ========== 点赞功能 ==========
    
    /**
     * 点赞
     * targetType: 1-游戏 2-资讯 3-帖子 4-评论
     */
    @PostMapping("/like")
    public Result<String> like(
            @RequestParam Long userId,
            @RequestParam Integer targetType,
            @RequestParam Long targetId) {
        
        QueryWrapper<UserLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("target_type", targetType)
                .eq("target_id", targetId);
        
        if (userLikeMapper.selectOne(queryWrapper) != null) {
            return Result.error("已经点赞过了");
        }
        
        UserLike userLike = new UserLike();
        userLike.setUserId(userId);
        userLike.setTargetType(targetType);
        userLike.setTargetId(targetId);
        userLikeMapper.insert(userLike);
        
        return Result.success("点赞成功");
    }
    
    /**
     * 取消点赞
     */
    @DeleteMapping("/like")
    public Result<String> unlike(
            @RequestParam Long userId,
            @RequestParam Integer targetType,
            @RequestParam Long targetId) {
        
        QueryWrapper<UserLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("target_type", targetType)
                .eq("target_id", targetId);
        
        userLikeMapper.delete(queryWrapper);
        return Result.success("取消点赞成功");
    }
    
    /**
     * 检查是否已点赞
     */
    @GetMapping("/like/check")
    public Result<Boolean> checkLiked(
            @RequestParam Long userId,
            @RequestParam Integer targetType,
            @RequestParam Long targetId) {
        
        QueryWrapper<UserLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("target_type", targetType)
                .eq("target_id", targetId);
        
        boolean liked = userLikeMapper.selectOne(queryWrapper) != null;
        return Result.success(liked);
    }
}
