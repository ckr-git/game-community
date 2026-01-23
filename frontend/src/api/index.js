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
  updateUser: (id, data) => request.put(`/user/${id}`, data)
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
