package com.game.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.game.community.common.Result;
import com.game.community.entity.Game;
import com.game.community.mapper.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {
    
    @Autowired
    private GameMapper gameMapper;
    
    /**
     * 分页查询游戏列表
     */
    @GetMapping("/list")
    public Result<Page<Game>> getGameList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        
        Page<Game> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }
        
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                    .like("title", keyword)
                    .or()
                    .like("description", keyword));
        }
        
        queryWrapper.orderByDesc("create_time");
        
        gameMapper.selectPage(page, queryWrapper);
        
        return Result.success(page);
    }
    
    /**
     * 获取游戏详情
     */
    @GetMapping("/{id}")
    public Result<Game> getGameDetail(@PathVariable Long id) {
        Game game = gameMapper.selectById(id);
        
        if (game != null) {
            // 增加浏览量
            game.setViewCount(game.getViewCount() + 1);
            gameMapper.updateById(game);
        }
        
        return Result.success(game);
    }
    
    /**
     * 获取热门游戏
     */
    @GetMapping("/hot")
    public Result<List<Game>> getHotGames(@RequestParam(defaultValue = "10") Integer limit) {
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("LIMIT " + limit);
        
        List<Game> games = gameMapper.selectList(queryWrapper);
        
        return Result.success(games);
    }
    
    /**
     * 新增游戏（管理员功能）
     */
    @PostMapping
    public Result<String> addGame(@RequestBody Game game) {
        game.setDownloadCount(0);
        game.setViewCount(0);
        game.setLikeCount(0);
        game.setCollectCount(0);
        game.setStatus(1);
        
        gameMapper.insert(game);
        
        return Result.success("添加成功");
    }
    
    /**
     * 更新游戏信息（管理员功能）
     */
    @PutMapping("/{id}")
    public Result<String> updateGame(@PathVariable Long id, @RequestBody Game game) {
        game.setId(id);
        gameMapper.updateById(game);
        return Result.success("更新成功");
    }
    
    /**
     * 删除游戏（管理员功能）
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteGame(@PathVariable Long id) {
        gameMapper.deleteById(id);
        return Result.success("删除成功");
    }
}
