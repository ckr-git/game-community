package com.game.community.controller;

import com.game.community.common.Result;
import com.game.community.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private ForumPostMapper forumPostMapper;

    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("/stats")
    public Result<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("userCount", userMapper.selectCount(null));
        stats.put("gameCount", gameMapper.selectCount(null));
        stats.put("postCount", forumPostMapper.selectCount(null));
        stats.put("commentCount", commentMapper.selectCount(null));
        return Result.success(stats);
    }
}
