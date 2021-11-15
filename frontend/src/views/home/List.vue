<template>
  <div>
    <ul class="page1-box-wrapper" v-for="(list, index) in dataSource" :key="index">
      <template v-for="item in list">
        <list-item :item="item" :key="item.title"/>
      </template>
    </ul>
  </div>
</template>

<script>
import ListItem from './ListItem'

export default {
  name: 'List',
  components: {
    ListItem
  },
  props: {
    dataSource: {
      type: Array,
      requited: true,
      default: () => {
        return []
      }
    }
  },
  watch: {
    dataSource (val) {
      console.log('dataSource::update', val)
    }
  }
}
</script>

<style scoped>

</style>
