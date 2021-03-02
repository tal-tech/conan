function createBar (dom, params, domContainer, columns, echartsH, colorString, toolbox) {
  // 引入 ECharts 主模块
  let echarts = require("echarts/lib/echarts");
  // 引入柱状图
  require("echarts/lib/chart/bar");
   // 引入主题
  require("echarts/theme/macarons");
  // 引入提示框
  require("echarts/lib/component/tooltip");
  // 引入标题
  require("echarts/lib/component/title");
  // 引入下载项
  require("echarts/lib/component/toolbox");
  // 引入引导项
  require("echarts/lib/component/legend");
console.log(params,'params');
  // 动态设置饼图的宽度和高度
  let clientWidth = domContainer.clientWidth;
  let mW;
  columns = columns || 2;
  if (columns == 1) {
    mW = clientWidth - 20;
  } else if (columns == 2) {
    mW = clientWidth / 2 - 25;
  }
  dom.style.width = mW + "px";
  dom.style.height = (echartsH || 400) + "px";

  // 基于准备好的dom，初始化echarts实例
  let myChart = echarts.init(dom,  colorString||'macarons');
  
  // 绘制图表
  myChart.setOption({
    title: params.title,
    tooltip:params.tooltip,
    legend: {
      data: params.legend
    },
    toolbox:params.toolbox,
    grid: {
      left: "1%",
      right: "4%",
      bottom: "3%",
      containLabel: true
    },
    xAxis: params.xAxis,
    yAxis: params.yAxis,
    series: params.series
  });
  // 监听页面大小变动,动态改变饼图大小
  window.addEventListener(
      "resize",
      function () {
        // 设置延迟器作用：监听事件的触发快于主动触发resize事件，获取不到元素真实的大小。
        // resize会触发两次，在chome中
        this.setTimeout(function () {
          let clientWidth = domContainer.clientWidth;
          let mW;
          columns = columns || 2;
          if (columns == 1) {
            mW = clientWidth - 20;
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

export default createBar;
