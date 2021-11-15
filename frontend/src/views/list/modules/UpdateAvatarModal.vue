<template>
  <a-modal title="编辑封面" :width="400" :visible="visible" :confirmLoading="confirmLoading" @cancel="handleCancel">
    <p>截图直接粘贴到下面即可，建议图片不要大于80*80</p>
    <div id="summernote-exam-avatar"></div>
    <template slot="footer">
      <a-button key="update" @click="handleUpdate">完成</a-button>
      <a-button key="cancel" @click="handleCancel">关闭</a-button>
    </template>
  </a-modal>
</template>

<script>
import { examUpdate } from '@api/exam'
import '../../../plugins/summernote'
import $ from 'jquery'

export default {
  // 编译图片的弹出框，用富文本编辑
  name: 'UpdateAvatarModal',
  data () {
    return {
      confirmLoading: false,
      visible: false,
      // 每个问题
      exam: {}
    }
  },
  updated () {
    this.initSummernote()
    $('#summernote-exam-avatar').summernote('code', this.exam.avatar) // 把图片数据写入进去
  },
  methods: {
    initSummernote () {
      console.log('初始化富文本插件')
      $('#summernote-exam-avatar').summernote({
        lang: 'zh-CN',
        placeholder: '请输入内容',
        height: 200,
        width: 320,
        htmlMode: true,
        toolbar: [],
        fontSizes: ['8', '9', '10', '11', '12', '14', '18', '24', '36'],
        fontNames: [
          'Arial', 'Arial Black', 'Comic Sans MS', 'Courier New',
          'Helvetica Neue', 'Helvetica', 'Impact', 'Lucida Grande',
          'Tahoma', 'Times New Roman', 'Verdana'
        ]
      })
    },
    edit (exam) {
      this.visible = true
      // 把当前的记录赋值到data中的变量
      Object.assign(this.exam, exam)
      this.avatar = exam.avatar
    },
    handleCancel () {
      // clear form & currentStep
      this.visible = false
    },
    handleUpdate () {
      const that = this
      that.exam.avatar = $('#summernote-exam-avatar').summernote('code')
      examUpdate(that.exam).then(res => {
        // 成功就跳转到结果页面
        console.log(res)
        if (res.code === 0) {
          that.$notification.success({
            message: '更新成功',
            description: '考试更新成功'
          })
          // 关闭弹出框
          that.visible = false
          that.$emit('ok')
        }
      }).catch(err => {
        // 失败就弹出警告消息
        that.$notification.error({
          message: '考试更新失败',
          description: err.message
        })
      })
    }
  }
}
</script>
