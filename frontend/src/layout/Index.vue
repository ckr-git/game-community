<template>
  <div class="layout">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo" @click="goHome">
            <el-icon :size="30"><GameController /></el-icon>
            <span>游戏社区</span>
          </div>
          
          <el-menu
            :default-active="activeMenu"
            mode="horizontal"
            @select="handleMenuSelect"
            class="nav-menu"
          >
            <el-menu-item index="/home">首页</el-menu-item>
            <el-menu-item index="/games">游戏中心</el-menu-item>
            <el-menu-item index="/news">资讯</el-menu-item>
            <el-menu-item index="/forum">论坛</el-menu-item>
            <el-menu-item index="/chat">聊天室</el-menu-item>
          </el-menu>

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
import { GameController } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const userInfo = ref({})
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

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  height: 60px;
  line-height: 60px;
}

.header-content {
  max-width: 1200px;
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
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}

.nav-menu {
  flex: 1;
  border: none;
  margin: 0 30px;
}

.user-area {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.username {
  color: #303133;
}

.main-content {
  flex: 1;
  padding: 0;
}

.footer {
  background: #2c3e50;
  color: white;
  text-align: center;
  padding: 20px;
}
</style>
