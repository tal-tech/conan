<template>
  <div class="app-container">
    <div>
      <el-form
        :inline="true"
        :model="queryParams"
        class="execution-query-form"
        size="small"
        label-width="68px"
      >
        <el-form-item label="关键字">
          <el-input placeholder="支持模糊查询" v-model="queryParams.keyword"></el-input>
        </el-form-item>
        <el-form-item label="部门">
          <treeselect
            class="w10"
            v-model="queryParams.deptId"
            :options="deptOptions"
            :show-count="true"
            placeholder="请选择部门"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            clearable
            filterable
            palceholder="执行状态"
            v-model="queryParams.status"
          >
            <el-option
              v-for="item in statusList"
              :key="item.dictValue"
              :label="item.dictLabel"
              :value="parseInt(item.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            :picker-options="timeRangeOptions"
            align="right"
            size="small"
            style="width: 240px"
            end-placeholder="结束日期"
            range-separator="至"
            start-placeholder="开始日期"
            type="datetimerange"
            v-model="queryParams.dateRange"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button
            @click="searchExecution"
            class="el-button el-button--cyan el-button--mini"
            icon="el-icon-search"
            >查询
          </el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="dataList-Table">
      <el-table
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        :data="tableData"
        :header-cell-style="{ background: '#F3F4F7', color: '#555' }"
        fit
        tooltip-effect="light"
      >
        <el-table-column align="center" type="expand">
          <template slot-scope="props">
            <el-form class="task-table-expand" inline label-position="left">
              <el-form-item label="任务ID">
                <span>{{ props.row.taskId }}</span>
              </el-form-item>
              <el-form-item label="任务执行Id">
                <span>{{ props.row.executionId }}</span>
              </el-form-item>
              <el-form-item label="更新人">
                <span>{{ props.row.updateBy }}</span>
              </el-form-item>
              <el-form-item label="更新时间">
                <span>{{ props.row.updateTime }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column align="center" label="#" type="index"></el-table-column>
        <el-table-column
          align="center"
          label="任务名称"
          show-overflow-tooltip
          prop="taskName"
          width="200"
        ></el-table-column>
        <el-table-column align="center" label="部门" prop="deptName"></el-table-column>
        <el-table-column align="center" label="状态" prop="status">
          <template slot-scope="scope">
            <span
              ><i
                :class="[
                  scope.row.status === 2
                    ? 'iconfont icon-luzhi text-navy'
                    : scope.row.status === 5
                    ? 'iconfont icon-huifang text-navy'
                    : scope.row.status === 8
                    ? 'iconfont icon-ziyuan1282 text-navy'
                    : scope.row.status === 1
                    ? 'iconfont icon-luzhi text-warning'
                    : scope.row.status === 4
                    ? 'iconfont icon-huifang text-warning'
                    : scope.row.status === 7
                    ? 'iconfont icon-ziyuan1282 text-warning'
                    : scope.row.status === 3
                    ? 'iconfont icon-luzhi text-danger'
                    : scope.row.status === 6
                    ? 'iconfont icon-huifang text-danger'
                    : scope.row.status === 9
                    ? 'iconfont icon-ziyuan1282 text-danger'
                    : 'iconfont icon-bofang ',
                ]"
              >
                {{ scope.row.status | taskFormat }}</i
              >
            </span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="创建时间"
          prop="createTime"
          width="180"
        ></el-table-column>
        <el-table-column align="center" width="200" label="操作">
          <template slot-scope="scope">
            <el-tooltip class="item" effect="light" content="回放" placement="top-start">
              <el-dropdown trigger="click">
                <div class="el-dropdown-link" style="margin-right: 10px;">
                  <el-button
                    type="warning"
                    size="mini"
                    circle
                    class="iconfont icon-huifang"
                  ></el-button>
                </div>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item icon="el-icon-video-camera">
                    <el-button
                      @click="doReplay(scope.row, 'online')"
                      type="text"
                      size="mini"
                      >线上回放
                    </el-button>
                  </el-dropdown-item>
                  <el-dropdown-item icon="el-icon-video-camera-solid">
                    <el-button
                      @click="doReplay(scope.row, 'gray')"
                      type="text"
                      size="mini"
                      style="color: #8e8d8e"
                      >灰度回放
                    </el-button>
                  </el-dropdown-item>
                  <el-dropdown-item icon="el-icon-upload">
                    <el-button
                      @click="doReplay(scope.row, 'test')"
                      type="text"
                      size="mini"
                      style="color: #8e8d8e"
                      >测试回放
                    </el-button>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-tooltip>
            <el-tooltip
              class="item"
              effect="light"
              content="查看详情"
              placement="top-start"
            >
              <el-button
                @click="executionDetail(scope.row)"
                type="primary"
                circle
                icon="el-icon-document"
                size="mini"
              ></el-button>
            </el-tooltip>
            <el-tooltip
              class="item"
              effect="light"
              content="录制日志"
              placement="top-start"
            >
              <el-button
                @click="recordInfo(scope.row)"
                type="text"
                size="mini"
                circle
                icon="el-icon-video-camera-solid"
                class="el-button--lvse"
              >
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getTaskExecutionLists(queryParams)"
      />
    </div>
  </div>
