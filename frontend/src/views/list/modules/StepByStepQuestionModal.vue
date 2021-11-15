<template>
  <a-modal title="创建问题" :width="800" :visible="visible" :confirmLoading="confirmLoading" @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-steps :current="currentStep" :style="{ marginBottom: '28px' }" size="small">
        <a-step title="问题内容" />
        <a-step title="问题分类" />
        <a-step title="问题选项" />
      </a-steps>
      <a-form :form="form">
        <!-- step1 -->
        <div v-show="currentStep === 0">
          <a-form-item label="题干" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <div id="summernote-question-name"></div>
          </a-form-item>
          <a-form-item label="解析" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <div id="summernote-question-desc"></div>
          </a-form-item>
        </div>
        <div v-show="currentStep === 1">
          <a-form-item label="题型" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="['type', {rules: [{required: true}]}]" placeholder="请选择题型" style="width: 100%">
              <a-select-option v-for="typeObj in types" :value="typeObj.id" :key="typeObj.id">
                {{ typeObj.description }}
              </a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item label="归类" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="['category', { rules: [{required: true}]}]" placeholder="请选择分类" style="width: 100%">
              <a-select-option v-for="category in categories" :value="category.id" :key="category.id">
                {{ category.name }}
              </a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item label="难度" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="['level', { rules: [{required: true}]}]" placeholder="请选择难度" style="width: 100%">
              <a-select-option v-for="level in levels" :value="level.id" :key="level.id">
                {{ level.description }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </div>

        <div v-show="currentStep === 2">
          <!-- 非判断题的时候显示 -->
          <a-form-item label="创建选项" :labelCol="labelCol" :wrapperCol="wrapperCol" v-if="type!==3">
            <a-input
              v-decorator="['option', { rules: [{required: true}]}]"
              placeholder="输入内容后按Enter添加到下方选项列表"
              @pressEnter="addOption()"
            />
          </a-form-item>

          <a-form-item label="设置答案" :labelCol="labelCol" :wrapperCol="wrapperCol" enterButton="Search">
            <!-- 注意这里要按照单选、多选和判断进行区分 -->
            <!-- 单选 -->
            <a-select
              style="width: 100%"
              placeholder="请选择单选题的答案"
              v-if="type===1"
              @change="handleSingleChange"
              :value="answerOption"
            >

              <a-select-option v-for="(option,index) in options" :value="option.content" :key="index">
                {{ option.content }}
              </a-select-option>
            </a-select>
            <!-- 多选 -->
            <a-select
              mode="multiple"
              :size="size"
              placeholder="请选择多选题的答案"
              :value="answerOptions"
              v-if="type===2"
              style="width: 100%"
              @popupScroll="popupScroll"
              @change="handleMultiChange"
            >
              <a-select-option v-for="(option, index) in options" :value="option.content" :key="index">
                {{ option.content }}
              </a-select-option>
            </a-select>
            <!-- 判断，本质上和单选一样 -->
            <a-select
              style="width: 100%"
              placeholder="请选择判断题的答案"
              v-if="type===3"
              @change="handleSingleChange"
              :value="answerOption"
            >

              <a-select-option v-for="(option,index) in options" :value="option.content" :key="index">
                {{ option.content }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </div>
        <!-- step1 end -->
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button key="back" @click="backward" v-if="currentStep > 0" :style="{ float: 'left' }">上一步</a-button>
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button key="forward" :loading="confirmLoading" type="primary" @click="handleNext(currentStep)">
        {{ currentStep === 2 && '完成' || '下一步' }}
      </a-button>
    </template>
  </a-modal>
</template>

<script>
import '../../../plugins/summernote'
import $ from 'jquery'
import { getQuestionSelection, questionCreate } from '../../../api/exam'

const stepForms = [[], ['type', 'category', 'level'], ['option']]

export default {
  name: 'StepByStepQuestionModal',
  data () {
    return {
      labelCol: {
        xs: { span: 2 },
        sm: { span: 2 }
      },
      size: 'default',
      wrapperCol: {
        xs: { span: 22 },
        sm: { span: 22 }
      },
      // 单选和判断题的答案
      answerOption: '',
      // 多选题的答案
      answerOptions: [],
      visible: false,
      confirmLoading: false,
      currentStep: 0,
      mdl: {},

      form: this.$form.createForm(this),
      categories: [],
      levels: [],
      types: [],
      optionToAdd: '添加',
      options: [],
      judgeOptions: [
        {
          answer: false,
          content: '正确'
        },
        {
          answer: false,
          content: '错误'
        }
      ],
      type: ''
    }
  },
  updated () {
    this.initSummernote('summernote-question-name')
    this.initSummernote('summernote-question-desc')
  },
  methods: {
    initSummernote (divId) {
      console.log('初始化富文本插件：' + divId)
      $('#' + divId).summernote({
        lang: 'zh-CN',
        placeholder: '请输入内容',
        height: 150,
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
    create () {
      this.visible = true
      // const { form: { setFieldsValue } } = this
      // this.$nextTick(() => {
      //   setFieldsValue(pick(record, []))
      // })
      // 把data中的question属性提交到后端，待写完后端接口
      getQuestionSelection().then(res => {
        console.log(res)
        if (res.code === 0) {
          console.log(res.data)
          this.categories = res.data.categories
          this.levels = res.data.levels
          this.types = res.data.types
        } else {
          this.$notification.error({
            message: '获取问题下拉选项失败',
            description: res.msg
          })
        }
      }).catch(err => {
        // 失败就弹出警告消息
        this.$notification.error({
          message: '获取问题下拉选项失败',
          description: err.message
        })
      })
    },
    popupScroll () {
      console.log('popupScroll')
    },
    handleNext (step) {
      // 处理下一步或者完成事件
      const { form: { validateFields } } = this
      const currentStep = step + 1
      if (currentStep <= 2) {
        // stepForms
        validateFields(stepForms[this.currentStep], (errors, values) => {
          if (!errors) {
            this.currentStep = currentStep
            // 设置题目类型(单选1、多选2或判断3)，用于最后一步问题选项的展示
            this.type = values.type
            // 清空必要的数据
            this.answerOptions = []
            this.answerOption = ''
            if (this.type === 3) {
              // 如果是判断题的话，把选项直接改成正确和错误两种
              this.options = this.judgeOptions
            } else {
              // 不是判断题那么每次要保证清空
              this.options = []
            }
          }
        })
        return
      }
      // last step，最后一步，代表完成题目编写，需要点击"完成"创建题目
      this.confirmLoading = true
      validateFields((errors, values) => { // values就是表单中校验的值，后面提交到后端接口时主要就是用这个values
        console.log('提交数据到后端')
        console.log('errors:', errors, 'val:', values)
        values.options = this.options
        values.name = this.getSummernoteContent('summernote-question-name')
        values.desc = this.getSummernoteContent('summernote-question-desc')
        this.confirmLoading = false
        if (!errors) {
          console.log('values:', values)
          // 把data中的question属性提交到后端，待写完后端接口.把前端的创建的题型提交到后端
          questionCreate(values).then(res => {
            // 成功就跳转到结果页面
            console.log(res)
            if (res.code === 0) {
              this.$notification.success({
                message: '创建成功',
                description: '问题创建成功'
              })
              // 关闭弹出框
              this.visible = false
              this.$emit('ok')
            }
          }).catch(err => {
            // 失败就弹出警告消息
            this.$notification.error({
              message: '更新',
              description: err.message
            })
          })
        } else {
          this.confirmLoading = false
        }
      })
    },
    backward () {
      this.currentStep--
    },
    handleCancel () {
      // clear form & currentStep
      this.visible = false
      this.currentStep = 0
    },
    addOption () {
      console.log('添加选项')
      // 这里需要把选项+是否是题目答案作为一个整体添加到options中
      const { form: { validateFields } } = this
      validateFields((errors, values) => { // values就是表单中校验的值，后面提交到后端接口时主要就是用这个values
        console.log('errors:', errors, 'val:', values)
        for (let i = 0; i < this.options.length; i++) {
          const option = this.options[i]
          if (option.content === values.option) {
            this.$notification.error({
              message: '错误',
              description: '不要添加重复选项！'
            })
            return
          }
        }
        const optionObj = { content: values.option, answer: false }
        this.options.push(optionObj)
        this.$notification.success({
          message: '创建选项成功',
          description: '请到\'设置答案\'下拉框去查看！'
        })
      })
    },
    // 单选问题改变选项
    handleSingleChange (value) {
      // 单选题的处理情况
      for (let i = 0; i < this.options.length; i++) {
        if (this.options[i].content === value) {
          // 更新唯一的正确答案
          this.options[i].answer = true
          // 设置答案的内容
          this.answerOption = this.options[i].content
        } else {
          // id不和答案相同地就设置为false，这样可以保证只有一个正确答案
          this.options[i].answer = false
        }
      }
      // 单选题的处理情况
      console.log(`Selected: ${value}`)
    },
    // 多选问题改变选项
    handleMultiChange (values) {
      console.log(values)
      // 直接更新选项id的数组
      this.answerOptions = values
      // 更新question中options的answer位置
      for (let i = 0; i < this.options.length; i++) { // 遍历所有的题目的选项
        // 取出一个选项的id
        const content = this.options[i].content
        let isAnswer = false
        for (let j = 0; j < values.length; j++) { // 拿着
          const value = values[j]
          if (content === value) {
            // 说明这个选项是答案，设置为是答案，直接退出
            isAnswer = true
            this.options[i].answer = true
            break // 跳出内部的for循环
          }
        }
        // 这个选项遍历到最后，发现还不是答案(不在答案数组中)，那么就把这个选项的answer属性设置为false
        if (isAnswer === false) {
          this.options[i].answer = false
        }
      }
    }

  }
}
</script>
