<template>
  <div class="news-detail-container">
    <el-row :gutter="20">
      <el-col :span="18">
        <el-card class="news-main" v-loading="loading">
          <div class="news-header">
            <h1 class="news-title">{{ news.title }}</h1>
            <div class="news-meta">
              <span>{{ formatDate(news.createTime) }}</span>
              <span><el-icon><View /></el-icon> {{ news.viewCount || 0 }} 浏览</span>
              <span v-if="news.gameName">
                <el-tag size="small">{{ news.gameName }}</el-tag>
              </span>
            </div>
          </div>

          <img v-if="news.coverImage" :src="news.coverImage" class="news-cover" />

          <el-divider />

          <div class="news-content" v-html="news.content"></div>
        </el-card>

        <!-- 评论区 -->
        <el-card class="comment-section">
          <template #header>
            <span>评论 ({{ comments.length }})</span>
          </template>

          <div class="comment-input">
            <el-input
              v-model="newComment"
              type="textarea"
              :rows="3"
              placeholder="写下你的评论..."
            />
            <el-button type="primary" @click="submitComment" :disabled="!newComment.trim()">
              发表评论
            </el-button>
          </div>

          <div class="comment-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <el-avatar :size="36" :src="comment.userAvatar || '/default-avatar.png'" />
              <div class="comment-content-wrap">
                <div class="comment-header">
                  <span class="comment-author">{{ comment.nickname || '用户' }}</span>
                  <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
                </div>
                <div class="comment-text">{{ comment.content }}</div>
              </div>
            </div>
            <el-empty v-if="!comments.length" description="暂无评论" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="latest-news">
          <template #header>最新资讯</template>
          <div
            v-for="item in latestNews"
            :key="item.id"
            class="latest-item"
            @click="goToNews(item.id)"
          >
            <div class="latest-title">{{ item.title }}</div>
            <div class="latest-time">{{ formatDate(item.createTime) }}</div>
          </div>
          <el-empty v-if="!latestNews.length" description="暂无资讯" :image-size="60" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { View } from '@element-plus/icons-vue'
import request from '@/api/request'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const news = ref({})
const comments = ref([])
const latestNews = ref([])
const newComment = ref('')

const newsId = route.params.id

const loadNews = async () => {
  loading.value = true
  try {
    const res = await request.get(`/news/${newsId}`)
    news.value = res.data
  } catch (error) {
    ElMessage.error('加载资讯失败')
  } finally {
    loading.value = false
  }
}

const loadComments = async () => {
  try {
    const res = await request.get(`/comment/news/${newsId}`)
    comments.value = res.data || []
  } catch (error) {
    console.error('加载评论失败', error)
  }
}

const loadLatestNews = async () => {
  try {
    const res = await request.get('/news/latest', { params: { limit: 10 } })
    latestNews.value = (res.data || []).filter(n => n.id !== parseInt(newsId))
  } catch (error) {
    console.error('加载最新资讯失败', error)
  }
}

const submitComment = async () => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  if (!userInfo.userId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    await request.post('/comment', {
      targetType: 'news',
      targetId: newsId,
      content: newComment.value,
      userId: userInfo.userId
    })
    ElMessage.success('评论成功')
    newComment.value = ''
    loadComments()
  } catch (error) {
    ElMessage.error('评论失败')
  }
}

const goToNews = (id) => {
  router.push(`/news/${id}`)
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString()
}

onMounted(() => {
  loadNews()
  loadComments()
  loadLatestNews()
})
</script>

<style scoped>
.news-detail-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.news-main {
  margin-bottom: 20px;
}

.news-title {
  font-size: 28px;
  margin: 0 0 15px 0;
}

.news-meta {
  display: flex;
  gap: 20px;
  color: #999;
  font-size: 14px;
}

.news-cover {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 8px;
  margin: 20px 0;
}

.news-content {
  line-height: 1.8;
  font-size: 16px;
}

.comment-section {
  margin-bottom: 20px;
}

.comment-input {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 20px;
}

.comment-input .el-button {
  align-self: flex-end;
}

.comment-item {
  display: flex;
  gap: 12px;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.comment-content-wrap {
  flex: 1;
}

.comment-header {
  margin-bottom: 8px;
}

.comment-author {
  font-weight: bold;
  margin-right: 10px;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-text {
  line-height: 1.6;
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
