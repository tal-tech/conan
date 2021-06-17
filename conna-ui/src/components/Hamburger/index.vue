<template>
  <div style="padding: 0 15px;" @click="toggleClick">
    <i class="hamburger iconfont icon-zhedie" :class="{'is-active':isActive}"></i>
  </div>
</template>

<script>
export default {
  name: 'Hamburger',
  props: {
    isActive: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    toggleClick() {
      this.$emit('toggleClick')
    }
  }
}
</script>

<style scoped>
.hamburger {
  display: inline-block;
  vertical-align: middle;
  color: #fff;
}

.hamburger.is-active {
  transform: rotate(180deg);
}
</style>
