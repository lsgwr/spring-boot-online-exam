<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import http from '../api'
import { useSession } from '../session'

const router = useRouter()
const session = useSession()
const loading = ref(false)
const form = reactive({ username: 'admin', password: 'admin123' })

async function submit() {
  loading.value = true
  try {
    const res = await http.post('/auth/login', form)
    session.save(res.data)
    router.push('/dashboard')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <section class="auth">
    <div class="auth-hero">
      <div class="seal" style="width:72px;height:72px;font-size:34px">考</div>
      <h1>把一场考试<br>收进一页纸。</h1>
      <p>题库、组卷、判分和学情放在同一张桌面上。手机上也能答完。</p>
    </div>
    <div class="auth-panel">
      <div class="auth-card">
        <h2 class="serif">登录</h2>
        <el-form @submit.prevent="submit">
          <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
          <el-form-item label="密码"><el-input v-model="form.password" type="password" show-password /></el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading" style="width:100%">进入考场</el-button>
        </el-form>
        <p class="demo">演示账号 admin / teacher / student，密码都是 admin123</p>
        <router-link to="/register">没有账号，去注册</router-link>
      </div>
    </div>
  </section>
</template>
