<template>
  <div class="project-create">
    <el-card>
      <template #header>
        <div class="header">创建项目</div>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目编号" prop="projectCode">
          <el-input v-model="form.projectCode" placeholder="PROJ-YYYY-XXX" />
        </el-form-item>
        <el-form-item label="项目类型" prop="projectType">
          <el-select v-model="form.projectType" placeholder="请选择项目类型">
            <el-option label="正常" value="NORMAL" />
            <el-option label="历史" value="HISTORY" />
            <el-option label="政治性" value="POLITICAL" />
            <el-option label="重点支撑" value="KEY_SUPPORT" />
          </el-select>
        </el-form-item>
        <el-form-item label="分配方式" prop="allocationMethod">
          <el-select v-model="form.allocationMethod" placeholder="请选择分配方式">
            <el-option label="按岗位切分" value="BY_POSITION" />
            <el-option label="共同分配" value="SHARED" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.allocationMethod === 'BY_POSITION'" label="售前比例" prop="presaleRatio">
          <el-input-number v-model="form.presaleRatio" :min="0" :max="100" :step="1" />
          <span class="unit">%</span>
        </el-form-item>
        <el-form-item v-if="form.allocationMethod === 'BY_POSITION'" label="研发比例" prop="rdRatio">
          <el-input-number v-model="form.rdRatio" :min="0" :max="100" :step="1" />
          <span class="unit">%</span>
        </el-form-item>
        <div class="weight-section">
          <div class="section-title">节点权重（%）</div>
          <el-table :data="form.nodeWeights" style="width: 100%">
            <el-table-column prop="nodeName" label="节点名称" min-width="160" />
            <el-table-column label="权重" min-width="160">
              <template #default="{ row }">
                <el-input-number v-model="row.weight" :min="0" :max="100" :step="0.1" />
              </template>
            </el-table-column>
          </el-table>
          <div class="weight-summary">
            权重总和：{{ totalWeight.toFixed(2) }}%
            <span v-if="totalWeight !== 100" class="warning">权重总和需为100%</span>
          </div>
        </div>
        <div class="actions">
          <el-button type="primary" :loading="loading" @click="onSubmit">保存</el-button>
          <el-button @click="onCancel">取消</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { createProject } from '@/api/project.js'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const defaultNodes = [
  { nodeName: '商机', weight: 2 },
  { nodeName: '项目建议书', weight: 3 },
  { nodeName: '可研', weight: 5 },
  { nodeName: '招投标', weight: 5 },
  { nodeName: '前向签约', weight: 5 },
  { nodeName: '系统研发', weight: 50 },
  { nodeName: '后向采购', weight: 5 },
  { nodeName: '后向签约', weight: 5 },
  { nodeName: '项目试运行', weight: 10 },
  { nodeName: '初验', weight: 5 },
  { nodeName: '终验', weight: 5 }
]

const form = reactive({
  projectName: '',
  projectCode: '',
  projectType: 'NORMAL',
  allocationMethod: 'BY_POSITION',
  presaleRatio: 30,
  rdRatio: 70,
  nodeWeights: defaultNodes.map((node) => ({ ...node }))
})

const rules = {
  projectName: [
    { required: true, message: '请输入项目名称', trigger: 'blur' },
    { min: 2, max: 200, message: '项目名称长度需在2到200之间', trigger: 'blur' }
  ],
  projectCode: [
    { required: true, message: '请输入项目编号', trigger: 'blur' },
    { pattern: /^PROJ-\d{4}-\d{3}$/, message: '项目编号格式必须为PROJ-YYYY-XXX', trigger: 'blur' }
  ],
  projectType: [{ required: true, message: '请选择项目类型', trigger: 'change' }],
  allocationMethod: [{ required: true, message: '请选择分配方式', trigger: 'change' }],
  presaleRatio: [{ required: true, message: '请输入售前比例', trigger: 'blur' }],
  rdRatio: [{ required: true, message: '请输入研发比例', trigger: 'blur' }]
}

const totalWeight = computed(() => {
  return form.nodeWeights.reduce((sum, node) => sum + Number(node.weight || 0), 0)
})

const onSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch (err) {
    return
  }
  if (Math.abs(totalWeight.value - 100) > 0.01) {
    ElMessage.warning('节点权重总和必须为100%')
    return
  }
  if (form.allocationMethod === 'BY_POSITION' && Math.abs(form.presaleRatio + form.rdRatio - 100) > 0.01) {
    ElMessage.warning('售前比例与研发比例之和必须为100%')
    return
  }
  loading.value = true
  try {
    const payload = {
      projectName: form.projectName,
      projectCode: form.projectCode,
      projectType: form.projectType,
      allocationMethod: form.allocationMethod,
      presaleRatio: form.allocationMethod === 'BY_POSITION' ? form.presaleRatio / 100 : null,
      rdRatio: form.allocationMethod === 'BY_POSITION' ? form.rdRatio / 100 : null,
      nodeWeights: form.nodeWeights.map((node) => ({
        nodeName: node.nodeName,
        weight: Number(node.weight) / 100
      }))
    }
    await createProject(payload)
    ElMessage.success('创建成功')
    router.push('/projects')
  } catch (err) {
  } finally {
    loading.value = false
  }
}

const onCancel = () => {
  router.push('/projects')
}
</script>

<style scoped>
.project-create {
  padding: 24px;
}

.header {
  font-size: 16px;
  font-weight: 600;
}

.unit {
  margin-left: 8px;
  color: #606266;
}

.weight-section {
  margin: 16px 0;
}

.section-title {
  margin-bottom: 12px;
  font-weight: 600;
}

.weight-summary {
  margin-top: 12px;
  text-align: right;
}

.warning {
  color: #f56c6c;
  margin-left: 8px;
}

.actions {
  display: flex;
  gap: 12px;
  margin-top: 16px;
}
</style>
