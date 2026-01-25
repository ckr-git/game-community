<template>
  <div class="post-detail-container">
    <el-row :gutter="20">
      <el-col :span="18">
        <el-card class="post-main" v-loading="loading">
          <div class="post-header">
            <h1 class="post-title">
              <el-tag v-if="post.isTop" type="danger" size="small">置顶</el-tag>
              <el-tag v-if="post.isHot" type="warning" size="small">热门</el-tag>
              {{ post.title }}
            </h1>
            <div class="post-meta">
              <el-avatar :size="40" :src="author.avatar || '/default-avatar.png'" />
              <div class="meta-info">
                <span class="author-name">{{ author.nickname || '用户' + post.authorId }}</span>
                <span class="post-time">{{ formatDate(post.createTime) }}</span>
              </div>
            </div>
          </div>

          <el-divider />

          <div class="post-content" v-html="post.content"></div>

          <el-divider />

          <div class="post-actions">
            <el-button :type="isLiked ? 'primary' : 'default'" @click="handleLike">
              <el-icon><Star /></el-icon>
              {{ isLiked ? '已点赞' : '点赞' }} ({{ post.likeCount || 0 }})
            </el-button>
            <el-button :type="isCollected ? 'warning' : 'default'" @click="handleCollect">
              <el-icon><Collection /></el-icon>
              {{ isCollected ? '已收藏' : '收藏' }} ({{ post.collectCount || 0 }})
            </el-button>
            <span class="view-count">
              <el-icon><View /></el-icon> {{ post.viewCount || 0 }} 浏览
            </span>
          </div>
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
              <div class="comment-content">
                <div class="comment-header">
                  <span class="comment-author">{{ comment.nickname || '用户' }}</span>
                  <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
                </div>
                <div class="comment-text">{{ comment.content }}</div>
                <div class="comment-actions">
                  <el-button link size="small" @click="replyTo(comment)">回复</el-button>
                </div>

                <!-- 回复列表 -->
                <div v-if="comment.replies?.length" class="reply-list">
                  <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                    <span class="reply-author">{{ reply.nickname }}</span>
                    <span v-if="reply.replyToName"> 回复 {{ reply.replyToName }}</span>：
                    <span class="reply-text">{{ reply.content }}</span>
                    <span class="reply-time">{{ formatDate(reply.createTime) }}</span>
                  </div>
                </div>
              </div>
            </div>

            <el-empty v-if="!comments.length" description="暂无评论" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="related-posts">
          <template #header>相关帖子</template>
          <div
            v-for="item in relatedPosts"
            :key="item.id"
            class="related-item"
            @click="goToPost(item.id)"
          >
            <div class="related-title">{{ item.title }}</div>
            <div class="related-stats">{{ item.viewCount }} 浏览</div>
          </div>
          <el-empty v-if="!relatedPosts.length" description="暂无相关帖子" :image-size="60" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 回复对话框 -->
    <el-dialog v-model="showReplyDialog" title="回复评论" width="500px">
      <p>回复 @{{ replyTarget?.nickname }}：</p>
      <el-input v-model="replyContent" type="textarea" :rows="3" placeholder="写下你的回复..." />
      <template #footer>
        <el-button @click="showReplyDialog = false">取消</el-button>
        <el-button type="primary" @click="submitReply">回复</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star, Collection, View } from '@element-plus/icons-vue'
import request from '@/api/request'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const post = ref({})
const author = ref({})
const comments = ref([])
const relatedPosts = ref([])
const isLiked = ref(false)
const isCollected = ref(false)
const newComment = ref('')
const showReplyDialog = ref(false)
const replyTarget = ref(null)
const replyContent = ref('')

const postId = route.params.id

const loadPost = async () => {
  loading.value = true
  try {
    const res = await request.get(`/forum/${postId}`)
    post.value = res.data
    if (post.value.authorId) {
      loadAuthor(post.value.authorId)
    }
  } catch (error) {
    ElMessage.error('加载帖子失败')
  } finally {
    loading.value = false
  }
}

const loadAuthor = async (userId) => {
  try {
    const res = await request.get(`/user/${userId}`)
    author.value = res.data
  } catch (error) {
    console.error('加载作者信息失败', error)
  }
}

const loadComments = async () => {
  try {
    // targetType: 3 = 帖子
    const res = await request.get('/comment/list', { params: { targetType: 3, targetId: postId } })
    comments.value = res.data?.records || res.data || []
  } catch (error) {
    console.error('加载评论失败', error)
  }
}

const loadRelatedPosts = async () => {
  try {
    const res = await request.get('/forum/hot', { params: { limit: 5 } })
    relatedPosts.value = (res.data || []).filter(p => p.id !== parseInt(postId))
  } catch (error) {
    console.error('加载相关帖子失败', error)
  }
}

