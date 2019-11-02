<template>
  <div class="banner-wrapper">
    <div class="banner-title-wrapper">
      <div class="title-line-wrapper" style="opacity: 1; transform: translate(0px, 0px);">
        <div class="title-line" style="transform: translateX(-64px);"></div>
      </div>
      <h1 class="" style="opacity: 1; transform: translate(0px, 0px);">Online Exam</h1>
      <p style="opacity: 1; transform: translate(0px, 0px);">
        <span>基于SpringBoot+Vue技术栈开发的在线考试系统</span>
      </p>
      <div class="button-wrapper">
        <a href="https://github.com/19920625lsg">
          <a-button type="primary">预览</a-button>
        </a>
        <a @click="$router.push({ name: 'docs' })">
          <a-button style="margin: 0 16px;">开始使用</a-button>
        </a>
      </div>
    </div>
    <div class="banner-image-wrapper" style="opacity: 1;">
      <a-carousel arrows autoplay>
        <div v-for="i in 5" :key="i">
          <img :src="`/home/cover${i}.jpg`" style="height: 324px"/>
        </div>
      </a-carousel>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Banner'
}
</script>

<style lang="less">
  @import "home";
</style>
