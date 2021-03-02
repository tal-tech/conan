<template>
  <DetailsExecution></DetailsExecution>
</template>
<script>
import DetailsExecution from "../execution/list";

export default {
  name: "TaskViewDetails",
  components: {
    DetailsExecution
  },
  data() {
    return {
    };
  },
  mounted() {
    // this.getList();
  },
  methods: {
    // 子传父
    getStartDiff(val){
      document.getElementById("tabs").scrollIntoView();
      this.activeName=val;
    },
    getList() {
      let obj = {
        task_execution_id: 3012
      };
      getRecord(obj).then(res => {
        this.recordInfo = JSON.parse("[" + JSON.stringify(res.data.desc) + "]");
      });
    },
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
<style lang="scss">

</style>
