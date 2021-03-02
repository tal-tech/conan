// 注解
// dom: 渲染元素DOM（必填）
// params: 折线图所需要的参数（必填）
/**
 * params = {
 *  title: {
 *    text: "" 主标题
 *  },
 *  legend: {
 *    data: [] 标注
 *  },
 *  xAxis: {
 *    data: [] 折线图X轴坐标名称
 *  },
 *  series: [
      {
        name: "", 显示名称
        itemStyle: {
            normal: {
                color: "#eb800e",
                lineStyle: {
                    color: "#eb800e" 线条颜色
                }
            }
        },
        data: [] 值
      }
    ]
 * };
 */
// domContainer: 所在容器DOM（必填）
// columns: 列数，默认2，即一行容纳饼图个数。目前支持1、2
// echartsH: 高度，默认400

function createLine(dom, params, domContainer, columns, echartsH, colorString) {
  // 引入 ECharts 主模块
  let echarts = require("echarts/lib/echarts");
  // 引入折线图
  require("echarts/lib/chart/line");
  // 引入主题
  require("echarts/theme/macarons");
  // 引入提示框
  require("echarts/lib/component/tooltip");
  // 引入标题
  require("echarts/lib/component/title");
  // 引入引导项
  require("echarts/lib/component/legend");
  // 引入下载项
  require("echarts/lib/component/toolbox");

  // 动态设置饼图的宽度和高度
  let clientWidth = domContainer.clientWidth;
  let mW;
  columns = columns || 2;
  if (columns == 1) {
    mW = clientWidth;
  } else if (columns == 2) {
    mW = clientWidth / 2 - 25;
  }
  dom.style.width = mW + "px";
  dom.style.height = (echartsH || 400) + "px";

  // 基于准备好的dom，初始化echarts实例
  let myChart = echarts.init(dom, colorString||'macarons');
  let option = {
    title: {
      text: params.title.text,
      x: "center"
    },
    tooltip: {
      trigger: "axis",
      textStyle: {
        align: "left"
      }
    },
    legend: {
      top: 25, // 据顶部的距离，处理标题之间的位置关系
      data: params.legend.data,
      textStyle: params.legend.textStyle || {}
    },
    grid: {
      left: "3%",
      right: "4%",
      bottom: "3%",
      containLabel: true
    },
    toolbox: {
      right: 15,
      feature: {
        saveAsImage: {}
      }
    },
    xAxis: {
      type: "category",
      boundaryGap: false,
      data: params.xAxis.data,
      axisLine: params.xAxis && params.xAxis.axisLine ||{}
    },
    yAxis: {
      type: "value",
      axisLine: params.yAxis && params.yAxis.axisLine || {

      }
    },
    series: params.series
  };

  // 绘制图表
  myChart.setOption(option);
  // 监听页面大小变动,动态改变饼图大小
  window.addEventListener(
      "resize",
      function() {
        // 设置延迟器作用：监听事件的触发快于主动触发resize事件，获取不到元素真实的大小。
        // resize会触发两次，在chome中
        this.setTimeout(function() {
          let clientWidth = domContainer.clientWidth;
          let mW;
          columns = columns || 2;
          if (columns == 1) {
            mW = clientWidth;
          } else if (columns == 2) {
            mW = clientWidth / 2 - 25;
          }
          dom.style.width = mW + "px";
          myChart.resize();
        }, 260);
      },
      { passive: true }
  );
  return myChart;
}

export default createLine;
