<template>
  <a-modal
    title="编辑封面"
    :width="640"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
  >
    <p>{{ avatar }}</p>
    <template slot="footer">
      <a-button key="cancel" @click="handleCancel">完成</a-button>
      <a-button key="cancel" @click="handleCancel">关闭</a-button>
    </template>
  </a-modal>
</template>

<script>
import { examUpdate } from '../../../api/exam'
export default {
  // 编译图片的弹出框，用富文本编辑
  name: 'UpdateAvatarModal',
  data () {
    return {
      visible: false,
      confirmLoading: false,
      form: this.$form.createForm(this),
      // 每个问题
      exam: {},
      avatar: ''
    }
  },
  methods: {
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
