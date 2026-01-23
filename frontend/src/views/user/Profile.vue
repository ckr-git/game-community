<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="user-card">
          <div class="user-info">
            <el-avatar :size="100" :src="userInfo.avatar" />
            <h2>{{ userInfo.nickname || userInfo.username }}</h2>
            <p>@{{ userInfo.username }}</p>
          </div>
          <el-divider />
          <div class="user-stats">
            <div class="stat-item">
              <div class="stat-value">{{ collections.length }}</div>
              <div class="stat-label">收藏</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ posts.length }}</div>
              <div class="stat-label">帖子</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="个人信息" name="info">
              <el-form :model="editForm" label-width="80px" style="max-width: 500px">
                <el-form-item label="昵称">
                  <el-input v-model="editForm.nickname" />
                </el-form-item>
                <el-form-item label="邮箱">
                  <el-input v-model="editForm.email" />
                </el-form-item>
                <el-form-item label="手机">
                  <el-input v-model="editForm.phone" />
                </el-form-item>
                <el-form-item label="性别">
                  <el-radio-group v-model="editForm.gender">
                    <el-radio :label="0">保密</el-radio>
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="2">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="saveProfile">保存修改</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <el-tab-pane label="我的收藏" name="collections">
              <div v-for="item in collections" :key="item.id" class="collection-item">
                <span>游戏ID: {{ item.gameId }}</span>
                <span>{{ formatDate(item.createTime) }}</span>
              </div>
              <el-empty v-if="collections.length === 0" description="暂无收藏" />
            </el-tab-pane>

            <el-tab-pane label="我的帖子" name="posts">
              <div v-for="post in posts" :key="post.id" class="post-item">
                <div class="post-title">{{ post.title }}</div>
                <div class="post-meta">
                  <span>{{ post.viewCount }} 浏览</span>
                  <span>{{ post.commentCount }} 评论</span>
                </div>
              </div>
              <el-empty v-if="posts.length === 0" description="暂无帖子" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { userApi } from '@/api'
import request from '@/api/request'

const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
const activeTab = ref('info')
const collections = ref([])
const posts = ref([])

const editForm = reactive({
  nickname: '',
  email: '',
  phone: '',
  gender: 0
})

const loadUserInfo = async () => {
  if (!userInfo.value.userId) return
  try {
    const res = await userApi.getUserInfo(userInfo.value.userId)
    Object.assign(editForm, res.data)
  } catch (error) {
    console.error(error)
  }
}

const loadCollections = async () => {
  if (!userInfo.value.userId) return
  try {
    const res = await request.get('/interaction/game/collect/list', {
      params: { userId: userInfo.value.userId }
    })
    collections.value = res.data.records || []
  } catch (error) {
    console.error(error)
  }
}

const loadPosts = async () => {
  if (!userInfo.value.userId) return
  try {
    const res = await request.get(`/forum/user/${userInfo.value.userId}`)
    posts.value = res.data.records || []
  } catch (error) {
    console.error(error)
  }
}

const saveProfile = async () => {
  try {
    await userApi.updateUser(userInfo.value.userId, editForm)
    ElMessage.success('保存成功')
    // 更新本地存储
    const newUserInfo = { ...userInfo.value, ...editForm }
    localStorage.setItem('userInfo', JSON.stringify(newUserInfo))
    userInfo.value = newUserInfo
  } catch (error) {
    console.error(error)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString()
}

onMounted(() => {
  loadUserInfo()
  loadCollections()
  loadPosts()
})
</script>

<style scoped>
.profile-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.user-card {
  text-align: center;
}

.user-info h2 {
  margin: 15px 0 5px 0;
}

.user-info p {
  color: #999;
}

.user-stats {
  display: flex;
  justify-content: space-around;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

.stat-label {
  color: #999;
  font-size: 14px;
}

.collection-item, .post-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.post-title {
  font-weight: bold;
}

.post-meta {
  color: #999;
  font-size: 14px;
}

.post-meta span {
  margin-left: 15px;
}
</style>
