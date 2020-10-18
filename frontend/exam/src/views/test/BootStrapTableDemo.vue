<template>
  <div>
    <BootstrapTable
      ref="table"
      :columns="columns"
      :data="data"
      :options="options"
    />
  </div>
</template>

<script>
import '../../plugins/bootstrap-table'
import $ from 'jquery'

export default {
  data () {
    const that = this // 方便在bootstrap-table中引用methods
    return {
      columns: [
        {
          field: 'state',
          checkbox: true
        },
        {
          title: 'Item ID',
          field: 'id'
        },
        {
          field: 'name',
          title: 'Item Name'
        },
        {
          field: 'price',
          title: 'Item Price'
        },
        {
          field: 'actions',
          title: 'Actions',
          align: 'center',
          formatter: (value, row) => {
            return '<button type="button" class="btn btn-success mybtn">Success</button>'
          },
          events: {
            'click .mybtn': function (e, value, row, index) { // 双击是dblclick
              that.clickRow(row, value, index)
            }
          }
        }
      ],
      data: {
        total: 5,
        rows: [
          {
            id: 1,
            name: 'Item 1',
            price: '$1'
          },
          {
            id: 2,
            name: 'Item 2',
            price: '$2'
          },
          {
            id: 3,
            name: 'Item 3',
            price: '$3'
          },
          {
            id: 4,
            name: 'Item 4',
            price: '$4'
          },
          {
            id: 5,
            name: 'Item 5',
            price: '$5'
          }
        ]
      },
      options: {
        search: true,
        showColumns: true,
        showExport: true,
        showRefresh: true,
        // 下面两行是支持高级搜索，即按照字段搜索
        advancedSearch: true,
        idTable: 'advancedTable',
        // 下面是常用的事件，更多的点击事件可以参考：http://www.itxst.com/bootstrap-table-events/tutorial.html
        // onClickRow: that.clickRow,
        // onClickCell: that.clickCell, // 单元格单击事件
        onDblClickCell: that.dblClickCell // 单元格双击事件
      }
    }
  },
  methods: {
    clickRow (row, value, index) {
      alert(JSON.stringify(row) + ', ' + index)
    },
    clickCell (field, value, row, $element) {
      alert(JSON.stringify(field))
    },
    /**
     * 单元格双击事件
     * @param field 字段名，如id、name、price等
     * @param value 字段值 如Item 1、$1等
     * @param row 一行的值，如{"id":3,"name":"Item 3","price":"$3"}
     * @param $element 行所在的dom元素，可以动态往里面加入页面元素，比如双击可编辑或者弹出富文本框等
     */
    dblClickCell (field, value, row, $element) {
      console.log($element.text()) // 获取元素的值
      console.log($element.html()) // 获取页面元素的值
      const p = $('<p>弹框出现前修改内容</p>')
      $element.append(p)
      console.log($element.text()) // 获取元素的值
      row[field] = $element.text() // 更新行的值
      alert(field + ':' + row + ', ' + JSON.stringify(row))
      const index = $element.parent().attr('data-index') // 获取当前行的数据的id，方便更新data中的rows数组
      row[field] = '弹框出现后修改内容'
      this.data.rows[index] = row
      this.$refs.table._initTable() // 直接看源码找出这么个重新渲染表格的事件
    }
  }
}
</script>
