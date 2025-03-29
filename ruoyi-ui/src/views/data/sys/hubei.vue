<template>
  <div>
    <!-- 标题 -->
    <div class="model_title">
      <span>重点关注病虫害分析</span>
    </div>
    <div class="content-container">
      <!-- 查询表单 -->
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <div class="model_form">
          <el-form-item label="区域" style="margin-left: 20px;">
            <el-select
              v-model="formInline.regionType"
              placeholder="请选择区域类型"
              size="small"
              style="width: 120px;"
              @change="handleRegionTypeChange"
            >
              <el-option label="全国" value="nation"></el-option>
              <el-option label="省级" value="province"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="地区" style="margin-left: 20px;" v-if="formInline.regionType === 'province'">
            <el-select
              v-model="formInline.region"
              placeholder="请选择省份"
              size="small"
              style="width: 120px;"
              @change="handleRegionChange"
            >
              <el-option
                v-for="province in provinces"
                :key="province.id"
                :label="province.name"
                :value="province.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="作物" style="margin-left: 20px;">
            <el-select v-model="formInline.crop" placeholder="请选择作物" size="small" style="width: 120px;">
              <el-option label="小麦" value="小麦"></el-option>
              <el-option label="油菜" value="油菜"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="指标" style="margin-left: 20px;">
            <el-select v-model="formInline.indicator" placeholder="请选择指标" size="small" style="width: 150px;">
              <el-option
                v-for="item in availableIndicators"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="日期" style="margin-left: 20px;">
            <el-date-picker
              v-model="formInline.date"
              type="date"
              placeholder="请选择日期"
              value-format="yyyy-MM-dd"
              size="small"
              style="width: 150px;"
            ></el-date-picker>
          </el-form-item>
          <div class="model_button">
            <el-form-item>
              <el-button type="primary" @click="onSubmit" :loading="loading">查询</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="info" @click="onReset">重置</el-button>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <!-- 地图和数据展示 -->
      <el-card class="table-card">
        <div class="chart-switcher">
          <el-button-group>
            <el-button
              size="small"
              :type="chartType === 'map' ? 'primary' : ''"
              @click="showMapChart"
              icon="el-icon-map-location"
            >地图</el-button>
            <el-button
              size="small"
              :type="chartType === 'line' ? 'primary' : ''"
              @click="showLineChart"
              icon="el-icon-data-line"
            >折线图</el-button>
            <el-button
              size="small"
              :type="chartType === 'bar' ? 'primary' : ''"
              @click="showBarChart"
              icon="el-icon-data-bar"
            >柱状图</el-button>
          </el-button-group>
        </div>
        <div class="map-container">
          <div id="map" style="width: 100%; height: 500px;"></div>
          <div class="legend-container" v-if="chartType === 'map'">
            <div class="legend-item">
              <span class="legend-title">{{ legendTitle }}</span>
              <ul>
                <li v-for="(item, index) in legendData" :key="index">
                  <span class="legend-color" :style="{ backgroundColor: legendColors[index] }"></span>
                  <span class="legend-text">{{ item.range }} ({{ item.count }}个)</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import axios from 'axios';
import cityMap from "@/js/china-main-city-map.js";

