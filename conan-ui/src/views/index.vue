<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="mgb20" style="height:272px;">
          <div class="user-info">
            <div class="user-avator icon-Git iconfont">
              <img
                src="../assets/images/conan.png"
                style="opacity:0.2;margin-left: 102px;margin-top: -41px;"
                alt
              />
            </div>
            <div class="user-info-cont">
              <div class="user-info-name">Hi {{ name }},Welcome back</div>
              <br />
              <div>以下数据从2020/9/9开始统计</div>
            </div>
          </div>
          <div class="user-info-list">
            本次登录时间：
            <span>{{ timeId }}</span>
          </div>
          <div class="user-info-list">
            上次登录地点：
            <span>北京</span>
          </div>
        </el-card>
        <el-card shadow="hover" class="leftCont common">
          <el-row>
            <el-col :span="24">
              <div class="grid-contents bg-purple-dark">
                <i class="iconfont icon-renyuan grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div>平台活跃用户数</div>
                  <div class="grid-num">{{ homeData.user_count }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
        <el-card shadow="hover" class="common">
          <el-row>
            <el-col :span="24">
              <div class="grid-contents bg-purple-dark">
                <i class="iconfont icon-fangwenliang1 grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div>平台累计访问量（PV）</div>
                  <div class="grid-num">{{ homeData.pv_count }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-row :gutter="10" class="mgb20">
          <el-col :span="6">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-1">
                <i class="iconfont icon-jiekou1 grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ homeData.api_count }}</div>
                  <div>已接入接口</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-2">
                <i class="iconfont icon-huigui grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ homeData.replay_count }}</div>
                  <div>自动回归次数</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-3">
                <i class="iconfont icon-huifang grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ homeData.replay_flows }}</div>
                  <div>累计回放流量</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-4">
                <i class="iconfont icon-ziyuan1282 grid-con-icon"></i>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ homeData.diff_count }}</div>
                  <div>比对次数</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="18">
            <el-card style="height:406px">
              <div>
                <div slot="header" class="clearfix">
                  <span>核心监控数据 </span>
                  <br />
                  <small>Your can see important data for a week</small>
                </div>
                <div ref="detail2">
                  <div ref="line"></div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="versionBox">
            <div class="clearfix">
              <span>当前版本V3.0</span>
            </div>
            <div class="box-body">
              <p class="text-muted">
                增加数据中心、开放中心模块，已和PTS压测平台打通提供线上真实压力模型与压测数据，实现全链路自动化压测，目前已在大班业务试运行
              </p>
              <p class="text-muted">
                柯南流量回放平台是一个将线上环境真实流量复制并用于自动回归测试的平台，帮助大家提升测试质量，提高回归效率，并已经和发布系统联动来自动进行服务端的接口回归。如果有感兴趣的，可以联系质量研发部-刘劲松老师了解。
              </p>
              <a
                href="http://app.xesv5.com/ep-docs/CONAN/"
                target="_blank"
                class="btn btn-sm btn-outline rounded b-success"
                >阅读更多
              </a>
            </div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header" class="clearfix" >
            <span>累计任务回放排行 </span>
            <br />
            <small>Calculated in 48 hours for 5 tasks</small>
          </div>
          <div ref="taskRankData" style="height: 300px;">
            <div ref="taskRankPie"></div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>机器执行状态 </span>
            <br />
            <small>Agent Machine for task replay</small>
          </div>
          <div style="height:300px;">
            <healthyMap></healthyMap>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>各部门监控数据 </span>
            <br />
            <small>Agent Machine for task replay</small>
          </div>
          <div ref="productLineData">
            <div ref="productLineLine"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span
              >留言板
              <el-link
                icon="el-icon-edit"
                style="float: right"
                @click="messageDialogVisible = true"
                >留言</el-link
              >
            </span>
            <br />
            <small>欢迎留下你的意见</small>
          </div>
          <div class="box-body">
            <div class="block" style="overflow-y: auto;height: 300px;">
              <el-timeline>
                <el-timeline-item
                  v-for="(activity, index) in messages"
                  :key="index"
                  :timestamp="activity.timestamp"
                  placement="top"
                >
                  <el-card>
                    <h6 style="margin: 10px 1px; font-size: 16px;">
                      {{ activity.content }}
                    </h6>
                    <p class="comment">{{ activity.comment }}</p>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-dialog
      title="发布留言"
      :visible.sync="messageDialogVisible"
      width="30%"
      center
    >
      <el-input
        type="textarea"
        :rows="2"
        maxlength="50"
        minlength="5"
        placeholder="请输入内容"
        show-word-limit
        v-model="msgContent"
      >
      </el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="messageDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitMessage">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import createPie from "../utils/echarts/pie";
