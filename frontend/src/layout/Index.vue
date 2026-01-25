<template>
  <div class="layout">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo" @click="goHome">
            <el-icon :size="30"><Monitor /></el-icon>
            <span>游戏社区</span>
          </div>
          
          <el-menu
            :default-active="activeMenu"
            mode="horizontal"
            @select="handleMenuSelect"
            class="nav-menu"
          >
            <el-menu-item index="/home">
              <el-icon><HomeFilled /></el-icon>首页
            </el-menu-item>
            <el-menu-item index="/games">
              <el-icon><Monitor /></el-icon>游戏中心
            </el-menu-item>
            <el-menu-item index="/news">
              <el-icon><Document /></el-icon>资讯
            </el-menu-item>
            <el-menu-item index="/forum">
              <el-icon><ChatDotRound /></el-icon>论坛
            </el-menu-item>
            <el-menu-item index="/chat">
              <el-icon><ChatLineRound /></el-icon>聊天室
            </el-menu-item>
          </el-menu>

          <div class="search-box">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索游戏、帖子..."
              prefix-icon="Search"
              @keyup.enter="handleSearch"
              clearable
              size="default"
            />
          </div>

          <div class="user-area">
            <template v-if="userInfo.userId">
              <el-dropdown @command="handleUserCommand">
                <span class="user-info">
                  <el-avatar :src="userInfo.avatar" :size="35" />
                  <span class="username">{{ userInfo.nickname || userInfo.username }}</span>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                    <el-dropdown-item command="admin" v-if="userInfo.role === 2">管理后台</el-dropdown-item>
                    <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
            <template v-else>
              <el-button @click="goLogin">登录</el-button>
              <el-button type="primary" @click="goRegister">注册</el-button>
            </template>
          </div>
        </div>
      </el-header>

      <el-main class="main-content">
        <router-view />
      </el-main>

      <el-footer class="footer">
        <div>© 2024 游戏社区交流平台 | SpringBoot + Vue3</div>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Monitor, HomeFilled, Document, ChatDotRound, ChatLineRound } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const userInfo = ref({})
const searchKeyword = ref('')
const activeMenu = computed(() => route.path)

const loadUserInfo = () => {
  const storedUserInfo = localStorage.getItem('userInfo')
  if (storedUserInfo) {
    userInfo.value = JSON.parse(storedUserInfo)
  }
}

const handleMenuSelect = (index) => {
  router.push(index)
}

const handleUserCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/user/profile')
      break
    case 'admin':
      router.push('/admin/dashboard')
      break
    case 'logout':
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      userInfo.value = {}
      router.push('/login')
      ElMessage.success('退出登录成功')
      break
  }
}

const goHome = () => {
  router.push('/home')
}

const goLogin = () => {
  router.push('/login')
}

const goRegister = () => {
  router.push('/register')
}

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push(`/games?keyword=${encodeURIComponent(searchKeyword.value)}`)
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg-primary);
}

.header {
  background: rgba(13, 13, 26, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid var(--border-color);
  box-shadow: 0 0 20px rgba(0, 255, 245, 0.1);
  padding: 0;
  height: var(--header-height);
  line-height: var(--header-height);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  font-size: 22px;
  font-weight: bold;
  color: var(--neon-cyan);
  text-shadow: 0 0 10px var(--neon-cyan);
  transition: var(--transition-fast);
}

.logo:hover {
  text-shadow: 0 0 20px var(--neon-cyan), 0 0 30px var(--neon-cyan);
}

.logo .el-icon {
  color: var(--neon-pink);
  filter: drop-shadow(0 0 5px var(--neon-pink));
}

.nav-menu {
  flex: 1;
  border: none;
  margin: 0 20px;
  background: transparent !important;
}

.nav-menu :deep(.el-menu-item) {
  color: var(--text-secondary);
  border-bottom: none !important;
  transition: var(--transition-fast);
}

.nav-menu :deep(.el-menu-item:hover) {
  color: var(--neon-cyan);
  background: var(--bg-hover) !important;
}

.nav-menu :deep(.el-menu-item.is-active) {
  color: var(--neon-cyan) !important;
  border-bottom: 2px solid var(--neon-cyan) !important;
  text-shadow: 0 0 10px var(--neon-cyan);
}

.search-box {
  width: 220px;
  margin-right: 20px;
}

.search-box :deep(.el-input__wrapper) {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  box-shadow: none;
}

.search-box :deep(.el-input__wrapper:hover),
.search-box :deep(.el-input__wrapper.is-focus) {
  border-color: var(--neon-cyan);
  box-shadow: 0 0 10px rgba(0, 255, 245, 0.3);
}

.search-box :deep(.el-input__inner) {
  color: var(--text-primary);
}

.search-box :deep(.el-input__inner::placeholder) {
  color: var(--text-muted);
}

.user-area {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-area :deep(.el-button) {
  border: 1px solid var(--border-color);
  background: transparent;
  color: var(--text-secondary);
}

.user-area :deep(.el-button:hover) {
  border-color: var(--neon-cyan);
  color: var(--neon-cyan);
}

.user-area :deep(.el-button--primary) {
  background: var(--gradient-btn);
  border: none;
  color: var(--bg-primary);
}

.user-area :deep(.el-button--primary:hover) {
  box-shadow: var(--shadow-neon);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.username {
  color: var(--text-primary);
}

.main-content {
  flex: 1;
  padding: 0;
  background: var(--bg-primary);
}

.footer {
  background: var(--bg-secondary);
  border-top: 1px solid var(--border-color);
  color: var(--text-secondary);
  text-align: center;
  padding: 20px;
}
</style>
