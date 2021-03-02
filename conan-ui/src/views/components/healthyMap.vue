<!-- 健康地图 -->
<template>
  <div ref="healthyMap">
    <!-- 健康地图 -->
    <div id="main" style="width: 100%;height:300px" ref="main"></div>
  </div>
</template>

<script>
import { getAgentHome } from "@/api/home/index.js";
import createMap from "@/utils/echarts/map.js";

export default {
  name: "HealthMap",
  data() {
    return {
      geoCoordMap: {},
      keys: [],
      obj: {},
      mm: {},
      map_loading: true,
      TimeId: ""
    };
  },
  // 生命周期 - 创建完成（访问当前this实例）
  created() {
    this.getMessage();
  },
  // 生命周期 - 挂载完成（访问DOM元素）
  mounted() {
    this.healthyMap();
    this.TimeId = setInterval(() => {
      this.getMessage();
    }, 30000);
  },
  watch: {
    mm() {
      if (JSON.stringify(this.mm) !== "{}") {
        this.healthyMap();
        this.map_loading = false;
      } else {
        this.$message.error("暂无数据");
      }
    },
    $route(route) {
      if (route.path != "/index") {
        //当前不在主页就要清楚定时器
         clearInterval(this.TimeId);
      } 
    },
  },
  methods: {
    async getMessage() {
      try {
        getAgentHome().then(res => {
          if (res.code === 200) {
            if (Object.keys(res.data).length) {
              var clearObj = {};
              var e = res.data;
              for (var key in e) {
                clearObj[key] = [];
                e[key].forEach(item => {
                  // item.agentId = (item.agentId * 100).toFixed(2) * 1;
                  // // if (item.agentId !== 0) {
                  //   item.agentId = (item.agentId / 100).toFixed(4) + "";
                    clearObj[key].push(item);
                  // }
                });
              }
              this.obj = clearObj;
              this.mm = this.map(this.obj);
            } else {
              this.$message.error("当前无数据");
            }
          } else {
            this.$message.error(res.msg);
          }
        });
      } catch (error) {
        this.$message.error("网络错误");
      }
    },
    // 创建地图
    healthyMap() {
      var aobj = {
        Afghanistan: "阿富汗",
        Albania: "阿尔巴尼亚",
        Algeria: "阿尔及利亚",
        Andorra: "安道尔",
        Angola: "安哥拉",
        Antarctica: "南极洲",
        "Antigua and Barbuda": "安提瓜和巴布达",
        Argentina: "阿根廷",
        Armenia: "亚美尼亚",
        Australia: "澳大利亚",
        Austria: "奥地利",
        Azerbaijan: "阿塞拜疆",
        "The Bahamas": "巴哈马",
        Bahrain: "巴林",
        Bangladesh: "孟加拉国",
        Barbados: "巴巴多斯",
        Belarus: "白俄罗斯",
        Belgium: "比利时",
        Belize: "伯利兹",
        Benin: "贝宁",
        Bermuda: "百慕大",
        Bhutan: "不丹",
        Bolivia: "玻利维亚",
        "Bosnia and Herz.": "波黑",
        Botswana: "博茨瓦纳",
        Brazil: "巴西",
        Brunei: "文莱",
        Bulgaria: "保加利亚",
        "Burkina Faso": "布基纳法索",
        Burundi: "布隆迪",
        Bahamas: "巴哈马国",
        Cambodia: "柬埔寨",
        Cameroon: "喀麦隆",
        Canada: "加拿大",
        "Cape Verde": "佛得角",
        "Central African Rep.": "中非共和国",
        Chad: "乍得",
        Chile: "智利",
        China: "中国",
        Colombia: "哥伦比亚",
        Comoros: "科摩罗",
        Congo: "刚果共和国",
        "Costa Rica": "哥斯达黎加",
        Croatia: "克罗地亚",
        Cuba: "古巴",
        Cyprus: "塞浦路斯",
        "Czech Rep.": "捷克共和国",
        "Côte dIvoire": "科特迪瓦",
        Denmark: "丹麦",
        Djibouti: "吉布提",
        "Dominican Rep.": "多米尼加",
        "Dominican Republic": "多明尼加共和国",
        Ecuador: "厄瓜多尔",
        Egypt: "埃及",
        "El Salvador": "萨尔瓦多",
        "Eq. Guinea": "赤道几内亚",
        Eritrea: "厄立特里亚",
        Estonia: "爱沙尼亚",
        Ethiopia: "埃塞俄比亚",
        "Falkland Islands": "福克兰群岛",
        "Faroe Islands": "法罗群岛",
        Fiji: "斐济",
        Finland: "芬兰",
        France: "法国",
        "French Guiana": "法属圭亚那",
        "French Southern and Antarctic Lands": "法属南半球和南极领地",
        Gabon: "加蓬",
        Gambia: "冈比亚",
        "Gaza Strip": "加沙",
        Georgia: "格鲁吉亚",
        Germany: "德国",
        Ghana: "加纳",
        Greece: "希腊",
        Greenland: "格陵兰",
        Grenada: "格林纳达",
        Guadeloupe: "瓜德罗普",
        Guatemala: "危地马拉",
        Guinea: "几内亚",
        "Guinea-Bissau": "几内亚比绍",
        Guyana: "圭亚那",
        Haiti: "海地",
        Honduras: "洪都拉斯",
        "Hong Kong": "香港",
        Hungary: "匈牙利",
        Iceland: "冰岛",
        India: "印度",
        Indonesia: "印尼",
        Iran: "伊朗",
        Iraq: "伊拉克",
        "Iraq-Saudi Arabia Neutral Zone": "伊拉克阿拉伯中立区",
        Ireland: "爱尔兰",
        "Isle of Man": "马恩岛",
        Israel: "以色列",
        Italy: "意大利",
        "Ivory Coast": "科特迪瓦",
        Jamaica: "牙买加",
        "Jan Mayen": "扬马延岛",
        Japan: "日本",
        Jordan: "约旦",
        Kazakhstan: "哈萨克斯坦",
        Kenya: "肯尼亚",
        Kerguelen: "凯尔盖朗群岛",
        Kiribati: "基里巴斯",
        "Dem. Rep. Korea": "朝鲜",
        Korea: "韩国",
        Kuwait: "科威特",
        Kyrgyzstan: "吉尔吉斯斯坦",
        "Lao PDR": "老挝",
        Latvia: "拉脱维亚",
        Lebanon: "黎巴嫩",
        Lesotho: "莱索托",
        Liberia: "利比里亚",
        Libya: "利比亚",
        Liechtenstein: "列支敦士登",
        Lithuania: "立陶宛",
        Luxembourg: "卢森堡",
        Macau: "澳门",
        Macedonia: "马其顿",
        Madagascar: "马达加斯加",
        Malawi: "马拉维",
        Malaysia: "马来西亚",
        Maldives: "马尔代夫",
        Mali: "马里",
        Malta: "马耳他",
        Martinique: "马提尼克",
        Mauritania: "毛里塔尼亚",
        Mauritius: "毛里求斯",
        Mexico: "墨西哥",
        Moldova: "摩尔多瓦",
        Monaco: "摩纳哥",
        Mongolia: "蒙古",
        Morocco: "摩洛哥",
        Mozambique: "莫桑比克",
        Myanmar: "缅甸",
        Namibia: "纳米比亚",
        Nepal: "尼泊尔",
        Netherlands: "荷兰",
        "New Caledonia": "新喀里多尼亚",
        "New Zealand": "新西兰",
        Nicaragua: "尼加拉瓜",
        Niger: "尼日尔",
        Nigeria: "尼日利亚",
        "Northern Mariana Islands": "北马里亚纳群岛",
        Norway: "挪威",
        Oman: "阿曼",
        Pakistan: "巴基斯坦",
        Panama: "巴拿马",
        "Papua New Guinea": "巴布亚新几内亚",
        Paraguay: "巴拉圭",
        Peru: "秘鲁",
        Philippines: "菲律宾",
        Poland: "波兰",
        Portugal: "葡萄牙",
        "Puerto Rico": "波多黎各",
        Qatar: "卡塔尔",
        Reunion: "留尼旺岛",
        Romania: "罗马尼亚",
        Russia: "俄罗斯",
        Rwanda: "卢旺达",
        "San Marino": "圣马力诺",
        "Sao Tome and Principe": "圣多美和普林西比",
        "Saudi Arabia": "沙特阿拉伯",
        Senegal: "塞内加尔",
        Seychelles: "塞舌尔",
        "Sierra Leone": "塞拉利昂",
        Singapore: "新加坡",
        Slovakia: "斯洛伐克",
        Slovenia: "斯洛文尼亚",
        "Solomon Is.": "所罗门群岛",
        Somalia: "索马里",
        "South Africa": "南非",
        Spain: "西班牙",
        "Sri Lanka": "斯里兰卡",
        "St. Christopher-Nevis": "圣",
        "St. Lucia": "圣露西亚",
        "St. Vincent and the Grenadines": "圣文森特和格林纳丁斯",
        Sudan: "苏丹",
        Suriname: "苏里南",
        Svalbard: "斯瓦尔巴特群岛",
        Swaziland: "斯威士兰",
        Sweden: "瑞典",
        Switzerland: "瑞士",
        Syria: "叙利亚",
        Taiwan: "台湾",
        Tajikistan: "塔吉克斯坦",
        Tanzania: "坦桑尼亚",
        Thailand: "泰国",
        Togo: "多哥",
        Tonga: "汤加",
        "Trinidad and Tobago": "特里尼达和多巴哥",
        Tunisia: "突尼斯",
        Turkey: "土耳其",
        Turkmenistan: "土库曼斯坦",
        "Turks and Caicos Islands": "特克斯和凯科斯群岛",
        "Timor-Leste": "东帝汶",
        Uganda: "乌干达",
        Ukraine: "乌克兰",
        "United Arab Emirates": "阿联酋",
        "United Kingdom": "英国",
        "United States": "美国",
        Uruguay: "乌拉圭",
        Uzbekistan: "乌兹别克斯坦",
        Vanuatu: "瓦努阿图",
        Venezuela: "委内瑞拉",
        Vietnam: "越南",
        "W. Sahara": "西撒哈拉",
        "Western Samoa": "西萨摩亚",
        Yemen: "也门",
        Yugoslavia: "南斯拉夫",
        "Dem. Rep. Congo": "刚果民主共和国",
        Zambia: "赞比亚",
        Zimbabwe: "津巴布韦",
        "S. Sudan": "南苏丹",
        Somaliland: "索马里兰",
        Montenegro: "黑山",
        Kosovo: "科索沃",
        Serbia: "塞尔维亚"
      };
      const provinceArr = Object.values(aobj);
      const regions = [];
      provinceArr.forEach(item => {
        regions.push({
          name: item,
          value: 0,
          itemStyle: {
            normal: {
              opacity: 0,
              label: {
                show: false
              }
            }
          }
        });
      });
      var geoCoordMap = this.geoCoordMap;
      console.log('this.geoCoordMap坐标点===>>>>',this.geoCoordMap);
      var ff = [];
      this.keys.forEach((item, index) => {
        // console.log(item, index);
        ff.push([item, this.mm["arr" + index]]);
      });
      var convertData = function(data) {
        var res = [];
        for (var i = 0; i < data.length; i++) {
          var dataItem = data[i];
          var fromCoord = geoCoordMap[dataItem[0].name];
          var toCoord = geoCoordMap[dataItem[1].name];
          if (fromCoord && toCoord) {
            res.push({
              fromName: dataItem[0].name,
              toName: dataItem[1].name,
              coords: [fromCoord, toCoord],
              value: dataItem[1].value
            });
          }
        }
        return res;
      };
      var color = ["#a6c84c", "#ffa022", "#46bee9", "#fff"];
      var series = [];
      ff.forEach((item, i) => {
        series.push(
          {
            name: item[0],
            type: "lines",
            zlevel: 1,
            effect: {
              show: true,
              period: 6,
              trailLength: 0.7,
              color: "#fff",
              symbolSize: 3
            },
            lineStyle: {
              normal: {
                color: color[i],
                width: 0,
                curveness: 0.2
              }
            },
            data: convertData(item[1])
          },
          {
            name: item[0],
            type: "lines",
            zlevel: 2,
            effect: {
              show: true,
              period: 6,
              trailLength: 0,
              symbolSize: 1
            },
            lineStyle: {
              normal: {
                color: e => {
                  // if (e.value) {
                  //   if ((e.value.agentId * 100).toFixed(2) < 80) {
                  //     return "red";
                  //   } else {
                  //     return color[i];
                  //   }
                  // } else {
                  //   return color[i];
                  // }
                  return color[i];
                },
                width: 2,
                opacity: 0.6,
                curveness: 0.2
              }
            },
            data: convertData(item[1])
          },
          {
            name: item[0],
            type: "effectScatter",
            coordinateSystem: "geo",
            zlevel: 2,
            rippleEffect: {
              brushType: "stroke"
            },
            label: {
              normal: {
                show: true,
                position: "right",
                formatter: "{b}"
              }
            },
            symbolSize: function(val) {
              // return val[2] / 8;
              return 10;
            },
            itemStyle: {
              normal: {
                color: e => {
                  var a = "";
                  var obj = this.obj;
                  for (var key in obj) {
                    obj[key].forEach(item => {
                      // if ((item.agentId * 100).toFixed(2) < 80) {
                        a = key;
                        return a;
                      // }
                    });
                  }
                  if (e.data.name === a) {
                    // return "red";
                        return color[i];
                  }
                  if (e.value[e.value.length - 1]) {
                    // if (  (e.value[e.value.length - 1].agentId * 100).toFixed(  2  ) < 80  ) {
                    //   return "red";
                    // } else {
                      return color[i];
                    // }
                  } else {
                    // return color[i+1];
                      return color[i];

                  }
                }
              }
            },
            data: item[1].map(function(dataItem) {
              if (!dataItem[1].value) {
                return {
                  name: `${dataItem[1].name}`,
                  value: geoCoordMap[dataItem[1].name].concat([
                    dataItem[1].value
                  ])
                };
              }
              return {
                // name: `${(dataItem[1].value.agentId * 100).toFixed(2)}%,${
                //   dataItem[1].name
                // }`,
                name: `${dataItem[1].value.agentId } , ${
                  dataItem[1].name
                }`,
                value: geoCoordMap[dataItem[1].name].concat([dataItem[1].value])
              };
            })
          }
        );
      });
      let params = {
        regions: regions,
        series: series
      };
      createMap(this.$refs.main, params, this.$refs.healthyMap, 1, 300);
    },

    map(obj) {
      console.log(obj,'初始化中心点====>>>> obj');
      var keys = Object.keys(obj); // 获取键名
      this.keys = keys;
      // console.log(keys,'keys');
      // 先初始化中心点
      keys.forEach((item, index, arr) => {
        // console.log(arr)
        if (index === 0) {
          this.geoCoordMap[item] = [-170, -30];
          return;
        }
        // console.log(geoCoordMap[arr[index-1]][0])
        this.geoCoordMap[item] = [
          this.geoCoordMap[arr[index - 1]][0] + 30,
          -30
        ];
      });
      for (var key in obj) {
        obj[key].forEach((item, index, arr) => {
          var num = this.geoCoordMap[key];
          console.log(num,'num');
          if (key === this.keys[0]) {
            this.geoCoordMap[arr[index].name] = [
              (Math.random() * 15 + num[0]).toFixed(3) * 1,
              (Math.random() * 37 + num[1] - 19).toFixed(3) * 1
            ]; // geoCoordMap[key] // (Math.random()*(n-m) + m).toFixed(2)
          } else {
            this.geoCoordMap[arr[index].name] = [
              (Math.random() * 30 + (num[0] - 15)).toFixed(3) * 1,
              (Math.random() * 37 + num[1] - 19).toFixed(3) * 1
            ]; // geoCoordMap[key] // (Math.random()*(n-m) + m).toFixed(2)
          }
          console.log(index,arr[index].name,key,'?????')
        });
      }
      var values = Object.values(this.geoCoordMap);
      // console.log(values)
      for (var i = 0; i < values.length; i++) {
        for (var j = i + 1; j < values.length; j++) {
          // console.log(equar(values[i],values[j]))
          if (this.equar(values[i], values[j])) {
            this.map();
            break;
          }
        }
      }
      var objs = {};
      keys.map((item, index, arr) => {
        // console.log(index,arr.length)
        if (index !== arr.length - 1) {
          objs["arr" + index] = [
            [{ name: arr[index] }, { name: arr[index + 1] }]
          ];
          if (index === 0) {
            objs["arr" + index].push([
              { name: arr[index] },
              { name: arr[index] }
            ]);
          }
          obj[item].forEach((items, indexs, arrs) => {
            objs["arr" + index].push([
              {
                name: arr[index]
              },
              {
                name: obj[item][indexs].name,
                value: obj[item][indexs]
              }
            ]);
          });
        } else {
          // console.log(...obj[item],'===>>>> obj[item]');
          objs["arr" + index] = [];
          obj[item].forEach((items, indexs, arrs) => {
            objs["arr" + index].push([
              {
                name: arr[index]
              },
              {
                name: obj[item][indexs].name,
                value: obj[item][indexs]
              }
            ]);
          });
        }
      });
      // console.log(objs)
      // console.log(geoCoordMap)
      return {
        ...objs
      };
    },
    equar(a, b) {
      // 判断数组的长度
      if (a.length !== b.length) {
        return false;
      } else {
        // 循环遍历数组的值进行比较
        for (var i = 0; i < a.length; i++) {
          if (a[i] !== b[i]) {
            return false;
          }
        }
        return true;
      }
    }
  },
  beforeDestroy() {
    // 清除定时器
    clearInterval(this.TimeId);
  },

};
</script>
<style scoped>
</style>
