<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import http from '../api'

const route = useRoute()
const detail = ref(null)
onMounted(async () => { detail.value = (await http.get(`/records/${route.params.id}`)).data })
function picked(line, option) { return line.chosen.includes(option.id) }
</script>

<template>
  <div v-if="detail">
    <div class="page-head">
      <div>
        <h1>{{ detail.summary.examName }}</h1>
        <p>{{ detail.summary.studentName }} · {{ detail.summary.score }}/{{ detail.summary.total }} · {{ detail.summary.joinDate }}</p>
      </div>
    </div>
    <article v-for="(line, index) in detail.lines" :key="line.questionId" class="card q-block">
      <span class="chip" :class="line.correct ? 'ok' : 'bad'">{{ line.correct ? '正确' : '错误' }} · {{ line.awarded }} 分</span>
      <h3 class="serif">{{ index + 1 }}.</h3>
      <div class="prose" v-html="line.stem"></div>
      <img v-if="line.image" :src="line.image" alt="" style="max-width:240px;border-radius:12px" />
      <div v-for="option in line.options" :key="option.id" class="option" :class="{ picked: picked(line, option), right: option.correct }">
        <div class="prose" v-html="option.content"></div>
      </div>
    </article>
  </div>
</template>
