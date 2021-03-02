<template>
  <div>
    <div
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      class="box-card felxCardOther bg"
      v-for="item in recordInfo"
      :key="item.id"
      :taskExecutionId="taskExecutionId"
      :type="type"
    >
      <el-card shadow="hover" class="flexOtherBox">
        <span class="felxName">任务名称</span>
        <el-tooltip
          class="item"
          effect="light"
          :content="item.task_name"
          placement="top-end"
        >
          <span class="flexResult">{{ item.task_name }}</span>
        </el-tooltip>
      </el-card>
      <el-card shadow="hover" class="flexOtherBox">
        <span class="felxName">部门</span>
        <el-tooltip
          class="item"
          effect="light"
          :content="item.dept_name"
          placement="top-end"
        >
          <span class="flexResult">{{ item.dept_name }}</span>
        </el-tooltip>
      </el-card>
      <el-card shadow="hover" class="flexOtherBox">
        <span class="felxName">任务ID</span>
        <el-tooltip
          class="item"
          effect="light"
          :content="item.task_id+''"
          placement="top-end"
        >
          <span class="flexResult">{{ item.task_id }}</span>
        </el-tooltip>
      </el-card>
      <el-card shadow="hover" class="flexOtherBox" v-if="item.api_count != 0">
        <span class="felxName">接口个数</span>
        <el-tooltip
          class="item"
          effect="light"
          :content="item.api_count+''"
          placement="top-end"
        >
          <span class="flexResult">{{ item.api_count }}</span>
        </el-tooltip>
      </el-card>
      <el-card shadow="hover" class="flexOtherBox" v-if="item.success_rate">
        <span class="felxName">成功率</span>
        <el-tooltip
          class="item"
          effect="light"
          :content="item.success_rate+''"
          placement="top-end"
        >
          <span class="flexResult">{{ item.success_rate }}%</span>
        </el-tooltip>
      </el-card>
      <el-card shadow="hover" class="flexOtherBox" v-if="item.operateBy">
        <span class="felxName">执行人</span>
        <el-tooltip
          class="item"
          effect="light"
          :content="item.operateBy"
          placement="top-end"
        >
          <span class="flexResult">{{ item.operateBy }}</span>
        </el-tooltip>
      </el-card>
        <el-card shadow="hover" class="flexOtherBox"  v-if="item.start_at != null">
          <span class="felxName"> {{type=='record'?'录制':type == 'replay'?'回放':'比对'}}开始时间</span>
          <el-tooltip
            class="item"
            effect="light"
            :content="item.start_at"
            placement="top-end"
          >
            <span class="flexResult">{{ item.start_at }}</span>
          </el-tooltip>
        </el-card>
        <el-card shadow="hover" class="flexOtherBox" v-if="item.end_at != null">
          <span class="felxName">{{type=='record'?'录制':type == 'replay'?'回放':'比对'}}结束时间</span>
          <el-tooltip
            class="item"
            effect="light"
            :content="item.end_at"
            placement="top-end"
          >
            <span class="flexResult">{{ item.end_at }}</span>
          </el-tooltip>
        </el-card>
       </div>
  </div>
</template>
<script>
import { findTaskInfo } from "@/api/task/index.js";
export default {
  name: "CommonCard",
  components: {},
  props: ["taskExecutionId", "type"],
  data() {
    return {
      loading: false,
      recordInfo: {},
      queryInfo: {
        type: null,
        taskExecutionId: null,
      },
    };
  },
  mounted() {
    this.getList();
    this.queryInfo.id = this.taskExecutionId;
    this.queryInfo.type = this.type;
  },
  methods: {
    getList() {
      this.loading = true;
      findTaskInfo(this.queryInfo)
        .then((res) => {
          if (res.code == "200" && JSON.stringify(res.data) != "{}") {
            this.recordInfo = JSON.parse("[" + JSON.stringify(res.data) + "]");
          } else {
            this.recordInfo = {};
          }
          setTimeout(() => {
            this.loading = false;
          }, 1000);
        })
        .catch((err) => {
          this.loading = false;
        });
    },
  },
};
</script>
<style lang="scss">
.flexOtherBox {
  flex: 1;
}
.tabsFlex {
  margin-top: 15px;
  .el-tabs__nav-scroll {
    background: #fff;
    padding: 0 20px;
  }
  .el-tabs__item {
    font-size: 16px !important;
  }
  .el-tabs__content {
    background: #fff;
    padding:10px;
  }
}
</style>
