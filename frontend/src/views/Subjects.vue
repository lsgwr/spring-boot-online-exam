<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import http from '../api'

const rows = ref([])
const open = ref(false)
const form = reactive({ id: null, name: '', description: '' })
async function load() { rows.value = (await http.get('/subjects')).data }
onMounted(load)
function create() { Object.assign(form, { id: null, name: '', description: '' }); open.value = true }
function edit(row) { Object.assign(form, row); open.value = true }
async function save() {
  if (form.id) await http.put(`/subjects/${form.id}`, form)
  else await http.post('/subjects', form)
  open.value = false
  ElMessage.success('学科已保存')
  await load()
}
async function remove(row) {
  await http.delete(`/subjects/${row.id}`)
  ElMessage.success('学科已删除')
  await load()
}
</script>

<template>
  <div>
    <div class="page-head">
      <div><h1>学科</h1><p>仍有题目的学科不能删除。</p></div>
      <el-button type="primary" @click="create">新建学科</el-button>
    </div>
    <div class="grid cards">
      <article v-for="row in rows" :key="row.id" class="card">
        <h3 class="serif" style="margin:0">{{ row.name }}</h3>
        <p class="demo">{{ row.description || '暂无简介' }}</p>
        <span class="chip">{{ row.questionCount }} 题</span>
        <div style="margin-top:12px">
          <el-button link @click="edit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </div>
      </article>
    </div>
    <el-dialog v-model="open" title="学科" width="min(480px, 92vw)">
      <el-form label-position="top">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>
