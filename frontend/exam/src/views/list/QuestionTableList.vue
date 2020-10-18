<template>
  <a-card :bordered="false">
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="$refs.createQuestionModal.create()">新建</a-button>
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
          <a-divider type="vertical" />
          <a @click="handleEdit(record)">编辑</a>
        </template>
      </span>
    </s-table>
    <!-- ref是为了方便用this.$refs.modal直接引用，下同 -->
    <step-by-step-question-modal ref="createQuestionModal" @ok="handleOk" />
    <question-view-modal ref="modalView" @ok="handleOk" />
    <question-edit-modal ref="modalEdit" @ok="handleOk" />
    <BootstrapTable
      ref="table"
      :columns="columns"
      :data="tableData"
      :options="options"
    />
  </a-card>
</template>

<script>
import '../../plugins/bootstrap-table'
import { STable } from '../../components'
import QuestionViewModal from './modules/QuestionViewModal'
import QuestionEditModal from './modules/QuestionEditModal'
import StepByStepQuestionModal from './modules/StepByStepQuestionModal'
import CreateForm from './modules/CreateForm'
import { getQuestionList, getQuestionAll } from '../../api/exam'

export default {
  name: 'QuestionTableList',
  components: {
    StepByStepQuestionModal,
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
          title: '序号',
          field: 'serial',
          scopedSlots: { customRender: 'serial' },
          formatter: function (value, row, index) {
            return index + 1 // 这样的话每翻一页都会重新从1开始，
          }
        },
        {
          title: '题干',
          dataIndex: 'name',
          field: 'name',
          width: 250
        },
        {
          title: '分数',
          dataIndex: 'score',
          field: 'score'
        },
        {
          title: '创建人',
          dataIndex: 'creator',
          field: 'creator'
        },
        {
          title: '难度',
          dataIndex: 'level',
          field: 'level'
        },
        {
          title: '题型',
          dataIndex: 'type',
          field: 'type'
        },
        {
          title: '学科',
          dataIndex: 'category',
          field: 'category'
        },
        {
          title: '更新时间',
          dataIndex: 'updateTime',
          field: 'updateTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          field: 'action',
          width: '150px',
          scopedSlots: { customRender: 'action' }
        }
      ],
      // 计算属性，监听parameter变量，当变化时，自动重新请求后端数据。加载数据方法 必须为 Promise 对象.获取分页数据
      loadData: parameter => {
        // 从表格组件中获取分页参数
        console.log('loadData.parameter', parameter)
        // 给queryParam赋值，然后把queryParam传给后端
        return getQuestionList(Object.assign(parameter, this.queryParam))
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
      tableData: [], // bootstrap-table的数据
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
  mounted () {
    this.loadAll() // 加载所有问题的数据
  },
  methods: {
    handleEdit (record) {
      // 弹出一个可修改的输入框
      console.log(record)
      this.$refs.modalEdit.edit(record)
    },
    handleSub (record) {
      // 查看题目
      console.log(record)
      this.$refs.modalView.edit(record)
    },
    handleOk () {
      this.$refs.table.refresh()
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    loadAll () {
      const that = this
      getQuestionAll()
        .then(res => {
          if (res.code === 0) {
            that.tableData = res.data
          } else {
            this.$notification.error({
              message: '获取全部问题的列表失败',
              description: res.msg
            })
          }
        })
    }
  }
}
</script>
