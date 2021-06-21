<template>
  <div id="app">
    <router-view />
    <div class="conan-watermark"></div>
  </div>
</template>

<script>
export default  {
  name:  'App'
}
</script>
<style lang="scss" scoped>
   .conan-watermark {
    // background-image: url("../src/assets/images/conan-watermark.png") !important;
    background-size: 330px, auto;
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    z-index: 999999;
    pointer-events: none;
    opacity: 0.05;
  }
</style>