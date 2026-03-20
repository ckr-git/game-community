-- 游戏社区交流平台 种子数据
-- 执行前请确保数据库 game_community 已创建且表结构已就绪

-- ========== 游戏分类 ==========
INSERT INTO game_category (name, description, sort_order, deleted, create_time, update_time) VALUES
('动作冒险', '包含动作元素和冒险探索的游戏类型', 1, 0, NOW(), NOW()),
('角色扮演', '玩家扮演虚拟角色进行冒险与成长的游戏', 2, 0, NOW(), NOW()),
('射击竞技', '以射击操作和竞技对抗为核心的游戏', 3, 0, NOW(), NOW()),
('策略模拟', '注重策略思考和模拟经营的游戏类型', 4, 0, NOW(), NOW()),
('休闲益智', '轻松有趣、适合碎片时间的休闲游戏', 5, 0, NOW(), NOW());

-- ========== 游戏 ==========
INSERT INTO game (title, cover_image, description, category_id, tags, developer, release_date, download_url, download_count, view_count, like_count, collect_count, status, deleted, create_time, update_time) VALUES
('黑神话：悟空', 'https://picsum.photos/seed/wukong/400/225', '一款以中国神话为背景的动作角色扮演游戏，玩家扮演天命人踏上取经路。', 1, '动作,单机,国产', 'Game Science', '2024-08-20', '', 28600, 156000, 8920, 3210, 1, 0, NOW(), NOW()),
('原神', 'https://picsum.photos/seed/genshin/400/225', '开放世界冒险RPG，探索提瓦特大陆七国的奇幻之旅。', 2, 'RPG,开放世界,免费', 'miHoYo', '2020-09-28', '', 85000, 320000, 15600, 7800, 1, 0, NOW(), NOW()),
('永劫无间', 'https://picsum.photos/seed/naraka/400/225', '60人大逃杀动作竞技游戏，融合东方武侠元素。', 3, '竞技,多人,武侠', '24 Entertainment', '2021-08-12', '', 42000, 98000, 5200, 2100, 1, 0, NOW(), NOW()),
('文明6', 'https://picsum.photos/seed/civ6/400/225', '回合制策略游戏，带领文明从远古走向太空时代。', 4, '策略,回合制,历史', 'Firaxis Games', '2016-10-21', '', 31000, 72000, 4800, 1950, 1, 0, NOW(), NOW()),
('蛋仔派对', 'https://picsum.photos/seed/eggy/400/225', '欢乐派对竞技手游，多种趣味关卡等你挑战。', 5, '休闲,派对,多人', 'NetEase Games', '2022-12-15', '', 120000, 250000, 18000, 6500, 1, 0, NOW(), NOW()),
('艾尔登法环', 'https://picsum.photos/seed/eldenring/400/225', '宫崎英高与乔治·马丁联手打造的开放世界魂类游戏。', 1, '魂类,开放世界,硬核', 'FromSoftware', '2022-02-25', '', 38000, 142000, 9500, 4200, 1, 0, NOW(), NOW()),
('最终幻想14', 'https://picsum.photos/seed/ff14/400/225', '经典MMORPG，光之战士的史诗冒险故事。', 2, 'MMO,RPG,订阅制', 'Square Enix', '2013-08-27', '', 22000, 89000, 6700, 2800, 1, 0, NOW(), NOW()),
('无畏契约', 'https://picsum.photos/seed/valorant/400/225', '5v5战术射击游戏，结合精准射击与独特技能。', 3, 'FPS,竞技,免费', 'Riot Games', '2020-06-02', '', 56000, 178000, 11200, 4500, 1, 0, NOW(), NOW()),
('戴森球计划', 'https://picsum.photos/seed/dyson/400/225', '国产科幻工厂建造模拟游戏，打造星际工业帝国。', 4, '模拟,建造,国产', 'Youthcat Studio', '2021-01-21', '', 15000, 52000, 3800, 1600, 1, 0, NOW(), NOW()),
('元梦之星', 'https://picsum.photos/seed/yuanmeng/400/225', '腾讯出品的派对竞技游戏，丰富的UGC创作玩法。', 5, '休闲,UGC,社交', 'Tencent Games', '2023-12-15', '', 95000, 185000, 12000, 4800, 1, 0, NOW(), NOW());

