<template>
  <a-locale-provider :locale="locale">
    <div id="app">
      <router-view />
    </div>
  </a-locale-provider>
</template>

<script>
import zhCN from 'ant-design-vue/lib/locale-provider/zh_CN'
import { AppDeviceEnquire } from './utils/mixin'

export default {
  mixins: [AppDeviceEnquire],
  data () {
    return {
      locale: zhCN
    }
  },
  mounted () {

  }
}
</script>
<style>
#app {
  height: 100%;
}

h1 {
  line-height: inherit !important;
}

.page-header .breadcrumb {
  background-color: transparent !important;
  padding: inherit !important;
}

.row {
  margin-left: inherit !important;
}

a:hover {
  text-decoration: none !important;
  line-height: inherit !important;
}

i > svg {
  vertical-align: initial !important;
}
</style>
