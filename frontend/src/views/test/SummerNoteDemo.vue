<template>
  <div>
    <div id="summernote"></div>
    <button @click="getContent()">查看富文本内容</button>
    <button @click="setContent()">设置富文本内容</button>
  </div>
</template>

<script>
import '../../plugins/summernote'
import $ from 'jquery'

export default {
  name: 'NoteDemo',
  data () {
    return {
      richContent: ''
    }
  },
  methods: {
    getContent () {
      console.log($('#summernote').summernote('code'))
    },
    setContent () {
      $('#summernote').summernote('code', 'hello world!')
    }
  },
  mounted () {
    $('#summernote').summernote({
      lang: 'zh-CN',
      placeholder: '请输入内容',
      height: 300,
      width: 600,
      htmlMode: true,
      toolbar: [
        ['style', ['bold', 'italic', 'underline', 'clear']],
        ['fontsize', ['fontsize']],
        ['fontname', ['fontname']],
        ['color', ['color']],
        ['para', ['ul', 'ol', 'paragraph']],
        ['insert', ['link', 'picture']],
        ['mybutton', ['myVideo']]
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
  }
}
</script>
