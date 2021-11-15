<template>
  <a-modal title="编辑题目" :width="640" :visible="visible" :confirmLoading="confirmLoading" @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <h3><b>题干：</b></h3>
        <div id="summernote-question-name-edit" />
        <ul v-show="question.type==='多选题'">
          <li v-for="option in question.options" :key="option.id">
            <a-input v-model="option.content" />
          </li>
        </ul>

        <ul v-show="question.type!=='多选题'">
          <li v-for="option in question.options" :key="option.id">
            <a-input v-model="option.content" />
          </li>
        </ul>

        <h3><b>答案：</b></h3>
        <ul v-show="question.type!=='多选题'">
          <li>
            <a-select :size="size" :value="answerOptionId" style="width: 100%" @change="handleSingleChange">
              <a-select-option v-for="option in question.options" :key="option.id">
                {{ option.content }}
              </a-select-option>
            </a-select>
          </li>
        </ul>

        <ul v-show="question.type==='多选题'">
          <li>
            <a-select
              mode="multiple"
              :size="size"
              placeholder="Please select"
              :value="answerOptionIds"
              style="width: 100%"
              @change="handleMultiChange"
              @popupScroll="popupScroll"
            >
              <a-select-option v-for="option in question.options" :key="option.id">
                {{ option.content }}
              </a-select-option>
            </a-select>
          </li>
        </ul>
        <h3><b>解析：</b></h3>
        <div id="summernote-question-desc-edit" />
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button key="cancel" @click="handleCancel">关闭</a-button>
      <a-button key="update" type="primary" @click="handleUpdate">更新</a-button>
    </template>
  </a-modal>
</template>

<script>
import '../../../plugins/summernote'
import $ from 'jquery'
import { questionUpdate } from '../../../api/exam'

export default {
  // 问题查看的弹出框，用于查看问题/修改问题
  name: 'QuestionEditModal',
  data () {
    return {
      visible: false,
      size: 'default',
      confirmLoading: false,

      form: this.$form.createForm(this),
      // 每个问题
      question: {},
      // 单选和判断题的答案
      answerOptionId: '',
      // 多选题的答案
      answerOptionIds: [],
      radioStyle: {
        display: 'block',
        height: '30px',
        lineHeight: '30px'
      },
      name: '',
      desc: ''
    }
  },

  updated () {
    this.initSummernote('summernote-question-name-edit')
    this.initSummernote('summernote-question-desc-edit')
    this.setSummernoteContent('summernote-question-name-edit', this.name)
    this.setSummernoteContent('summernote-question-desc-edit', this.desc)
  },
  methods: {
    initSummernote (divId) {
      console.log('初始化富文本插件：' + divId)
      $('#' + divId).summernote({
        lang: 'zh-CN',
        placeholder: '请输入内容',
        height: 200,
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
        ],
        callbacks: {
          onSubmit: function () {
            this.richContent = $('#summernote').summernote('code')
          }
        }
      })
    },
    getSummernoteContent (divId) {
      return $('#' + divId).summernote('code')
    },
    setSummernoteContent (divId, content) {
      return $('#' + divId).summernote('code', content)
    },
    edit (record) {
      this.name = record.name // 题干
      this.desc = record.description // 解析
      // 把当前的记录赋值到data中的变量
      this.question = record
      // 上来先把之前的清理干净
      this.answerOptionId = ''
      this.answerOptionIds = []
      this.visible = true
      // 单选题的处理情况,设置默认值
      for (let i = 0; i < this.question.options.length; i++) {
        if (this.question.options[i].answer === true) {
          // 设置单选题或者判断题答案
          this.answerOptionId = this.question.options[i].id
          // 设置多选题的答案
          this.answerOptionIds.push(this.question.options[i].id)
        }
      }
    },

    handleCancel () {
      // clear form & currentStep
      this.visible = false
    },

    handleSingleChange (value) {
      // 单选题的处理情况
      for (let i = 0; i < this.question.options.length; i++) {
        if (this.question.options[i].id === value) {
          // 更新唯一的正确答案
          this.question.options[i].answer = true
          // 设置答案的内容
          this.answerOptionId = this.question.options[i].id
        } else {
          // id不和答案相同地就设置为false，这样可以保证只有一个正确答案
          this.question.options[i].answer = false
        }
      }
      console.log(`Selected: ${value}`)
    },

    handleMultiChange (values) {
      console.log(values)
      // 直接更新选项id的数组
      this.answerOptionIds = values
      // 更新question中options的answer位置
      for (let i = 0; i < this.question.options.length; i++) { // 遍历所有的题目的选项
        // 取出一个选项的id
        const id = this.question.options[i].id
        let isAnswer = false
        for (let j = 0; j < values.length; j++) { // 拿着
          const value = values[j]
          if (id === value) {
            // 说明这个选项是答案，设置为是答案，直接退出
            isAnswer = true
            this.question.options[i].answer = true
            break // 跳出内部的for循环
          }
        }
        // 这个选项遍历到最后，发现还不是答案(不在答案数组中)，那么就把这个选项的answer属性设置为false
        if (isAnswer === false) {
          this.question.options[i].answer = false
        }
      }
    },

    popupScroll () {
      console.log('popupScroll')
    },

    handleUpdate () {
      const that = this
      that.question.name = that.getSummernoteContent('summernote-question-name-edit')
      that.question.description = that.getSummernoteContent('summernote-question-desc-edit')
      console.log(that.question)
      // 把data中的question属性提交到后端，待写完后端接口
      questionUpdate(that.question).then(res => {
        // 成功就跳转到结果页面
        console.log(res)
        if (res.code === 0) {
          that.$notification.success({
            message: '更新成功',
            description: '问题更新成功'
          })
          // 关闭弹出框
          that.visible = false
          that.$emit('ok')
        }
      }).catch(err => {
        // 失败就弹出警告消息
        that.$notification.error({
          message: '更新',
          description: err.message
        })
      })
    }
  }
}
</script>