export default {
  data() {
    return {
      formInline: {
        regionType: 'nation', // 区域类型: nation/province
        region: '',           // 区域ID
        crop: '小麦',
        indicator: '累计防治面积',
        date: '2025-02-23',
      },
      chartType: '', // 当前图表类型: map/line/bar
      loading: false,
      mapData: [],
      timeSeriesData: {
        '2024': [],
        '2025': [],
        '常年': []
      },
      legendData: [
        { range: '0 - 1000公顷', count: 8 },
        { range: '1000 - 3000公顷', count: 3 },
        { range: '3000 - 5000公顷', count: 1 },
        { range: '超过5000公顷', count: 0 },
      ],
      legendColors: ['#83CC94', '#75B9FA', '#FF9F00', '#EA7E8C'],
      legendTitle: '累计防治面积（公顷）',
      chartInstance: null, // 保存图表实例
      cityNames: [],       // 动态生成的城市名称列表
      // 省份列表
      provinces: [
        { id: '420000', name: '湖北省' },
        { id: '110000', name: '北京市' },
        { id: '310000', name: '上海市' },
        { id: '440000', name: '广东省' },
        { id: '330000', name: '浙江省' },
        { id: '320000', name: '江苏省' },
        { id: '370000', name: '山东省' },
        { id: '410000', name: '河南省' },
        { id: '430000', name: '湖南省' },
        { id: '510000', name: '四川省' },
      ],
      // 根据图表类型动态显示的指标选项
      availableIndicators: [
        { label: '累计防治面积', value: '累计防治面积' },
        { label: '累计发生面积', value: '累计发生面积' },
        { label: '达标率', value: '达标率' }
      ],
      // 所有可能的指标选项
      allIndicators: [
        { label: '累计防治面积', value: '累计防治面积' },
        { label: '累计发生面积', value: '累计发生面积' },
        { label: '达标率', value: '达标率' }
      ],
      // 城市映射关系
      cityMapping: {
        'nation': ['北京市', '天津市', '河北省', '山西省', '内蒙古自治区', '辽宁省', '吉林省', '黑龙江省', '上海市', '江苏省', '浙江省', '安徽省', '福建省', '江西省', '山东省', '河南省', '湖北省', '湖南省', '广东省', '广西壮族自治区', '海南省', '重庆市', '四川省', '贵州省', '云南省', '西藏自治区', '陕西省', '甘肃省', '青海省', '宁夏回族自治区', '新疆维吾尔自治区'],
        '420000': ['武汉市', '黄石市', '十堰市', '宜昌市', '襄阳市', '鄂州市', '荆门市', '孝感市', '荆州市', '黄冈市', '咸宁市', '随州市', '恩施州', '仙桃市', '潜江市', '天门市', '神农架林区'],
        '110000': ['东城区', '西城区', '朝阳区', '丰台区', '石景山区', '海淀区', '顺义区', '通州区', '大兴区', '房山区', '门头沟区', '昌平区', '平谷区', '密云区', '延庆区'],
        '310000': ['黄浦区', '徐汇区', '长宁区', '静安区', '普陀区', '虹口区', '杨浦区', '闵行区', '宝山区', '嘉定区', '浦东新区', '金山区', '松江区', '青浦区', '奉贤区', '崇明区'],
        '440000': ['广州市', '韶关市', '深圳市', '珠海市', '汕头市', '佛山市', '江门市', '湛江市', '茂名市', '肇庆市', '惠州市', '梅州市', '汕尾市', '河源市', '阳江市', '清远市', '东莞市', '中山市', '潮州市', '揭阳市', '云浮市'],
        '330000': ['杭州市', '宁波市', '温州市', '嘉兴市', '湖州市', '绍兴市', '金华市', '衢州市', '舟山市', '台州市', '丽水市'],
        '320000': ['南京市', '无锡市', '徐州市', '常州市', '苏州市', '南通市', '连云港市', '淮安市', '盐 Cities', '扬州市', '镇江市', '泰州市', '宿迁市'],
        '370000': ['济南市', '青岛市', '淄博市', '枣庄市', '东营市', '烟台市', '潍坊市', '济宁市', '泰安市', '威海市', '日照市', '临沂市', '德州市', '聊城市', '滨州市', '菏泽市'],
        '410000': ['郑州市', '开封市', '洛阳市', '平顶山市', '安阳市', '鹤壁市', '新乡市', '焦作市', '濮阳市', '许昌市', '漯河市', '三门峡市', '南阳市', '商丘市', '信阳市', '周口市', '驻马店市', '济源市'],
        '430000': ['长沙市', '株洲市', '湘潭市', '衡阳市', '邵阳市', '岳阳市', '常德市', '张家界市', '益阳市', '郴州市', '永州市', '怀化市', '娄底市', '湘西州'],
        '510000': ['成都市', '自贡市', '攀枝花市', '泸州市', '德阳市', '绵阳市', '广元市', '遂宁市', '内江市', '乐山市', '南充市', '眉山市', '宜宾市', '广安市', '达州市', '雅安市', '巴中市', '资阳市', '阿坝州', '甘孜州', '凉山州']
      }
    };
  },
  watch: {
    chartType(newVal) {
      if (newVal === 'map') {
        this.availableIndicators = this.allIndicators;
      } else {
        this.availableIndicators = this.allIndicators.filter(
          item => item.value !== '达标率'
        );
        if (this.formInline.indicator === '达标率') {
          this.formInline.indicator = this.availableIndicators[0].value;
        }
      }
    },
    'formInline.regionType': {
      handler(newVal) {
        if (newVal === 'nation') {
          this.loadChinaMap();
        } else if (newVal === 'province' && this.formInline.region) {
          this.loadProvinceMap(this.formInline.region);
        }
      },
      immediate: true
    }
  },
  mounted() {
    this.chartType = 'map';
    this.loadChinaMap();
  },
  methods: {
    handleRegionTypeChange(val) {
      if (val === 'nation') {
        this.loadChinaMap();
      } else if (val === 'province') {
        if (this.formInline.region) {
          this.loadProvinceMap(this.formInline.region);
        } else {
          this.formInline.region = this.provinces[0].id;
          this.loadProvinceMap(this.formInline.region);
        }
      }
    },

    handleRegionChange(provinceId) {
      this.loadProvinceMap(provinceId);
    },

    async loadChinaMap() {
      try {
        const response = await axios.get("/static/json/map/100000.json");
        echarts.registerMap('china', response.data);
        this.cityNames = this.cityMapping['nation'].map(ele => {
          return ele.replace(/(自治区|省|市|特别行政区|壮族|回族|维吾尔)/g, "")
            .replace(/^北京$/, "北京");
        });
        this.initMap();
        this.fetchMockData();
      } catch (error) {
        console.error('加载中国地图失败:', error);
      }
    },

    async loadProvinceMap(provinceId) {
      try {
        const response = await axios.get(`/static/json/map/${provinceId}.json`);
        const mapName = `province_${provinceId}`;
        echarts.registerMap(mapName, response.data);
        this.cityNames = this.cityMapping[provinceId] || [].map(ele => {
          return ele.replace(/(自治区|省|市|特别行政区|壮族|回族|维吾尔)/g, "")
            .replace(/^北京$/, "北京");
        });
        this.initMap(mapName);
        this.fetchMockData();
      } catch (error) {
        console.error('加载省份地图失败:', error);
      }
    },

    showMapChart() {
      this.chartType = 'map';
      this.updateLegendTitle();
      this.initMap();
    },

    showLineChart() {
      this.chartType = 'line';
      this.updateChart();
    },

    showBarChart() {
      this.chartType = 'bar';
      this.updateChart();
    },

    updateChart() {
      if (!this.chartInstance) {
        this.chartInstance = echarts.init(document.getElementById('map'));
      }

      const isRate = this.formInline.indicator === '达标率';
      const unit = isRate ? '%' : '公顷';

      const option = {
        title: {
          text: `${this.formInline.crop}病虫害${this.formInline.indicator}趋势对比`,
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          formatter: params => {
            let result = params[0].name + '<br/>';
            params.forEach(item => {
              result += `${item.seriesName}: ${item.value}${unit}<br/>`;
            });
            return result;
          }
        },
        legend: {
          data: ['2024年', '2025年', '常年'],
          top: 30
        },
        grid: {
          top: 80,
          bottom: 80,
          left: 80,
          right: 50
        },
        xAxis: {
          type: 'category',
          data: this.cityNames,
          axisLabel: {
            rotate: 45,
            interval: 0,
            fontSize: 10,
            margin: 15,
            formatter: function(value) {
              if (value.length > 3) {
                return value.substring(0, 2) + '\n' + value.substring(2);
              }
              return value;
            }
          }
        },
        yAxis: {
          type: 'value',
          name: this.formInline.indicator + (isRate ? '(%)' : '(公顷)')
        },
        series: [
          {
            name: '2024年',
            type: this.chartType,
            data: this.timeSeriesData['2024'],
            smooth: this.chartType === 'line',
            areaStyle: this.chartType === 'line' ? {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(255, 154, 0, 0.8)' },
                { offset: 1, color: 'rgba(255, 154, 0, 0.1)' }
              ])
            } : undefined,
            itemStyle: this.chartType === 'bar' ? {
              color: '#FF9A00'
            } : undefined,
            barWidth: '20%'
          },
          {
            name: '2025年',
            type: this.chartType,
            data: this.timeSeriesData['2025'],
            smooth: this.chartType === 'line',
            areaStyle: this.chartType === 'line' ? {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(58, 77, 233, 0.8)' },
                { offset: 1, color: 'rgba(58, 77, 233, 0.1)' }
              ])
            } : undefined,
            itemStyle: this.chartType === 'bar' ? {
              color: '#3A4DE9'
            } : undefined,
            barWidth: '20%'
          },
          {
            name: '常年',
            type: this.chartType,
            data: this.timeSeriesData['常年'],
            smooth: this.chartType === 'line',
            areaStyle: this.chartType === 'line' ? {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(120, 200, 80, 0.8)' },
                { offset: 1, color: 'rgba(120, 200, 80, 0.1)' }
              ])
            } : undefined,
            itemStyle: this.chartType === 'bar' ? {
              color: '#78C850'
            } : undefined,
            barWidth: '20%'
          }
        ]
      };

      this.chartInstance.setOption(option, true);
    },

    updateLegendTitle() {
      const isRate = this.formInline.indicator === '达标率';
      this.legendTitle = this.formInline.indicator + (isRate ? '（%）' : '（公顷）');

      if (isRate) {
        this.legendData = [
          { range: '0 - 10%', count: 11 },
          { range: '10 - 30%', count: 1 },
          { range: '30 - 50%', count: 0 },
          { range: '超过50%', count: 0 },
        ];
      } else {
        this.legendData = [
          { range: '0 - 1000公顷', count: 8 },
          { range: '1000 - 3000公顷', count: 3 },
          { range: '3000 - 5000公顷', count: 1 },
          { range: '超过5000公顷', count: 0 },
        ];
      }
    },

    initMap(mapName) {
      if (!this.chartInstance) {
        this.chartInstance = echarts.init(document.getElementById('map'));
      }

      const isRate = this.formInline.indicator === '达标率';
      const maxValue = isRate ? 100 : Math.max(...this.mapData.map(item => item.value), 5000);
      const currentMap = this.formInline.regionType === 'nation' ? 'china' : `province_${this.formInline.region}`;

      const option = {
        title: {
          text: `${this.formInline.crop}病虫害${this.formInline.indicator}(${this.formInline.date})`,
          left: 'center',
          top: '10px',
        },
        tooltip: {
          trigger: 'item',
          formatter: (params) => {
            return `${params.name}: ${params.value || 0}${isRate ? '%' : '公顷'}`;
          },
        },
        visualMap: {
          min: 0,
          max: maxValue,
          left: 'left',
          top: 'bottom',
          text: ['高', '低'],
          calculable: true,
          inRange: {
            color: ['#83CC94', '#EA7E8C'],
          },
        },
        series: [
          {
            type: 'map',
            map: currentMap,
            data: this.mapData,
            roam: true,
            label: {
              show: true,
              position: 'inside',
              align: 'center',
              verticalAlign: 'middle',
              fontSize: this.formInline.regionType === 'nation' ? 10 : 8,
              color: '#333',
              fontWeight: 'bold',
              formatter: (params) => {
                if (this.formInline.regionType === 'nation') {
                  const shortNames = {
                    '北京市': '北京',
                    '天津市': '天津',
                    '内蒙古自治区': '内蒙古',
                    '广西壮族自治区': '广西',
                    '西藏自治区': '西藏',
                    '宁夏回族自治区': '宁夏',
                    '新疆维吾尔自治区': '新疆',
                    '黑龙江省': '黑龙江',
                    '辽宁省': '辽宁',
                    '吉林省': '吉林'
                  };
                  return shortNames[params.name] || params.name.replace(/省|市|自治区|壮族|回族|维吾尔/g, '');
                }
                if (params.name.length > 3 && (params.name.endsWith('区') || params.name.endsWith('市'))) {
                  return params.name.substring(0, params.name.length - 1);
                }
                return params.name;
              },
              padding: [5, 5],
              backgroundColor: 'rgba(255,255,255,0.7)',
              borderColor: '#999',
              borderWidth: 1,
              borderRadius: 4
            },
            itemStyle: {
              borderWidth: 1,
              borderColor: '#fff'
            },
            emphasis: {
              label: {
                show: true
              },
              itemStyle: {
                areaColor: '#FFA500',
              },
            },
          },
        ],
      };
      this.chartInstance.setOption(option, true);
    },

    fetchMockData() {
      const isRate = this.formInline.indicator === '达标率';
      this.mapData = this.cityNames.map(name => ({
        name,
        value: isRate ? Math.floor(Math.random() * 100) : Math.floor(Math.random() * 10000)
      }));

      this.timeSeriesData = {
        '2024': this.cityNames.map(() => isRate ? Math.floor(Math.random() * 100) : Math.floor(Math.random() * 10000)),
        '2025': this.cityNames.map(() => isRate ? Math.floor(Math.random() * 100) : Math.floor(Math.random() * 10000)),
        '常年': this.cityNames.map(() => isRate ? Math.floor(Math.random() * 80 + 10) : Math.floor(Math.random() * 8000 + 2000))
      };

      this.updateLegendTitle();

      if (this.chartType === 'map') {
        this.initMap();
      } else {
        this.updateChart();
      }
    },

    onSubmit() {
      this.loading = true;
      setTimeout(() => {
        this.fetchMockData();
        this.loading = false;
      }, 500);
    },

    onReset() {
      this.formInline = {
        regionType: 'nation',
        region: '',
        crop: '小麦',
        indicator: '累计防治面积',
        date: '2025-02-23',
      };
      this.loadChinaMap();
    },
  },
};
</script>

