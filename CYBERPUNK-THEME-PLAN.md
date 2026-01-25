# 赛博朋克电竞主题 UI 升级计划

## 色彩方案

### 核心色板
```css
/* 背景色系 */
--bg-primary: #0d0d1a;      /* 主背景 - 深紫黑 */
--bg-secondary: #1a1a2e;    /* 次背景 - 深紫 */
--bg-card: rgba(26, 26, 46, 0.85);  /* 卡片背景 - 玻璃态 */
--bg-hover: rgba(0, 255, 245, 0.1); /* 悬停背景 */

/* 霓虹强调色 */
--neon-cyan: #00fff5;       /* 霓虹青 - 主强调 */
--neon-pink: #ff00ff;       /* 霓虹粉 - 次强调 */
--neon-purple: #bd00ff;     /* 霓虹紫 - 第三强调 */
--neon-blue: #00d4ff;       /* 霓虹蓝 */

/* 文字色系 */
--text-primary: #ffffff;    /* 主文字 */
--text-secondary: #a0a0c0;  /* 次文字 */
--text-muted: #6a6a8a;      /* 弱化文字 */

/* 边框与分割 */
--border-color: rgba(0, 255, 245, 0.2);
--border-glow: rgba(0, 255, 245, 0.5);

/* 渐变 */
--gradient-cyber: linear-gradient(135deg, #00fff5 0%, #ff00ff 100%);
--gradient-bg: linear-gradient(180deg, #0d0d1a 0%, #1a1a2e 100%);
```

---

## Phase 1: 基础样式系统

### 1.1 创建 CSS 变量文件
**新建**: `frontend/src/styles/variables.css`

### 1.2 创建主题样式文件
**新建**: `frontend/src/styles/theme.css`

### 1.3 创建动画效果文件
**新建**: `frontend/src/styles/animations.css`

### 1.4 更新入口文件
**修改**: `frontend/src/main.js` - 导入样式文件

---

## Phase 2: 布局组件升级

### 2.1 用户端导航栏
**修改**: `frontend/src/layout/Index.vue`

改造内容:
- Header: 深色玻璃态背景 + 霓虹边框
- Logo: 霓虹发光效果
- 菜单: 悬停霓虹高亮
- 搜索框: 霓虹边框 + 聚焦发光
- 用户区: 霓虹按钮样式
- Footer: 深色背景 + 霓虹分割线

### 2.2 管理端侧边栏
**修改**: `frontend/src/layout/Admin.vue`

改造内容:
- Aside: 深紫背景 + 霓虹边框
- Logo: 霓虹发光
- 菜单项: 霓虹悬停效果
- 激活状态: 霓虹青高亮

---

## Phase 3: 首页改造

### 3.1 首页整体
**修改**: `frontend/src/views/Home.vue`

改造内容:
- 页面背景: 深色渐变
- 轮播图: 霓虹边框 + 扫描线效果
- 公告栏: 玻璃态 + 霓虹图标
- 区块标题: 霓虹下划线
- 卡片: 玻璃态 + 悬停发光

---

## Phase 4: 核心页面升级

### 4.1 登录/注册页
**修改**: `frontend/src/views/Login.vue`
**修改**: `frontend/src/views/Register.vue`

### 4.2 论坛页面
**修改**: `frontend/src/views/Forum.vue`
**修改**: `frontend/src/views/PostDetail.vue`

### 4.3 其他页面
**修改**: `frontend/src/views/Games.vue`
**修改**: `frontend/src/views/News.vue`
**修改**: `frontend/src/views/NewsDetail.vue`

---

## Phase 5: 组件升级

### 5.1 卡片组件
**修改**: `frontend/src/components/GameCard.vue`
**修改**: `frontend/src/components/PostCard.vue`
**修改**: `frontend/src/components/NewsCard.vue`

### 5.2 其他组件
**修改**: `frontend/src/components/CommentSection.vue`
**修改**: `frontend/src/components/Skeleton.vue`

---

## Phase 6: 管理后台升级

### 6.1 Dashboard
**修改**: `frontend/src/views/admin/Dashboard.vue`

### 6.2 管理页面
**修改**: `frontend/src/views/admin/Users.vue`
**修改**: `frontend/src/views/admin/Games.vue`
**修改**: `frontend/src/views/admin/News.vue`
**修改**: `frontend/src/views/admin/Posts.vue`
**修改**: `frontend/src/views/admin/Banners.vue`

---

## 文件清单

### 新建文件 (3个)
```
frontend/src/styles/variables.css
frontend/src/styles/theme.css
frontend/src/styles/animations.css
```

### 修改文件 (18个)
```
frontend/src/main.js
frontend/src/App.vue
frontend/src/layout/Index.vue
frontend/src/layout/Admin.vue
frontend/src/views/Home.vue
frontend/src/views/Login.vue
frontend/src/views/Register.vue
frontend/src/views/Forum.vue
frontend/src/views/PostDetail.vue
frontend/src/views/Games.vue
frontend/src/views/News.vue
frontend/src/views/NewsDetail.vue
frontend/src/components/GameCard.vue
frontend/src/components/PostCard.vue
frontend/src/components/NewsCard.vue
frontend/src/components/CommentSection.vue
frontend/src/components/Skeleton.vue
frontend/src/views/admin/Dashboard.vue
```

---

## 验证方法

完成后运行 `autotest` 验证:
1. 页面正常加载，无样式错误
2. 所有交互功能正常
3. 霓虹效果正确显示
4. 响应式布局正常
