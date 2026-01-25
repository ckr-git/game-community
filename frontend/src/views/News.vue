<template>
  <div class="news-container">
    <el-row :gutter="20">
      <el-col :span="18">
        <el-card v-for="news in newsList" :key="news.id" class="news-card" @click="goToNews(news.id)">
          <div class="news-item">
            <img :src="news.coverImage || '/default-news.jpg'" class="news-cover" />
            <div class="news-content">
              <h3>{{ news.title }}</h3>
              <p>{{ news.content?.substring(0, 150) }}...</p>
              <div class="news-meta">
                <span>{{ formatDate(news.createTime) }}</span>
                <span><el-icon><View /></el-icon> {{ news.viewCount }}</span>
                <span><el-icon><ChatDotRound /></el-icon> {{ news.commentCount }}</span>
              </div>
            </div>
          </div>
        </el-card>

        <div class="pagination">
          <el-pagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="loadNews"
          />
        </div>
      </el-col>

      <el-col :span="6">
        <el-card class="latest-news">
          <template #header>最新资讯</template>
          <div v-for="item in latestNews" :key="item.id" class="latest-item" @click="goToNews(item.id)">
            <div class="latest-title">{{ item.title }}</div>
            <div class="latest-time">{{ formatDate(item.createTime) }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/api/request'
import { View, ChatDotRound } from '@element-plus/icons-vue'

import { useRouter } from 'vue-router'

const router = useRouter()
const newsList = ref([])
const latestNews = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadNews = async () => {
  try {
    const res = await request.get('/news/list', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value }
    })
    newsList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('加载资讯失败', error)
  }
}

const loadLatestNews = async () => {
  try {
    const res = await request.get('/news/latest', { params: { limit: 10 } })
    latestNews.value = res.data
  } catch (error) {
    console.error('加载最新资讯失败', error)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString()
}

const goToNews = (id) => {
  router.push(`/news/${id}`)
}

onMounted(() => {
  loadNews()
  loadLatestNews()
})
</script>

<style scoped>
.news-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.news-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: box-shadow 0.3s;
}

.news-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.news-item {
  display: flex;
  gap: 20px;
}

.news-cover {
  width: 200px;
  height: 130px;
  object-fit: cover;
  border-radius: 8px;
}

.news-content {
  flex: 1;
}

.news-content h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
}

.news-content p {
  color: #666;
  line-height: 1.6;
  margin-bottom: 15px;
}

.news-meta {
  display: flex;
  gap: 20px;
  color: #999;
  font-size: 14px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.latest-item {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}

.latest-item:hover {
  color: #409eff;
}

.latest-title {
  font-size: 14px;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.latest-time {
  font-size: 12px;
  color: #999;
}
</style>
