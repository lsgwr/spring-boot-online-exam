<template>
  <a-modal
    title="题目信息"
    :width="640"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <h3>题目：{{ question.name }}</h3>
        <ul>
          <li v-for="option in question.options" :key="option.id">
            {{ option.content }}
          </li>
        </ul>
        <h4>答案</h4>
        <ul>
          <li v-for="option in question.options" :key="option.id" v-show="option.answer===true">
            {{ option.content }}
          </li>
        </ul>
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button key="cancel" @click="handleCancel">关闭</a-button>
    </template>
  </a-modal>
</template>

<script>

export default {
  // 问题查看的弹出框，用于查看问题/修改问题
  name: 'QuestionViewModal',
  data () {
    return {
      visible: false,
      confirmLoading: false,

      form: this.$form.createForm(this),
      // 每个问题
      question: {},
      options: [],
      answerOption: ''
    }
  },
  methods: {
    edit (record) {
      this.visible = true
      // 把当前的记录赋值到data中的变量
      this.question = record
    },

    handleCancel () {
      // clear form & currentStep
      this.visible = false
    }
  }
}
</script>
