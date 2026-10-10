<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import http from '../api'
import { useSession } from '../session'

const router = useRouter()
const session = useSession()
const loading = ref(false)
const form = reactive({ username: '', password: '', nickname: '', email: '', phone: '' })

async function submit() {
  loading.value = true
  try {
    const res = await http.post('/auth/register', form)
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
      <h1>先注册，<br>再进考场。</h1>
      <p>新账号默认是学生。教师和管理员由管理员分配。</p>
    </div>
    <div class="auth-panel">
      <div class="auth-card">
        <h2 class="serif">注册</h2>
        <el-form @submit.prevent="submit">
          <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
          <el-form-item label="密码"><el-input v-model="form.password" type="password" show-password /></el-form-item>
          <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
          <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
          <el-button type="primary" :loading="loading" style="width:100%" @click="submit">创建账号</el-button>
        </el-form>
        <router-link to="/login">返回登录</router-link>
      </div>
    </div>
  </section>
</template>
