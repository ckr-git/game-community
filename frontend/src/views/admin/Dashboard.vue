<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #409eff"><el-icon><User /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.userCount }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #67c23a"><el-icon><GameController /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.gameCount }}</div>
            <div class="stat-label">游戏总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #e6a23c"><el-icon><Document /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.postCount }}</div>
            <div class="stat-label">帖子总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #f56c6c"><el-icon><ChatDotRound /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.commentCount }}</div>
            <div class="stat-label">评论总数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card>
          <template #header>数据趋势</template>
          <div ref="chartRef" style="height: 400px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>热门游戏</template>
          <div v-for="(game, index) in hotGames" :key="game.id" class="hot-game-item">
            <span class="rank">{{ index + 1 }}</span>
            <span class="name">{{ game.title }}</span>
            <span class="count">{{ game.viewCount }} 浏览</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { gameApi } from '@/api'
import { User, GameController, Document, ChatDotRound } from '@element-plus/icons-vue'

const stats = ref({
  userCount: 128,
  gameCount: 56,
  postCount: 342,
  commentCount: 1205
})
const hotGames = ref([])
const chartRef = ref(null)

const loadHotGames = async () => {
  try {
    const res = await gameApi.getHotGames(5)
    hotGames.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const initChart = () => {
  const chart = echarts.init(chartRef.value)
  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: ['用户', '帖子', '评论'] },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月']
    },
    yAxis: { type: 'value' },
    series: [
      { name: '用户', type: 'line', data: [20, 32, 45, 58, 72, 95, 128] },
      { name: '帖子', type: 'line', data: [50, 82, 120, 180, 230, 290, 342] },
      { name: '评论', type: 'line', data: [100, 220, 380, 520, 750, 980, 1205] }
    ]
  }
  chart.setOption(option)
}

onMounted(() => {
  loadHotGames()
  initChart()
})
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  margin-right: 15px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
}

.stat-label {
  color: #999;
  font-size: 14px;
}

.hot-game-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}

.rank {
  width: 24px;
  height: 24px;
  border-radius: 4px;
  background: #409eff;
  color: white;
  text-align: center;
  line-height: 24px;
  font-size: 12px;
  margin-right: 10px;
}

.hot-game-item:nth-child(1) .rank { background: #f56c6c; }
.hot-game-item:nth-child(2) .rank { background: #e6a23c; }
.hot-game-item:nth-child(3) .rank { background: #67c23a; }

.name {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.count {
  color: #999;
  font-size: 12px;
}
</style>
