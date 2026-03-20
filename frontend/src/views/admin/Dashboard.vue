<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card stat-cyan">
          <div class="stat-icon"><el-icon><User /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.userCount }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card stat-pink">
          <div class="stat-icon"><el-icon><Monitor /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.gameCount }}</div>
            <div class="stat-label">游戏总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card stat-purple">
          <div class="stat-icon"><el-icon><Document /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.postCount }}</div>
            <div class="stat-label">帖子总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card stat-green">
          <div class="stat-icon"><el-icon><ChatDotRound /></el-icon></div>
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
import request from '@/api/request'
import { User, Monitor, Document, ChatDotRound } from '@element-plus/icons-vue'

const stats = ref({
  userCount: 0,
  gameCount: 0,
  postCount: 0,
  commentCount: 0
})
const hotGames = ref([])
const chartRef = ref(null)

const loadStats = async () => {
  try {
    const res = await request.get('/admin/stats')
    if (res.data) {
      stats.value = res.data
    }
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

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
    backgroundColor: 'transparent',
    tooltip: { trigger: 'axis' },
    legend: { data: ['用户', '帖子', '评论'], textStyle: { color: '#a0a0c0' } },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月'],
      axisLine: { lineStyle: { color: 'rgba(0,255,245,0.3)' } },
      axisLabel: { color: '#a0a0c0' }
    },
    yAxis: {
      type: 'value',
      axisLine: { lineStyle: { color: 'rgba(0,255,245,0.3)' } },
      axisLabel: { color: '#a0a0c0' },
      splitLine: { lineStyle: { color: 'rgba(0,255,245,0.1)' } }
    },
    series: [
      { name: '用户', type: 'line', data: [20, 32, 45, 58, 72, 95, 128], itemStyle: { color: '#00fff5' } },
      { name: '帖子', type: 'line', data: [50, 82, 120, 180, 230, 290, 342], itemStyle: { color: '#ff00ff' } },
      { name: '评论', type: 'line', data: [100, 220, 380, 520, 750, 980, 1205], itemStyle: { color: '#bd00ff' } }
    ]
  }
  chart.setOption(option)
}

onMounted(() => {
  loadStats()
  loadHotGames()
  initChart()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
}

.stat-cyan { border-left: 3px solid #00fff5; }
.stat-pink { border-left: 3px solid #ff00ff; }
.stat-purple { border-left: 3px solid #bd00ff; }
.stat-green { border-left: 3px solid #00ff88; }

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  margin-right: 15px;
  background: var(--bg-secondary);
}

.stat-cyan .stat-icon { color: #00fff5; }
.stat-pink .stat-icon { color: #ff00ff; }
.stat-purple .stat-icon { color: #bd00ff; }
.stat-green .stat-icon { color: #00ff88; }

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: var(--text-primary);
}

.stat-label {
  color: var(--text-muted);
  font-size: 14px;
}

.hot-game-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid var(--border-color);
}

.rank {
  width: 24px;
  height: 24px;
  border-radius: 4px;
  background: var(--bg-secondary);
  color: var(--neon-cyan);
  text-align: center;
  line-height: 24px;
  font-size: 12px;
  margin-right: 10px;
}

.hot-game-item:nth-child(1) .rank { background: #ff00ff; color: white; }
.hot-game-item:nth-child(2) .rank { background: #bd00ff; color: white; }
.hot-game-item:nth-child(3) .rank { background: #00fff5; color: #0d0d1a; }

.name {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: var(--text-primary);
}

.count {
  color: var(--text-muted);
  font-size: 12px;
}

:deep(.el-card) {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  color: var(--text-primary);
}

:deep(.el-card__header) {
  border-bottom: 1px solid var(--border-color);
  color: var(--neon-cyan);
}
</style>
