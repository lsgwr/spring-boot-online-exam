<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import http from '../api'

const rows = ref([])
const open = ref(false)
const form = reactive({ id: '', username: '', password: '', nickname: '', email: '', phone: '', roleId: 3, enabled: 1, description: '' })
const roles = [{ id: 1, name: '管理员' }, { id: 2, name: '教师' }, { id: 3, name: '学生' }]
async function load() { rows.value = (await http.get('/users')).data }
onMounted(load)
function create() {
  Object.assign(form, { id: '', username: '', password: '', nickname: '', email: '', phone: '', roleId: 3, enabled: 1, description: '' })
  open.value = true
}
function edit(row) {
  Object.assign(form, { ...row, password: '' })
  open.value = true
}
async function save() {
  if (form.id) await http.put(`/users/${form.id}`, form)
  else await http.post('/users', form)
  open.value = false
  ElMessage.success('用户已保存')
  await load()
}
async function toggle(row) {
  await http.put(`/users/${row.id}`, { enabled: row.enabled === 0 ? 1 : 0 })
  await load()
}
</script>

<template>
  <div>
    <div class="page-head">
      <div><h1>用户</h1><p>停用后无法登录。密码使用新的加密方式保存。</p></div>
      <el-button type="primary" @click="create">新建用户</el-button>
    </div>
    <div class="card table-wrap">
      <el-table :data="rows">
        <el-table-column prop="username" label="用户名" width="140" />
        <el-table-column prop="nickname" label="昵称" width="140" />
        <el-table-column prop="roleName" label="角色" width="100" />
        <el-table-column prop="email" label="邮箱" min-width="160" />
        <el-table-column label="状态" width="90"><template #default="{ row }">{{ row.enabled === 0 ? '停用' : '正常' }}</template></el-table-column>
        <el-table-column width="160">
          <template #default="{ row }">
            <el-button link @click="edit(row)">编辑</el-button>
            <el-button link @click="toggle(row)">{{ row.enabled === 0 ? '启用' : '停用' }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog v-model="open" title="用户" width="min(480px, 92vw)">
      <el-form label-position="top">
        <el-form-item v-if="!form.id" label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item :label="form.id ? '重置密码（可空）' : '密码'"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="角色"><el-select v-model="form.roleId"><el-option v-for="role in roles" :key="role.id" :label="role.name" :value="role.id" /></el-select></el-form-item>
      </el-form>
      <template #footer><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>
