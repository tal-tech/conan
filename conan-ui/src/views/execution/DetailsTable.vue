<template>
  <div class="tableIcon">
    <el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" :data="dataList" :header-cell-style="{ background: '#F3F4F7', color: '#555' }" fit
      max-height="590" tooltip-effect="light">
      <el-table-column align="center" label="#" type="index"></el-table-column>
      <el-table-column align="center" label="回放开始时间" prop="start_at"></el-table-column>
      <el-table-column align="center" label="回放结束时间" prop="end_at"></el-table-column>
      <el-table-column align="center" label="回放成功率" prop="success_rate">
        <template slot-scope="scope">
          <el-progress :text-inside="true" :stroke-width="20" :percentage="Number(scope.row.success_rate)"></el-progress>
        </template>
      </el-table-column>
      <el-table-column align="center" label="回放操作人" prop="operate_by"></el-table-column>
      <el-table-column align="center" label="执行环境" prop="replay_env"></el-table-column>
      <el-table-column align="center" label="是否基准" prop="is_baseline">
        <template slot-scope="scope">
          <el-tag effect="plain" :type="
              scope.row.is_baseline === true
                ? 'success'
                : scope.row.status == null
                ? 'danger'
                : 'info'
            ">
            {{ scope.row.is_baseline == true ? "是" : "否" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="light" content="置为基准" placement="top-start">
            <el-button @click="setAsBaseLine(scope.row.id)" v-if="scope.row.is_baseline != true" size="mini" plain type="success" class="iconfont icon-zhiding"></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="light" content="执行比对" placement="top-start">
            <el-button v-on:click="startDiff(scope.row.id)" v-if="
                scope.row.success_rate != '0' &&
                scope.row.success_rate != null &&
                scope.row.is_baseline != true
              " size="mini" circle class="icon-ziyuan1282 iconfont el-button--lvse"></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="light" content="比对结果" placement="top-start">
            <el-button @click="getDiffList(scope.row)" v-if="scope.row.success_rate != '0'" type="primary" circle size="mini" class="iconfont icon-jiaochabidui"></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="light" content="回放日志" placement="top-start">
            <el-button @click="getReplayDetail(scope.$index, scope.row)" size="mini" circle type="warning" class="iconfont icon-rizhi"></el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList()" />
  </div>
</template>
<script>
import {
  setBaseLineByTaskExecutionID,
  getToDiff,
  getDiffIdByReplayId,
} from '@/api/execution/printDiffLog.js';

import { getReplay } from '@/api/execution/printReplayLog.js';
export default {
  name: 'DetailsTable',
  data() {
    return {
      recordInfo: {},
      dataList: [],
      loading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0, //总页数
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      let obj = {
        task_execution_id: this.$route.query.task_execution_id,
      };
      getReplay(obj)
        .then((res) => {
          if (res.code == 200) {
            this.dataList = res.data;
          }
          this.loading = false;
        })
        .catch((err) => {
          this.loading = false;
          this.dataList = [];
        });
    },
    // 设为基准
    setAsBaseLine(replay_id) {
      let obj = {
        replayId: replay_id,
      };
      setBaseLineByTaskExecutionID(obj)
        .then((res) => {
          if (res.code == 200) {
            this.getList();
          } else {
            console.log('设置失败');
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 执行比对
    startDiff(replayId) {
      console.log(replayId, 'replayId');
      let obj = {
        replayId: replayId,
      };
      getToDiff(obj)
        .then((res) => {
          if (res.code == '200') {
            this.$router.push({
              name: 'printDiffLog',
              query: {
                replayId: replayId,
                type: 'diff',
                task_execution_id: this.$route.query.task_execution_id,
                diffId: res.data.data.data.diffId,
              },
            });
          } else {
            console.log(res);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //比对结果
    getDiffList(row) {
      console.log(row, 'diff');
      let obj = {
        replayId: row.id,
      };
      getDiffIdByReplayId(obj).then((res) => {
        console.log(res);
        if (res.data == 0) {
          this.msgError('请先执行比对后在进行比对结果操作');
        } else {
          this.$router.push({
            name: 'printDiffLog',
            query: {
              replayId: row.id,
              type: 'diff',
              task_execution_id: this.$route.query.task_execution_id,
              diffId: res.data,
            },
          });
        }
      });
    },
    // 回放日志
    getReplayDetail(index, row) {
      this.$router.push({
        name: 'printReplayLog',
        query: {
          replay_id: row.id,
          type: 'replay',
          task_execution_id: this.$route.query.task_execution_id,
        },
      });
    },
  },
};
</script>
<style lang="scss" scoped></style>