import createBar from "../utils/echarts/bar";
import createLine from "../utils/echarts/line";
import {
  getAllcountHome,
  getHomeDataWithTimeHome,
  getTaskRankHome,
  getProductLineDataHome,
  messagesHome,
  messagesAddHome
} from "../api/home/index.js";
import Cookies from "js-cookie";
import HealthyMap from "./components/healthyMap.vue";
export default {
  name: "dashboard",
  data() {
    return {
      name: Cookies.get("username") || "admin",
      homeData: {}, // 获取首页方框数据信息（6个）
      timeId: "",
      agentMachine: [], //agent机器数据
      messages: [],
      msgContent: "",
      messageDialogVisible: false
    };
  },
  components: { HealthyMap },
  computed: {},
  mounted() {
    this.initData();
    console.log(this.$store.state.user.name, "获取name值");
  },
  methods: {
    //   初始化
    async initData() {
      this.timeId = this.getNowDate();
      await this.getHomeDataWithTimeList();
      await this.getAllcountList();
      await this.getTaskRankList();
      await this.getProductLineDataList();
      await this.messagesList();
    },

    // 获取时间
    getNowDate() {
      let time = new Date();
      return (
        time.getFullYear() +
        "-" +
        (time.getMonth() + 1) +
        "-" +
        time.getDate() +
        "  " +
        time.getHours() +
        ":" +
        time.getMinutes() +
        ":" +
        time.getSeconds()
      );
    },
    // 获取大屏方框数据信息（6个）
    getAllcountList() {
      getAllcountHome().then(res => {
        if (res.code == 200 && JSON.stringify(res.data) != "{}") {
          this.homeData = res.data;
        } else {
          return false;
        }
      }).catch(err=>{
        console.log(err,'err');
      })
    },
    // 获取核心监控数据
    getHomeDataWithTimeList() {
      getHomeDataWithTimeHome().then(res => {
        if (res.code == 200 && res.data.length != 0) {
          this.initLine(res.data, 300);
        } else {
          return false;
        }
      }).catch(err=>{
        console.log(err,'err');
      })
    },
    // 获取任务排名数据
    getTaskRankList() {
      let obj = {
        count: 5
      };
      getTaskRankHome(obj).then(res => {
        if (res.code == 200 && res.data.length != 0) {
          this.taskRank(res.data);
        } else {
          return false;
        }
      }).catch(err=>{
        // 默认数据
        console.log(err,'err');
      })
    },
    // 获取各部门监控数据
    getProductLineDataList() {
      getProductLineDataHome()
        .then(res => {
          if (res.code == 200 && res.msg == "ok") {
            this.initBar(res.data, 300);
          } else {
            return false;
          }
        })
        .catch(err => {
          return false;
        });
    },
    // message
    messagesList() {
      //获取留言消息
      messagesHome(10).then(res => {
        if (res.code == 200 && JSON.stringify(res.data) != "{}") {
          this.messages = res.data.data;
        } else {
          return false;
        }
      }).catch(err=>{
        console.log(err,'err');
      })
    },
    // 提交留言
    submitMessage() {
      if (!this.name || !this.msgContent) return;
      messagesAddHome(this.name,this.msgContent).then(res => {
        if (res.code !== 200) {
          this.$message.error("发布留言失败，errCode = " + res.code);
          return;
        } else {
          this.messageDialogVisible = false;
          this.$notify.success({
            title: "留言成功",
            message: "感谢您的留言，多谢支持！",
            showClose: false,
            duration: 3000
          });
          this.messagesList();
        }
      }).catch(err=>{
        console.log(err,'err');
      })
    },

    //核心监控数据图表
    initLine(list, height) {
      let legendData = [];
      let seriesData = [];
      let xAxisData = [];
      list.forEach((item, index) => {
        // legendData.push(item.api_name);
        xAxisData.push(item.time);
        if (index == 0) {
          let _value = item.value;
          for (let i in _value) {
            legendData.push(i);
            seriesData.push({
              name: i,
              type: "line",
              data: []
            });
          }
        }
      });
      list.forEach(item => {
        let _value = item.value;
        Object.keys(_value).forEach((item1, index) => {
          seriesData[index].data.push(_value[item1]);
        });
      });

      let params = {
        title: {
          text: "",
          subtext: ""
        },
        xAxis: {
          name: "时间",
          show: true,
          type: "category",
          axisLabel: {
            //坐标轴刻度标签的相关设置。
            interval: 0,
            rotate: "20"
          },
          splitLine: {
            show: false
          },
          data: xAxisData
        },

        legend: {
          data: legendData
        },
        series: seriesData
      };
      createLine(
        this.$refs.line,
        params,
        this.$refs.detail2,
        1,
        height,
        "macarons"
      );
    },
    taskRank(list) {
      let legendData = [];
      let seriesData = [];
      list.forEach(item => {
        legendData.push(item.taskName);
        let obj = {
          value: item.replayCount || 0,
          name: item.taskName
        };
        seriesData.push(obj);
      });

      let params = {
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b}: {c} ({d}%)",
          position: "TOP"
        },
        title: {
          text: "",
          subtext: ""
        },
        legend: {
          data: legendData
        },
        series: {
          name: "回放次数",
          data: seriesData,
          type: "pie",
          radius: ["50%", "75%"],
          center: ["65%", "55%"],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: "center"
          },
          emphasis: {
            label: {
              show: true,
              fontWeight: "bold"
            }
          },
          labelLine: {
            show: false
          }
        }
      };
      createPie(
        this.$refs.taskRankPie,
        params,
        this.$refs.taskRankData,
        1,
        300,
        "macarons"
      );
    },
    //各部门监控数据
    initBar(list, height) {
      console.log(list, "list==>>>>department");
      let legendData = [list.apis.name, list.replays.name];
      let params = {
        title: {
          text: "",
          x: "center"
        },
        tooltip: {
          trigger: "axis"
        },
        legend: legendData,
        toolbox: {
          show: true,
          feature: {
            // dataView: { show: true, readOnly: false },
            // magicType: { show: true, type: ["line", "bar"] },
            // restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        xAxis: {
          type: "category",
          data: list.xData
        },
        yAxis: {
          type: "value"
        },
        series: [
          {
            name: list.apis.name,
            data: list.apis.value,
            type: "bar",
            barWidth: 30,
            markPoint: {
              data: [
                { type: "max", name: "最大值" },
                { type: "min", name: "最小值" }
              ]
            },
            markLine: {
              data: [{ type: "average", name: "平均值" }]
            }
          },
          {
            name: list.replays.name,
            data: list.replays.value,
            type: "bar",
            barWidth: 30,
            markPoint: {
              data: [
                { type: "max", name: "最大值" },
                { type: "min", name: "最小值" }
              ]
            },
            markLine: {
              data: [{ type: "average", name: "平均值" }]
            }
          }
        ]
      };
      createBar(
        this.$refs.productLineLine,
        params,
        this.$refs.productLineData,
        1,
        height,
        "macarons"
      );
    }
  }
};
</script>

