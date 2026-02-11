<template>
  <div class="project-page">
    <div class="toolbar">
      <el-form :inline="true" :model="filters">
        <el-form-item label="项目状态">
          <el-select v-model="filters.status" placeholder="全部" clearable>
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已结束" value="FINISHED" />
            <el-option label="已结算" value="SETTLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSearch">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="goCreate">创建项目</el-button>
    </div>
    <el-table :data="tableData" v-loading="loading" style="width: 100%">
      <el-table-column prop="projectCode" label="项目编号" min-width="160" />
      <el-table-column prop="projectName" label="项目名称" min-width="200" />
      <el-table-column label="分配方式" min-width="120">
        <template #default="{ row }">
          {{ allocationMethodMap[row.allocationMethod] || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" min-width="100">
        <template #default="{ row }">
          {{ statusMap[row.status] || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="项目负责人" min-width="120">
        <template #default="{ row }">
          {{ row.manager?.realName || row.manager?.username || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" min-width="160">
        <template #default="{ row }">
          {{ formatTime(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="140">
        <template #default="{ row }">
          <el-button type="primary" link @click="goDetail(row.id)">查看详情</el-button>
          <el-button type="primary" link disabled>编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pager">
      <el-pagination
        background
        layout="total, prev, pager, next"
        :total="total"
        :page-size="pagination.size"
        :current-page="pagination.page"
        @current-change="onPageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import { fetchProjectPage } from '@/api/project.js'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const filters = reactive({
  status: ''
})
const pagination = reactive({
  page: 1,
  size: 10
})

const statusMap = {
  IN_PROGRESS: '进行中',
  FINISHED: '已结束',
  SETTLED: '已结算'
}

const allocationMethodMap = {
  BY_POSITION: '按岗位切分',
  SHARED: '共同分配'
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size
    }
    if (filters.status) {
      params.status = filters.status
    }
    const resp = await fetchProjectPage(params)
    tableData.value = resp.data.records || []
    total.value = resp.data.total || 0
  } catch (err) {
  } finally {
    loading.value = false
  }
}

const onSearch = () => {
  pagination.page = 1
  fetchData()
}

const onPageChange = (page) => {
  pagination.page = page
  fetchData()
}

const goCreate = () => {
  router.push('/projects/create')
}

const goDetail = (id) => {
  router.push(`/projects/${id}`)
}

const formatTime = (value) => {
  if (!value) return '-'
  return dayjs(value).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.project-page {
  padding: 24px;
}

.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  gap: 12px;
  flex-wrap: wrap;
}

.pager {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
