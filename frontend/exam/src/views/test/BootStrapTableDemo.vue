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
        showRefresh: true
      }
    }
  },
  methods: {
    clickRow (row, value, index) {
      alert(JSON.stringify(row) + ', ' + index)
    }
  }
}
</script>
