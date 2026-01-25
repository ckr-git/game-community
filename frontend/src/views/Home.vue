<template>
  <div class="home">
    <el-carousel height="400px" class="banner">
      <el-carousel-item v-for="item in banners" :key="item.id">
        <div class="banner-item" :style="getBannerStyle(item)">
          <h2>{{ item.title }}</h2>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- 公告滚动条 -->
    <div class="announcement-bar" v-if="announcements.length">
      <el-icon><Bell /></el-icon>
      <div class="announcement-content">
        <span v-for="(ann, index) in announcements" :key="ann.id">
          {{ ann.content }}
          <span v-if="index < announcements.length - 1" class="separator">|</span>
        </span>
      </div>
    </div>

    <div class="container">
      <el-row :gutter="20">
        <el-col :span="16">
          <div class="section">
            <h2>热门游戏</h2>
            <el-row :gutter="20">
              <el-col :span="8" v-for="game in hotGames" :key="game.id">
                <el-card :body-style="{ padding: '0px' }" shadow="hover" @click="goToGame(game.id)">
                  <img :src="game.coverImage || '/default-game.jpg'" class="game-cover" />
                  <div style="padding: 14px">
                    <span>{{ game.title }}</span>
                    <div class="game-info">
                      <span><el-icon><View /></el-icon> {{ game.viewCount }}</span>
                      <span><el-icon><Download /></el-icon> {{ game.downloadCount }}</span>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <div class="section">
            <h2>最新资讯</h2>
            <el-timeline>
              <el-timeline-item v-for="news in newsList" :key="news.id" :timestamp="news.createTime">
                {{ news.title }}
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="section">
            <h2>热门帖子</h2>
            <el-card v-for="post in hotPosts" :key="post.id" class="post-card">
              <div class="post-title">{{ post.title }}</div>
              <div class="post-meta">
                <span><el-icon><View /></el-icon> {{ post.viewCount }}</span>
                <span><el-icon><ChatDotRound /></el-icon> {{ post.commentCount }}</span>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { gameApi, commonApi } from '@/api'
import request from '@/api/request'
import { View, Download, ChatDotRound, Bell } from '@element-plus/icons-vue'

const router = useRouter()
const hotGames = ref([])
const newsList = ref([])
const hotPosts = ref([])
const announcements = ref([])
const banners = ref([
  { id: 1, title: '热门游戏推荐', imageUrl: '', color: '#00fff5' },
  { id: 2, title: '新游戏上线', imageUrl: '', color: '#ff00ff' },
  { id: 3, title: '社区活动', imageUrl: '', color: '#bd00ff' }
])

const loadHotGames = async () => {
  try {
    const res = await gameApi.getHotGames(6)
    hotGames.value = res.data
  } catch (error) {
    console.error('加载热门游戏失败', error)
  }
}

const goToGame = (id) => {
  router.push(`/game/${id}`)
}

const getBannerStyle = (item) => {
  if (item.imageUrl) {
    return { backgroundImage: `url(${item.imageUrl})`, backgroundSize: 'cover' }
  }
  return { background: `linear-gradient(135deg, ${item.color} 0%, ${item.color}99 100%)` }
}

const loadBanners = async () => {
  try {
    const res = await request.get('/common/banners')
    if (res.data?.length) {
      banners.value = res.data
    }
  } catch (error) {
    console.error('加载轮播图失败', error)
  }
}

const loadAnnouncements = async () => {
  try {
    const res = await request.get('/common/announcements')
    announcements.value = res.data || []
  } catch (error) {
    console.error('加载公告失败', error)
  }
}

const loadNews = async () => {
  try {
    const res = await request.get('/news/latest', { params: { limit: 5 } })
    newsList.value = res.data || []
  } catch (error) {
    console.error('加载资讯失败', error)
  }
}

const loadHotPosts = async () => {
  try {
    const res = await request.get('/forum/hot', { params: { limit: 5 } })
    hotPosts.value = res.data || []
  } catch (error) {
    console.error('加载热门帖子失败', error)
  }
}

onMounted(() => {
  loadHotGames()
  loadBanners()
  loadAnnouncements()
  loadNews()
  loadHotPosts()
})
</script>

<style scoped>
.home {
  min-height: 100vh;
  background: var(--bg-primary);
}

.banner {
  border-bottom: 1px solid var(--border-color);
}

.banner-item {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.banner-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(180deg, transparent 0%, rgba(13,13,26,0.8) 100%);
}

.banner-item h2 {
  color: white;
  font-size: 42px;
  text-shadow: 0 0 20px var(--neon-cyan), 0 0 40px var(--neon-cyan);
  position: relative;
  z-index: 1;
}

.announcement-bar {
  max-width: 1400px;
  margin: 20px auto;
  padding: 12px 20px;
  background: var(--bg-card);
  backdrop-filter: blur(10px);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  display: flex;
  align-items: center;
  gap: 12px;
}

.announcement-bar .el-icon {
  color: var(--neon-pink);
  font-size: 20px;
  filter: drop-shadow(0 0 5px var(--neon-pink));
}

.announcement-content {
  flex: 1;
  overflow: hidden;
  white-space: nowrap;
  color: var(--text-secondary);
}

.announcement-content .separator {
  margin: 0 20px;
  color: var(--border-color);
}

.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.section {
  margin-bottom: 30px;
  background: var(--bg-card);
  backdrop-filter: blur(10px);
  border: 1px solid var(--border-color);
  padding: 24px;
  border-radius: var(--border-radius-lg);
}

.section h2 {
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid var(--neon-cyan);
  color: var(--text-primary);
  text-shadow: 0 0 10px rgba(0, 255, 245, 0.3);
}

.game-cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
  cursor: pointer;
}

.game-info {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  color: var(--text-muted);
  font-size: 14px;
}

.section :deep(.el-card) {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  color: var(--text-primary);
}

.section :deep(.el-card:hover) {
  border-color: var(--neon-cyan);
  box-shadow: 0 0 15px rgba(0, 255, 245, 0.2);
}

.post-card {
  margin-bottom: 12px;
  cursor: pointer;
}

.post-title {
  font-size: 16px;
  margin-bottom: 10px;
  color: var(--text-primary);
}

.post-meta {
  display: flex;
  gap: 15px;
  color: var(--text-muted);
  font-size: 14px;
}

.section :deep(.el-timeline-item__content) {
  color: var(--text-secondary);
}

.section :deep(.el-timeline-item__timestamp) {
  color: var(--text-muted);
}
</style>
