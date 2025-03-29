<template>
  <div class="app-container">
    <div class="header">
      <div class="title">设备监测</div>
      <div class="controls">
        <button
          v-if="currentView === 'map'"
          class="back-btn"
          @click="back()"
        >
          <i class="icon-back"></i> 返回上级
        </button>

        <div class="view-tabs">
          <button
            :class="['tab-btn', { 'active': currentView === 'map' }]"
            @click="switchView('map')"
          >
            <i class="icon-map"></i> 地图视图
          </button>
          <button
            :class="['tab-btn', { 'active': currentView === 'bar' }]"
            @click="switchView('bar')"
          >
            <i class="icon-chart"></i> 柱状图
          </button>
        </div>
      </div>
    </div>

    <div class="content-box">
      <div id="mapChart" class="chart" v-show="currentView === 'map'"></div>
      <div id="barChart" class="chart" v-show="currentView === 'bar'"></div>
    </div>

    <!-- 设备信息表格 -->
    <div v-if="showDeviceTable" class="device-table">
      <div class="table-header">
        <h3>{{ currentRegion }} 设备列表 (共{{ selectedDevices.length }}台)</h3>
        <button @click="showDeviceTable = false" class="close-btn">
          <i class="icon-close"></i>
        </button>
      </div>
      <div class="table-container">
        <table>
          <thead>
          <tr>
            <th>序号</th>
            <th>设备名称</th>
            <th>设备ID</th>
            <th>坐标</th>
            <th>状态</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(device, index) in selectedDevices" :key="device.id">
            <td>{{ index + 1 }}</td>
            <td>{{ device.name }}</td>
            <td>{{ device.id }}</td>
            <td>{{ device.coordinates.join(', ') }}</td>
            <td :class="device.status">{{ device.status }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import cityMap from "@/js/china-main-city-map.js";
import axios from "axios";
import * as echarts from "echarts";
import { listDeviceByRole } from "@/api/device/device";

// 中国地图基础数据
const chinaId = 100000;
const chinaName = "china";

// 记录地图导航历史
let mapStack = [];
let parentId = null;
let parentName = null;

// ECharts实例
let myChart = null;
let barChart = null;

export default {
  name: "chinaMap",
  data() {
    return {
      deviceList: [],
      currentRegion: "",
      showDeviceTable: false,
      selectedDevices: [],
      currentView: "map",
      mapInitialized: false,
      barInitialized: false
    };
  },
  mounted() {
    this.loadDeviceData("china").then(() => {
      this.initMap();
      this.initBarChart();
    });
  },
  beforeDestroy() {
    if (myChart) myChart.dispose();
    if (barChart) barChart.dispose();
  },
  methods: {
    async loadDeviceData(name) {
      try {
        const res = await listDeviceByRole({
          pageSize: 1000,
          roleName: this.$store.state.user.roleName,
          deptName: this.$store.state.user.deptName || '陕西省'
        });
        const getRegion = (name, ele) => {
          // 处理全国数据
          if (name === "china") {
            return ele.province
              .replace(/(自治区|省|市|特别行政区|壮族|回族|维吾尔)/g, "")
              .replace(/^北京$/, "北京");
          }

          // 处理省级数据（name是不带后缀的省份名称）
          const provinceVariations = [
            name,
            name + "省",
            name + "自治区",
            // 特殊处理四个直辖市
            ...(name === "北京" ? ["北京市"] : []),
            ...(name === "天津" ? ["天津市"] : []),
            ...(name === "上海" ? ["上海市"] : []),
            ...(name === "重庆" ? ["重庆市"] : [])
          ];

          // 检查省份是否匹配（考虑各种可能的形式）
          const isProvinceMatch = provinceVariations.some(variation =>
            ele.province === variation
          );

          if (isProvinceMatch) {
            // 如果是直辖市，返回区名；否则返回城市名
            if (["北京", "天津", "上海", "重庆"].includes(name)) {
              return ele.district || "";
            }
            return ele.city || "";
          }

          // 处理市级数据（name是城市名）
          if (ele.city === name) {
            return ele.district || "";
          }

          // 不匹配则返回null，后续可以过滤掉
          return null;
        };
        this.deviceList = res.rows.map((ele) => ({
          id: ele.deviceId,
          name: ele.province,
          region: getRegion(name, ele),
          status: ele.status,
          coordinates: ele.coordinates.split(","),
        })).filter((ele) => {
          return ele.region !== null
        });
      } catch (error) {
        console.error("加载设备数据失败:", error);
        this.deviceList = [
          {id: 1, name: "北京设备1", coordinates: [116.4074, 39.9042], region: "北京", status: "正常"},
          {id: 2, name: "上海设备1", coordinates: [121.4737, 31.2304], region: "上海", status: "正常"},
          {id: 3, name: "上海设备2", coordinates: [121.4737, 31.2304], region: "上海", status: "警告"},
          {id: 4, name: "广东设备1", coordinates: [113.2644, 23.1291], region: "广东", status: "正常"},
          {id: 5, name: "广东设备2", coordinates: [113.2644, 23.1291], region: "广东", status: "故障"},
          {id: 6, name: "广东设备3", coordinates: [113.2644, 23.1291], region: "广东", status: "正常"}
        ];
      }
    },

    initMap() {
      if (this.mapInitialized) return;

      axios.get("/static/json/map/" + chinaId + ".json").then((response) => {
        const mapJson = response.data;
        myChart = echarts.init(document.getElementById("mapChart"));
        this.registerAndsetOption(myChart, chinaId, chinaName, mapJson, false);
        parentId = chinaId;
        parentName = chinaName;
        this.mapInitialized = true;
      });
    },

    initBarChart() {
      if (this.barInitialized) return;

      barChart = echarts.init(document.getElementById("barChart"));
      this.updateBarChart();
      this.barInitialized = true;
    },

    updateBarChart() {
      const provinceData = {};
      this.deviceList.forEach((device) => {
        const province = device.region;
        provinceData[province] = (provinceData[province] || 0) + 1;
      });

      const barData = Object.entries(provinceData)
        .map(([name, value]) => ({name, value}))
        .sort((a, b) => b.value - a.value);

      const option = {
        backgroundColor: '#ffffff',
        title: {
          text: "各省设备数量统计",
          left: "center",
          textStyle: {
            color: '#333',
            fontSize: 18,
            fontWeight: 'bold'
          }
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow"
          },
          backgroundColor: 'rgba(255,255,255,0.95)',
          borderColor: '#eee',
          borderWidth: 1,
          textStyle: {
            color: '#333'
          }
        },
        grid: {
          top: "20%",
          left: "5%",
          right: "5%",
          bottom: "10%",
          containLabel: true
        },
        xAxis: {
          type: "category",
          data: barData.map((item) => item.name),
          axisLabel: {
            interval: 0,
            rotate: 30,
            color: '#666',
            fontSize: 12
          },
          axisLine: {
            lineStyle: {
              color: '#ddd'
            }
          },
          axisTick: {
            alignWithLabel: true,
            lineStyle: {
              color: '#ddd'
            }
          }
        },
        yAxis: {
          type: "value",
          name: "设备数量",
          nameTextStyle: {
            color: '#666',
            fontSize: 12
          },
          axisLine: {
            lineStyle: {
              color: '#ddd'
            }
          },
          axisTick: {
            lineStyle: {
              color: '#ddd'
            }
          },
          splitLine: {
            lineStyle: {
              color: '#f0f0f0'
            }
          }
        },
        series: [
          {
            name: "设备数量",
            type: "bar",
            data: barData,
            itemStyle: {
              color: (params) => {
                const colors = [
                  '#4E9AFF', '#7B72E9', '#FF6B6B',
                  '#48D1CC', '#FFA500', '#9B59B6'
                ];
                return colors[params.dataIndex % colors.length];
              },
              borderRadius: [6, 6, 0, 0],
              shadowColor: 'rgba(0,0,0,0.1)',
              shadowBlur: 5,
              shadowOffsetY: 2
            },
            label: {
              show: true,
              position: "top",
              color: '#555',
              fontSize: 12,
              fontWeight: "bold",
              formatter: '{c}'
            },
            emphasis: {
              itemStyle: {
                shadowColor: 'rgba(0,0,0,0.2)',
                shadowBlur: 8,
                shadowOffsetY: 3
              }
            }
          }
        ]
      };

      barChart.setOption(option);

      barChart.off("click");
      barChart.on("click", (params) => {
        this.currentRegion = params.name;
        this.selectedDevices = this.deviceList.filter(
          (device) => device.region === params.name
        );
        this.showDeviceTable = true;
      });
    },

    switchView(viewType) {
      this.currentView = viewType;
      this.$nextTick(() => {
        if (viewType === "map" && myChart) {
          myChart.resize();
        } else if (viewType === "bar" && barChart) {
          this.updateBarChart();
          barChart.resize();
        }
      });
    },

    async back() {
      if (mapStack.length > 0) {
        const map = mapStack.pop();
        await this.loadDeviceData(map.mapName);
        axios.get("/static/json/map/" + map.mapId + ".json").then((response) => {
          this.registerAndsetOption(myChart, map.mapId, map.mapName, response.data, false);
          parentId = map.mapId;
          parentName = map.mapName;
        });
      }
    },

    registerAndsetOption(myChart, id, name, mapJson, flag) {
      echarts.registerMap(name, mapJson);

      const regionDeviceMap = {};
      this.deviceList.forEach((device) => {
        if (!regionDeviceMap[device.region]) {
          regionDeviceMap[device.region] = [];
        }
        regionDeviceMap[device.region].push(device);
      });

      const mapData = [];
      const scatterData = [];

      const findGeoCoord = (regionName) => {
        const feature = mapJson.features.find((f) => f.properties.name === regionName);
        if (feature?.properties?.cp) {
          return [feature.properties.cp[0] + 0.2, feature.properties.cp[1] + 0.1];
        }
        if (feature?.geometry?.coordinates) {
          // const coords = feature.geometry.coordinates[0];
          // const sum = coords.reduce((acc, cur) => [acc[0] + cur[0], acc[1] + cur[1]], [0, 0]);
          // return [
          //   sum[0] / coords.length + 0.2,
          //   sum[1] / coords.length + 0.1
          // ];
          return feature.geometry.coordinates[0][0][0];
        }
        return null;
      };

      mapJson.features.forEach((feature) => {
        const regionName = feature.properties.name;
        const devices = regionDeviceMap[regionName] || [];
        const deviceCount = devices.length;

        mapData.push({
          name: regionName,
          value: deviceCount
        });

        if (deviceCount > 0) {
          const coords = findGeoCoord(regionName);
          if (coords) {
            scatterData.push({
              name: regionName,
              value: coords,
              count: deviceCount,
              devices: devices
            });
          }
        }
      });

      const option = {
        geo: {
          map: name,
          roam: true,
          label: {
            show: true,
            position: "inside",
            align: "center",
            verticalAlign: "middle",
            fontSize: 10,
            color: "#333",
            fontWeight: "bold",
            formatter: (params) => params.name,
            padding: [5, 5],
            backgroundColor: "rgba(255,255,255,0.7)",
            borderColor: "#999",
            borderWidth: 1,
            borderRadius: 4
          },
          emphasis: {
            label: {
              color: "#ff0",
              fontSize: 14
            }
          },
          itemStyle: {
            normal: {
              borderColor: "#2ab8ff",
              borderWidth: 1,
              areaColor: {
                type: "linear",
                x: 0,
                y: 1,
                x2: 0,
                y2: 0,
                colorStops: [
                  {offset: 0, color: "#001934"},
                  {offset: 1, color: "#00377C"}
                ]
              }
            },
            emphasis: {
              areaColor: "#206474",
              borderColor: "#00F8C9",
              borderWidth: 2
            }
          },
          data: mapData
        },
        backgroundColor: "transparent",
        tooltip: {
          trigger: "item",
          formatter: (params) => {
            if (params.seriesType === "map") {
              return `${params.name}<br/>设备数量: ${params.value || 0}台`;
            }
            return `${params.name}: ${params.data.count}台设备`;
          }
        },
        series: [
          {
            type: "scatter",
            symbol: "pin",
            coordinateSystem: "geo",
            geoIndex: 0,
            data: scatterData,
            symbolSize: 30,
            silent: false,
            label: {
              show: true,
              position: "inside",
              formatter: (params) => params.data.count,
              color: "#fff",
              fontSize: 14,
              fontWeight: "bold"
            },
            itemStyle: {
              color: "#FF5722",
              opacity: 0.9,
              borderColor: "#fff",
              borderWidth: 2
            },
            emphasis: {
              itemStyle: {
                color: "#FF9800",
                opacity: 1,
                borderWidth: 3
              }
            }
          }
        ]
      };

      myChart.setOption(option, true);
      myChart.off("click");

      myChart.on("click", async (params) => {
        if (params.componentType === "geo") {
          const cityId = cityMap[params.name];
          if (cityId) {
            await this.loadDeviceData(params.name);
            axios.get("/static/json/map/" + cityId + ".json").then((response) => {
              this.registerAndsetOption(myChart, cityId, params.name, response.data, true);
            });
          }
        } else if (params.componentType === "series" && params.seriesType === "scatter") {
          this.currentRegion = params.name;
          this.selectedDevices = params.data.devices || [];
          this.showDeviceTable = true;
        }
      });

      if (flag) {
        mapStack.push({
          mapId: parentId,
          mapName: parentName
        });
        parentId = id;
        parentName = name;
      }
    }
  }
};
</script>

