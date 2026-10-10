<script setup>
import { computed, onBeforeUnmount, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import http from '../api'

const route = useRoute()
const router = useRouter()
const paper = ref(null)
const answers = reactive({})
const left = ref(0)
let timer = 0
let started = 0

const clock = computed(() => {
  const m = String(Math.floor(left.value / 60)).padStart(2, '0')
  const s = String(left.value % 60).padStart(2, '0')
  return `${m}:${s}`
})

onMounted(async () => {
  paper.value = (await http.get(`/exams/${route.params.id}/paper`)).data
  paper.value.questions.forEach((q) => { answers[q.id] = q.typeId === 2 ? [] : '' })
  left.value = (paper.value.timeLimit || 30) * 60
  started = Date.now()
  timer = window.setInterval(() => {
    left.value -= 1
    if (left.value <= 0) submit(true)
  }, 1000)
})
onBeforeUnmount(() => clearInterval(timer))

function chosen(question) {
  const value = answers[question.id]
  return question.typeId === 2 ? value : (value ? [value] : [])
}

async function submit(auto = false) {
  clearInterval(timer)
  if (!auto) {
    await ElMessageBox.confirm('确认交卷？', '交卷')
  }
  const payload = { timeCost: Math.round((Date.now() - started) / 1000), answers: {} }
  paper.value.questions.forEach((q) => { payload.answers[q.id] = chosen(q) })
  const res = await http.post(`/exams/${route.params.id}/submit`, payload)
  router.replace(`/records/${res.data.summary.id}`)
}
</script>

<template>
  <div v-if="paper">
    <div class="page-head">
      <div>
        <h1>{{ paper.name }}</h1>
        <p>总分 {{ paper.totalScore }} · {{ paper.questions.length }} 题</p>
      </div>
      <div class="timer">{{ clock }}</div>
    </div>
    <article v-for="(question, index) in paper.questions" :key="question.id" class="card q-block">
      <div class="chips"><span class="chip">{{ question.typeName }}</span><span class="chip">{{ question.score }} 分</span></div>
      <h3 class="serif">{{ index + 1 }}.</h3>
      <div class="prose" v-html="question.stem"></div>
      <img v-if="question.image" :src="question.image" alt="" style="max-width:100%;border-radius:12px" />
      <el-radio-group v-if="question.typeId !== 2" v-model="answers[question.id]" style="display:block;margin-top:12px">
        <label v-for="option in question.options" :key="option.id" class="option" :class="{ picked: answers[question.id] === option.id }">
          <el-radio :value="option.id">&nbsp;</el-radio>
          <div class="prose" v-html="option.content"></div>
        </label>
      </el-radio-group>
      <el-checkbox-group v-else v-model="answers[question.id]" style="display:block;margin-top:12px">
        <label v-for="option in question.options" :key="option.id" class="option" :class="{ picked: answers[question.id].includes(option.id) }">
          <el-checkbox :value="option.id">&nbsp;</el-checkbox>
          <div class="prose" v-html="option.content"></div>
        </label>
      </el-checkbox-group>
    </article>
    <el-button type="primary" size="large" @click="submit(false)">交卷</el-button>
  </div>
</template>
