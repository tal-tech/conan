<template>
  <div style="width: 100%" ref="detail">
    <!-- 执行回放日志详情 -->
    <div style="">
      <CommonCard :type="type" :taskExecutionId="id"></CommonCard>
      <el-tabs
        class="tabsFlex"
        v-model="activeName"
        id="tabs"
        v-if="!recordComplete"
      >
        <el-tab-pane label="回放详情" name="first">
          <!-- 进度条 -->
          <el-progress
            :text-inside="true"
            :stroke-width="26"
            :percentage="percentage"
          ></el-progress>
          <!-- 日志打印 -->
          <div
            v-loading="loading"
            class="commonPre"
            element-loading-spinner="el-icon-loading"
            element-loading-text="日志拼命加载中"
          >
            <pre
              id="textarea_id"
              readonly
              spellcheck="false"
              v-show="logs.length > 1"
              class="preStyle isMyStyle"
              v-html="logs"
            ></pre>
          </div>
        </el-tab-pane>
      </el-tabs>
      <div v-show="recordComplete">
        <el-tabs class="tabsFlex" v-model="activeLog" id="tabs">
          <el-tab-pane label="回放详情" name="first">
            <!-- 图表 -->
            <div class="card" v-show="recordDetail.length != 0">
              <el-card shadow="always" body-style="padding: 10px;">
                <div ref="pie"></div>
              </el-card>
              <el-card shadow="always" body-style="padding: 10px;">
                <div ref="bar"></div>
              </el-card>
            </div>

            <!-- 日志详情 -->
            <div style="margin: 10px 0">
              <el-table
                ref="apiTable"
                :data="recordDetail"
                max-height="560"
                :header-cell-style="{ background: '#F3F4F7', color: '#555' }"
                fit
                tooltip-effect="light"
              >
                <el-table-column
                  align="center"
                  label="#"
                  width="40px"
                  type="index"
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="域名"
                  show-overflow-tooltip
                  prop="domain_name"
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="接口"
                  prop="api_name"
                  show-overflow-tooltip
                  :filters="[
                    { text: '/literacySift', value: '/literacySift' },
                    {
                      text: '/app/CourseMall/getGradeList',
                      value: '/app/CourseMall/getGradeList'
                    }
                  ]"
                  filter-placement="bottom-end"
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="方法"
                  width="80px"
                  show-overflow-tooltip
                  prop="api_method"
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="实际回放条数"
                  width="100px"
                  prop="actual_count"
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="预期回放条数"
                  width="100px"
                  prop="expect_count"
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="回放成功率"
                  width="120px"
                  prop="success_rate"
                >
                  <template slot-scope="scope">
                    <span> {{ scope.row.success_rate }}% </span>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="操作">
                  <template slot-scope="scope">
                    <el-button
                      @click="replayData(scope.$index, scope.row)"
                      size="small"
                      type="text"
                      >查看接口回放数据</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane name="second" label="回放日志">
            <div id="recordShowContent">
              <pre class="preStyle" v-html="logs"></pre>
            </div>
          </el-tab-pane>
        </el-tabs>
        <el-tabs class="tabsFlex" v-model="activeResult" id="tabs">
          <el-tab-pane label="精准校验结果" name="first">
            <!-- 接口请求细节列表 -->
            <div class="response">
              <el-form
                :model="queryParams"
                :inline="true"
                label-width="100px"
                class="response-search"
              >
                <el-form-item :label="apiNameLable" prop="apiName">
                  <el-select
                    v-model="queryParams.apiName"
                    placeholder="请输入接口名"
                  >
                    <el-option
                      v-for="(item,index) in ApiOptions"
                      :key="index"
                      :label="item"
                      :value="item"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="请求内容" prop="requst">
                  <el-input
                    placeholder="请输入请求内容"
                    v-model="queryParams.requst"
                    clearable
                  ></el-input>
                </el-form-item>
                <el-form-item label="错误描述" prop="errorDesc">
                  <el-input
                    placeholder="请输入错误描述"
                    v-model="queryParams.errorDesc"
                    clearable
                  ></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button
                    type="primary"
                    @click="search"
                    :loading="isLoading"
                    size="mini"
                  >
                    {{ isLoading ? "查询中" : "查询" }}
                  </el-button>
                </el-form-item>
              </el-form>
              <div class="text"><el-tag>Tips：双击左键表格区域即可复制；单击左键表格区域即可查看JSON格式;</el-tag></div>
              <div>
                <el-table
                  v-loading="isLoading"
                  element-loading-text="拼命加载中"
                  element-loading-spinner="el-icon-loading"
                  :data="responseData"
                  min-height="100"
                  max-height="500"
                  :header-cell-style="{ background: '#F3F4F7', color: '#555' }"
                  style="width: 100%"
                  fit
                  tooltip-effect="light"
                  @cell-dblclick="celldblclick"
                >
                  <el-table-column
                    align="center"
                    label="接口名"
                    show-overflow-tooltip
                    prop="apiName"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    label="错误描述"
                    show-overflow-tooltip
                    prop="errorDesc"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    label="请求内容"
                    show-overflow-tooltip
                    prop="requst"
                  >
                   <template slot-scope="scope">
                    <el-popover placement="right" width="500" trigger="click">
                      <div style="width: 500px; overflow:scroll ;height: 600px">
                        <Json :jsonString="scope.row.requst"></Json>
                      </div>
                      <span slot="reference">{{ scope.row.requst }}</span>
                    </el-popover>
                  </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    show-overflow-tooltip
                    label="响应内容"
                    prop="response"
                  >
                  <template slot-scope="scope">
                    <el-popover placement="right" width="500" trigger="click">
                      <div style="width: 500px; overflow:scroll ;height: 600px">
                        <Json :jsonString="scope.row.response"></Json>
                      </div>
                      <span slot="reference">{{ scope.row.response }}</span>
                    </el-popover>
                  </template>
                  </el-table-column>
                  <el-table-column align="center" width="120px" label="查看数据">
                  <template slot-scope="scope">
                    <el-button
                      @click="replayData(scope.$index, scope.row)"
                      size="small"
                      type="text"
                      >查看接口回放数据</el-button
                    >
                  </template>
                </el-table-column>
                  <el-table-column align="center" width="70px" label="操作">
                    <template slot-scope="scope">
                      <el-button
                        type="danger"
                        icon="el-icon-delete"
                        circle
                        @click="dl(scope.row)"
                      ></el-button>
                    </template>
                  </el-table-column>
                 
                </el-table>
                <!-- 分页 -->
                <pagination
                  v-show="total > 0"
                  :total="total"
                  :page.sync="queryParams.pageNum"
                  :limit.sync="queryParams.pageSize"
                  @pagination="getResponse"
                />
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>
<script>
import pagination from "@/components/Pagination";
import createPie from "@/utils/echarts/pie";
import createBar from "@/utils/echarts/bar";
import CommonCard from "./commonCard";
import Json from "../../conanApi/Edit-component/Json.vue";
import {
  getReplaySchemaErrorNumber,
  delReplaySchemaError,
  getReplaySchemaErrorList
} from "@/api/execution/replayError.js";
import {
  getLogByReplay,
  getProgressByReplay,
  getDetailByReplay
} from "@/api/execution/printReplayLog";
export default {
  name: "printReplayLog",
  components: { CommonCard, pagination ,Json},
  data() {
    return {
      type: "",
      id: "",
      percentage: 0,
      activeName: "first",
      activeLog: "first",
      activeResult: "first",
      resultInfo: "", //日志标题
      logs: "回放执行中...",
      loading: false,
      show: false,
      recordComplete: false, //当前回放是否已完成
      recordDetail: [], //回放详情
      intervalId: "", //定时器
      isLoading: false,
      responseData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        replayId: ""
      },
      ApiOptions: [],
      apiNameLable:'接口名',
    };
  },
  created() {
    this.id = this.$route.query.replay_id;
    this.type = this.$route.query.type;
    this.initData();
  },
  mounted() {
    this.intervalId = setInterval(() => {
      this.initData();
    }, 5000);
  },
  watch: {
    $route(route) {
      if (route.path != "/common/printRecordLog") {
        //当前不在主页就要清楚定时器
        clearInterval(this.intervalId);
      }
    }
  },
  methods: {
    //  查询数据的接口
    search() {
      this.queryParams.pageNum = 1;
      this.getResponse();
    },
    // 获取接口返回信息
    getResponse() {
      this.isLoading = true;
      Object.assign(this.queryParams, {
        replayId: this.$route.query.replay_id
      });
      getReplaySchemaErrorList(this.queryParams)
        .then(res => {
          if (res.code == 200) {
            this.responseData = res.data.data;
            this.total = res.data.total;
            this.isLoading = false;
          }
        })
        .catch(err => {
          this.isLoading = false;
        });
    },
    dl(row) {
      const ids = row.taskId;
      this.$confirm(
        '是否确认删除任务管理名称为"' + row.name + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function() {
          return delReplaySchemaError(ids);
        })
        .then(() => {
          this.getTaskTable();
          this.msgSuccess("删除成功");
        });
    },
    // 初始化
    async initData() {
      await this.searchRecord();
      await this.searchProgress();
    },
    // 查询进度条
    searchProgress() {
      let obj = {
        replay_id: this.$route.query.replay_id
      };
      getProgressByReplay(obj)
        .then(res => {
          console.log(res, "sssss");
          if (res.code == "200") {
            this.percentage = Number(res.data);
            if (res.data == "100") {
              this.recordComplete = true;
              clearInterval(this.intervalId);
              this.getRecordDetail();
              // 下拉数据
              this.getNumberList();
              // 最下面查询图表数据
              this.search();
              return;
            } 
          }
        })
        .catch(err => {
          clearInterval(this.intervalId);
        });
    },
    //   查询日志
    searchRecord() {
      let obj = {
        replay_id: this.$route.query.replay_id
      };
      getLogByReplay(obj)
        .then(res => {
          if (res.code !== 200) {
            console.error("查询日志详情失败：res = " + res);
            clearInterval(this.intervalId);
          } else {
            if (res.data) {
              this.stringFilter(res.data);
              if (res.data.includes("end") == true) {
                this.recordComplete = true;
                clearInterval(this.intervalId);
                this.getRecordDetail();
                // 下拉数据
                this.getNumberList();
                // 最下面查询图表数据
                this.search();
              }
            }
          }
        })
        .catch(err => {
          clearInterval(this.intervalId);
        });
    },
    //   获取回放详情
    getRecordDetail() {
      let obj = {
        replay_id: this.$route.query.replay_id
      };
      getDetailByReplay(obj).then(res => {
        if (res.code !== 200) {
          console.error("查询回放详情失败：res = " + res);
          return;
        }
        let list = res.data;
        this.recordDetail = res.data;
        this.resultInfo = "回放日志";
        this.initPie(list);
        this.initBar(list);
      });
    },
    // 查看数据详情
    getNumberList() {
      let obj = {
        rePlayid: this.$route.query.replay_id,
      };
      getReplaySchemaErrorNumber(obj).then(res => {
        if (res.code == 200 || res.code == 0) {
          this.apiNameLable=`接口名 (${res.data.count} 个)`;
          let object=res.data.apiNameMap;
          let ary=[];
          for (const key in object) {
            if (Object.hasOwnProperty.call(object, key)) {
               ary.push(key);
            }
          }
          this.ApiOptions=ary;
        }
      });
    },
    // 查看单接口数据流量
    replayData(index, row) {
      this.$router.push({
        name: "ReplayDetail",
        query: { replay_id: this.$route.query.replay_id, api_id: row.api_id?row.api_id:row.apiId }
      });
    },
    // 实际回放接口占比饼图
    initPie(list) {
      let legendData = [];
      let seriesData = [];
      list.forEach(item => {
        legendData.push(item.api_name);
        let obj = {
          value: item.actual_count,
          name: item.api_name
        };
        seriesData.push(obj);
      });

      let params = {
        title: {
          text: "实际回放接口占比",
          subtext: ""
        },
        tooltip: {
          trigger: "item",
          position: "inside",
          formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
          data: []
        },
        series: {
          radius: "50%",
          name: "实际回放接口",
          data: seriesData,
          label: {
            position: "outer",
            alignTo: "edge",
            margin: 200
          }
        }
      };
      createPie(this.$refs.pie, params, this.$refs.detail, 2, 400);
    },
    // 接口回放成功率条状图
    initBar(list) {
      let seriesData = [];
      let xAxisData = [];
      list.forEach(item => {
        let num = item.success_rate;
        xAxisData.push(item.api_name);
        seriesData.push(num);
      });

      let params = {
        title: {
          text: "接口回放成功率",
          x: "center"
        },
        xAxis: {
          type: "category",
          axisLabel: {
            //坐标轴刻度标签的相关设置。
            interval: 0,
            rotate: "20"
          },
          data: xAxisData
        },
        yAxis: {
          type: "value",
          max: 100,
          min: 0,
          axisLabel: {
            formatter: "{value}%"
          }
        },
        series: [
          {
            data: seriesData,
            type: "bar",
            barMaxWidth: "30",
            barMinHeight: 1
          }
        ],
        legend: []
      };
      createBar(this.$refs.bar, params, this.$refs.detail);
    },
    // 复制表格数据
    celldblclick(row, column, cell, event) {
      let _this = this;
      this.$copyText(row[column.property]).then(
        function(e) {
          _this.msgSuccess("复制成功");
        },
        function(e) {
          _this.msgError("复制失败");
        }
      );
    },
    // 过滤字符串
    stringFilter(data) {
      // 去除字符串
      let a = data.replace(/"/g, "");
      // 日期
      let b = a.replace(/(\d{4}-\d{2}-\d{2})/g, function(a) {
        return '<span class="datablue">' + a + "</span>";
      });
      // 时分秒
      let c = b.replace(/(\d{2}:\d{2}:\d{2})/g, function(b) {
        return '<span class="datatuse">' + b + "</span>";
      });
      // info状态
      let d = c.replace(/(\[INFO\])/g, function(c) {
        return '<span class="datagreen">' + c + "</span>";
      });
      // [ERROR]状态
      let e = d.replace(/(\[ERROR\])/g, function(d) {
        return '<span class="datared">' + d + "</span>";
      });
      let f = e.replace(
        /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/g,
        function(e) {
          return '<span class="dataoldBule">' + e + "</span>";
        }
      );
      this.logs = f;
    }
  }
};
</script>
<style lang="scss">
@import "@/assets/styles/printLog.scss";
.response-search {
  background: #fff;
  padding-top: 20px;
}
.text{
    color: #555;
    font-size: 14px;
    margin-bottom: 5px;
}
</style>
