<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useSession } from '../session'

const session = useSession()
const route = useRoute()
const router = useRouter()
const links = computed(() => [
  { to: '/dashboard', label: '概览', show: true },
  { to: '/hall', label: '考场', show: true },
  { to: '/records', label: '我的答卷', show: true },
  { to: '/questions', label: '题库', show: session.staff },
  { to: '/papers', label: '试卷', show: session.staff },
  { to: '/grades', label: '学情', show: session.staff },
  { to: '/subjects', label: '学科', show: session.admin },
  { to: '/users', label: '用户', show: session.admin }
].filter((item) => item.show))

function logout() {
  session.clear()
  router.push('/login')
}
</script>

<template>
  <div class="shell">
    <aside class="side">
      <div class="brand">
        <div class="seal">考</div>
        <div>在线考场<small>EXAM HALL</small></div>
      </div>
      <nav class="nav">
        <router-link v-for="item in links" :key="item.to" :to="item.to" :class="{ active: route.path.startsWith(item.to) }">{{ item.label }}</router-link>
      </nav>
      <div class="side-foot">
        <strong>{{ session.user?.nickname }}</strong>
        <div class="demo">{{ session.user?.roleName }}</div>
        <el-button text style="color:#f6efe4;padding-left:0" @click="logout">退出</el-button>
      </div>
    </aside>
    <div>
      <header class="topbar">
        <div class="brand"><div class="seal">考</div><span>在线考场</span></div>
        <el-button size="small" @click="logout">退出</el-button>
      </header>
      <main class="main"><router-view /></main>
    </div>
    <nav class="bottom">
      <router-link v-for="item in links" :key="item.to" :to="item.to" :class="{ active: route.path.startsWith(item.to) }">{{ item.label }}</router-link>
    </nav>
  </div>
</template>