-- ========== 资讯 ==========
INSERT INTO news (title, cover_image, content, author_id, game_id, view_count, like_count, comment_count, status, deleted, create_time, update_time) VALUES
('《黑神话：悟空》DLC"前尘旧梦"正式公布', 'https://picsum.photos/seed/news1/600/300', '游戏科学工作室今日正式公布了《黑神话：悟空》首个大型DLC"前尘旧梦"的预告片，将带领玩家回到天命人出发前的故事。DLC预计将于2025年下半年推出，届时将新增全新的BOSS对战和剧情内容。', 1, 1, 12800, 560, 89, 1, 0, NOW(), NOW()),
('《原神》5.0版本"纳塔"地图全面开放', 'https://picsum.photos/seed/news2/600/300', '米哈游宣布《原神》5.0版本将全面开放火之国纳塔地图，引入全新的部落文化和战斗机制。新版本还将加入恐龙骑乘系统，为探索增添更多乐趣。', 1, 2, 18500, 920, 156, 1, 0, NOW(), NOW()),
('全球电竞大赛2025赛季正式开幕', 'https://picsum.photos/seed/news3/600/300', '2025年全球电竞大赛（GEC 2025）今日在上海正式拉开帷幕，涵盖《无畏契约》《永劫无间》等热门竞技项目，总奖金池超过5000万元。', 1, 8, 8600, 380, 67, 1, 0, NOW(), NOW()),
('Steam春季特卖活动即将开启', 'https://picsum.photos/seed/news4/600/300', 'Valve确认Steam春季特卖将于下周开启，届时数千款游戏将参与折扣活动。据悉《文明6》全DLC合集和《艾尔登法环》黄金版均在打折名单中。', 1, 4, 15200, 720, 203, 1, 0, NOW(), NOW()),
('国产独立游戏《戴森球计划》获IGN年度最佳提名', 'https://picsum.photos/seed/news5/600/300', '由重庆柚子猫工作室开发的《戴森球计划》凭借出色的玩法设计和持续的内容更新，获得了IGN 2025年度最佳独立游戏提名，为国产游戏再添光彩。', 1, 9, 6400, 290, 45, 1, 0, NOW(), NOW());

-- ========== 聊天室 ==========
INSERT INTO chat_room (name, game_id, description, cover_image, member_count, status, deleted, create_time, update_time) VALUES
('原神交流大厅', 2, '原神玩家综合交流聊天室，分享攻略、组队副本都在这里', 'https://picsum.photos/seed/room1/200/200', 128, 1, 0, NOW(), NOW()),
('黑神话粉丝营', 1, '黑神话悟空玩家交流据点，讨论剧情、分享战斗技巧', 'https://picsum.photos/seed/room2/200/200', 86, 1, 0, NOW(), NOW()),
('电竞赛事讨论区', 8, '关注电竞赛事动态，一起为喜爱的战队加油助威', 'https://picsum.photos/seed/room3/200/200', 52, 1, 0, NOW(), NOW());

-- ========== 轮播图 ==========
INSERT INTO banner (title, image_url, link_url, sort_order, status, deleted, create_time, update_time) VALUES
('黑神话：悟空 DLC 即将上线', 'https://picsum.photos/seed/banner1/1200/400', '/game/1', 1, 1, 0, NOW(), NOW()),
('原神 5.0 纳塔版本更新', 'https://picsum.photos/seed/banner2/1200/400', '/game/2', 2, 1, 0, NOW(), NOW()),
('2025全球电竞大赛开幕', 'https://picsum.photos/seed/banner3/1200/400', '/news/3', 3, 1, 0, NOW(), NOW());

-- ========== 公告 ==========
INSERT INTO announcement (title, content, type, status, create_time, update_time) VALUES
('社区使用规范公告', '欢迎加入游戏社区交流平台！请遵守社区规范：禁止发布违规内容，文明交流，尊重他人。违规用户将被禁言或封号处理。', 1, 1, NOW(), NOW()),
('春日活动：分享你的游戏截图赢好礼', '即日起至4月30日，在社区论坛发布游戏精彩截图，带上 #春日游戏时光# 标签，点赞数最高的前10名将获得精美周边礼品！', 2, 1, NOW(), NOW());
