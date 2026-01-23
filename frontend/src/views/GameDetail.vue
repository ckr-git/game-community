<template>
  <div class="game-detail-container" v-if="game">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="main-card">
          <div class="game-header">
            <img :src="game.coverImage || '/default-game.jpg'" class="cover" />
            <div class="info">
              <h1>{{ game.title }}</h1>
              <div class="meta">
                <el-tag>{{ game.developer }}</el-tag>
                <span>发布时间: {{ game.releaseDate }}</span>
              </div>
              <div class="stats">
                <span><el-icon><View /></el-icon> {{ game.viewCount }} 浏览</span>
                <span><el-icon><Download /></el-icon> {{ game.downloadCount }} 下载</span>
                <span><el-icon><Star /></el-icon> {{ game.collectCount }} 收藏</span>
              </div>
              <div class="actions">
                <el-button type="primary" @click="handleDownload" v-if="game.downloadUrl">
                  <el-icon><Download /></el-icon> 下载游戏
                </el-button>
                <el-button :type="isCollected ? 'warning' : 'default'" @click="toggleCollect">
                  <el-icon><Star /></el-icon> {{ isCollected ? '取消收藏' : '收藏' }}
                </el-button>
              </div>
            </div>
          </div>
          
          <el-divider />
          
          <div class="description">
            <h3>游戏介绍</h3>
            <p>{{ game.description }}</p>
          </div>
          
          <el-divider />
          
          <div class="tags" v-if="game.tags">
            <h3>标签</h3>
            <el-tag v-for="tag in game.tags.split(',')" :key="tag" style="margin-right: 10px">{{ tag }}</el-tag>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card class="recommend-card">
          <template #header>
            <span>相关推荐</span>
          </template>
          <div v-for="item in relatedGames" :key="item.id" class="recommend-item" @click="goToGame(item.id)">
            <img :src="item.coverImage || '/default-game.jpg'" />
            <div>
              <p>{{ item.title }}</p>
              <span>{{ item.viewCount }} 浏览</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { gameApi } from '@/api'
import request from '@/api/request'
import { View, Download, Star } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const game = ref(null)
const relatedGames = ref([])
const isCollected = ref(false)

const loadGame = async () => {
  try {
    const res = await gameApi.getGameDetail(route.params.id)
    game.value = res.data
    loadRelatedGames()
    checkCollected()
  } catch (error) {
    console.error('加载游戏详情失败', error)
  }
}

const loadRelatedGames = async () => {
  try {
    const res = await gameApi.getHotGames(5)
    relatedGames.value = res.data.filter(g => g.id !== game.value?.id).slice(0, 4)
  } catch (error) {
    console.error('加载相关游戏失败', error)
  }
}

const checkCollected = async () => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  if (!userInfo.userId) return
  
  try {
    const res = await request.get('/interaction/game/collect/check', {
      params: { userId: userInfo.userId, gameId: route.params.id }
    })
    isCollected.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const toggleCollect = async () => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  if (!userInfo.userId) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  try {
    if (isCollected.value) {
      await request.delete('/interaction/game/collect', {
        params: { userId: userInfo.userId, gameId: route.params.id }
      })
      isCollected.value = false
      game.value.collectCount--
      ElMessage.success('取消收藏成功')
    } else {
      await request.post('/interaction/game/collect', null, {
        params: { userId: userInfo.userId, gameId: route.params.id }
      })
      isCollected.value = true
      game.value.collectCount++
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    console.error(error)
  }
}

const handleDownload = () => {
  if (game.value?.downloadUrl) {
    window.open(game.value.downloadUrl, '_blank')
  }
}

const goToGame = (id) => {
  router.push(`/game/${id}`)
}

watch(() => route.params.id, () => {
  if (route.params.id) {
    loadGame()
  }
})

onMounted(() => {
  loadGame()
})
</script>

<style scoped>
.game-detail-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.game-header {
  display: flex;
  gap: 30px;
}

.cover {
  width: 300px;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
}

.info h1 {
  margin: 0 0 15px 0;
}

.meta {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
  color: #666;
}

.stats {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  color: #666;
}

.actions {
  display: flex;
  gap: 10px;
}

.description p {
  line-height: 1.8;
  color: #333;
}

.recommend-item {
  display: flex;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}

.recommend-item:hover {
  background: #f5f5f5;
}

.recommend-item img {
  width: 80px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.recommend-item p {
  margin: 0;
  font-size: 14px;
}

.recommend-item span {
  color: #999;
  font-size: 12px;
}
</style>
