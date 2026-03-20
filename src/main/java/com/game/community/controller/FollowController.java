package com.game.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.game.community.common.Result;
import com.game.community.entity.User;
import com.game.community.entity.UserFollow;
import com.game.community.mapper.UserFollowMapper;
import com.game.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户关注列表
     */
    @GetMapping("/following/{userId}")
    public Result<List<User>> getFollowing(@PathVariable Long userId) {
        QueryWrapper<UserFollow> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<UserFollow> follows = userFollowMapper.selectList(wrapper);

        if (follows.isEmpty()) {
            return Result.success(Collections.emptyList());
        }

        List<Long> followUserIds = follows.stream()
                .map(UserFollow::getFollowUserId)
                .collect(Collectors.toList());
        List<User> users = userMapper.selectBatchIds(followUserIds);
        users.forEach(u -> u.setPassword(null));

        return Result.success(users);
    }

    /**
     * 获取用户粉丝列表
     */
    @GetMapping("/followers/{userId}")
    public Result<List<User>> getFollowers(@PathVariable Long userId) {
        QueryWrapper<UserFollow> wrapper = new QueryWrapper<>();
        wrapper.eq("follow_user_id", userId);
        List<UserFollow> follows = userFollowMapper.selectList(wrapper);

        if (follows.isEmpty()) {
            return Result.success(Collections.emptyList());
        }

        List<Long> userIds = follows.stream()
                .map(UserFollow::getUserId)
                .collect(Collectors.toList());
        List<User> users = userMapper.selectBatchIds(userIds);
        users.forEach(u -> u.setPassword(null));

        return Result.success(users);
    }

    /**
     * 关注用户
     */
    @PostMapping("/{targetUserId}")
    public Result<String> follow(@PathVariable Long targetUserId,
                                  @RequestParam Long userId) {
        QueryWrapper<UserFollow> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("follow_user_id", targetUserId);
        if (userFollowMapper.selectCount(wrapper) > 0) {
            return Result.error("已关注该用户");
        }

        UserFollow follow = new UserFollow();
        follow.setUserId(userId);
        follow.setFollowUserId(targetUserId);
        userFollowMapper.insert(follow);

        return Result.success("关注成功");
    }

    /**
     * 取消关注
     */
    @DeleteMapping("/{targetUserId}")
    public Result<String> unfollow(@PathVariable Long targetUserId,
                                    @RequestParam Long userId) {
        QueryWrapper<UserFollow> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("follow_user_id", targetUserId);
        userFollowMapper.delete(wrapper);

        return Result.success("取消关注成功");
    }
}
