<template>
  <div class="home">
    <h1>人员管理与绩效考核系统</h1>
    <el-button type="primary">Element Plus 按钮</el-button>
    <p>{{ msg }}</p>
  </div>
</template>

<script setup>
import { helloMsg } from '@/utils/hello.js'
const msg = helloMsg()
</script>

<style scoped>
.home {
  padding: 24px;
}
</style>
