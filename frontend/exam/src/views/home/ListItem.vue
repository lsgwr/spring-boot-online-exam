<template>
  <li :key="item.title">
    <div class="page1-box">
      <div class="page1-point-wrapper"></div>
      <div class="page1-image" :style="{ boxShadow: `${item.shadowColor} 0px 6px 12px` }">
        <img :src="item.src" />
      </div>
      <h3>{{ item.title }}</h3>
      <p>{{ item.content }}</p>
    </div>
  </li>
</template>

<script>
export default {
  name: 'ListItem',
  props: {
    item: {
      type: Object,
      required: true
    }
  }
}
</script>
