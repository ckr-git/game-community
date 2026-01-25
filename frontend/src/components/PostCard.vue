<template>
  <el-card class="post-card" shadow="hover" @click="$emit('click')">
    <div class="title">
      <el-tag v-if="post.isTop" type="danger" size="small">置顶</el-tag>
      <el-tag v-if="post.isHot" type="warning" size="small">热门</el-tag>
      {{ post.title }}
    </div>
    <div class="content">{{ post.content?.substring(0, 100) }}...</div>
    <div class="meta">
      <span>{{ post.authorName || '用户' + post.authorId }}</span>
      <span><el-icon><View /></el-icon> {{ post.viewCount || 0 }}</span>
      <span><el-icon><ChatDotRound /></el-icon> {{ post.commentCount || 0 }}</span>
    </div>
  </el-card>
</template>

<script setup>
import { View, ChatDotRound } from '@element-plus/icons-vue'
defineProps({ post: { type: Object, required: true } })
defineEmits(['click'])
</script>

<style scoped>
.post-card {
  cursor: pointer;
  margin-bottom: 12px;
  transition: var(--transition-fast);
}

.post-card:hover {
  transform: translateX(4px);
}

.title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  color: var(--text-primary);
}

.content {
  color: var(--text-secondary);
  line-height: 1.6;
  margin-bottom: 12px;
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
  border-color: var(--neon-cyan);
  box-shadow: var(--shadow-neon);
}
</style>
