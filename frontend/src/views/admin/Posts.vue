<template>
  <div class="posts-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>帖子管理</span>
          <el-input v-model="keyword" placeholder="搜索帖子" style="width: 200px" @keyup.enter="loadPosts" clearable />
        </div>
      </template>

      <el-table :data="posts" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="authorId" label="作者ID" width="100" />
        <el-table-column prop="viewCount" label="浏览" width="80" />
        <el-table-column prop="commentCount" label="评论" width="80" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.isTop" type="danger" size="small">置顶</el-tag>
            <el-tag v-if="row.isHot" type="warning" size="small">热门</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button size="small" @click="toggleTop(row)">
              {{ row.isTop ? '取消置顶' : '置顶' }}
            </el-button>
            <el-button size="small" @click="toggleHot(row)">
              {{ row.isHot ? '取消热门' : '热门' }}
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="loadPosts"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { forumApi } from '@/api'

const loading = ref(false)
const posts = ref([])
const keyword = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadPosts = async () => {
  loading.value = true
  try {
    const res = await forumApi.getPostList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyword: keyword.value
    })
    posts.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载帖子失败', error)
  } finally {
    loading.value = false
  }
}

const toggleTop = async (row) => {
  try {
    await forumApi.toggleTop(row.id)
    row.isTop = !row.isTop
    ElMessage.success('操作成功')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const toggleHot = async (row) => {
  try {
    await forumApi.toggleHot(row.id)
    row.isHot = !row.isHot
    ElMessage.success('操作成功')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该帖子？', '提示', { type: 'warning' })
    .then(async () => {
      await forumApi.deletePost(row.id)
      ElMessage.success('删除成功')
      loadPosts()
    })
    .catch(() => {})
}

onMounted(() => loadPosts())
</script>

<style scoped>
.posts-manage { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.pagination { display: flex; justify-content: center; margin-top: 20px; }

:deep(.el-card) {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
}

:deep(.el-card__header) {
  border-bottom: 1px solid var(--border-color);
  color: var(--neon-cyan);
}

:deep(.el-table) {
  background: transparent;
  --el-table-bg-color: transparent;
  --el-table-tr-bg-color: transparent;
  --el-table-header-bg-color: var(--bg-secondary);
  --el-table-row-hover-bg-color: rgba(0, 255, 245, 0.1);
  --el-table-border-color: var(--border-color);
  --el-table-text-color: var(--text-primary);
  --el-table-header-text-color: var(--neon-cyan);
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background: rgba(26, 26, 46, 0.5);
}

:deep(.el-input__wrapper) {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  box-shadow: none;
}

:deep(.el-input__inner) {
  color: var(--text-primary);
}
</style>