<style scoped>
/* 基础样式 */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

/* 主容器 */
.app-container {
  position: relative;
  width: 100%;
  height: 100vh;
  background-image: url("../../../assets/beijingtupian.png");
  background-size: cover;
  background-position: center;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

/* 头部区域 */
.header {
  width: 100%;
  padding: 15px 20px;
  background: rgba(24, 27, 52, 0.85);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 10;
}

.title {
  color: #fff;
  font-size: 22px;
  font-weight: bold;
  letter-spacing: 1px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.controls {
  display: flex;
  align-items: center;
  gap: 15px;
}

/* 按钮样式 */
.back-btn, .tab-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.back-btn {
  background: linear-gradient(135deg, #4E9AFF, #7B72E9);
  color: white;
}

.back-btn:hover {
  background: linear-gradient(135deg, #3A89FF, #6A5FE9);
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.tab-btn {
  background: rgba(255, 255, 255, 0.1);
  color: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.tab-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.tab-btn.active {
  background: linear-gradient(135deg, #4E9AFF, #7B72E9);
  color: white;
  border-color: transparent;
}

/* 图标样式 */
.icon-back, .icon-map, .icon-chart, .icon-close {
  display: inline-block;
  margin-right: 6px;
  font-size: 16px;
}

.icon-back::before {
  content: "←";
}

.icon-map::before {
  content: "🗺";
}

.icon-chart::before {
  content: "📊";
}

.icon-close::before {
  content: "×";
}

/* 内容区域 */
.content-box {
  flex: 1;
  position: relative;
  width: 100%;
  padding: 0 20px 20px;
  overflow: hidden;
}

.chart {
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

/* 设备表格样式 */
.device-table {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 70%;
  max-height: 70vh;
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.table-header {
  padding: 15px 20px;
  background: linear-gradient(135deg, #4E9AFF, #7B72E9);
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
  transition: all 0.2s;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.1);
}

.table-container {
  overflow-y: auto;
  flex-grow: 1;
  max-height: 60vh;
}

table {
  width: 100%;
  border-collapse: collapse;
  color: #333;
}

th, td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

th {
  background-color: #f8f9fa;
  position: sticky;
  top: 0;
  font-weight: 600;
  color: #555;
}

tr:hover {
  background-color: #f5f7fa;
}

.normal {
  color: #4CAF50;
  font-weight: 500;
}

.警告 {
  color: #FF9800;
  font-weight: 500;
}

.故障 {
  color: #F44336;
  font-weight: 500;
}

/* 滚动条样式 */
.table-container::-webkit-scrollbar {
  width: 8px;
}

.table-container::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.05);
}

.table-container::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: 4px;
}

.table-container::-webkit-scrollbar-thumb:hover {
  background-color: rgba(0, 0, 0, 0.3);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .header {
    flex-direction: column;
    align-items: flex-start;
    padding: 10px 15px;
  }

  .controls {
    width: 100%;
    margin-top: 10px;
    justify-content: space-between;
  }

  .device-table {
    width: 90%;
    max-height: 80vh;
  }
}
</style>