const checkInteractionStatus = async () => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  if (!userInfo.userId) return

  try {
    // targetType: 3 = 帖子
    const [likeRes, collectRes] = await Promise.all([
      request.get('/interaction/like/check', {
        params: { userId: userInfo.userId, targetType: 3, targetId: postId }
      }),
      request.get('/interaction/post/collect/check', {
        params: { userId: userInfo.userId, postId: postId }
      })
    ])
    isLiked.value = likeRes.data
    isCollected.value = collectRes.data
  } catch (error) {
    console.error('检查互动状态失败', error)
  }
}

const handleLike = async () => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  if (!userInfo.userId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    // targetType: 3 = 帖子
    if (isLiked.value) {
      await request.delete('/interaction/like', {
        params: { userId: userInfo.userId, targetType: 3, targetId: postId }
      })
      post.value.likeCount = (post.value.likeCount || 1) - 1
    } else {
      await request.post('/interaction/like', null, {
        params: { userId: userInfo.userId, targetType: 3, targetId: postId }
      })
      post.value.likeCount = (post.value.likeCount || 0) + 1
    }
    isLiked.value = !isLiked.value
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleCollect = async () => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  if (!userInfo.userId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    if (isCollected.value) {
      await request.delete('/interaction/post/collect', {
        params: { userId: userInfo.userId, postId: postId }
      })
      post.value.collectCount = (post.value.collectCount || 1) - 1
    } else {
      await request.post('/interaction/post/collect', null, {
        params: { userId: userInfo.userId, postId: postId }
      })
      post.value.collectCount = (post.value.collectCount || 0) + 1
    }
    isCollected.value = !isCollected.value
  } catch (error) {
    ElMessage.error('操作失败')
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
      targetType: 3, // 3 = 帖子
      targetId: parseInt(postId),
      content: newComment.value,
      userId: userInfo.userId,
      parentId: 0
    })
    ElMessage.success('评论成功')
    newComment.value = ''
    loadComments()
  } catch (error) {
    ElMessage.error('评论失败')
  }
}

const replyTo = (comment) => {
  replyTarget.value = comment
  replyContent.value = ''
  showReplyDialog.value = true
}

const submitReply = async () => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  if (!userInfo.userId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    await request.post('/comment', {
      targetType: 3,
      targetId: parseInt(postId),
      parentId: replyTarget.value.id,
      content: replyContent.value,
      userId: userInfo.userId
    })
    ElMessage.success('回复成功')
    showReplyDialog.value = false
    loadComments()
  } catch (error) {
    ElMessage.error('回复失败')
  }
}

const goToPost = (id) => {
  router.push(`/forum/${id}`)
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString()
}

onMounted(() => {
  loadPost()
  loadComments()
  loadRelatedPosts()
  checkInteractionStatus()
})
</script>

<style scoped>
.post-detail-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.post-main {
  margin-bottom: 20px;
}

.post-title {
  font-size: 26px;
  margin: 0 0 20px 0;
  color: var(--text-primary);
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.meta-info {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-weight: bold;
  color: var(--neon-cyan);
}

.post-time {
  font-size: 12px;
  color: var(--text-muted);
}

.post-content {
  line-height: 1.8;
  min-height: 200px;
  color: var(--text-secondary);
}

.post-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.view-count {
  color: var(--text-muted);
  font-size: 14px;
  margin-left: auto;
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
  border-bottom: 1px solid var(--border-color);
}

.comment-content {
  flex: 1;
}

.comment-header {
  margin-bottom: 8px;
}

.comment-author {
  font-weight: bold;
  margin-right: 10px;
  color: var(--neon-cyan);
}

.comment-time {
  font-size: 12px;
  color: var(--text-muted);
}

.comment-text {
  line-height: 1.6;
  margin-bottom: 8px;
  color: var(--text-secondary);
}

.reply-list {
  background: var(--bg-secondary);
  padding: 10px;
  border-radius: 4px;
  margin-top: 10px;
}

.reply-item {
  padding: 8px 0;
  font-size: 14px;
}

.reply-author {
  color: var(--neon-pink);
}

.reply-time {
  font-size: 12px;
  color: var(--text-muted);
  margin-left: 10px;
}

.related-item {
  padding: 10px 0;
  border-bottom: 1px solid var(--border-color);
  cursor: pointer;
}

.related-item:hover {
  color: var(--neon-cyan);
}

.related-title {
  font-size: 14px;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: var(--text-primary);
}

.related-stats {
  font-size: 12px;
  color: var(--text-muted);
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
