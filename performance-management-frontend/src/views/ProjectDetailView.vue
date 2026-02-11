<template>
  <div class="project-detail">
    <el-card>
      <template #header>
        <div class="header">
          <span>项目详情</span>
          <el-button type="primary" link @click="goBack">返回列表</el-button>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="项目名称">{{ detail.projectName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="项目编号">{{ detail.projectCode || '-' }}</el-descriptions-item>
        <el-descriptions-item label="项目类型">{{ projectTypeMap[detail.projectType] || '-' }}</el-descriptions-item>
        <el-descriptions-item label="分配方式">{{ allocationMethodMap[detail.allocationMethod] || '-' }}</el-descriptions-item>
        <el-descriptions-item label="项目状态">{{ statusMap[detail.status] || '-' }}</el-descriptions-item>
        <el-descriptions-item label="项目负责人">
          {{ detail.manager?.realName || detail.manager?.username || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="售前比例">
          {{ detail.presaleRatio != null ? (detail.presaleRatio * 100).toFixed(2) + '%' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="研发比例">
          {{ detail.rdRatio != null ? (detail.rdRatio * 100).toFixed(2) + '%' : '-' }}
        </el-descriptions-item>
      </el-descriptions>
      <div class="node-section">
        <div class="section-title">项目节点权重</div>
        <el-table :data="detail.nodes || []" v-loading="loading" style="width: 100%">
          <el-table-column prop="nodeOrder" label="顺序" width="80" />
          <el-table-column prop="nodeName" label="节点名称" min-width="160" />
          <el-table-column label="权重" min-width="120">
            <template #default="{ row }">
              {{ ((row.weight || 0) * 100).toFixed(2) }}%
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchProjectDetail } from '@/api/project.js'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const detail = reactive({})

const statusMap = {
  IN_PROGRESS: '进行中',
  FINISHED: '已结束',
  SETTLED: '已结算'
}

const allocationMethodMap = {
  BY_POSITION: '按岗位切分',
  SHARED: '共同分配'
}

const projectTypeMap = {
  NORMAL: '正常',
  HISTORY: '历史',
  POLITICAL: '政治性',
  KEY_SUPPORT: '重点支撑'
}

const fetchDetail = async () => {
  loading.value = true
  try {
    const resp = await fetchProjectDetail(route.params.id)
    Object.assign(detail, resp.data || {})
  } catch (err) {
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/projects')
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped>
.project-detail {
  padding: 24px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.node-section {
  margin-top: 20px;
}

.section-title {
  margin-bottom: 12px;
  font-weight: 600;
}
</style>
