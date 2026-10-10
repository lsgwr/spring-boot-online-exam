<script setup>
import { computed, onMounted, ref } from 'vue'
import axios from 'axios'
import http from '../api'

const rows = ref([])
const exams = ref([])
const examId = ref('')
async function load() {
  rows.value = (await http.get('/grades', { params: { examId: examId.value || undefined } })).data
  exams.value = (await http.get('/exams/editing')).data
}
onMounted(load)
const summary = computed(() => {
  if (!rows.value.length) return { avg: 0, max: 0, pass: 0 }
  const scores = rows.value.map((r) => r.total ? r.score / r.total : 0)
  const avg = Math.round(rows.value.reduce((s, r) => s + r.score, 0) / rows.value.length * 10) / 10
  const max = Math.max(...rows.value.map((r) => r.score))
  const pass = Math.round(scores.filter((s) => s >= 0.6).length * 1000 / scores.length) / 10
  return { avg, max, pass }
})
async function download() {
  const res = await axios.get('/api/grades/export', {
    params: { examId: examId.value || undefined },
    headers: { 'Access-Token': localStorage.getItem('token') },
    responseType: 'blob'
  })
  const url = URL.createObjectURL(res.data)
  const a = document.createElement('a')
  a.href = url
  a.download = 'grades.csv'
  a.click()
  URL.revokeObjectURL(url)
}
</script>

<template>
  <div>
    <div class="page-head">
      <div><h1>学情</h1><p>教师可以看到全部学生的成绩，并按考试筛选。</p></div>
      <el-button @click="download">导出 CSV</el-button>
    </div>
    <div class="grid stats">
      <div class="card stat"><span>答卷</span><b>{{ rows.length }}</b></div>
      <div class="card stat"><span>平均分</span><b>{{ summary.avg }}</b></div>
      <div class="card stat"><span>最高分</span><b>{{ summary.max }}</b></div>
      <div class="card stat"><span>及格率</span><b>{{ summary.pass }}%</b></div>
    </div>
    <el-select v-model="examId" clearable placeholder="全部考试" style="margin:16px 0;width:min(320px,100%)" @change="load">
      <el-option v-for="exam in exams" :key="exam.id" :label="exam.name" :value="exam.id" />
    </el-select>
    <div class="card table-wrap">
      <el-table :data="rows">
        <el-table-column prop="examName" label="考试" min-width="140" />
        <el-table-column prop="studentName" label="考生" width="120" />
        <el-table-column label="得分" width="110"><template #default="{ row }">{{ row.score }}/{{ row.total }}</template></el-table-column>
        <el-table-column prop="joinDate" label="时间" min-width="140" />
        <el-table-column width="90"><template #default="{ row }"><router-link :to="`/records/${row.id}`">答卷</router-link></template></el-table-column>
      </el-table>
    </div>
  </div>
</template>
