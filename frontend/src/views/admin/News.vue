<template>
  <div class="news-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>资讯管理</span>
          <el-button type="primary" @click="openDialog()">新增资讯</el-button>
        </div>
      </template>

      <el-table :data="newsList" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column prop="createTime" label="发布时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
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
          @current-change="loadNews"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑资讯' : '新增资讯'" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="封面图">
          <el-input v-model="form.coverImage" placeholder="请输入封面图URL" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="8" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { newsApi } from '@/api'

const loading = ref(false)
const newsList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({ id: null, title: '', coverImage: '', content: '' })

const loadNews = async () => {
  loading.value = true
  try {
    const res = await newsApi.getNewsList({ pageNum: pageNum.value, pageSize: pageSize.value })
    newsList.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载资讯失败', error)
  } finally {
    loading.value = false
  }
}

const openDialog = (row = null) => {
  isEdit.value = !!row
  if (row) {
    Object.assign(form, row)
  } else {
    form.id = null
    form.title = ''
    form.coverImage = ''
    form.content = ''
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!form.title || !form.content) {
    ElMessage.warning('请填写完整信息')
    return
  }
  try {
    if (isEdit.value) {
      await newsApi.updateNews(form.id, form)
    } else {
      await newsApi.createNews(form)
    }
    ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
    dialogVisible.value = false
    loadNews()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该资讯？', '提示', { type: 'warning' })
    .then(async () => {
      await newsApi.deleteNews(row.id)
      ElMessage.success('删除成功')
      loadNews()
    })
    .catch(() => {})
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString()
}

onMounted(() => loadNews())
</script>

<style scoped>
.news-manage { padding: 20px; }
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

:deep(.el-dialog) {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
}

:deep(.el-dialog__title) {
  color: var(--neon-pink);
}

:deep(.el-form-item__label) {
  color: var(--text-secondary);
}

:deep(.el-input__wrapper),
:deep(.el-textarea__inner) {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  box-shadow: none;
  color: var(--text-primary);
}

:deep(.el-button--primary) {
  background: var(--gradient-btn);
  border: none;
}
</style>
