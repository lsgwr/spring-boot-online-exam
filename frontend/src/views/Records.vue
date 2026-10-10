<script setup>
import { onMounted, ref } from 'vue'
import http from '../api'

const rows = ref([])
onMounted(async () => { rows.value = (await http.get('/records')).data })
</script>

<template>
  <div>
    <div class="page-head"><div><h1>我的答卷</h1><p>点进一场，看每题对错。</p></div></div>
    <div class="card table-wrap">
      <el-table :data="rows">
        <el-table-column prop="examName" label="考试" min-width="160" />
        <el-table-column label="得分" width="120">
          <template #default="{ row }">{{ row.score }}/{{ row.total }}</template>
        </el-table-column>
        <el-table-column prop="timeCost" label="用时(秒)" width="110" />
        <el-table-column prop="joinDate" label="时间" min-width="140" />
        <el-table-column label="" width="100">
          <template #default="{ row }"><router-link :to="`/records/${row.id}`">查看</router-link></template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
