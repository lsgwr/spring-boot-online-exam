<script setup>
import { onMounted, ref } from 'vue'
import http from '../api'

const exams = ref([])
onMounted(async () => {
  exams.value = (await http.get('/exams')).data
})
function cover(exam) {
  const raw = exam.avatar || ''
  const matched = raw.match(/src="(data:image\/[^"]+|https?:\/\/[^"]+)"/)
  if (matched) return matched[1]
  if (raw.startsWith('data:image') || raw.startsWith('http://') || raw.startsWith('https://')) return raw
  return ''
}
</script>

<template>
  <div>
    <div class="page-head">
      <div>
        <h1>考场</h1>
        <p>选一场正在开放的考试。封面图片保存在数据库里。</p>
      </div>
    </div>
    <div class="grid cards">
      <article v-for="exam in exams" :key="exam.id" class="card exam-card">
        <img v-if="cover(exam)" :src="cover(exam)" alt="" />
        <div v-else class="cover"></div>
        <h3 class="serif" style="margin:0">{{ exam.name }}</h3>
        <p class="demo" style="margin:0">{{ exam.description || '这场考试没有简介' }}</p>
        <div class="chips">
          <span class="chip">{{ exam.score }} 分</span>
          <span class="chip">{{ exam.timeLimit }} 分钟</span>
          <span class="chip">单选 {{ exam.radioCount }}</span>
          <span class="chip">多选 {{ exam.checkCount }}</span>
          <span class="chip">判断 {{ exam.judgeCount }}</span>
        </div>
        <router-link :to="`/exam/${exam.id}`"><el-button type="primary">开始作答</el-button></router-link>
      </article>
    </div>
  </div>
</template>
