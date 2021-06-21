<template>
  <div class="app-container">
    <div style="background: #fff; padding: 5px;">
      <el-button
        style="margin-bottom: 10px"
        type="primary"
        size="medium"
        @click="addTab(editableTabsValue)"
        >新建请求</el-button
      >
      <el-tabs
        v-model="editableTabsValue"
        type="border-card"
        closable
        @tab-remove="removeTab"
      >
        <el-tab-pane
          v-for="(item, index) in editableTabs"
          :key="item.name"
          :label="`${item.title}`"
          :name="item.name"
        >
          <Edit
            @requestUrlChange="requestUrlChange"
            :tabsIndex="index"
          ></Edit>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
import Edit from "./Edit-component/Edit.vue";
export default {
  name: "ApiEdit",
  components: {
    Edit  
  },
  data () {
    return {
      editableTabsValue: '1',
      tabIndex: 1,
      editableTabs: [
        {
          title: "New Request",
          name: "1",
        },
      ]
    }
  },
  methods: {
    // 删除Tabs
    removeTab (targetName) {
      console.log(targetName);
      let tabs = this.editableTabs;
      let activeName = this.editableTabsValue;
      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.name;
            }
          }
        });
      }

      this.editableTabsValue = activeName;
      this.editableTabs = tabs.filter((tab) => tab.name !== targetName);
    },
    // 添加新请求
    addTab (targetName) {
      let newTabName = ++this.tabIndex + "";
      this.editableTabs.push({
        title: "New Request",
        name: newTabName,
      });
      this.editableTabsValue = newTabName;
    },

    // 接收请求链接
    requestUrlChange (params) {
      let methodValue = params.methodValue;
      let url = params.requestUrl;
      let tabsIndex = params.tabsIndex;
      let editableTabs = this.editableTabs;
      editableTabs[tabsIndex].title = methodValue + " " + url.slice(0, 20);
      this.editableTabs = editableTabs;
    }
  },
  created() {
    
  },
};
</script>
