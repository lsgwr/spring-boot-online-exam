<script setup>
import { onBeforeUnmount, onMounted, ref, shallowRef } from 'vue'
import * as echarts from 'echarts'
import http from '../api'
import { useSession } from '../session'

const session = useSession()
const data = ref(null)
const chartEl = ref(null)
const chart = shallowRef(null)

onMounted(async () => {
  const res = await http.get('/analytics')
  data.value = res.data
  chart.value = echarts.init(chartEl.value)
  chart.value.setOption({
    color: ['#c4452d', '#2c6b5a', '#a67c3d', '#1c2430', '#d7a07a'],
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['46%', '72%'],
      data: res.data.buckets.map((b) => ({ name: b.label, value: b.count }))
    }]
  })
  window.addEventListener('resize', resize)
})
function resize() { chart.value?.resize() }
onBeforeUnmount(() => {
  window.removeEventListener('resize', resize)
  chart.value?.dispose()
})
</script>

<template>
  <div>
    <div class="page-head">
      <div>
        <h1>{{ session.staff ? '学情概览' : '我的考场' }}</h1>
        <p>{{ session.user?.nickname }}，今天的分数分布在这张图里。</p>
      </div>
    </div>
    <div v-if="data" class="grid stats">
      <div class="card stat"><span>考试</span><b>{{ data.overview.exams }}</b></div>
      <div class="card stat"><span>题目</span><b>{{ data.overview.questions }}</b></div>
      <div class="card stat"><span>答卷</span><b>{{ data.overview.attempts }}</b></div>
      <div class="card stat"><span>平均分</span><b>{{ data.overview.average }}</b></div>
    </div>
    <div class="split">
      <div class="card">
        <h3 class="serif">得分率分布</h3>
        <div ref="chartEl" style="height:280px"></div>
      </div>
      <div class="card">
        <h3 class="serif">最近答卷</h3>
        <div v-for="row in data?.recent || []" :key="row.id" style="display:flex;justify-content:space-between;padding:8px 0;border-bottom:1px solid var(--line)">
          <span>{{ row.examName }}<br><small class="demo">{{ row.studentName }} · {{ row.joinDate }}</small></span>
          <strong>{{ row.score }}/{{ row.total }}</strong>
        </div>
      </div>
    </div>
    <div v-if="session.staff && data" class="card" style="margin-top:16px">
      <h3 class="serif">各场考试</h3>
      <div class="table-wrap">
        <el-table :data="data.examStats">
          <el-table-column prop="name" label="考试" min-width="140" />
          <el-table-column prop="attempts" label="人次" width="80" />
          <el-table-column prop="average" label="均分" width="80" />
          <el-table-column prop="max" label="最高" width="80" />
          <el-table-column prop="min" label="最低" width="80" />
          <el-table-column prop="passRate" label="及格率%" width="100" />
        </el-table>
      </div>
      <div class="chips" style="margin-top:12px">
        <span v-for="item in data.categories" :key="item.name" class="chip">{{ item.name }} {{ item.count }}</span>
      </div>
    </div>
  </div>
</template>
