<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import http from '../api'

const exams = ref([])
const questions = ref([])
const open = ref(false)
const form = reactive(blank())
function blank() {
  return { id: '', name: '', description: '', avatar: '', timeLimit: 60, radioScore: 5, checkScore: 6, judgeScore: 5, radioIds: [], checkIds: [], judgeIds: [] }
}
const groups = computed(() => ({
  1: questions.value.filter((q) => q.typeId === 1),
  2: questions.value.filter((q) => q.typeId === 2),
  3: questions.value.filter((q) => q.typeId === 3)
}))
function text(html) { return (html || '').replace(/<[^>]+>/g, '').slice(0, 36) }
async function load() {
  exams.value = (await http.get('/exams/editing')).data
  questions.value = (await http.get('/questions')).data
}
onMounted(load)
function create() { Object.assign(form, blank()); open.value = true }
function edit(row) { Object.assign(form, { ...blank(), ...row }); open.value = true }
async function save() {
  if (form.id) await http.put(`/exams/${form.id}`, form)
  else await http.post('/exams', form)
  open.value = false
  ElMessage.success('试卷已保存')
  await load()
}
async function remove(row) {
  await http.delete(`/exams/${row.id}`)
  ElMessage.success('考试已隐藏，题目仍在题库')
  await load()
}
async function onImage(file) {
  const body = new FormData()
  body.append('file', file.raw)
  form.avatar = (await http.post('/media', body)).data
}
</script>

<template>
  <div>
    <div class="page-head">
      <div><h1>试卷</h1><p>删除考试只会隐藏场次，不会删掉题目。</p></div>
      <el-button type="primary" @click="create">新建考试</el-button>
    </div>
    <div class="card table-wrap">
      <el-table :data="exams">
        <el-table-column prop="name" label="名称" min-width="160" />
        <el-table-column prop="totalScore" label="总分" width="80" />
        <el-table-column prop="timeLimit" label="分钟" width="80" />
        <el-table-column label="" width="160">
          <template #default="{ row }">
            <el-button link @click="edit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-drawer v-model="open" :title="form.id ? '编辑考试' : '新建考试'" size="min(640px, 100%)">
      <el-form label-position="top">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-upload :auto-upload="false" :show-file-list="false" accept="image/*" :on-change="onImage"><el-button>上传封面</el-button></el-upload>
        <img v-if="form.avatar" :src="form.avatar" alt="" style="max-width:180px;margin:8px 0;border-radius:8px" />
        <div class="grid" style="grid-template-columns:1fr 1fr">
          <el-form-item label="时长（分钟）"><el-input-number v-model="form.timeLimit" :min="1" /></el-form-item>
          <el-form-item label="单选每题"><el-input-number v-model="form.radioScore" :min="0" /></el-form-item>
          <el-form-item label="多选每题"><el-input-number v-model="form.checkScore" :min="0" /></el-form-item>
          <el-form-item label="判断每题"><el-input-number v-model="form.judgeScore" :min="0" /></el-form-item>
        </div>
        <el-form-item label="单选题">
          <el-select v-model="form.radioIds" multiple filterable style="width:100%">
            <el-option v-for="q in groups[1]" :key="q.id" :label="text(q.stem)" :value="q.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="多选题">
          <el-select v-model="form.checkIds" multiple filterable style="width:100%">
            <el-option v-for="q in groups[2]" :key="q.id" :label="text(q.stem)" :value="q.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="判断题">
          <el-select v-model="form.judgeIds" multiple filterable style="width:100%">
            <el-option v-for="q in groups[3]" :key="q.id" :label="text(q.stem)" :value="q.id" />
          </el-select>
        </el-form-item>
        <el-button type="primary" @click="save">保存</el-button>
      </el-form>
    </el-drawer>
  </div>
</template>
