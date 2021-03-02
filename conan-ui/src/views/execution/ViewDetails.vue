<template>
  <div>
    <CommonCard :type="type" :taskExecutionId="id"></CommonCard>
    <el-tabs
      class="tabsFlex"
      v-model="activeName"
      @tab-click="handleClick"
      id="tabs"
    >
      <el-tab-pane label="回放列表" name="first">
        <el-tag style="margin:10px 0">
         点击执行比对时要满足的条件，回放条数大于两条并且要将其中一条置为基准
        </el-tag>
        <DetailsTable v-on:startDiff="getStartDiff"></DetailsTable>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import DetailsTable from "./DetailsTable";
import PrintReplayLog from "./common/printReplayLog";
import CommonCard from "./common/commonCard";
export default {
  name: "ViewDetails",
  components: {
    DetailsTable,
    PrintReplayLog,
    CommonCard,
  },
  data() {
    return {
      activeName: "first",
      type: "",
      id: "",
    };
  },
  created() {
    this.activeName = this.$route.query.activeName;
    this.id = this.$route.query.task_execution_id;
    this.type = this.$route.query.type;
  },
  mounted() {
    console.log(this.$route.query, "this.$route.query");
  },
  methods: {
    // 子传父
    getStartDiff(response) {
      document.getElementById("tabs").scrollIntoView();
      // console.log(response,'子传父==>>response');
      this.activeName = response.activedName;
    },
    handleClick(tab, event) {
      console.log(tab, "我希望看到的数据");
    },
  },
};
</script>
<style lang="scss" >

</style>