</template>
<script>
import pagination from "../../components/Pagination";
import { getTaskExecution } from "../../api/execution/index";
import { getToReplay } from "../../api/execution/printReplayLog";

import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "ExecutionList",
  components: {
    pagination,
    Treeselect,
  },
  data() {
    return {
      statusList: [],
      timeRangeOptions: {
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      loading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        keyword: "",
        deptId: null,
        taskId: null,
        taskName: "",
        deptName: "",
        status: null,
      },
      deptOptions: [],
      tableData: [],
      total: 1, //总页数
    };
  },
  created() {},

  mounted() {
    this.queryParams.keyword = this.$route.query.name;
    this.initData();
  },
  methods: {
    initData() {
      this.getTaskExecutionLists();
      this.getTreeselect();
      this.getDicts("bss_task_status").then((response) => {
        this.statusList = response.data;
      });
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then((response) => {
        this.deptOptions = response.data;
      });
    },

    // 分页、获取列表数据
    getTaskExecutionLists() {
      this.loading = true;
      getTaskExecution(this.queryParams)
        .then((res) => {
          if (res.code == 200 && JSON.stringify(res.data) !== "{}") {
            console.log(res, "res");
            this.tableData = res.data.data;
            this.total = res.data.total;
            setTimeout(() => {
              this.loading = false;
            }, 500);
          } else {
            this.loading = false;
          }
        })
        .catch((err) => {
          this.loading = false;
          this.tableData = [];
        });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.getTaskExecutionLists();
    },
    getUserListByKeyword() {},
    onDepartmentChange() {},
    // 查询信息接口
    searchExecution() {
      this.queryParams.pageNum = 1;
      this.getTaskExecutionLists();
    },
    doReplay(row, replayType) {
      getToReplay(replayType, 1, row.executionId).then((res) => {
        if (res.code == 200) {
          this.$router.push({
            name: "printReplayLog",
            query: {
              replay_id: res.data.data.data.replayId,
              replayType: replayType,
              task_execution_id: res.data.data.data.taskExecutionId,
              type: "replay",
            },
          });
        }
      });
    },
    executionDetail(row) {
      this.$router.push({
        name: "ViewDetails",
        query: {
          task_execution_id: row.executionId,
          type: "record",
          activeName: "first",
        },
      });
    },
    
    // 录制日志
    recordInfo(row) {
      this.$router.push({
        name: "printRecordLog",
        query: { task_execution_id: row.executionId, type: "record" },
      });
    },
  },
};
</script>
<style lang="scss"></style>
