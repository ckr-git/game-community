<template>
  <el-card class="news-card" shadow="hover" @click="$emit('click')">
    <div class="news-item">
      <img :src="news.coverImage || '/default-news.jpg'" class="cover" />
      <div class="info">
        <div class="title">{{ news.title }}</div>
        <div class="content">{{ news.content?.substring(0, 100) }}...</div>
        <div class="meta">
          <span>{{ formatDate(news.createTime) }}</span>
          <span><el-icon><View /></el-icon> {{ news.viewCount || 0 }}</span>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { View } from '@element-plus/icons-vue'
defineProps({ news: { type: Object, required: true } })
defineEmits(['click'])
const formatDate = (d) => d ? new Date(d).toLocaleDateString() : ''
</script>

<style scoped>
.news-card {
  cursor: pointer;
  margin-bottom: 15px;
  transition: var(--transition-fast);
}

.news-card:hover {
  transform: translateX(4px);
}

.news-item {
  display: flex;
  gap: 15px;
}

.cover {
  width: 180px;
  height: 110px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid var(--border-color);
}

.info {
  flex: 1;
}

.title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
  color: var(--text-primary);
}

.content {
  color: var(--text-secondary);
  line-height: 1.5;
  margin-bottom: 10px;
  font-size: 14px;
}

.meta {
  display: flex;
  gap: 20px;
  color: var(--text-muted);
  font-size: 13px;
}

:deep(.el-card) {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
}

:deep(.el-card:hover) {
  border-color: var(--neon-pink);
  box-shadow: var(--shadow-pink);
}
</style>
