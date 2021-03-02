// 注解
// dom: 饼图渲染元素DOM（必填）
// params: 饼图所需要的参数（必填）
/**
 * params = {
 *  title: {
 *    text: "", 主标题
 *    subtext: "" 副标题
 *  },
 *  legend: {
 *    data: [] 标注
 *  },
 *  series: {
 *    name: '', 浮层标题
 *    data: [] 饼图字块数据;元素格式:{name: String, value: Number},name-子块名称，value-子块值
 *  }
 * };
 */
// domContainer: 饼图所在容器DOM（必填）
// columns: 饼图列数，默认2，即一行容纳饼图个数。目前支持1、2
// echartsH: 饼图高度，默认400
function createPie (dom, params, domContainer, columns, echartsH, colorString) {
  console.log(params,'params===>>>');
  // 引入 ECharts 主模块
  let echarts = require("echarts/lib/echarts");
  // 引入饼状图
  require("echarts/lib/chart/pie");
  // 引入主题
  require("echarts/theme/macarons");
  // walden
  require("echarts/theme/vintage");
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
    mW = clientWidth - 20;
  } else if (columns == 2) {
    mW = clientWidth / 2 - 25;
  }
  dom.style.width = mW + "px";
  dom.style.height = (echartsH || 400) + "px";

  // 基于准备好的dom，初始化echarts实例
  let myChart = echarts.init(dom, colorString || 'macarons');
  let option = {
    title: {
      text: params.title.text || "",
      subtext: params.title.subtext || "",
      x: "center"
    },
    tooltip: params.tooltip,
    legend: {
      orient: "vertical",
      left: 0,
      data: params.legend.data || []
    },
    series: [
       {
        name: params.series.name,
        type: "pie",
        radius: params.series.radius || "55%",
        center: params.series.center || ["50%", "55%"],
        data: params.series.data || [],
        // itemStyle: {
        //   emphasis: {
        //     shadowBlur: 10,
        //     shadowOffsetX: 0,
        //     shadowColor: "rgba(0, 0, 0, 0.5)"
        //   }
        // },
        label: params.series.label
      }
    ],
    toolbox: {
      right: 15,
      feature: {
        saveAsImage: {}
      }
    },
  };
  // 绘制图表
  myChart.setOption(option);
  // 监听页面大小变动,动态改变饼图大小
  window.addEventListener("resize", function () {
    // 设置延迟器作用：监听事件的触发快于主动触发resize事件，获取不到元素真实的大小。
    // resize会触发两次，在chome中
    this.setTimeout(function () {
      let clientWidth = domContainer.clientWidth;
      columns = columns || 2;
      let mW;
      if (columns == 1) {
        mW = clientWidth - 20;
      } else if (columns == 2) {
        mW = clientWidth / 2 - 25;
      }
      dom.style.width = mW + "px";
      myChart.resize();
    }, 260);
  });
  return myChart;
}

export default createPie;
