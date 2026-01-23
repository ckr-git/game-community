<template>
  <div class="games-container">
    <div class="search-bar">
      <el-input v-model="keyword" placeholder="搜索游戏..." prefix-icon="Search" @keyup.enter="loadGames" clearable style="width: 300px" />
      <el-select v-model="categoryId" placeholder="选择分类" clearable @change="loadGames" style="margin-left: 10px">
        <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
      </el-select>
      <el-button type="primary" @click="loadGames" style="margin-left: 10px">搜索</el-button>
    </div>

    <el-row :gutter="20" class="game-list">
      <el-col :span="6" v-for="game in games" :key="game.id">
        <el-card :body-style="{ padding: '0px' }" shadow="hover" class="game-card" @click="goToGame(game.id)">
          <img :src="game.coverImage || '/default-game.jpg'" class="game-cover" />
          <div class="game-info">
            <h3>{{ game.title }}</h3>
            <p class="game-desc">{{ game.description?.substring(0, 50) }}...</p>
            <div class="game-stats">
              <span><el-icon><View /></el-icon> {{ game.viewCount }}</span>
              <span><el-icon><Download /></el-icon> {{ game.downloadCount }}</span>
              <span><el-icon><Star /></el-icon> {{ game.collectCount }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div class="pagination">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="loadGames"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { gameApi } from '@/api'
import request from '@/api/request'
import { View, Download, Star } from '@element-plus/icons-vue'

const router = useRouter()
const games = ref([])
const categories = ref([])
const keyword = ref('')
const categoryId = ref(null)
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)

const loadGames = async () => {
  try {
    const res = await gameApi.getGameList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      categoryId: categoryId.value,
      keyword: keyword.value
    })
    games.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('加载游戏列表失败', error)
  }
}

const loadCategories = async () => {
  try {
    const res = await request.get('/common/categories')
    categories.value = res.data
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const goToGame = (id) => {
  router.push(`/game/${id}`)
}

onMounted(() => {
  loadGames()
  loadCategories()
})
</script>

<style scoped>
.games-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-bar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
}

.game-list {
  margin-top: 20px;
}

.game-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.game-card:hover {
  transform: translateY(-5px);
}

.game-cover {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.game-info {
  padding: 15px;
}

.game-info h3 {
  margin: 0 0 10px 0;
  font-size: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.game-desc {
  color: #666;
  font-size: 14px;
  margin-bottom: 10px;
  height: 40px;
  overflow: hidden;
}

.game-stats {
  display: flex;
  justify-content: space-between;
  color: #909399;
  font-size: 13px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
