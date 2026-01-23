package com.game.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.game.community.common.Result;
import com.game.community.entity.Comment;
import com.game.community.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    
    @Autowired
    private CommentMapper commentMapper;
    
    /**
     * 获取评论列表
     * targetType: 1-游戏 2-资讯 3-帖子
     */
    @GetMapping("/list")
    public Result<Page<Comment>> getCommentList(
            @RequestParam Integer targetType,
            @RequestParam Long targetId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        
        Page<Comment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target_type", targetType);
        queryWrapper.eq("target_id", targetId);
        queryWrapper.eq("parent_id", 0); // 只获取顶级评论
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("create_time");
        
        commentMapper.selectPage(page, queryWrapper);
        return Result.success(page);
    }
    
    /**
     * 获取评论的回复列表
     */
    @GetMapping("/replies/{parentId}")
    public Result<List<Comment>> getReplies(@PathVariable Long parentId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("create_time");
        
        List<Comment> replies = commentMapper.selectList(queryWrapper);
        return Result.success(replies);
    }
    
    /**
     * 发布评论
     */
    @PostMapping
    public Result<String> addComment(@RequestBody Comment comment) {
        comment.setLikeCount(0);
        comment.setStatus(1);
        
        commentMapper.insert(comment);
        return Result.success("评论成功");
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteComment(@PathVariable Long id) {
        commentMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    /**
     * 获取用户的评论列表
     */
    @GetMapping("/user/{userId}")
    public Result<Page<Comment>> getUserComments(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Page<Comment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("create_time");
        
        commentMapper.selectPage(page, queryWrapper);
        return Result.success(page);
    }
}
