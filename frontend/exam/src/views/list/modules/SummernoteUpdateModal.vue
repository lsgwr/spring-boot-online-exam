<template>
  <a-modal :title="title" :width="600" :visible="visible" :confirmLoading="confirmLoading" @cancel="handleCancel">
    <div :id="divId"></div>
    <template slot="footer">
      <a-button key="update" @click="handleUpdate">完成</a-button>
      <a-button key="cancel" @click="handleCancel">关闭</a-button>
    </template>
  </a-modal>
</template>

<script>
import '../../../plugins/summernote'
import $ from 'jquery'

export default {
  // 编译图片的弹出框，用富文本编辑
  name: 'SummernoteUpdateModal',
  data () {
    return {
      confirmLoading: false,
      visible: false,
      divId: 'summernote-id', // 富文本组件的id
      record: {},
      key: '', // 键
      content: '', // 键对应的值，即富文本组件的内容
      title: '',
      fn: Function
    }
  },
  updated () {
    this.initSummernote(this.divId)
    $('#' + this.divId).summernote('code', this.content) // 把初始数据写入进去
  },
  methods: {
    initSummernote (divId) {
      console.log('初始化富文本插件：' + divId)
      $('#' + divId).summernote({
        lang: 'zh-CN',
        placeholder: '请输入内容',
        height: 300,
        width: '100%',
        htmlMode: true,
        toolbar: [
          ['style', ['bold', 'italic', 'underline', 'clear']],
          ['fontsize', ['fontsize']],
          ['fontname', ['fontname']],
          ['para', ['ul', 'ol', 'paragraph']]
        ],
        fontSizes: ['8', '9', '10', '11', '12', '14', '18', '24', '36'],
        fontNames: [
          'Arial', 'Arial Black', 'Comic Sans MS', 'Courier New',
          'Helvetica Neue', 'Helvetica', 'Impact', 'Lucida Grande',
          'Tahoma', 'Times New Roman', 'Verdana'
        ]
      })
    },
    getSummernoteContent (divId) {
      return $('#' + divId).summernote('code')
    },
    setSummernoteContent (divId, content) {
      return $('#' + divId).summernote('code', content)
    },
    edit (divId, record, key, title, fn) {
      this.divId = divId // 初始化元素的id
      this.visible = true
      // 把当前的记录赋值到data中的变量
      Object.assign(this.record, record)
      this.key = key
      this.content = this.record[key] // 拿到键为key对应的属性值
      this.title = title
      this.fn = fn // 自定义更新函数
    },
    handleCancel () {
      this.visible = false
    },
    handleUpdate () {
      const that = this
      that.record[that.key] = $('#' + that.divId).summernote('code')
      that.fn(that.record).then(res => {
        // 成功就跳转到结果页面
        console.log(res)
        if (res.code === 0) {
          that.$notification.success({
            message: '更新成功',
            description: '更新成功'
          })
          // 关闭弹出框
          that.visible = false
          that.$emit('ok')
        }
      }).catch(err => {
        // 失败就弹出警告消息
        that.$notification.error({
          message: '更新失败',
          description: err.message
        })
      })
    }
  }
}
</script>
