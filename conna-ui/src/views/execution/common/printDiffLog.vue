<template>
  <div ref="detail">
    <!-- 比对 -->
    <div>
      <CommonCard :type="type" :taskExecutionId="id"></CommonCard>
      <el-tabs
        class="tabsFlex"
        v-model="activeName"
        id="tabs"
        v-if="!recordComplete"
      >
        <el-tab-pane label="比对日志" name="first">
          <!-- 进度条 -->
          <el-progress
            v-if="!recordComplete"
            :text-inside="true"
            :stroke-width="26"
            :percentage="percentage"
          ></el-progress>
          <!-- 日志打印 -->
          <div
            class="commonPre"
            v-if="!recordComplete"
            v-loading="loading"
            element-loading-spinner="el-icon-loading"
            element-loading-text="日志拼命加载中"
          >
            <pre
              class="preStyle isMyStyle"
              v-show="logs.length > 1"
              spellcheck="false"
              id="textarea_id"
              v-html="logs"
              readonly
            ></pre>
          </div>
        </el-tab-pane>
      </el-tabs>
      <div v-show="recordComplete">
        <el-tabs class="tabsFlex" v-model="activeLog" id="tabs">
          <el-tab-pane label="比对详情" name="first">
            <!-- 图表 -->
            <div class="card" v-show="recordDetail.length != 0">
              <el-card shadow="always" body-style="padding: 10px;">
                <div ref="pie"></div>
              </el-card>
              <el-card shadow="always" body-style="padding: 10px;">
                <div ref="bar"></div>
              </el-card>
            </div>
            <el-table
              :data="recordDetail"
              :header-cell-style="{ background: '#F3F4F7', color: '#555' }"
              style="width: 100%; margin-top: 10px"
              fit
              :default-sort="{ prop: 'diffSuccessRate', order: 'descending' }"
              tooltip-effect="light"
            >
              <el-table-column type="expand">
                <template slot-scope="props">
                  <el-form
                    class="task-table-expand"
                    inline
                    label-position="center"
                  >
                    <el-form-item label="更新人">
                      <span>{{ props.row.update_by }}</span>
                    </el-form-item>
                    <el-form-item label="更新时间">
                      <span>{{ props.row.update_at }}</span>
                    </el-form-item>
                    <el-form-item label="负责人">
                      <span>{{ props.row.notify_list }}</span>
                    </el-form-item>
                  </el-form>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="#"
                type="index"
              ></el-table-column>
              <el-table-column
                align="center"
                label="域名"
                prop="domainName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="接口"
                prop="apiName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="接口条数"
                prop="apiCount"
              ></el-table-column>
              <el-table-column
                align="center"
                label="负责人"
                prop="ownerName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="平均对比成功率"
                sortable
                prop="diffSuccessRate"
              >
                <template slot-scope="scope">
                  <div>
                    <el-progress
                      :text-inside="true"
                      :stroke-width="26"
                      :percentage="scope.row.diffSuccessRate"
                    ></el-progress>
                  </div>
                </template>
              </el-table-column>
              <el-table-column align="center" label="操作">
                <template slot-scope="scope">
                  <el-button
                    @click="getDiffResult(scope.row)"
                    type="text"
                    size="small"
                  >
                    比对详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane name="second" label="比对日志">
            <div id="recordShowContent">
              <pre class="preStyle" v-html="logs"></pre>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>
<script>
import createPie from "@/utils/echarts/pie";
import createBar from "@/utils/echarts/bar";
import CommonCard from "./commonCard";

import {
  getDiffInfo,
  getDiffInfoList,
  getDiffProgress,
  getDiffDetailList,
} from "@/api/execution/printDiffLog";
export default {
  name: "printDiffLog",
  components: { CommonCard },

  data() {
    return {
      type: "",
      id: "",
      percentage: 0,
      activeLog: "first",
      resultInfo: "", //日志标题
      logs: "比对执行中...",
      loading: false,
      show: false,
      replayListInfo: null,
      replayDetailInfo: null,
      activeName: "first",
      recordComplete: false, //当前录制是否已完成
      recordDetail: [], //录制详情
      recordShow: false, //日志弹框是否显示
      intervalId: null
    };
  },
  created() {
    this.id = this.$route.query.diffId;
    this.type = this.$route.query.type;
  },
  watch: {
    $route(route) {
      if (route.path != "/common/printDiffLog") {
        //当前不在主页就要清楚定时器
        clearInterval(this.intervalId);
      }
    }
  },
  mounted() {
    this.initData();
    this.intervalId = setInterval(() => {
      this.initData();
    }, 5000);
  },
  methods: {
    // 初始化
    async initData() {
      await this.searchRecord();
      await this.searchProgress();
    },
    // 比对接口
    getDiffResult(row) {
      this.$router.push({
        name: "DiffResult",
        query: {
          apiId: row.apiId,
          diffId: row.diffId,
          successRate: row.diffSuccessRate
        }
      });
    },

    // 单接口回放接口
    // 查询进度条
    searchProgress() {
      let obj = {
        diff_id: this.$route.query.diffId
      };
      getDiffProgress(obj)
        .then(res => {
          console.log(res, "sssss");
          if (res.code == "200") {
            this.percentage = Number(res.data);
            if (res.data == "100") {
              this.recordComplete = true;
              clearInterval(this.intervalId);
              this.getRecordDetail();
              return;
            }
          }
        })
        .catch(err => {
          console.log(err, "err");
          clearInterval(this.intervalId);
        });
    },
    //   查询日志
    searchRecord() {
      let obj = {
        diff_id: this.$route.query.diffId
      };
      getDiffInfo(obj)
        .then(res => {
          if (res.code !== 200) {
            clearInterval(this.intervalId);
          } else {
            if (res.data) {
              this.stringFilter(res.data);
              if (res.data.includes("end") == true) {
                this.recordComplete = true;
                clearInterval(this.intervalId);
                this.getRecordDetail();
              }
            }
          }
        })
        .catch(err => {
          clearInterval(this.intervalId);
        });
    },
    //   获取录制详情
    getRecordDetail() {
      let obj = {
        replayId: this.$route.query.replayId
      };
      getDiffInfoList(obj).then(res => {
        if (res.code !== 200) {
          return;
        }
        let list = res.data;
        this.recordDetail = res.data;
        this.resultInfo = "比对日志";
        this.initPie(list);
        this.initBar(list);
      });
    },
    // 实际录制接口占比饼图
    initPie(list) {
      let legendData = [];
      let seriesData = [];
      list.forEach(item => {
        legendData.push(item.apiName);
        let obj = {
          value: item.apiCount,
          name: item.apiName
        };
        seriesData.push(obj);
      });

      let params = {
        title: {
          text: "实际比对接口占比",
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
          name: "实际比对接口",
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
    // 接口录制成功率条状图
    initBar(list) {
      let seriesData = [];
      let xAxisData = [];
      list.forEach(item => {
        xAxisData.push(item.apiName);
        seriesData.push(item.diffSuccessRate);
      });

      let params = {
        title: {
          text: "接口比对成功率",
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
</style>
