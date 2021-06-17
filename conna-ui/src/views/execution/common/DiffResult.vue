<template>
  <!-- 单条比对 -->
  <div>
    <div class="box-card felxCard diff" v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading">
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
    <div class="mergely-conan">
      <div id="mergely"></div>
    </div>
  </div>
</template>

<script>
import { getDiffDetailList } from '@/api/execution/printDiffLog.js';
import 'codemirror/lib/codemirror.css';
import 'mergely/lib/mergely.css';
import 'mergely';
export default {
  name: 'diffResult',
  data() {
    return {
      loading: false,
      apiName: '',
      successRate: '',
      domain: '',
      queryParams: {
        diffId: null,
        apiId: null,
      },
    };
  },

  created() {
    this.successRate = this.$route.query.successRate
      ? this.$route.query.successRate
      : 0;
    this.queryParams.diffId = this.$route.query.diffId;
    this.queryParams.apiId = this.$route.query.apiId;
  },
  mounted() {
    this.searchDiffDetail();
  },
  methods: {
    //获取diffResult
    searchDiffDetail() {
      this.loading = true;
      getDiffDetailList(this.queryParams).then((res) => {
        let basedata = '';
        let comparedata = '';
        this.apiName = res.data.apiName;
        this.domain = res.data.domainName;
        for (let i = 0; i < res.data.diffApiLogInfoList.length; i++) {
          let number = i + 1;
          try {
            basedata =
              basedata +
              '\n' +
              '-----------------------流量' +
              number +
              '---------------------' +
              '\n' +
              '\n' +
              'RequestBody:' +
              '\n' +
              res.data.diffApiLogInfoList[i].requestBody +
              '\n' +
              '\n' +
              'Response:' +
              '\n' +
              JSON.stringify(
                JSON.parse(res.data.diffApiLogInfoList[i].baseData),
                null,
                2
              );
            comparedata =
              comparedata +
              '\n' +
              '-----------------------流量' +
              number +
              '---------------------' +
              '\n' +
              '\n' +
              'RequestBody:' +
              '\n' +
              res.data.diffApiLogInfoList[i].requestBody +
              '\n' +
              '\n' +
              'Response:' +
              '\n' +
              JSON.stringify(
                JSON.parse(res.data.diffApiLogInfoList[i].compareData),
                null,
                2
              );
          } catch (e) {
            if (res.data.diffApiLogInfoList[i].baseData.length > 200) {
              basedata =
                basedata +
                '\n' +
                '-----------------------流量' +
                number +
                '---------------------' +
                '\n' +
                '\n' +
                'RequestBody:' +
                '\n' +
                res.data.diffApiLogInfoList[i].requestBody +
                '\n' +
                '\n' +
                'Response:' +
                '\n' +
                res.data.diffApiLogInfoList[i].baseData.substring(0, 200) +
                '流量过长已忽略';
            } else {
              basedata =
                basedata +
                '\n' +
                '-----------------------流量' +
                number +
                '---------------------' +
                '\n' +
                '\n' +
                'RequestBody:' +
                '\n' +
                res.data.diffApiLogInfoList[i].requestBody +
                '\n' +
                '\n' +
                'Response:' +
                '\n' +
                res.data.diffApiLogInfoList[i].baseData;
            }
            if (res.data.diffApiLogInfoList[i].compareData.length > 200) {
              comparedata =
                comparedata +
                '\n' +
                '-----------------------流量' +
                number +
                '---------------------' +
                '\n' +
                '\n' +
                'RequestBody:' +
                '\n' +
                res.data.diffApiLogInfoList[i].requestBody +
                '\n' +
                '\n' +
                'Response:' +
                '\n' +
                res.data.diffApiLogInfoList[i].compareData.substring(0, 200) +
                '流量过长已忽略';
            } else {
              comparedata =
                comparedata +
                '\n' +
                '-----------------------流量' +
                number +
                '---------------------' +
                '\n' +
                '\n' +
                'RequestBody:' +
                '\n' +
                res.data.diffApiLogInfoList[i].requestBody +
                '\n' +
                '\n' +
                'Response:' +
                '\n' +
                res.data.diffApiLogInfoList[i].compareData;
            }
          }
        }
        this.showNextDiffResult(basedata, comparedata);
        setTimeout(() => {
          this.loading = false;
        }, 500);
      });
    },
    showNextDiffResult(baseData, compareData) {
      let l = baseData;
      let r = compareData;
      this.$nextTick(() => {
        $('#mergely').mergely({
          lhs: function (setValue) {
            setValue(l);
          },
          rhs: function (setValue) {
            setValue(r);
          },
        });
      });
    },
  },
};
</script>

