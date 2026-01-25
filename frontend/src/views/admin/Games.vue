<template>
  <div class="games-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>游戏管理</span>
          <el-button type="primary" @click="showAddDialog">新增游戏</el-button>
        </div>
      </template>
      
      <el-table :data="games" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="游戏名称" />
        <el-table-column prop="developer" label="开发商" />
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column prop="downloadCount" label="下载量" width="100" />
        <el-table-column prop="collectCount" label="收藏数" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="editGame(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteGame(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="loadGames"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑游戏' : '新增游戏'" width="600px">
      <el-form :model="gameForm" label-width="100px">
        <el-form-item label="游戏名称">
          <el-input v-model="gameForm.title" />
        </el-form-item>
        <el-form-item label="封面图URL">
          <el-input v-model="gameForm.coverImage" />
        </el-form-item>
        <el-form-item label="开发商">
          <el-input v-model="gameForm.developer" />
        </el-form-item>
        <el-form-item label="游戏描述">
          <el-input v-model="gameForm.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="下载链接">
          <el-input v-model="gameForm.downloadUrl" />
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="gameForm.tags" placeholder="多个标签用逗号分隔" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitGame">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { gameApi } from '@/api'

const games = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)

const gameForm = reactive({
  id: null,
  title: '',
  coverImage: '',
  developer: '',
  description: '',
  downloadUrl: '',
  tags: ''
})

const loadGames = async () => {
  try {
    const res = await gameApi.getGameList({ pageNum: pageNum.value, pageSize: pageSize.value })
    games.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const showAddDialog = () => {
  isEdit.value = false
  Object.keys(gameForm).forEach(key => gameForm[key] = key === 'id' ? null : '')
  dialogVisible.value = true
}

const editGame = (row) => {
  isEdit.value = true
  Object.assign(gameForm, row)
  dialogVisible.value = true
}

const submitGame = async () => {
  try {
    if (isEdit.value) {
      await gameApi.updateGame(gameForm.id, gameForm)
      ElMessage.success('更新成功')
    } else {
      await gameApi.addGame(gameForm)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadGames()
  } catch (error) {
    console.error(error)
  }
}

const deleteGame = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这个游戏吗？', '提示', { type: 'warning' })
    await gameApi.deleteGame(row.id)
    ElMessage.success('删除成功')
    loadGames()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

onMounted(() => {
  loadGames()
})
</script>

<style scoped>
.games-manage {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

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

:deep(.el-dialog__header) {
  color: var(--neon-pink);
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

:deep(.el-textarea__inner) {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  color: var(--text-primary);
}

:deep(.el-button--primary) {
  background: var(--gradient-btn);
  border: none;
}
</style>
