function createBar (dom, params, domContainer, columns, echartsH, colorString, toolbox) {
  // 引入 ECharts 主模块
  let echarts = require("echarts/lib/echarts");
  // 引入柱状图
  require("echarts/lib/chart/bar");
   // 引入主题
  require("./echarts-theme");
  // 引入提示框
  require("echarts/lib/component/tooltip");
  // 引入标题
  require("echarts/lib/component/title");
  // 引入下载项
  require("echarts/lib/component/toolbox");
  // 引入引导项
  require("echarts/lib/component/legend");
  // 动态设置饼图的宽度和高度
  let clientWidth = domContainer.clientWidth;
  let mW;
  let option = {};
  columns = columns || 2;
  if (columns == 1) {
    mW = clientWidth / 1.25 - 80;
  } else if (columns == 2) {
    mW = clientWidth / 2 - 25;
  }
  dom.style.width = mW + "px";
  dom.style.height = (echartsH || 400) + "px";

  // 基于准备好的dom，初始化echarts实例
  let myChart = echarts.init(dom, colorString || 'walden');
  // 绘制图表
  option = {
    title: {
      text: '压力模型',
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#283b56'
        }
      }
    },
    legend: {
      data: ['接口名', '接口数量']
    },
    toolbox: {
      show: true,
      feature: {
        dataView: { readOnly: false },
        restore: {},
        saveAsImage: {}
      }
    },
    dataZoom: {
      show: false,
      start: 0,
      end: 100
    },
    grid: {
      bottom: '40%',
      left:'10%'
    },
    xAxis: [
      {
        type: 'category',
        axisLabel: {
          interval: 0,
          rotate: 30
        },
        boundaryGap: true,
        data: params.xAxis
        // data: (function () {
        //   var now = new Date();
        //   var res = [];
        //   var len = params.xAxis.length;
        //   while (len--) {
        //     res.unshift(now.toLocaleTimeString().replace(/^\D*/, ''));
        //     // now = new Date(now - 2000);
        //     // params.xAxis.forEach(item => {
        //     //   res.unshift(item)
        //   }
        //   return res;
        // })()
      },
      {
        type: 'category',
        boundaryGap: true,
        axisLabel: {
          textStyle: {
            color: '#fff'
          }
        },
        data: params.xAxis
        // data: (function () {
        //   var res = [];
        //   var len = params.xAxis.length;
        //   while (len--) {
        //     res.push(params.xAxis.length - len - 1);
        //   }
        //   return res;
        // })()
      }
    ],
    yAxis: [
      {
        type: 'value',
        scale: true,
        // name: '接口名',
        // max: 100,
        // min: 0,
        // boundaryGap: [0.2, 0.2]
      },
      {
        type: 'value',
        scale: true,
        // name: '接口数量',
        // max: 50,
        // min: 0,
        // boundaryGap: [0.2, 0.6]
      }
    ],
    series: [
      {
        name: '接口名',
        type: 'bar',
        xAxisIndex: 1,
        yAxisIndex: 1,
        // data:params.series
        data: (function () {
          var res = [];
          var len = params.series.length;
          while (len--) {
            // res.push(Math.round(Math.random() * 1000));
            params.series.forEach(item => {
              res.push(item)
            })
          }
          return res;
        })()
      },
      {
        name: '接口数量',
        type: 'line',
        // data:params.series

        data: (function () {
          var res = [];
          var len = 0;
          while (len < 10) {
            // res.push((Math.random() * 10 + 5).toFixed(1) - 0);
            params.series.forEach(item => {
              res.push(item)
            })
            len++;
          }
          return res;
        })()
      }
    ]
  }
  myChart.setOption(option);

  // app.count = 11;
  // setInterval(function () {
  //   // var axisData = (new Date()).toLocaleTimeString().replace(/^\D*/, '');
  //   var axisData = params.xAxis;


  //   var data0 = option.series[0].data;
  //   console.log(option.series[0].data,'option.series[0].data');
  //   var data1 = option.series[1].data;
  //   data0.shift();
  //   // params.xAxis.forEach(item => {
  //   //   data0.push(item);
  //   // })
  //   data1.shift();
  //   data1.push(1);


  //   option.xAxis[0].data.shift();
  //   option.xAxis[0].data.push(axisData);
  //   option.xAxis[1].data.shift();
  //   option.xAxis[1].data.push(app.count++);

  //   myChart.setOption(option);
  // }, 2100);

  // 监听页面大小变动,动态改变饼图大小
  window.addEventListener(
    "resize",
    function () {
      // 设置延迟器作用：监听事件的触发快于主动触发resize事件，获取不到元素真实的大小。
      // resize会触发两次，在chome中
      this.setTimeout(function () {
        let clientWidth = domContainer.clientWidth;
        console.log(clientWidth, 'clientWidth');
        let mW;
        columns = columns || 2;
        if (columns == 1) {
          mW = clientWidth - 80;
        } else if (columns == 2) {
          mW = clientWidth / 2 - 25;
        }
        dom.style.width = mW + "px";
        myChart.resize();
      }, 10);
    },
    { passive: true }
  );
  return myChart;
}

export default createBar;
