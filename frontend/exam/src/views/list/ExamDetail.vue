<template>
  <a-layout>
    <a-layout-header class="header" style="color: #fff">
      <!--   v-if="examDetail.exam" 是为了防止 异步请求时页面渲染的时候还没有拿到这个值而报错， 下面多处这个判断都是这个道理 -->
      <span style="font-size:25px;" v-if="examDetail.exam">{{ examDetail.exam.examName }}</span>
      <span style="font-size:15px; font-family: Consolas" v-if="examDetail.exam">{{ examDetail.exam.examDescription }} </span>
      <span style="float: right;">
        <span style="margin-right: 60px; font-size: 20px" v-if="examDetail.exam">考试限时：{{ examDetail.exam.examTimeLimit }}分钟 这里是倒计时</span>
        <a-button type="danger" ghost style="margin-right: 60px;">交卷</a-button>
        <a-avatar class="avatar" size="small" :src="avatar()"/>
        <span style="margin-left: 12px">{{ nickname() }}</span>
      </span>
    </a-layout-header>
    <a-layout>
      <a-layout-sider width="190" :style="{background: '#444',overflow: 'auto', height: '100vh', position: 'fixed', left: 0 }">
        <a-menu
          mode="inline"
          :defaultSelectedKeys="['1']"
          :defaultOpenKeys="['question_radio', 'question_check', 'question_judge']"
          :style="{ height: '100%', borderRight: 0 }"
        >
          <a-sub-menu key="question_radio">
            <span slot="title" v-if="examDetail.exam"><a-icon type="check-circle" theme="twoTone"/>单选题(每题{{ examDetail.exam.examScoreRadio }}分)</span>
            <a-menu-item v-for="(item, index) in examDetail.radioIds" :key="item" @click="getQuestionDetail(item)">题目{{ index + 1 }}</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="question_check">
            <span slot="title" v-if="examDetail.exam"><a-icon type="check-square" theme="twoTone"/>多选题(每题{{ examDetail.exam.examScoreCheck }}分)</span>
            <a-menu-item v-for="(item, index) in examDetail.checkIds" :key="item" @click="getQuestionDetail(item)">题目{{ index + 1 }}</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="question_judge">
            <span slot="title" v-if="examDetail.exam"><a-icon type="like" theme="twoTone"/>判断题(每题{{ examDetail.exam.examScoreJudge }}分)</span>
            <a-menu-item v-for="(item, index) in examDetail.judgeIds" :key="item" @click="getQuestionDetail(item)">题目{{ index + 1 }}</a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout :style="{ marginLeft: '200px' }">
        <a-layout-content :style="{ margin: '24px 16px 0',height: '84vh', overflow: 'initial' }">
          <div :style="{ padding: '24px', background: '#fff',height: '84vh'}">
            <span v-show="currentQuestion === ''" style="font-size: 30px;font-family: Consolas">欢迎参加考试，请点击左侧题目编号开始答题</span>
            <strong>{{ currentQuestion.type }} </strong> {{ currentQuestion.name }}
            <br><br>
            <a-radio-group @change="onRadioChange" v-model="radioValue">
              <a-radio v-for="(option, index) in currentQuestion.options" :key="option.id" :style="radioStyle" :value="index">
                {{ option.questionOptionContent }}
              </a-radio>
            </a-radio-group>
          </div>
        </a-layout-content>
        <a-layout-footer :style="{ textAlign: 'center' }">
          Spting Boot Online Exam ©2019 Crated by Liang Shan Guang
        </a-layout-footer>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script>
import { getExamDetail, getQuestionDetail } from '../../api/exam'
import UserMenu from '../../components/tools/UserMenu'
import { mapGetters } from 'vuex'

export default {
  name: 'ExamDetail',
  components: {
    UserMenu
  },
  data () {
    return {
      // 考试详情对象
      examDetail: {},
      // Todo:用户做过的问题都放到这个数组中，键为问题id, 值为currentQuestion(其属性answers属性用于存放答案选项地id或ids),，用于存放用户勾选的答案
      questions: {},
      // 当前用户的问题
      currentQuestion: '',
      // 当前问题用户选择的答案们
      checked: [],
      // 单选或判断题的绑定值
      radioValue: '',
      // 多选题的绑定值
      checkValues: [],
      radioStyle: {
        display: 'block',
        height: '30px',
        lineHeight: '30px'
      }
    }
  },
  mounted () {
    const that = this
    // 从后端获取考试的详细信息，渲染到考试详情里
    getExamDetail(this.$route.params.id)
      .then(res => {
        if (res.code === 0) {
          that.examDetail = res.data
          // 打印考试对象
          console.log(that.examDetail)
          return res.data
        } else {
          this.$notification.error({
            message: '获取考试详情失败',
            description: res.data.msg
          })
        }
      })
  },
  methods: {
    // 从全局变量中获取用户昵称和头像,
    ...mapGetters(['nickname', 'avatar']),
    getQuestionDetail (questionId) {
      // 问题切换时从后端拿到问题详情，渲染到前端content中
      console.log(questionId)
      this.radioValue = ''
      this.checkValues = []
      const that = this
      getQuestionDetail(questionId)
        .then(res => {
          if (res.code === 0) {
            that.currentQuestion = res.data
            // 打印考试对象
            console.log(that.currentQuestion)
            return res.data
          } else {
            this.$notification.error({
              message: '获取问题详情失败',
              description: res.data.msg
            })
          }
        })
    },
    onRadioChange (e) {
      console.log('radio checked', e.target.value)
    }
  }
}
</script>

<style scoped>

</style>
