<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="200px">
        <div class="logo">管理后台</div>
        <el-menu :default-active="activeMenu" router background-color="#1a1a2e" text-color="#a0a0c0" active-text-color="#00fff5">
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataBoard /></el-icon>
            <span>数据统计</span>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/games">
            <el-icon><Monitor /></el-icon>
            <span>游戏管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/news">
            <el-icon><Document /></el-icon>
            <span>资讯管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/posts">
            <el-icon><ChatDotRound /></el-icon>
            <span>帖子管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/banners">
            <el-icon><Picture /></el-icon>
            <span>轮播图管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header class="admin-header">
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="user-dropdown">
                <el-avatar :size="32" :src="userInfo.avatar" />
                <span>{{ userInfo.nickname || userInfo.username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="home">返回首页</el-dropdown-item>
                  <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { DataBoard, User, Monitor, Document, ChatDotRound, Picture } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  if (command === 'home') {
    router.push('/home')
  } else if (command === 'logout') {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.push('/login')
    ElMessage.success('退出成功')
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  background: var(--bg-primary);
}

.el-aside {
  background: var(--bg-secondary);
  border-right: 1px solid var(--border-color);
}

.logo {
  height: 64px;
  line-height: 64px;
  text-align: center;
  color: var(--neon-cyan);
  font-size: 18px;
  font-weight: bold;
  text-shadow: 0 0 10px var(--neon-cyan);
  border-bottom: 1px solid var(--border-color);
}

.admin-header {
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  color: var(--text-primary);
}

.el-main {
  background: var(--bg-primary);
}
</style>