<style scoped>
.model_title {
  padding: 20px 0 0 20px;
  span {
    font-weight: bold;
  }
}

.demo-form-inline {
  padding: 20px;
}

.model_form {
  padding-top: 20px;
  width: 100%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  flex-wrap: wrap;
}

.model_button {
  width: 180px;
  margin-left: auto;
  margin-right: 0;
}

.table-card {
  margin: 0 20px 20px 20px;
  padding-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.map-container {
  margin-top: 15px;
  position: relative; /* 添加相对定位 */
}

.chart-switcher {
  margin-bottom: 15px;
  text-align: right;
}

.legend-container {
  position: absolute; /* 绝对定位 */
  left: 80px; /* 距离左侧20px */
  bottom: 40px; /* 距离底部40px */
  z-index: 10; /* 确保图例显示在地图之上 */
  background-color: rgba(255, 255, 255, 0.9); /* 半透明背景 */
  border: 1px solid #ddd; /* 边框 */
  border-radius: 4px; /* 圆角 */
  padding: 10px 15px; /* 内边距 */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15); /* 阴影 */
  width: 220px; /* 固定宽度 */
}

.legend-item {
  font-size: 12px;
}

.legend-title {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.legend-item ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.legend-item li {
  margin: 5px 0;
  display: flex;
  align-items: center;
}

.legend-color {
  display: inline-block;
  width: 12px;
  height: 12px;
  margin-right: 8px;
  border-radius: 2px;
}

.legend-text {
  color: #666;
}
</style>
