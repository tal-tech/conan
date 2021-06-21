<template>
  <div>
    <svg-icon icon-class="question" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'ConanDoc',
  data() {
    return {
      url: 'http://app.xesv5.com/ep-docs/CONAN/'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>