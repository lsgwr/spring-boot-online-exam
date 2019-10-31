<template>
  <page-view :avatar="avatar" :title="false">
    <h1>这里是首页</h1>
  </page-view>
</template>

<script>

import { PageView } from '../../layouts'

export default {
  name: 'Workplace',
  components: {
    PageView
  },
  data () {
    return {
      avatar: ''
    }
  },
  computed: {},
  created () {
  },
  mounted () {
  },
  methods: {}
}
</script>

<style lang="less" scoped>
</style>
