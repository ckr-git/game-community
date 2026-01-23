package com.game.community;

import com.game.community.entity.*;
import cn.hutool.crypto.digest.BCrypt;

import java.time.LocalDate;

/**
 * 测试数据工厂
 * 提供各种测试数据的创建方法
 */
public class TestDataFactory {
    
    /**
     * 创建测试用户
     */
    public static User createUser(String username, String password, String nickname) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password));
        user.setNickname(nickname);
        user.setEmail(username + "@test.com");
        user.setRole(1);
        user.setStatus(1);
        return user;
    }
    
    /**
     * 创建管理员用户
     */
    public static User createAdmin(String username, String password) {
        User user = createUser(username, password, "管理员");
        user.setRole(2);
        return user;
    }
    
    /**
     * 创建测试游戏
     */
    public static Game createGame(String title, String description) {
        Game game = new Game();
        game.setTitle(title);
        game.setDescription(description);
        game.setCoverImage("https://example.com/cover.jpg");
        game.setDeveloper("Test Developer");
        game.setReleaseDate(LocalDate.now());
        game.setDownloadUrl("https://example.com/download");
        game.setDownloadCount(0);
        game.setViewCount(0);
        game.setLikeCount(0);
        game.setCollectCount(0);
        game.setStatus(1);
        return game;
    }
    
    /**
     * 创建测试帖子
     */
    public static ForumPost createPost(Long authorId, String title, String content) {
        ForumPost post = new ForumPost();
        post.setAuthorId(authorId);
        post.setTitle(title);
        post.setContent(content);
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCollectCount(0);
        post.setIsTop(0);
        post.setIsHot(0);
        post.setStatus(1);
        return post;
    }
    
    /**
     * 创建测试资讯
     */
    public static News createNews(Long authorId, String title, String content) {
        News news = new News();
        news.setAuthorId(authorId);
        news.setTitle(title);
        news.setContent(content);
        news.setViewCount(0);
        news.setLikeCount(0);
        news.setCommentCount(0);
        news.setStatus(1);
        return news;
    }
    
    /**
     * 创建测试评论
     */
    public static Comment createComment(Integer targetType, Long targetId, Long userId, String content) {
        Comment comment = new Comment();
        comment.setTargetType(targetType);
        comment.setTargetId(targetId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(0L);
        comment.setLikeCount(0);
        comment.setStatus(1);
        return comment;
    }
    
    /**
     * 创建测试聊天室
     */
    public static ChatRoom createChatRoom(String name, String description) {
        ChatRoom room = new ChatRoom();
        room.setName(name);
        room.setDescription(description);
        room.setMemberCount(0);
        room.setStatus(1);
        return room;
    }
    
    /**
     * 创建测试游戏分类
     */
    public static GameCategory createCategory(String name) {
        GameCategory category = new GameCategory();
        category.setName(name);
        category.setDescription(name + " 分类描述");
        category.setSortOrder(0);
        return category;
    }
}
