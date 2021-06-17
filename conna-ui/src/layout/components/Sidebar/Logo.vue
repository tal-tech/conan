<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}" :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBg : variables.menuLightBg }">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link isImg" to="/">
        <!-- <i  class="iconfont icon-zhibo sidebar-logo" ></i> -->
        <img src="../../../assets/images/profile.png" alt="" style="width:32px;height:32px;line-height: 50px;margin-right: 15px;">
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link isImg" to="/">
        <!-- <i class="icon-zhibo iconfont sidebar-logo"></i> -->
       <img src="../../../assets/images/profile.png" alt="" style="width:32px;height:32px;line-height: 50px;margin-right: 15px;">
        <h1 class="sidebar-title icon-kenan iconfont " :style="{ color: sideTheme === 'theme-dark' ? variables.sidebarTitle : variables.sidebarLightTitle }"></h1>
      </router-link>
    </transition>
  </div>
</template>

<script>

import variables from '@/assets/styles/variables.scss'

export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  computed: {
    variables() {
      return variables;
    },
	sideTheme() {
      return this.$store.state.settings.sideTheme
    }
  },
  data() {
    return {
      title: '柯南流量回放平台',
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 50px;
  line-height: 50px;
  background: #2b2f3a;
  text-align: center;
  overflow: hidden;
  .isImg{
     display: flex !important;
    align-items: center !important;
    justify-content: center !important;
  }
  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    & .sidebar-logo {
      width: 32px;
      height: 32px;
      font-size: 22px;
      color:#1890ff;
      vertical-align: middle;
      margin-right: 22px;
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      font-weight: 600;
      line-height: 50px;
      font-size: 22px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }
  }

  &.collapse {
    .sidebar-logo {
      margin-right: 0px;
      color:#1890ff
    }
  }
}
</style>
