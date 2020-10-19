<template>
  <a-card :bordered="false">
    <div id="toolbar">
      <a-button type="primary" icon="plus" @click="$refs.createQuestionModal.create()">新建</a-button>&nbsp;
      <a-button type="primary" icon="reload" @click="loadAll()">刷新</a-button>
    </div>
    <BootstrapTable
      ref="table"
      :columns="columns"
      :data="tableData"
      :options="options"
    />
    <!-- ref是为了方便用this.$refs.modal直接引用，下同 -->
    <step-by-step-question-modal ref="createQuestionModal" @ok="handleOk" />
    <question-view-modal ref="modalView" @ok="handleOk" />
    <question-edit-modal ref="modalEdit" @ok="handleOk" />
  </a-card>
</template>

<script>
import '../../plugins/bootstrap-table'
import { STable } from '../../components'
import QuestionViewModal from './modules/QuestionViewModal'
import QuestionEditModal from './modules/QuestionEditModal'
import StepByStepQuestionModal from './modules/StepByStepQuestionModal'
import CreateForm from './modules/CreateForm'
import { getQuestionAll } from '../../api/exam'

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
    const that = this // 方便在bootstrap-table中引用methods
    return {
      // 表头
      columns: [
        {
          title: '序号',
          field: 'serial',
          formatter: function (value, row, index) {
            return index + 1 // 这样的话每翻一页都会重新从1开始，
          }
        },
        {
          title: '题干',
          field: 'name',
          width: 300
        },
        {
          title: '分数',
          field: 'score'
        },
        {
          title: '创建人',
          field: 'creator'
        },
        {
          title: '难度',
          field: 'level'
        },
        {
          title: '题型',
          field: 'type'
        },
        {
          title: '学科',
          field: 'category'
        },
        {
          title: '更新时间',
          field: 'updateTime'
        },
        {
          title: '操作',
          field: 'action',
          align: 'center',
          formatter: (value, row) => {
            return '<button type="button" class="btn btn-success view-question">详情</button>' +
              '&nbsp;&nbsp;' +
              '<button type="button" class="btn btn-success edit-question">编辑</button>'
          },
          events: {
            'click .view-question': function (e, value, row, index) {
              that.handleSub(row)
            },
            'click .edit-question': function (e, value, row, index) {
              that.handleEdit(row)
            }
          }
        }
      ],
      tableData: [], // bootstrap-table的数据
      // custom bootstrap-table
      options: {
        search: true,
        showColumns: true,
        showExport: true,
        pagination: true,
        toolbar: '#toolbar',
        // 下面两行是支持高级搜索，即按照字段搜索
        advancedSearch: true,
        idTable: 'advancedTable'
        // 下面是常用的事件，更多的点击事件可以参考：http://www.itxst.com/bootstrap-table-events/tutorial.html
        // onClickRow: that.clickRow,
        // onClickCell: that.clickCell, // 单元格单击事件
        // onDblClickCell: that.dblClickCell // 单元格双击事件
      }
    }
  },
  mounted () {
    this.loadAll() // 加载所有问题的数据
  },
  methods: {
    handleEdit (record) {
      const that = this
      this.$refs.modalEdit.edit(record, that)
    },
    handleSub (record) {
      // 查看题目
      console.log(record)
      this.$refs.modalView.edit(record)
    },
    handleOk () {
      console.log('进入handleOk啦')
      this.loadAll() // 加载所有问题的数据
    },
    loadAll () {
      console.log('进入loadAll啦')
      const that = this
      getQuestionAll()
        .then(res => {
          if (res.code === 0) {
            that.tableData = res.data
            that.$refs.table._initTable()
          } else {
            that.$notification.error({
              message: '获取全部问题的列表失败',
              description: res.msg
            })
          }
        })
    }
  }
}
</script>
