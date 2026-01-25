<template>
  <div class="skeleton">
    <div v-if="type === 'card'" class="skeleton-card">
      <div class="skeleton-image"></div>
      <div class="skeleton-content">
        <div class="skeleton-title"></div>
        <div class="skeleton-text"></div>
      </div>
    </div>
    <div v-else-if="type === 'list'" class="skeleton-list">
      <div v-for="i in count" :key="i" class="skeleton-item">
        <div class="skeleton-avatar"></div>
        <div class="skeleton-info">
          <div class="skeleton-title"></div>
          <div class="skeleton-text"></div>
        </div>
      </div>
    </div>
    <div v-else class="skeleton-text-block">
      <div v-for="i in count" :key="i" class="skeleton-line"></div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  type: { type: String, default: 'text' },
  count: { type: Number, default: 3 }
})
</script>

<style scoped>
.skeleton-card {
  background: var(--bg-card);
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid var(--border-color);
}

.skeleton-image {
  height: 180px;
  background: linear-gradient(90deg, #1a1a2e 25%, #2a2a4e 50%, #1a1a2e 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.skeleton-content {
  padding: 12px;
}

.skeleton-title {
  height: 20px;
  background: var(--bg-secondary);
  border-radius: 4px;
  margin-bottom: 10px;
  width: 60%;
}

.skeleton-text {
  height: 14px;
  background: var(--bg-secondary);
  border-radius: 4px;
  width: 80%;
}

.skeleton-item {
  display: flex;
  gap: 12px;
  padding: 15px 0;
  border-bottom: 1px solid var(--border-color);
}

.skeleton-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--bg-secondary);
}

.skeleton-info {
  flex: 1;
}

.skeleton-line {
  height: 16px;
  background: var(--bg-secondary);
  border-radius: 4px;
  margin-bottom: 10px;
}

.skeleton-line:last-child {
  width: 60%;
}

@keyframes shimmer {
  0% { background-position: -200% 0; }
  100% { background-position: 200% 0; }
}
</style>
