<template>
  <div>
    <svg-icon icon-class="github" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'ConanGit',
  data() {
    return {
      url: 'http://conan.xesv5.com/conan/home/home'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>