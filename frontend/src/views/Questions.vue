<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import http from '../api'
import RichText from '../components/RichText.vue'

const rows = ref([])
const meta = ref({ types: [], levels: [], subjects: [] })
const open = ref(false)
const editorKey = ref(0)
const correctIndex = ref(0)
const form = reactive(blank())
const importing = ref(false)

function blank() {
  return { id: '', stem: '', analysis: '', score: 5, typeId: 1, levelId: 3, categoryId: null, image: '', options: [opt(true), opt(false), opt(false), opt(false)] }
}
function opt(correct) { return { content: '', correct } }
function text(html) { return (html || '').replace(/<[^>]+>/g, '').replace(/&nbsp;/g, ' ').trim() }
function meaningful(html) { return text(html).length > 0 }

async function load() {
  const [list, dict] = await Promise.all([http.get('/questions'), http.get('/meta')])
  rows.value = list.data
  meta.value = dict.data
  if (!form.categoryId && dict.data.subjects[0]) form.categoryId = dict.data.subjects[0].id
}
onMounted(load)

function create() {
  Object.assign(form, blank())
  form.categoryId = meta.value.subjects[0]?.id
  correctIndex.value = 0
  editorKey.value++
  open.value = true
}
function edit(row) {
  Object.assign(form, {
    id: row.id, stem: row.stem, analysis: row.analysis || '', score: row.score, typeId: row.typeId,
    levelId: row.levelId, categoryId: row.categoryId, image: row.image || '',
    options: row.options.map((o) => ({ content: o.content, correct: o.correct }))
  })
  correctIndex.value = Math.max(0, form.options.findIndex((o) => o.correct))
  editorKey.value++
  open.value = true
}
function useJudgeOptions() {
  form.typeId = 3
  form.options = [{ content: '<p>正确</p>', correct: true }, { content: '<p>错误</p>', correct: false }]
  correctIndex.value = 0
  editorKey.value++
}
async function save() {
  const options = form.options
    .map((option, index) => ({
      content: option.content,
      correct: form.typeId === 2 ? !!option.correct : index === correctIndex.value
    }))
    .filter((option) => meaningful(option.content))
  const body = { ...form, options }
  if (form.id) await http.put(`/questions/${form.id}`, body)
  else await http.post('/questions', body)
  open.value = false
  ElMessage.success('题目已保存')
  await load()
}
async function remove(row) {
  await http.delete(`/questions/${row.id}`)
  ElMessage.success('题目已隐藏')
  await load()
}
async function onImage(file) {
  const body = new FormData()
  body.append('file', file.raw)
  form.image = (await http.post('/media', body)).data
}
function template() {
  const csv = 'type,level,category,score,stem,optionA,optionB,optionC,optionD,answer\nsingle,易,数学,5,1+1等于几,1,2,3,4,B\njudge,易,生活,5,地球是圆的,,,,,正确\n'
  const blob = new Blob([csv], { type: 'text/csv;charset=utf-8' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = 'questions.csv'
  a.click()
  URL.revokeObjectURL(url)
}
async function onImport(file) {
  importing.value = true
  try {
    const body = new FormData()
    body.append('file', file.raw)
    const res = await http.post('/questions/import', body)
    ElMessage.success(`导入 ${res.data.created} 题，失败 ${res.data.failed} 行`)
    if (res.data.errors?.length) ElMessage.warning(res.data.errors.join('；'))
    await load()
  } finally {
    importing.value = false
  }
}
</script>

<template>
  <div>
    <div class="page-head">
      <div><h1>题库</h1><p>题干和选项都是富文本。被考试引用的题目不能直接删除。</p></div>
      <div style="display:flex;gap:8px;flex-wrap:wrap">
        <el-button @click="template">下载模板</el-button>
        <el-upload :auto-upload="false" :show-file-list="false" accept=".csv,.xlsx" :on-change="onImport">
          <el-button :loading="importing">批量导入</el-button>
        </el-upload>
        <el-button type="primary" @click="create">新建题目</el-button>
      </div>
    </div>
    <div class="card table-wrap">
      <el-table :data="rows">
        <el-table-column label="题干" min-width="220"><template #default="{ row }">{{ text(row.stem).slice(0, 42) }}</template></el-table-column>
        <el-table-column prop="typeName" label="题型" width="90" />
        <el-table-column prop="categoryName" label="学科" width="100" />
        <el-table-column prop="levelName" label="难度" width="80" />
        <el-table-column label="" width="160">
          <template #default="{ row }">
            <el-button link @click="edit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-drawer v-model="open" :title="form.id ? '编辑题目' : '新建题目'" size="min(640px, 100%)">
      <div v-if="open" :key="editorKey" class="grid">
        <el-form label-position="top">
          <el-form-item label="题干"><RichText v-model="form.stem" /></el-form-item>
          <el-form-item label="解析"><RichText v-model="form.analysis" /></el-form-item>
          <div class="grid" style="grid-template-columns:1fr 1fr">
            <el-form-item label="题型"><el-select v-model="form.typeId"><el-option v-for="t in meta.types" :key="t.id" :label="t.name" :value="t.id" /></el-select></el-form-item>
            <el-form-item label="难度"><el-select v-model="form.levelId"><el-option v-for="t in meta.levels" :key="t.id" :label="t.name" :value="t.id" /></el-select></el-form-item>
            <el-form-item label="学科"><el-select v-model="form.categoryId"><el-option v-for="t in meta.subjects" :key="t.id" :label="t.name" :value="t.id" /></el-select></el-form-item>
            <el-form-item label="分值"><el-input-number v-model="form.score" :min="1" /></el-form-item>
          </div>
          <el-upload :auto-upload="false" :show-file-list="false" accept="image/*" :on-change="onImage"><el-button>上传配图（Base64 入库）</el-button></el-upload>
          <img v-if="form.image" :src="form.image" alt="" style="max-width:180px;margin-top:8px;border-radius:8px" />
          <h3 class="serif">选项</h3>
          <el-button v-if="form.typeId !== 2" link @click="useJudgeOptions">填入判断题「正确 / 错误」</el-button>
          <el-radio-group v-if="form.typeId !== 2" v-model="correctIndex" style="display:block">
            <div v-for="(option, index) in form.options" :key="`${editorKey}-${index}`" style="margin-bottom:12px">
              <div style="display:flex;justify-content:space-between;align-items:center">
                <el-radio :value="index">正确答案</el-radio>
                <el-button link type="danger" @click="form.options.splice(index, 1)">删除此选项</el-button>
              </div>
              <RichText v-model="option.content" />
            </div>
          </el-radio-group>
          <div v-else>
          <div v-for="(option, index) in form.options" :key="`${editorKey}-m-${index}`" style="margin-bottom:12px">
            <div style="display:flex;justify-content:space-between;align-items:center">
              <el-checkbox v-model="option.correct">正确答案</el-checkbox>
              <el-button link type="danger" @click="form.options.splice(index, 1)">删除此选项</el-button>
            </div>
            <RichText v-model="option.content" />
          </div>
          </div>
          <el-button @click="form.options.push(opt(false))">增加选项</el-button>
          <div style="margin-top:16px"><el-button type="primary" @click="save">保存</el-button></div>
        </el-form>
      </div>
    </el-drawer>
  </div>
</template>
