<template>
  <page-view :avatar="avatar" :title="false">
    <a-row :gutter="16">
      <a-col>
        <a-carousel :afterChange="onChange" autoplay>
          <div><img src="https://i.loli.net/2019/11/02/qgXokaYh4bINzUP.jpg" class="carousel-img" alt=""></div>
          <div><img src="https://i.loli.net/2019/11/02/stbkA5xBDejJ2If.jpg" class="carousel-img" alt=""></div>
          <div><img src="https://i.loli.net/2019/11/02/6KOX21wl8CTkfQS.jpg" class="carousel-img" alt=""></div>
          <div><img src="https://i.loli.net/2019/11/02/uvCyWOmreo47Txw.jpg" class="carousel-img" alt=""></div>
        </a-carousel>
      </a-col>
    </a-row>
    <a-row :gutter="16">
      <a-col :span="6">
        <a-card hoverable style="width: 100%;height: 100%">
          <img
            alt="example"
            src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png"
            slot="cover"
          />
          <a-card-meta title="Card title" description="This is the description">
            <a-avatar
              slot="avatar"
              src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
            />
          </a-card-meta>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card hoverable style="width: 100%;height: 100%">
          <img
            alt="example"
            src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png"
            slot="cover"
          />
          <a-card-meta title="Card title" description="This is the description">
            <a-avatar
              slot="avatar"
              src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
            />
          </a-card-meta>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card hoverable style="width: 100%;height: 100%">
          <img
            alt="example"
            src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png"
            slot="cover"
          />
          <a-card-meta title="Card title" description="This is the description">
            <a-avatar
              slot="avatar"
              src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
            />
          </a-card-meta>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card hoverable style="width: 100%;height: 100%">
          <img
            alt="example"
            src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png"
            slot="cover"
          />
          <a-card-meta title="Card title" description="This is the description">
            <a-avatar
              slot="avatar"
              src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
            />
          </a-card-meta>
        </a-card>
      </a-col>
    </a-row>
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
  methods: {
    onChange (a, b, c) {
      console.log(a, b, c)
    }
  }
}
</script>

<style scoped>
  .carousel-img {
    width: 100%;
    height: 100%;
  }
</style>
