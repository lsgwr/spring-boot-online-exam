<template>
  <page-view :title="false">
    <banner/>
    <page1/>
  </page-view>
</template>

<script>
import { PageView } from '../layouts'
import Banner from './home/Banner'
import Page1 from './home/Page1'

export default {
  name: 'Home',
  components: {
    PageView,
    Banner,
    Page1
  }
}
</script>
