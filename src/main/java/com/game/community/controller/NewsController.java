package com.game.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.game.community.common.Result;
import com.game.community.entity.News;
import com.game.community.mapper.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    
    @Autowired
    private NewsMapper newsMapper;
    
    /**
     * 分页查询资讯列表
     */
    @GetMapping("/list")
    public Result<Page<News>> getNewsList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long gameId) {
        
        Page<News> page = new Page<>(pageNum, pageSize);
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        
        if (gameId != null) {
            queryWrapper.eq("game_id", gameId);
        }
        
        queryWrapper.orderByDesc("create_time");
        newsMapper.selectPage(page, queryWrapper);
        
        return Result.success(page);
    }
    
    /**
     * 获取资讯详情
     */
    @GetMapping("/{id}")
    public Result<News> getNewsDetail(@PathVariable Long id) {
        News news = newsMapper.selectById(id);
        
        if (news != null) {
            // 增加浏览量
            news.setViewCount(news.getViewCount() + 1);
            newsMapper.updateById(news);
        }
        
        return Result.success(news);
    }
    
    /**
     * 获取最新资讯
     */
    @GetMapping("/latest")
    public Result<List<News>> getLatestNews(@RequestParam(defaultValue = "10") Integer limit) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT " + limit);
        
        List<News> newsList = newsMapper.selectList(queryWrapper);
        return Result.success(newsList);
    }
    
    /**
     * 新增资讯（管理员）
     */
    @PostMapping
    public Result<String> addNews(@RequestBody News news) {
        news.setViewCount(0);
        news.setLikeCount(0);
        news.setCommentCount(0);
        news.setStatus(1);
        
        newsMapper.insert(news);
        return Result.success("添加成功");
    }
    
    /**
     * 更新资讯（管理员）
     */
    @PutMapping("/{id}")
    public Result<String> updateNews(@PathVariable Long id, @RequestBody News news) {
        news.setId(id);
        newsMapper.updateById(news);
        return Result.success("更新成功");
    }
    
    /**
     * 删除资讯（管理员）
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteNews(@PathVariable Long id) {
        newsMapper.deleteById(id);
        return Result.success("删除成功");
    }
}
