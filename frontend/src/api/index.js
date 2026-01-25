import request from './request'

// 用户相关API
export const userApi = {
  // 登录
  login: (data) => request.post('/user/login', data),
  // 注册
  register: (data) => request.post('/user/register', data),
  // 获取用户信息
  getUserInfo: (id) => request.get(`/user/${id}`),
  // 更新用户信息
  updateUser: (id, data) => request.put(`/user/${id}`, data),
  // 获取用户列表（管理员）
  getUserList: (params) => request.get('/user/list', { params })
}

// 游戏相关API
export const gameApi = {
  // 获取游戏列表
  getGameList: (params) => request.get('/game/list', { params }),
  // 获取游戏详情
  getGameDetail: (id) => request.get(`/game/${id}`),
  // 获取热门游戏
  getHotGames: (limit) => request.get('/game/hot', { params: { limit } }),
  // 添加游戏
  addGame: (data) => request.post('/game', data),
  // 更新游戏
  updateGame: (id, data) => request.put(`/game/${id}`, data),
  // 删除游戏
  deleteGame: (id) => request.delete(`/game/${id}`)
}

// 聊天室API
export const chatApi = {
  // 获取聊天室列表
  getRoomList: () => request.get('/chat/rooms'),
  // 获取聊天记录
  getMessages: (roomId, params) => request.get(`/chat/messages/${roomId}`, { params })
}

// 论坛帖子API
export const forumApi = {
  // 获取帖子列表
  getPostList: (params) => request.get('/forum/list', { params }),
  // 获取帖子详情
  getPostDetail: (id) => request.get(`/forum/${id}`),
  // 获取热门帖子
  getHotPosts: (limit) => request.get('/forum/hot', { params: { limit } }),
  // 获取用户帖子
  getUserPosts: (userId, params) => request.get(`/forum/user/${userId}`, { params }),
  // 发布帖子
  createPost: (data) => request.post('/forum', data),
  // 更新帖子
  updatePost: (id, data) => request.put(`/forum/${id}`, data),
  // 删除帖子
  deletePost: (id) => request.delete(`/forum/${id}`),
  // 置顶帖子
  toggleTop: (id) => request.put(`/forum/${id}/top`),
  // 设为热门
  toggleHot: (id) => request.put(`/forum/${id}/hot`)
}

// 资讯API
export const newsApi = {
  // 获取资讯列表
  getNewsList: (params) => request.get('/news/list', { params }),
  // 获取资讯详情
  getNewsDetail: (id) => request.get(`/news/${id}`),
  // 获取最新资讯
  getLatestNews: (limit) => request.get('/news/latest', { params: { limit } }),
  // 添加资讯
  createNews: (data) => request.post('/news', data),
  // 更新资讯
  updateNews: (id, data) => request.put(`/news/${id}`, data),
  // 删除资讯
  deleteNews: (id) => request.delete(`/news/${id}`)
}

// 评论API
export const commentApi = {
  // 获取评论列表 (targetType: 1-游戏 2-资讯 3-帖子)
  getComments: (targetType, targetId, params) =>
    request.get('/comment/list', { params: { targetType, targetId, ...params } }),
  // 发表评论
  createComment: (data) => request.post('/comment', data),
  // 删除评论
  deleteComment: (id) => request.delete(`/comment/${id}`),
  // 获取回复列表
  getReplies: (parentId) => request.get(`/comment/replies/${parentId}`)
}

// 互动API（点赞、收藏）
export const interactionApi = {
  // 点赞 (targetType: 1-游戏 2-资讯 3-帖子 4-评论)
  like: (userId, targetType, targetId) =>
    request.post('/interaction/like', null, { params: { userId, targetType, targetId } }),
  // 取消点赞
  unlike: (userId, targetType, targetId) =>
    request.delete('/interaction/like', { params: { userId, targetType, targetId } }),
  // 检查是否点赞
  checkLiked: (userId, targetType, targetId) =>
    request.get('/interaction/like/check', { params: { userId, targetType, targetId } }),
  // 收藏帖子
  collectPost: (userId, postId) =>
    request.post('/interaction/post/collect', null, { params: { userId, postId } }),
  // 取消收藏帖子
  uncollectPost: (userId, postId) =>
    request.delete('/interaction/post/collect', { params: { userId, postId } }),
  // 收藏游戏
  collectGame: (userId, gameId) =>
    request.post('/interaction/game/collect', null, { params: { userId, gameId } }),
  // 取消收藏游戏
  uncollectGame: (userId, gameId) =>
    request.delete('/interaction/game/collect', { params: { userId, gameId } }),
  // 检查是否收藏游戏
  checkGameCollected: (userId, gameId) =>
    request.get('/interaction/game/collect/check', { params: { userId, gameId } }),
  // 获取用户游戏收藏列表
  getUserGameCollections: (userId, params) =>
    request.get('/interaction/game/collect/list', { params: { userId, ...params } })
}

// 公共API
export const commonApi = {
  // 获取轮播图
  getBanners: () => request.get('/common/banners'),
  // 获取公告
  getAnnouncements: (type) => request.get('/common/announcements', { params: { type } }),
  // 获取分类
  getCategories: () => request.get('/common/categories')
}
