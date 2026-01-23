-- 游戏社区交流平台数据库表结构

CREATE DATABASE IF NOT EXISTS game_community DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE game_community;

-- 用户表
CREATE TABLE `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `email` VARCHAR(100) COMMENT '邮箱',
  `phone` VARCHAR(20) COMMENT '手机号',
  `gender` TINYINT DEFAULT 0 COMMENT '性别：0-未知 1-男 2-女',
  `role` TINYINT DEFAULT 1 COMMENT '角色：1-普通用户 2-管理员',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_username (`username`),
  INDEX idx_email (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 游戏分类表
CREATE TABLE `game_category` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `description` VARCHAR(255) COMMENT '分类描述',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏分类表';

-- 游戏表
CREATE TABLE `game` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '游戏ID',
  `title` VARCHAR(100) NOT NULL COMMENT '游戏标题',
  `cover_image` VARCHAR(255) COMMENT '封面图',
  `description` TEXT COMMENT '游戏描述',
  `category_id` BIGINT COMMENT '分类ID',
  `tags` VARCHAR(255) COMMENT '标签，逗号分隔',
  `developer` VARCHAR(100) COMMENT '开发商',
  `release_date` DATE COMMENT '发布日期',
  `download_url` VARCHAR(255) COMMENT '下载链接',
  `download_count` INT DEFAULT 0 COMMENT '下载次数',
  `view_count` INT DEFAULT 0 COMMENT '浏览次数',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `collect_count` INT DEFAULT 0 COMMENT '收藏数',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架 1-上架',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_category (`category_id`),
  INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏表';

-- 游戏收藏表
CREATE TABLE `game_collect` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `game_id` BIGINT NOT NULL COMMENT '游戏ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_game (`user_id`, `game_id`),
  INDEX idx_user (`user_id`),
  INDEX idx_game (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏收藏表';

-- 游戏下载记录表
CREATE TABLE `game_download` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '下载记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `game_id` BIGINT NOT NULL COMMENT '游戏ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user (`user_id`),
  INDEX idx_game (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏下载记录表';

-- 资讯表
CREATE TABLE `news` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '资讯ID',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `cover_image` VARCHAR(255) COMMENT '封面图',
  `content` LONGTEXT COMMENT '内容',
  `author_id` BIGINT COMMENT '作者ID',
  `game_id` BIGINT COMMENT '关联游戏ID',
  `view_count` INT DEFAULT 0 COMMENT '浏览量',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `comment_count` INT DEFAULT 0 COMMENT '评论数',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-草稿 1-发布',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_author (`author_id`),
  INDEX idx_game (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资讯表';

-- 论坛帖子表
CREATE TABLE `forum_post` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '帖子ID',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `content` LONGTEXT COMMENT '内容',
  `author_id` BIGINT NOT NULL COMMENT '作者ID',
  `game_id` BIGINT COMMENT '关联游戏ID',
  `view_count` INT DEFAULT 0 COMMENT '浏览量',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `comment_count` INT DEFAULT 0 COMMENT '评论数',
  `collect_count` INT DEFAULT 0 COMMENT '收藏数',
  `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶',
  `is_hot` TINYINT DEFAULT 0 COMMENT '是否热门',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-隐藏 1-正常 2-审核中',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_author (`author_id`),
  INDEX idx_game (`game_id`),
  INDEX idx_create_time (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛帖子表';

-- 帖子收藏表
CREATE TABLE `post_collect` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `post_id` BIGINT NOT NULL COMMENT '帖子ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_post (`user_id`, `post_id`),
  INDEX idx_user (`user_id`),
  INDEX idx_post (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子收藏表';

-- 评论表（通用）
CREATE TABLE `comment` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
  `target_type` TINYINT NOT NULL COMMENT '目标类型：1-游戏 2-资讯 3-帖子',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父评论ID，0表示顶级评论',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-隐藏 1-正常',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_target (`target_type`, `target_id`),
  INDEX idx_user (`user_id`),
  INDEX idx_parent (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 点赞表（通用）
CREATE TABLE `user_like` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
  `target_type` TINYINT NOT NULL COMMENT '目标类型：1-游戏 2-资讯 3-帖子 4-评论',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_target (`user_id`, `target_type`, `target_id`),
  INDEX idx_target (`target_type`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

-- 聊天室表
CREATE TABLE `chat_room` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '聊天室ID',
  `name` VARCHAR(100) NOT NULL COMMENT '聊天室名称',
  `game_id` BIGINT COMMENT '关联游戏ID',
  `description` VARCHAR(255) COMMENT '描述',
  `cover_image` VARCHAR(255) COMMENT '封面图',
  `member_count` INT DEFAULT 0 COMMENT '成员数',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-关闭 1-开启',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_game (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天室表';

-- 聊天消息表
CREATE TABLE `chat_message` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
  `room_id` BIGINT NOT NULL COMMENT '聊天室ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `content` TEXT NOT NULL COMMENT '消息内容',
  `message_type` TINYINT DEFAULT 1 COMMENT '消息类型：1-文本 2-图片 3-系统消息',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_room (`room_id`),
  INDEX idx_user (`user_id`),
  INDEX idx_create_time (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';

-- 用户关注表
CREATE TABLE `user_follow` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关注ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `follow_user_id` BIGINT NOT NULL COMMENT '被关注用户ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_follow (`user_id`, `follow_user_id`),
  INDEX idx_user (`user_id`),
  INDEX idx_follow (`follow_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关注表';

-- 举报表
CREATE TABLE `report` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '举报ID',
  `target_type` TINYINT NOT NULL COMMENT '举报类型：1-帖子 2-评论 3-用户',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `reporter_id` BIGINT NOT NULL COMMENT '举报人ID',
  `reason` VARCHAR(255) NOT NULL COMMENT '举报原因',
  `description` TEXT COMMENT '详细描述',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-待处理 1-已处理 2-已驳回',
  `handler_id` BIGINT COMMENT '处理人ID',
  `handle_result` VARCHAR(255) COMMENT '处理结果',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_reporter (`reporter_id`),
  INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报表';

-- 公告表
CREATE TABLE `announcement` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `content` TEXT NOT NULL COMMENT '内容',
  `type` TINYINT DEFAULT 1 COMMENT '类型：1-系统公告 2-活动公告',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-隐藏 1-显示',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 轮播图表
CREATE TABLE `banner` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '轮播图ID',
  `title` VARCHAR(100) NOT NULL COMMENT '标题',
  `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL',
  `link_url` VARCHAR(255) COMMENT '跳转链接',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';