<style lang="less">
.diff {
  background: #fff;
  padding: 5px;
}
.CodeMirror-code {
  color: #fff !important;
  background: #2b3c57;
}
/* required */
.mergely-column textarea {
  width: 80px;
  height: 200px;
  background: rgb(0, 33, 64);
}
.mergely-column {
  float: left;
}
.mergely-margin {
  float: left;
}
.mergely-canvas {
  float: left;
  width: 28px;
}

/* resizeable */
.mergely-resizer {
  width: 100%;
  height: 100%;
}
.mergely-full-screen-0 {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  overflow: hidden;
}
.mergely-full-screen-8 {
  position: absolute;
  top: 8px;
  bottom: 8px;
  left: 8px;
  right: 8px;
  overflow: hidden;
}
.mergely-conan {
  margin: 10px auto;
  width: 100%;
  min-height: 680px;
  max-height: 680px;
}

/* style configuration */
.mergely-column {
  border: 1px solid #ccc;
}
.mergely-active {
  border: 1px solid #a3d1ff;
}

.mergely.a,
.mergely.d,
.mergely.c {
  color: #000;
}

.mergely.a.rhs.start {
  border-top: 1px solid #a3d1ff;
}
.mergely.a.lhs.start.end,
.mergely.a.rhs.end {
  border-bottom: 1px solid #a3d1ff;
}
.mergely.a.rhs {
  background-color: #ddeeff;
  color: blue !important;
}
.mergely.a.lhs.start.end.first {
  border-bottom-width: 0;
  border-top: 1px solid #a3d1ff;
}

.mergely.d.lhs {
  background-color: #ffe9e9;
}
.mergely.d.lhs.end,
.mergely.d.rhs.start.end {
  border-bottom: 1px solid #f8e8e8;
}
.mergely.d.rhs.start.end.first {
  border-bottom-width: 0;
  border-top: 1px solid #f8e8e8;
}
.mergely.d.lhs.start {
  border-top: 1px solid #f8e8e8;
}

.mergely.c.lhs,
.mergely.c.rhs {
  background-color: #fafafa;
}
.mergely.c.lhs.start,
.mergely.c.rhs.start {
  border-top: 1px solid #a3a3a3;
}
.mergely.c.lhs.end,
.mergely.c.rhs.end {
  border-bottom: 1px solid #a3a3a3;
}

.mergely.ch.a.rhs {
  background-color: #ddeeff;
}
.mergely.ch.d.lhs {
  background-color: pink;
  text-decoration: line-through;
  color: red !important;
}

.mergely.current.start {
  border-top: 1px solid #000 !important;
}
.mergely.current.end {
  border-bottom: 1px solid #000 !important;
}
.mergely.current.lhs.a.start.end,
.mergely.current.rhs.d.start.end {
  border-top-width: 0 !important;
}

.mergely.current.lhs.a.start.end.empty,
.mergely.current.rhs.d.start.end.empty {
  border-top-width: 1px !important;
  border-bottom-width: 0px !important;
}

.mergely.current.CodeMirror-linenumber {
  color: #f9f9f9;
  font-weight: bold;
  background-color: #777;
}
.CodeMirror-linenumber {
  cursor: pointer;
}
.CodeMirror-code {
  color: #717171;
}

#mergely-splash {
  display: none !important;
}
</style>
