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

<script>
import Json from "../../conanApi/Edit-component/Json.vue";
import { searchReplayDetailByApi } from "@/api/execution/printReplayLog.js";
export default {
  name: "replayDetail",
  components: { Json },
  data() {
    return {
      domain: "",
      apiName: "",
      successRate: "",
      replayResultByApi: [],
      queryParams: {
        api_id: "",
        replay_id: "",
      },
    };
  },
  created() {
    this.queryParams.api_id = this.$route.query.api_id;
    this.queryParams.replay_id = this.$route.query.replay_id;
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
.detail {
  align-content: center;
  width: 100%;
  margin-top: 10px;
  min-height: 50px;
  max-height: 600px;
  padding: 0 10px 10px 50px;
  box-sizing: border-box;
  border: 0 solid #ebeef5;
  border-radius: 5px;
  overflow-y: auto;
  .spanStyle {
    font-weight: 900;
    font-size: 15px;
    line-height: 24px;
    // background: aliceblue;
  }
}
</style>
