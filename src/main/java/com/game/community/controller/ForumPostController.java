package com.game.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.game.community.common.Result;
import com.game.community.entity.ForumPost;
import com.game.community.mapper.ForumPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumPostController {
    
    @Autowired
    private ForumPostMapper forumPostMapper;
    
    /**
     * 分页查询帖子列表
     */
    @GetMapping("/list")
    public Result<Page<ForumPost>> getPostList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long gameId,
            @RequestParam(required = false) String keyword) {
        
        Page<ForumPost> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        
        if (gameId != null) {
            queryWrapper.eq("game_id", gameId);
        }
        
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(w -> w.like("title", keyword).or().like("content", keyword));
        }
        
        queryWrapper.orderByDesc("is_top", "create_time");
        forumPostMapper.selectPage(page, queryWrapper);
        
        return Result.success(page);
    }
    
    /**
     * 获取帖子详情
     */
    @GetMapping("/{id}")
    public Result<ForumPost> getPostDetail(@PathVariable Long id) {
        ForumPost post = forumPostMapper.selectById(id);
        
        if (post != null) {
            // 增加浏览量
            post.setViewCount(post.getViewCount() + 1);
            forumPostMapper.updateById(post);
        }
        
        return Result.success(post);
    }
    
    /**
     * 获取热门帖子
     */
    @GetMapping("/hot")
    public Result<List<ForumPost>> getHotPosts(@RequestParam(defaultValue = "10") Integer limit) {
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("view_count", "like_count");
        queryWrapper.last("LIMIT " + limit);
        
        List<ForumPost> posts = forumPostMapper.selectList(queryWrapper);
        return Result.success(posts);
    }
    
    /**
     * 发布帖子
     */
    @PostMapping
    public Result<String> createPost(@RequestBody ForumPost post) {
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCollectCount(0);
        post.setIsTop(0);
        post.setIsHot(0);
        post.setStatus(1); // 直接发布，如需审核可改为2
        
        forumPostMapper.insert(post);
        return Result.success("发布成功");
    }
    
    /**
     * 更新帖子
     */
    @PutMapping("/{id}")
    public Result<String> updatePost(@PathVariable Long id, @RequestBody ForumPost post) {
        post.setId(id);
        forumPostMapper.updateById(post);
        return Result.success("更新成功");
    }
    
    /**
     * 删除帖子
     */
    @DeleteMapping("/{id}")
    public Result<String> deletePost(@PathVariable Long id) {
        forumPostMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    /**
     * 获取用户发布的帖子
     */
    @GetMapping("/user/{userId}")
    public Result<Page<ForumPost>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Page<ForumPost> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", userId);
        queryWrapper.orderByDesc("create_time");
        
        forumPostMapper.selectPage(page, queryWrapper);
        return Result.success(page);
    }
}
