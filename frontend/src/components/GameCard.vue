<template>
  <el-card :body-style="{ padding: '0px' }" shadow="hover" class="game-card" @click="$emit('click')">
    <img :src="game.coverImage || '/default-game.jpg'" class="cover" />
    <div class="info">
      <div class="title">{{ game.title }}</div>
      <div class="meta">
        <span><el-icon><View /></el-icon> {{ game.viewCount || 0 }}</span>
        <span><el-icon><Download /></el-icon> {{ game.downloadCount || 0 }}</span>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { View, Download } from '@element-plus/icons-vue'
defineProps({ game: { type: Object, required: true } })
defineEmits(['click'])
</script>

<style scoped>
.game-card {
  cursor: pointer;
  transition: var(--transition-fast);
}

.game-card:hover {
  transform: translateY(-4px);
}

.cover {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-bottom: 1px solid var(--border-color);
}

.info {
  padding: 12px;
  background: var(--bg-secondary);
}

.title {
  font-weight: bold;
  margin-bottom: 8px;
  color: var(--text-primary);
}

.meta {
  display: flex;
  justify-content: space-between;
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
