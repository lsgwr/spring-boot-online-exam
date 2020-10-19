<template>
  <a-card :bordered="false">
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="$refs.createExamModal.create()">新建</a-button>
    </div>

    <s-table
      ref="table"
      size="default"
      rowKey="key"
      :columns="columns"
      :data="loadData"
      :alert="options.alert"
      :rowSelection="options.rowSelection"
    >
      <span slot="serial" slot-scope="text, record, index">
        {{ index + 1 }}
      </span>

      <span slot="action" slot-scope="text, record">
        <template>
          <a @click="handleSub(record)">详情</a>
          <a-divider type="vertical"/>
          <a @click="handleEdit(record)">编辑</a>
        </template>
      </span>

    </s-table>
    <!-- ref是为了方便用this.$refs.modal直接引用，下同 -->
    <step-by-step-exam-modal ref="createExamModal" @ok="handleOk"/>
    <question-view-modal ref="modalView" @ok="handleOk"/>
    <question-edit-modal ref="modalEdit" @ok="handleOk"/>
  </a-card>
</template>

<script>
import { STable } from '../../components'
import QuestionViewModal from './modules/QuestionViewModal'
import QuestionEditModal from './modules/QuestionEditModal'
import CreateForm from './modules/CreateForm'
import { getExamList, getExamAll } from '../../api/exam'
import StepByStepExamModal from './modules/StepByStepExamModal'

export default {
  name: 'ExamTableList',
  components: {
    StepByStepExamModal,
    STable,
    CreateForm,
    QuestionViewModal,
    QuestionEditModal
  },
  data () {
    return {
      // 表头
      columns: [
        {
          title: '#',
          scopedSlots: { customRender: 'serial' }
        },
        {
          title: '名称',
          dataIndex: 'name',
          width: 250
        },
        {
          title: '总分数',
          dataIndex: 'score'
        },
        {
          title: '创建人',
          dataIndex: 'creator'
        },
        {
          title: '时长',
          dataIndex: 'elapse'
        },
        {
          title: '更新时间',
          dataIndex: 'updateTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '150px',
          scopedSlots: { customRender: 'action' }
        }
      ],
      // 取parameter变量，当变化时，自动重新请求后端数据。加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        // 从表格组件中获取分页参数
        console.log('loadData.parameter', parameter)
        // 给queryParam赋值，然后把queryParam传给后端,待数据验证
        return getExamList(Object.assign(parameter, this.queryParam))
          .then(res => {
            if (res.code === 0) {
              return res.data
            } else {
              this.$notification.error({
                message: '获取问题列表失败',
                description: res.msg
              })
            }
          })
      },
      selectedRowKeys: [],
      selectedRows: [],

      // custom table alert & rowSelection
      options: {
        alert: {
          show: true,
          clear: () => {
            this.selectedRowKeys = []
          }
        },
        rowSelection: {
          selectedRowKeys: this.selectedRowKeys,
          onChange: this.onSelectChange
        }
      }
    }
  },
  methods: {
    handleEdit (record) {
      // Todo:修改考试信息和下面的题目，弹出一个可修改的输入框，实际上复用创建题目的模态框即可，还没做完
      console.log(record)
      this.$refs.modalEdit.edit(record)
    },
    handleSub (record) {
      // 查看题目，不在模态框里查啦，太麻烦
      // console.log(record)
      // this.$refs.modalView.edit(record)

      // 直接跳到参加考试的页面，查看所有题目的详细情况
      const routeUrl = this.$router.resolve({
        path: `/exam/${record.id}`
      })
      // 和点击考试卡片效果一样，跳转到考试页面，里面有所有题目的情况，相当于就是详情了
      window.open(routeUrl.href, '_blank')
    },
    handleOk () {
      this.$refs.table.refresh()
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    }
  }
}
</script>