<style scoped lang="less">
.el-row {
  margin-bottom: 20px;
  .versionBox {
    background-color: #f6f6f6;
    max-height: 406px;
    height: 406px;
    overflow: hidden;
    padding: 10px;
  }
  .btn-outline {
    transition: margin-left 0.3s linear;
  }
  .btn-outline:hover {
    margin-left: 10px;
    color: #0cc2aa;
  }
}

.grid-content {
  display: flex;
  align-items: center;
  height: 100px;
}

.grid-cont-right {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #999;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}

.grid-con-icon {
  font-size: 40px;
  width: 70px;
  height: 100px;
  text-align: center;
  line-height: 100px;
  color: #fff;
}

.grid-con-1 .grid-con-icon {
  background: #a88add;
}

.grid-con-1 .grid-num {
  color: #a88add;
}

.grid-con-2 .grid-con-icon {
  background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
  color: rgb(100, 213, 114);
}

.grid-con-3 .grid-con-icon {
  background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
  color: rgb(242, 94, 67);
}
.grid-con-4 .grid-con-icon {
  background: #6887ff;
}

.grid-con-4 .grid-num {
  color: #6887ff;
}
.box-header h3 {
  margin-top: 0;
}
.text-muted {
  color: inherit !important;
  opacity: 0.5;
  font-size: 0.875rem;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 7;
  -webkit-box-orient: vertical;
}
.leftCont {
  margin-bottom: 32px;
}
.common {
  height: 100px;
  background-color: #0cc2aa;
  line-height: 35px;
  .grid-cont-right {
    color: #fff;
  }
  .grid-contents {
    display: flex;
    i {
      height: 70px;
      line-height: 70px;
    }
  }
}
.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #ccc;
  margin-bottom: 20px;
}

.user-avator {
  width: 120px;
  height: 120px;
  font-size: 120px;
  border-radius: 50%;
}

.user-info-cont {
  padding-left: 50px;
  flex: 1;
  font-size: 14px;
  color: #999;
}

.user-info-cont div:first-child {
  font-size: 20px;
  color: #222;
}

.user-info-list {
  font-size: 14px;
  color: #999;
  line-height: 25px;
}

.user-info-list span {
  margin-left: 70px;
}

.mgb20 {
  margin-bottom: 20px;
}

.todo-item {
  font-size: 14px;
}

.todo-item-del {
  text-decoration: line-through;
  color: #999;
}

.schart {
  width: 100%;
  height: 300px;
}
.clearfix {
  span {
    font-weight: 500;
  }
}
</style>
