<template>
  <div class="banners-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>轮播图管理</span>
          <el-button type="primary" @click="openDialog()">新增轮播图</el-button>
        </div>
      </template>

      <el-table :data="banners" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="图片" width="200">
          <template #default="{ row }">
            <el-image :src="row.imageUrl" style="width: 150px; height: 80px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" @click="toggleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑轮播图' : '新增轮播图'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="图片URL">
          <el-input v-model="form.imageUrl" placeholder="请输入图片URL" />
        </el-form-item>
        <el-form-item label="跳转链接">
          <el-input v-model="form.linkUrl" placeholder="请输入跳转链接" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="100" />
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
import request from '@/api/request'

const loading = ref(false)
const banners = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({ id: null, title: '', imageUrl: '', linkUrl: '', sort: 0 })

const loadBanners = async () => {
  loading.value = true
  try {
    const res = await request.get('/banner/list')
    banners.value = res.data || []
  } catch (error) {
    console.error('加载轮播图失败', error)
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
    form.imageUrl = ''
    form.linkUrl = ''
    form.sort = 0
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!form.imageUrl) {
    ElMessage.warning('请填写图片URL')
    return
  }
  try {
    if (isEdit.value) {
      await request.put(`/banner/${form.id}`, form)
    } else {
      await request.post('/banner', form)
    }
    ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
    dialogVisible.value = false
    loadBanners()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const toggleStatus = async (row) => {
  try {
    await request.put(`/banner/${row.id}`, { status: row.status === 1 ? 0 : 1 })
    row.status = row.status === 1 ? 0 : 1
    ElMessage.success('操作成功')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该轮播图？', '提示', { type: 'warning' })
    .then(async () => {
      await request.delete(`/banner/${row.id}`)
      ElMessage.success('删除成功')
      loadBanners()
    })
    .catch(() => {})
}

onMounted(() => loadBanners())
</script>

<style scoped>
.banners-manage { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }

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

:deep(.el-input__wrapper) {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  box-shadow: none;
}

:deep(.el-input__inner) {
  color: var(--text-primary);
}

:deep(.el-button--primary) {
  background: var(--gradient-btn);
  border: none;
}
</style>
