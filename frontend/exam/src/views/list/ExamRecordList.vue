<template>
  <div>
    <a-card
      style="margin-top: 24px"
      :bordered="false"
      title="参加过的考试">

      <div slot="extra">
        <a-input-search style="margin-left: 16px; width: 272px;"/>
      </div>
      <a-list size="large">
        <a-list-item :key="index" v-for="(item, index) in data">
          <a-list-item-meta :description="item.exam.examDescription">
            <a-avatar slot="avatar" size="large" shape="square" :src="item.exam.examAvatar"/>
            <a slot="title">{{ item.exam.examName }}</a>
          </a-list-item-meta>
          <div slot="actions">
            <a>查看考试详情</a>
          </div>
          <div class="list-content">
            <div class="list-content-item">
              <span>Owner</span>
              <p>{{ item.user.userUsername }}</p>
            </div>
            <div class="list-content-item">
              <span>开始时间</span>
              <p>{{ item.examRecord.examJoinDate }}</p>
            </div>
            <div class="list-content-item">
              <span>分数</span>
              <p>{{ item.examRecord.examJoinScore }}</p>
            </div>
          </div>
        </a-list-item>
      </a-list>

    </a-card>
  </div>
</template>

<script>
import HeadInfo from '../../components/tools/HeadInfo'
import { getExamRecordList } from '../../api/exam'

export default {
  // 考试记录列表，记录考生参加过地所有考试和考试成绩
  name: 'ExamRecordList',
  components: {
    HeadInfo
  },
  data () {
    return {
      data: {}
    }
  },
  mounted () {
    // 从后端数据获取考试列表，适配前端卡片
    getExamRecordList().then(res => {
      if (res.code === 0) {
        this.data = res.data
      } else {
        this.$notification.error({
          message: '获取考试记录失败',
          description: res.msg
        })
      }
    }).catch(err => {
      // 失败就弹出警告消息
      this.$notification.error({
        message: '获取考试记录失败',
        description: err.message
      })
    })
  }
}
</script>

<style lang="less" scoped>
  .ant-avatar-lg {
    width: 48px;
    height: 48px;
    line-height: 48px;
  }

  .list-content-item {
    color: rgba(0, 0, 0, .45);
    display: inline-block;
    vertical-align: middle;
    font-size: 14px;
    margin-left: 40px;

    span {
      line-height: 20px;
    }

    p {
      margin-top: 4px;
      margin-bottom: 0;
      line-height: 22px;
    }
  }
</style>
