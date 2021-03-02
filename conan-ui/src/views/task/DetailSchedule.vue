<template>
  <div>
    <div class="box-card felxCard bg">
      <el-card shadow="hover" class="felxBox">
        <span class="felxName">域名</span>
        <span class="flexResult">{{ domain }}</span>
      </el-card>
      <el-card shadow="hover" class="felxBox">
        <span class="felxName">接口</span>
        <span class="flexResult">{{ apiName }}</span>
      </el-card>
      <el-card shadow="hover" class="felxBox">
        <span class="felxName">成功率</span>
        <span class="flexResult">{{ successRate }}</span>
      </el-card>
    </div>
    <el-card
      style="margin-top: 15px"
      body-style="color-info:"
      v-for="(item, index) in replayResultByApi"
      :key="index"
    >
      <div class="detail">
        <pre class="spanStyle">
第{{ index + 1 }}条流量回放详情
          request = {{ item.request_body }}</pre
        >
        <span class="spanStyle"> <br />response = </span>
        <div class="response"><Json :jsonString="item.response"></Json></div>
      </div>
    </el-card>
  </div>
</template>

<script >
export default {
  name: "DetailSchedule",
  data() {
    return {
      domain: "",
      apiName: "",
      successRate: "",
      replayResultByApi: [],
      queryParams: {
        id: "",
      },
    };
  },
  created() {
    this.queryParams.id = this.$route.query.id;
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      searchReplayDetailByApi(this.queryParams).then((res) => {
        if (res.code == 200) {
          this.domain = res.data.domain;
          this.apiName = res.data.api;
          this.successRate = res.data.successRate;
          this.replayResultByApi = res.data.replay_detail;
        } else {
          this.$message.error(res.data);
        }
      });
    },
  },
};
</script>

<style lang="less" scoped>

</style>
