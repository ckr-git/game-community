# 游戏社区交流平台

## 项目简介

这是一个基于SpringBoot3 + Vue3开发的游戏社区交流平台，提供游戏浏览、资讯阅读、论坛交流、实时聊天室等功能，并集成了基于协同过滤的游戏推荐系统。

## 技术栈

### 后端技术
- **架构**: B/S、MVC
- **JDK版本**: 17+
- **框架**: SpringBoot 3.1.5
- **ORM**: MyBatis-Plus 3.5.4.1
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **WebSocket**: 实时聊天功能
- **JWT**: 用户认证
- **工具类**: Hutool

### 前端技术
- **框架**: Vue 3
- **构建工具**: Vite
- **UI库**: Element Plus
- **数据可视化**: ECharts
- **HTTP客户端**: Axios

## 功能模块

### 用户模块
- 用户注册、登录
- 个人信息管理
- 游戏浏览与分类查询
- 游戏收藏管理
- 游戏下载记录
- 用户关注功能

### 游戏资讯模块
- 游戏详情展示
- 游戏收藏
- 资讯阅读
- 评论功能
- 点赞/踩功能
- 浏览量统计

### 论坛社区模块
- 发帖功能
- 评论交流
- 收藏帖子
- 留言板
- 举报功能
- 帖子审核

### 聊天室模块（WebSocket）
- 实时聊天功能
- 支持多个游戏主题聊天室
- 在线人数统计
- 消息广播

### 管理员模块
- 用户管理（查看、禁用账号）
- 游戏管理（添加、编辑、删除游戏）
- 游戏分类与标签管理
- 轮播图管理
- 资讯管理（发布、编辑游戏资讯）
- 论坛管理（帖子审核、举报处理）
- 聊天室管理
- 公告管理
- 快捷链接管理
- 数据统计（ECharts可视化）

### 游戏推荐系统
- 基于用户协同过滤的推荐
- 基于内容的推荐
- 热门游戏推荐
- 个性化推荐
- 推荐结果缓存优化

## 系统亮点

1. **实时聊天室**: 使用WebSocket实现实时聊天功能，支持多个游戏主题聊天室
2. **智能推荐**: 基于协同过滤的游戏推荐系统，通过构建用户-游戏收藏矩阵，使用余弦相似度算法计算用户间的相似性
3. **完整论坛系统**: 支持发帖、评论、收藏、点赞等社区互动功能
4. **内容安全管理**: 包括举报帖子内容和管理员审核机制
5. **数据可视化**: 使用ECharts实现数据统计图表
6. **流行技术栈**: 使用SpringBoot3+Vue3作为开发框架

## 项目结构

```
游戏社区交流平台/
├── src/
│   ├── main/
│   │   ├── java/com/game/community/
│   │   │   ├── CommunityPlatformApplication.java  # 主启动类
│   │   │   ├── common/                             # 通用类
│   │   │   │   ├── Result.java                    # 统一返回结果
│   │   │   │   └── JwtUtil.java                   # JWT工具类
│   │   │   ├── config/                             # 配置类
│   │   │   │   ├── MyBatisPlusConfig.java         # MyBatis-Plus配置
│   │   │   │   ├── WebSocketConfig.java           # WebSocket配置
│   │   │   │   └── CorsConfig.java                # 跨域配置
│   │   │   ├── controller/                         # 控制器
│   │   │   │   ├── UserController.java            # 用户控制器
│   │   │   │   └── GameController.java            # 游戏控制器
│   │   │   ├── entity/                             # 实体类
│   │   │   │   ├── User.java                      # 用户实体
│   │   │   │   ├── Game.java                      # 游戏实体
│   │   │   │   ├── ForumPost.java                 # 论坛帖子实体
│   │   │   │   ├── ChatRoom.java                  # 聊天室实体
│   │   │   │   ├── ChatMessage.java               # 聊天消息实体
│   │   │   │   ├── Comment.java                   # 评论实体
│   │   │   │   └── News.java                      # 资讯实体
│   │   │   ├── mapper/                             # Mapper接口
│   │   │   ├── service/                            # 服务层
│   │   │   │   └── RecommendationService.java     # 推荐服务
│   │   │   └── websocket/                          # WebSocket
│   │   │       └── ChatWebSocket.java             # 聊天WebSocket
│   │   └── resources/
│   │       ├── application.yml                     # 配置文件
│   │       └── mapper/                             # MyBatis XML
│   └── test/                                        # 测试
├── frontend/                                        # Vue3前端项目
├── database.sql                                     # 数据库SQL脚本
├── pom.xml                                          # Maven配置
└── README.md                                        # 项目文档
```

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Node.js 16+ (用于前端)

