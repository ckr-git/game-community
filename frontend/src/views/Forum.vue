<template>
  <div class="forum-container">
    <div class="forum-header">
      <el-input v-model="keyword" placeholder="搜索帖子..." prefix-icon="Search" @keyup.enter="loadPosts" clearable style="width: 300px" />
      <el-button type="primary" @click="showPostDialog = true">发布帖子</el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="18">
        <el-card v-for="post in posts" :key="post.id" class="post-card" @click="goToPost(post.id)">
          <div class="post-title">
            <el-tag v-if="post.isTop" type="danger" size="small">置顶</el-tag>
            <el-tag v-if="post.isHot" type="warning" size="small">热门</el-tag>
            {{ post.title }}
          </div>
          <div class="post-content">{{ post.content?.substring(0, 100) }}...</div>
          <div class="post-footer">
            <span>用户{{ post.authorId }}</span>
            <span><el-icon><View /></el-icon> {{ post.viewCount }}</span>
            <span><el-icon><ChatDotRound /></el-icon> {{ post.commentCount }}</span>
            <span><el-icon><Star /></el-icon> {{ post.collectCount }}</span>
          </div>
        </el-card>

        <div class="pagination">
          <el-pagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="loadPosts"
          />
        </div>
      </el-col>

      <el-col :span="6">
        <el-card class="hot-posts">
          <template #header>热门帖子</template>
          <div v-for="post in hotPosts" :key="post.id" class="hot-post-item" @click="goToPost(post.id)">
            <div class="hot-post-title">{{ post.title }}</div>
            <div class="hot-post-stats">{{ post.viewCount }} 浏览</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="showPostDialog" title="发布帖子" width="600px">
      <el-form :model="newPost" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="newPost.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="newPost.content" type="textarea" :rows="6" placeholder="请输入帖子内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPostDialog = false">取消</el-button>
        <el-button type="primary" @click="submitPost">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/api/request'
import { View, ChatDotRound, Star } from '@element-plus/icons-vue'

const router = useRouter()
const posts = ref([])
const hotPosts = ref([])
const keyword = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showPostDialog = ref(false)
const newPost = reactive({ title: '', content: '' })

const loadPosts = async () => {
  try {
    const res = await request.get('/forum/list', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value, keyword: keyword.value }
    })
    posts.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('加载帖子列表失败', error)
  }
}

const loadHotPosts = async () => {
  try {
    const res = await request.get('/forum/hot', { params: { limit: 10 } })
    hotPosts.value = res.data
  } catch (error) {
    console.error('加载热门帖子失败', error)
  }
}

const submitPost = async () => {
  if (!newPost.title || !newPost.content) {
    ElMessage.warning('请填写完整内容')
    return
  }
  
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  if (!userInfo.userId) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    await request.post('/forum', { ...newPost, authorId: userInfo.userId })
    ElMessage.success('发布成功')
    showPostDialog.value = false
    newPost.title = ''
    newPost.content = ''
    loadPosts()
  } catch (error) {
    console.error('发布失败', error)
  }
}

const goToPost = (id) => {
  router.push(`/forum/${id}`)
}

onMounted(() => {
  loadPosts()
  loadHotPosts()
})
</script>

<style scoped>
.forum-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.forum-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
}

.post-card {
  margin-bottom: 15px;
  cursor: pointer;
  transition: var(--transition-fast);
}

.post-card:hover {
  box-shadow: 0 0 15px rgba(0, 255, 245, 0.2);
}

.post-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
  color: var(--text-primary);
}

.post-content {
  color: var(--text-secondary);
  margin-bottom: 15px;
  line-height: 1.6;
}

.post-footer {
  display: flex;
  gap: 20px;
  color: var(--text-muted);
  font-size: 14px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.hot-post-item {
  padding: 10px 0;
  border-bottom: 1px solid var(--border-color);
  cursor: pointer;
}

.hot-post-item:hover {
  color: var(--neon-cyan);
}

.hot-post-title {
  font-size: 14px;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: var(--text-primary);
}

.hot-post-stats {
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

.forum-header :deep(.el-input__wrapper) {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  box-shadow: none;
}

.forum-header :deep(.el-input__inner) {
  color: var(--text-primary);
}

.forum-header :deep(.el-button--primary) {
  background: var(--gradient-btn);
  border: none;
}
</style>
