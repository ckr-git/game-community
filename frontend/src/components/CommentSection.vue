<template>
  <div class="comment-section">
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
        <div class="comment-content">
          <div class="comment-header">
            <span class="author">{{ comment.nickname || '用户' }}</span>
            <span class="time">{{ formatDate(comment.createTime) }}</span>
          </div>
          <div class="text">{{ comment.content }}</div>
          <div class="actions">
            <el-button link size="small" @click="$emit('reply', comment)">回复</el-button>
          </div>
        </div>
      </div>
      <el-empty v-if="!comments.length" description="暂无评论" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

defineProps({
  comments: { type: Array, default: () => [] }
})

const emit = defineEmits(['submit', 'reply'])
const newComment = ref('')

const submitComment = () => {
  emit('submit', newComment.value)
  newComment.value = ''
}

const formatDate = (d) => d ? new Date(d).toLocaleString() : ''
</script>

<style scoped>
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
  border-bottom: 1px solid var(--border-color);
}

.comment-content {
  flex: 1;
}

.comment-header {
  margin-bottom: 8px;
}

.author {
  font-weight: bold;
  margin-right: 10px;
  color: var(--neon-cyan);
}

.time {
  font-size: 12px;
  color: var(--text-muted);
}

.text {
  line-height: 1.6;
  color: var(--text-secondary);
}

:deep(.el-textarea__inner) {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  color: var(--text-primary);
}

:deep(.el-textarea__inner:focus) {
  border-color: var(--neon-cyan);
}

:deep(.el-button--primary) {
  background: var(--gradient-btn);
  border: none;
}
</style>
