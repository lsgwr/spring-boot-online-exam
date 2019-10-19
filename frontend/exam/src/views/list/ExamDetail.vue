<template>
  <div>
    <h1>{{ this.$route.params.id }}</h1>
    <a-row>
      <a-col :span="6">col-6 题目列表</a-col>
      <a-col :span="18">col-18 题目详情</a-col>
    </a-row>
  </div>
</template>

<script>
export default {
  name: 'ExamDetail',
  mounted () {
    // Todo:从后端获取考试的详细信息，渲染到考试详情里
  }
}
</script>

<style scoped>

</style>
