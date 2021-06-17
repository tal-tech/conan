<template>
  <div style="width: 100%; padding: 5px" ref="detail">
    <!-- 执行录制日志详情 -->
    <div>
      <!-- 修改部分，更换展示方式单独引css-->
      <CommonCard :type="type" :taskExecutionId="id"></CommonCard>
      <el-tabs
        class="tabsFlex"
        v-model="activeName"
        id="tabs"
        v-if="!recordComplete"
      >
        <el-tab-pane label="录制日志" name="first">
          <!-- 进度条 -->
          <el-progress
            :text-inside="true"
            :stroke-width="26"
            :percentage="percentage"
          ></el-progress>

          <!-- 日志打印 -->
          <div
            v-if="!recordComplete"
            v-loading="loading"
            class="commonPre"
            element-loading-spinner="el-icon-loading"
            element-loading-text="日志拼命加载中"
          >
            <pre
              id="textarea_id"
              style="width: 100%; height: 500px"
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
          <el-tab-pane label="录制详情" name="first">
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
                v-loading="Detailloading"
                element-loading-text="拼命加载中"
                element-loading-spinner="el-icon-loading"
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
                  label="可录制条数"
                  width="100px"
                  prop="recordable_count"
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="实际录制条数"
                  width="100px"
                  prop="actual_count"
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="预期录制条数"
                  width="100px"
                  prop="expect_count"
                ></el-table-column>
                <el-table-column
                  align="center"
                  label="录制成功率"
                  width="120px"
                  prop="success_rate"
                ></el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane name="second" label="录制日志">
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
import {
  getLogByRecord,
  getProgressByRecord,
  getDetailByRecord
} from "@/api/execution/printRecordLog";
import CommonCard from "./commonCard";

export default {
  name: "printRecordLog",
  components: { CommonCard },
  data() {
    return {
      id: "",
      type: "",
      percentage: 0, //进度条数据
      resultInfo: "", //日志标题
      logs: "录制执行中...",
      loading: false,
      Detailloading: false,
      show: false,
      recordComplete: false, //当前录制是否已完成
      recordDetail: [], //录制详情
      recordShow: false, //日志弹框是否显示
      intervalId: "", //定时器
      activeLog: "first",
      activeName: "first",
      queryParams: {
        task_execution_id: null
      }
    };
  },
  created() {
    this.id = this.$route.query.task_execution_id;
    this.type = this.$route.query.type;
    console.log(this.type, "type");
    this.queryParams.task_execution_id = this.$route.query.task_execution_id;
    this.initData();
  },
  watch: {
    $route(route) {
      if (route.path != "/common/printReplayLog") {
        //当前不在主页就要清楚定时器
        clearInterval(this.intervalId);
      }
    }
  },
  mounted() {
    this.intervalId = setInterval(() => {
      this.initData();
    }, 5000);
  },
  methods: {
    initData() {
      this.searchProgress();
      this.searchRecord();
    },
    // 查询进度条
    searchProgress() {
      getProgressByRecord(this.queryParams)
        .then(res => {
          if (res.code == "200") {
            this.percentage = Number(res.data);
            if (res.data == "100") {
              this.recordComplete = true;
              clearInterval(this.intervalId);
              this.getRecordDetail();
              return;
            } 
          } else {
            clearInterval(this.intervalId);
          }
        })
        .catch(err => {
          clearInterval(this.intervalId);
        });
    },
    //   查询日志
    searchRecord() {
      getLogByRecord(this.queryParams)
        .then(res => {
          if (res.code !== 200) {
            clearInterval(this.intervalId);
            return;
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
      this.Detailloading = true;
      getDetailByRecord(this.queryParams)
        .then(res => {
          if (res.code !== 200) return;
          let list = res.data.record_detail_list;
          this.recordDetail = res.data.record_detail_list;
          this.initPie(list);
          this.initBar(list);
          this.Detailloading = false;
        })
        .catch(err => {
          this.Detailloading = false;
        });
    },
    // 实际录制接口占比饼图
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
          text: "实际录制接口占比",
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
          name: "实际录制接口",
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
        let num = item.success_rate ? item.success_rate.split("%")[0] : 0;
        xAxisData.push(item.api_name);
        seriesData.push(num);
      });

      let params = {
        title: {
          text: "接口录制成功率",
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
