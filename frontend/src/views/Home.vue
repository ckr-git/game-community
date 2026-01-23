<template>
  <div class="home">
    <el-carousel height="400px" class="banner">
      <el-carousel-item v-for="item in banners" :key="item.id">
        <img :src="item.imageUrl" :alt="item.title" />
      </el-carousel-item>
    </el-carousel>

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
import { gameApi } from '@/api'
import { View, Download, ChatDotRound } from '@element-plus/icons-vue'

const router = useRouter()
const hotGames = ref([])
const newsList = ref([])
const hotPosts = ref([])
const banners = ref([
  { id: 1, title: '轮播图1', imageUrl: 'https://via.placeholder.com/1200x400' },
  { id: 2, title: '轮播图2', imageUrl: 'https://via.placeholder.com/1200x400' },
  { id: 3, title: '轮播图3', imageUrl: 'https://via.placeholder.com/1200x400' }
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

onMounted(() => {
  loadHotGames()
})
</script>

<style scoped>
.home {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.banner img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.section {
  margin-bottom: 30px;
  background: white;
  padding: 20px;
  border-radius: 8px;
}

.section h2 {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #409eff;
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
  color: #909399;
  font-size: 14px;
}

.post-card {
  margin-bottom: 10px;
  cursor: pointer;
}

.post-title {
  font-size: 16px;
  margin-bottom: 10px;
}

.post-meta {
  display: flex;
  gap: 15px;
  color: #909399;
  font-size: 14px;
}
</style>
