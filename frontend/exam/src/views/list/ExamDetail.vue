<template>
  <a-layout>
    <a-layout-header class="header" style="color: #fff">
      <span style="font-size:25px; font-family: 'Microsoft Sans'">{{ examDetail.exam.examName }}</span>
      <span style="font-size:15px; font-family: Consolas">{{ examDetail.exam.examDescription }} </span>
      <span style="float: right;">
          <span style="margin-right: 60px; font-size: 20px">考试限时：{{ examDetail.exam.examTimeLimit }}分钟 这里是倒计时</span>
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
          :defaultOpenKeys="['question_radio', 'question_check', 'question_check']"
          :style="{ height: '100%', borderRight: 0 }"
        >
          <a-sub-menu key="question_radio">
            <span slot="title"><a-icon type="check-circle" theme="twoTone"/>单选题(每题{{ examDetail.exam.examScoreRadio }}分)</span>
            <a-menu-item v-for="(item, index) in examDetail.radioIds" :key="item">题目{{ index + 1 }}</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="question_check">
            <span slot="title"><a-icon type="check-square" theme="twoTone"/>多选题(每题{{ examDetail.exam.examScoreCheck }}分)</span>
            <a-menu-item v-for="(item, index) in examDetail.checkIds" :key="item">题目{{ index + 1 }}</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="question_check">
            <span slot="title"><a-icon type="like" theme="twoTone"/>判断题(每题{{ examDetail.exam.examScoreJudge }}分)</span>
            <a-menu-item v-for="(item, index) in examDetail.judgeIds" :key="item">题目{{ index + 1 }}</a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout :style="{ marginLeft: '200px' }">
        <a-layout-content :style="{ margin: '24px 16px 0',height: '84vh', overflow: 'initial' }">
          <div :style="{ padding: '24px', background: '#fff',height: '84vh'}">
            ...
            <br/>
            Really
            <br/>...<br/>...<br/>...<br/>
            long
            <br/>...<br/>...<br/>...<br/>...<br/>...<br/>...
            <br/>...<br/>...<br/>...<br/>...<br/>...<br/>...
            <br/>...<br/>...<br/>...<br/>...<br/>...<br/>
            content
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
import { getExamDetail } from '../../api/exam'
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
      examDetail: {}
    }
  },
  mounted () {
    const that = this
    // Todo:从后端获取考试的详细信息，渲染到考试详情里
    getExamDetail(this.$route.params.id)
      .then(res => {
        if (res.code === 0) {
          that.examDetail = res.data
          // 打印考试对象
          console.log(that.examDetail)
          return res.data
        } else {
          this.$notification.error({
            message: '获取问题详情失败',
            description: res.data.msg
          })
        }
      })
  },
  methods: {
    ...mapGetters(['nickname', 'avatar']) // 从全局变量中获取用户昵称和头像
  }
}
</script>

<style scoped>

</style>