### 后端启动

1. **导入数据库**
```bash
# 执行database.sql创建数据库和表
mysql -u root -p < database.sql
```

2. **修改配置**
```yaml
# 修改src/main/resources/application.yml中的数据库和Redis配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/game_community
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
```

3. **启动项目**
```bash
# 方式1: Maven命令
mvn spring-boot:run

# 方式2: IDEA直接运行CommunityPlatformApplication类
```

4. **访问接口**
```
后端API: http://localhost:8080/api
WebSocket: ws://localhost:8080/api/websocket/chat/{roomId}/{userId}
```

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

访问: http://localhost:5173

## API接口文档

### 用户接口
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/user/{id}` - 获取用户信息
- `PUT /api/user/{id}` - 更新用户信息

### 游戏接口
- `GET /api/game/list` - 分页查询游戏列表
- `GET /api/game/{id}` - 获取游戏详情
- `GET /api/game/hot` - 获取热门游戏
- `POST /api/game` - 添加游戏（管理员）
- `PUT /api/game/{id}` - 更新游戏（管理员）
- `DELETE /api/game/{id}` - 删除游戏（管理员）

### WebSocket接口
- `ws://localhost:8080/api/websocket/chat/{roomId}/{userId}` - 聊天室连接

## 数据库表结构

主要数据表:
- `user` - 用户表
- `game` - 游戏表
- `game_category` - 游戏分类表
- `game_collect` - 游戏收藏表
- `game_download` - 游戏下载记录表
- `news` - 资讯表
- `forum_post` - 论坛帖子表
- `post_collect` - 帖子收藏表
- `comment` - 评论表（通用）
- `user_like` - 点赞表（通用）
- `chat_room` - 聊天室表
- `chat_message` - 聊天消息表
- `user_follow` - 用户关注表
- `report` - 举报表
- `announcement` - 公告表
- `banner` - 轮播图表

## 推荐算法说明

### 协同过滤推荐
1. 构建用户-游戏收藏矩阵
2. 计算用户之间的余弦相似度
3. 找出最相似的K个用户
4. 推荐这些用户收藏但目标用户未收藏的游戏
5. 按评分和下载量进行优化排序
6. 数据不足时自动降级为热门游戏推荐
7. 推荐结果缓存以提升性能

### 混合推荐策略
- 60% 基于用户行为的协同过滤推荐
- 40% 热门游戏推荐
- 实时去重，保证推荐结果的多样性和质量

## 开发说明

### 添加新的实体类
1. 在`entity`包中创建实体类
2. 在`mapper`包中创建对应的Mapper接口
3. 在`controller`包中创建Controller
4. 在`service`包中创建Service（如需要）

### WebSocket使用说明
```javascript
// 前端连接示例
const ws = new WebSocket('ws://localhost:8080/api/websocket/chat/1/1001');

ws.onopen = () => {
    console.log('WebSocket连接成功');
};

ws.onmessage = (event) => {
    const message = JSON.parse(event.data);
    console.log('收到消息:', message);
};

ws.send(JSON.stringify({
    type: 'text',
    content: '你好'
}));
```

## 注意事项

1. 首次运行前请确保MySQL和Redis服务已启动
2. 数据库密码请根据实际情况修改
3. JWT密钥建议在生产环境中使用更复杂的字符串
4. WebSocket在生产环境需要配置Nginx反向代理
5. 推荐系统需要一定数量的用户数据才能体现效果

## 后续优化方向

1. 完善推荐算法，增加更多特征维度
2. 添加图片上传功能
3. 集成第三方登录（微信、QQ等）
4. 增加消息队列处理异步任务
5. 添加全文搜索（Elasticsearch）
6. 优化前端性能和用户体验
7. 添加单元测试和集成测试

## 许可证

MIT License

## 联系方式

如有问题，欢迎提Issue或PR。
