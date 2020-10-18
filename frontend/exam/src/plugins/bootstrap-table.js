import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import 'bootstrap-table/dist/bootstrap-table.min.css'
import '@fortawesome/fontawesome-free/css/all.min.css'
import Vue from 'vue'
import 'bootstrap'
import 'tableexport.jquery.plugin/libs/FileSaver/FileSaver.min.js'
import 'tableexport.jquery.plugin/tableExport.min.js'
import 'bootstrap-table/dist/bootstrap-table'
import BootstrapTable from 'bootstrap-table/dist/bootstrap-table-vue.esm'
import 'bootstrap-table/dist/extensions/export/bootstrap-table-export'
import 'bootstrap-table/dist/extensions/toolbar/bootstrap-table-toolbar.min'
import jQuery from 'jquery'
window.jQuery = jQuery
window.$ = jQuery
Vue.component('BootstrapTable', BootstrapTable)
