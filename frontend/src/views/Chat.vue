<template>
  <div class="chat-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="room-list">
          <template #header>聊天室列表</template>
          <div v-for="room in rooms" :key="room.id" 
               :class="['room-item', { active: currentRoom?.id === room.id }]"
               @click="joinRoom(room)">
            <div class="room-name">{{ room.name }}</div>
            <div class="room-info">
              <span>在线: {{ room.memberCount }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="18">
        <el-card class="chat-area" v-if="currentRoom">
          <template #header>
            <div class="chat-header">
              <span>{{ currentRoom.name }}</span>
              <span class="online-count">在线人数: {{ onlineCount }}</span>
            </div>
          </template>
          
          <div class="messages" ref="messagesRef">
            <div v-for="msg in messages" :key="msg.timestamp" 
                 :class="['message', { 'self': msg.userId == userInfo.userId, 'system': msg.type === 'system' }]">
              <template v-if="msg.type === 'system'">
                <div class="system-msg">{{ msg.content }}</div>
              </template>
              <template v-else>
                <div class="msg-header">
                  <span class="nickname">用户{{ msg.userId }}</span>
                  <span class="time">{{ formatTime(msg.timestamp) }}</span>
                </div>
                <div class="msg-content">{{ msg.content }}</div>
              </template>
            </div>
          </div>
          
          <div class="input-area">
            <el-input v-model="inputMessage" placeholder="输入消息..." @keyup.enter="sendMessage" />
            <el-button type="primary" @click="sendMessage">发送</el-button>
          </div>
        </el-card>
        
        <el-empty v-else description="请选择一个聊天室" />
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { chatApi } from '@/api'

const rooms = ref([])
const currentRoom = ref(null)
const messages = ref([])
const inputMessage = ref('')
const onlineCount = ref(0)
const messagesRef = ref(null)
const ws = ref(null)
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

const loadRooms = async () => {
  try {
    const res = await chatApi.getRoomList()
    rooms.value = res.data
  } catch (error) {
    console.error('加载聊天室列表失败', error)
  }
}

const joinRoom = (room) => {
  if (!userInfo.value.userId) {
    ElMessage.warning('请先登录')
    return
  }
  
  // 断开之前的连接
  if (ws.value) {
    ws.value.close()
  }
  
  currentRoom.value = room
  messages.value = []
  
  // 建立WebSocket连接
  const wsUrl = `ws://localhost:8080/api/websocket/chat/${room.id}/${userInfo.value.userId}`
  ws.value = new WebSocket(wsUrl)
  
  ws.value.onopen = () => {
    console.log('WebSocket连接成功')
  }
  
  ws.value.onmessage = (event) => {
    const msg = JSON.parse(event.data)
    messages.value.push(msg)
    scrollToBottom()
  }
  
  ws.value.onerror = (error) => {
    console.error('WebSocket错误', error)
    ElMessage.error('连接失败')
  }
  
  ws.value.onclose = () => {
    console.log('WebSocket连接关闭')
  }
}

const sendMessage = () => {
  if (!inputMessage.value.trim()) return
  if (!ws.value || ws.value.readyState !== WebSocket.OPEN) {
    ElMessage.warning('连接已断开')
    return
  }
  
  ws.value.send(JSON.stringify({
    type: 'text',
    content: inputMessage.value
  }))
  
  inputMessage.value = ''
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}

const formatTime = (timestamp) => {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  return date.toLocaleTimeString()
}

onMounted(() => {
  loadRooms()
})

onUnmounted(() => {
  if (ws.value) {
    ws.value.close()
  }
})
</script>

<style scoped>
.chat-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  height: calc(100vh - 140px);
}

.room-list {
  height: 100%;
}

.room-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: background 0.3s;
}

.room-item:hover, .room-item.active {
  background: #f0f9ff;
}

.room-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.room-info {
  font-size: 12px;
  color: #999;
}

.chat-area {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.online-count {
  font-size: 14px;
  color: #67c23a;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  background: #f5f5f5;
  min-height: 400px;
  max-height: 500px;
}

.message {
  margin-bottom: 15px;
}

.message.self {
  text-align: right;
}

.message.system .system-msg {
  text-align: center;
  color: #999;
  font-size: 12px;
  padding: 5px;
}

.msg-header {
  margin-bottom: 5px;
}

.nickname {
  font-weight: bold;
  color: #409eff;
}

.time {
  font-size: 12px;
  color: #999;
  margin-left: 10px;
}

.msg-content {
  display: inline-block;
  padding: 10px 15px;
  background: white;
  border-radius: 8px;
  max-width: 70%;
  word-break: break-all;
}

.message.self .msg-content {
  background: #409eff;
  color: white;
}

.input-area {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}
</style>
