<script setup>
import { onBeforeUnmount, shallowRef } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'

const model = defineModel({ default: '' })
const editorRef = shallowRef()
const editorConfig = {
  placeholder: '支持加粗、列表和图片，图片会以 Base64 写入题干',
  MENU_CONF: {
    uploadImage: {
      async customUpload(file, insert) {
        const dataUrl = await read(file)
        insert(dataUrl, file.name, dataUrl)
      }
    }
  }
}

function onCreated(editor) {
  editorRef.value = editor
}
function onChange(editor) {
  model.value = editor.getHtml()
}
function read(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = () => resolve(reader.result)
    reader.onerror = reject
    reader.readAsDataURL(file)
  })
}
onBeforeUnmount(() => editorRef.value?.destroy())
</script>

<template>
  <div class="rich">
    <Toolbar :editor="editorRef" mode="simple" />
    <Editor :model-value="model" :default-config="editorConfig" mode="simple" style="height: 160px" @onCreated="onCreated" @onChange="onChange" />
  </div>
</template>
