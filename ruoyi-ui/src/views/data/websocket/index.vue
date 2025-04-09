<template>
  <div class="climate-monitor-container">
    <!-- 标题 -->
    <div class="header">
      <h1>气候实时监测系统</h1>
      <p>WebSocket实时数据接收与展示</p>
    </div>

    <!-- 连接状态 -->
    <div class="connection-status" :class="isConnected ? 'connected' : 'disconnected'">
      {{ isConnected ? '已连接到WebSocket服务器' : '未连接到WebSocket服务器' }}
    </div>

    <!-- 控制面板 -->
    <el-card shadow="hover" class="control-panel">
      <el-form :inline="true">
        <el-form-item label="设备ID">
          <el-select v-model="currentDevice" placeholder="选择监测设备" clearable>
            <el-option
              v-for="device in deviceOptions"
              :key="device"
              :label="device"
              :value="device"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="connectWebSocket"
            :disabled="isConnected"
            icon="el-icon-connection"
          >连接</el-button>
          <el-button
            type="danger"
            @click="disconnectWebSocket"
            :disabled="!isConnected"
            icon="el-icon-close"
          >断开</el-button>
          <el-button
            type="success"
            @click="fetchLatestData"
            :disabled="!isConnected"
            icon="el-icon-refresh"
          >刷新数据</el-button>
          <el-button
            type="warning"
            @click="handleTest"
            :disabled="!isConnected"
            icon="el-icon-bell"
          >数据监测</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据展示区 -->
    <div class="data-display">
      <!-- 当前数据卡片 -->
      <el-card shadow="hover" class="data-card">
        <div slot="header">
          <span>当前气候数据 ({{ currentDevice || '未选择设备' }})</span>
        </div>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="温度">{{ currentData.airTemperature || '--' }} °C</el-descriptions-item>
          <el-descriptions-item label="湿度">{{ currentData.airHumidity || '--' }} %</el-descriptions-item>
          <el-descriptions-item label="气压">{{ currentData.atmosphericPressure || '--' }} hPa</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ currentData.collectTime || '--' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTagType(currentData.status)">
              {{ currentData.status || 'NORMAL' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 图表展示 -->
      <el-card shadow="hover" class="chart-card" v-loading="loading">
        <div slot="header" class="chart-header">
          <span>气候数据趋势 (最近24小时)</span>
          <el-button-group>
            <el-button
              size="mini"
              :type="chartType === 'line' ? 'primary' : ''"
              @click="changeChartType('line')"
              icon="el-icon-data-line"
            >折线图</el-button>
            <el-button
              size="mini"
              :type="chartType === 'bar' ? 'primary' : ''"
              @click="changeChartType('bar')"
              icon="el-icon-data-bar"
            >柱状图</el-button>
          </el-button-group>
        </div>
        <div class="chart-container">
          <div ref="climateChart" style="height: 400px;"></div>
        </div>
      </el-card>
    </div>

    <!-- 消息日志 -->
    <el-card shadow="hover" class="message-log">
      <div slot="header">
        <span>消息日志</span>
        <el-tag type="info">总数: {{ messageLog.length }}</el-tag>
        <el-tag type="danger">异常: {{ alertCount }}</el-tag>
      </div>
      <div class="log-content">
        <div
          v-for="(message, index) in messageLog"
          :key="index"
          class="log-item"
          :class="getMessageClass(message.type)"
        >
          <div class="log-time">{{ message.time }}</div>
          <div class="log-message">{{ message.content }}</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { listClimateDataByDeviceIds, manualCheck } from '@/api/device/device'

export default {
  name: 'ClimateMonitor',
  data() {
    return {
      wsUrl: 'ws://localhost:8080/ws/climate',
      socket: null,
      isConnected: false,
      currentDevice: '',
      deviceOptions: ['YBQ0000144', 'YBQ0000176', 'YBQ0000200'], // 示例设备ID

      // 当前数据
      currentData: {},

      // 图表相关
      chartType: 'line',
      chartInstance: null,
      loading: false,
      chartData: {
        time: [],
        airTemperature: [],
        airHumidity: [],
        atmosphericPressure: []
      },

      // 消息日志
      messageLog: [],
      alertCount: 0,

      // 连接信息
      connectionTime: null,
      startTime: null
    }
  },
  computed: {
    uptime() {
      if (!this.startTime) return '--'
      const seconds = Math.floor((new Date() - this.startTime) / 1000)
      const hours = Math.floor(seconds / 3600)
      const minutes = Math.floor((seconds % 3600) / 60)
      const secs = seconds % 60
      return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
    }
  },
  mounted() {
    this.initChart()
    // 默认连接第一个设备
    if (this.deviceOptions.length > 0) {
      this.currentDevice = this.deviceOptions[0]
    }
    this.connectWebSocket();
    this.handleTest();
  },
  beforeDestroy() {
    this.disconnectWebSocket()
    if (this.chartInstance) {
      this.chartInstance.dispose()
    }
  },
  methods: {
    async handleTest() {
      await manualCheck();
    },
    initChart() {
      this.chartInstance = echarts.init(this.$refs.climateChart)
      this.updateChartOption()
    },

    // 更新图表配置
    updateChartOption() {
      const isBarChart = this.chartType === 'bar';
      const option = {
        backgroundColor: '#fff',
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['温度', '湿度', '气压'],
          right: 10,
          top: 10
        },
        grid: {
          left: '3%',
          right: '8%',  // 增加右侧空间
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: true,  // 柱状图需要边界间隔
          data: this.chartData.time,
          axisLabel: {
            rotate: 30,
            fontSize: 10
          }
        },
        yAxis: [
          {
            type: 'value',
            name: '温度(℃)',
            position: 'left',
            show: true,
            axisLabel: {
              margin: isBarChart ? 20 : 0  // 柱状图增加边距
            }
          },
          {
            type: 'value',
            name: '湿度(%)',
            position: 'right',
            show: true,
            offset: isBarChart ? 40 : 0,  // 柱状图增加偏移
            axisLabel: {
              margin: isBarChart ? 20 : 0
            }
          },
          {
            type: 'value',
            name: '气压(hPa)',
            position: 'right',
            offset: isBarChart ? 80 : 60,  // 柱状图增加更多偏移
            show: true,
            axisLabel: {
              margin: isBarChart ? 20 : 0
            }
          }
        ],
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 100
          },
          {
            start: 0,
            end: 100
          }
        ],
        series: [
          {
            name: '温度',
            type: this.chartType,
            data: this.chartData.airTemperature,
            barWidth: isBarChart ? '30%' : null,  // 控制柱状图宽度
            itemStyle: {
              color: '#FF6B6B'
            }
          },
          {
            name: '湿度',
            type: this.chartType,
            yAxisIndex: 1,
            data: this.chartData.airHumidity,
            barWidth: isBarChart ? '30%' : null,
            itemStyle: {
              color: '#5B8FF9'
            }
          },
          {
            name: '气压',
            type: this.chartType,
            yAxisIndex: 2,
            data: this.chartData.atmosphericPressure,
            barWidth: isBarChart ? '30%' : null,
            itemStyle: {
              color: '#69D2A0'
            }
          }
        ]
      }
      this.chartInstance.setOption(option, true)
    },

    // 更新图表数据
    updateChart() {
      if (!this.chartInstance) {
        this.initChart()
        return
      }

      const option = {
        xAxis: {
          data: this.chartData.time
        },
        series: [
          { data: this.chartData.airTemperature },
          { data: this.chartData.airHumidity },
          { data: this.chartData.atmosphericPressure }
        ],
        yAxis: [
          { show: true },
          { show: true },
          { show: true }
        ]
      }
      this.chartInstance.setOption(option)
    },

    // 改变图表类型
    changeChartType(type) {
      this.chartType = type
      this.updateChartOption() // 完全重新设置图表选项
      this.fetchLatestData()
    },

    // 连接WebSocket
    connectWebSocket() {
      if (!this.currentDevice) {
        this.$message.warning('请先选择设备')
        return
      }

      // 关闭现有连接
      if (this.socket) {
        this.socket.close()
      }

      this.addMessage('正在连接WebSocket服务器...', 'info')

      try {
        const url = `${this.wsUrl}?deviceId=${this.currentDevice}`
        this.socket = new WebSocket(url)

        this.socket.onopen = () => {
          this.isConnected = true
          this.connectionTime = new Date().toLocaleString()
          this.startTime = new Date()
          this.addMessage('WebSocket连接成功', 'success')
          // this.fetchLatestData() // 连接成功后立即获取一次数据
        }

        this.socket.onmessage = (event) => {
          try {
            console.log(event.data)
            const data = JSON.parse(event.data)
            console.log(data)
            // this.processClimateData(data)
            if(data.type === 'alert') {
              this.addMessage('收到告警数据: ' + data.message, 'alert');
            } else if(data.type === 'monitoring') {
              this.fetchLatestData();
              this.addMessage('收到数据: ' + JSON.stringify(data), 'data');
            }
          } catch (e) {
            this.addMessage('收到数据: ' + event.data , 'info');
          }
        }

        this.socket.onerror = (error) => {
          this.addMessage('WebSocket错误: ' + JSON.stringify(error), 'error')
        }

        this.socket.onclose = () => {
          this.isConnected = false
          this.addMessage('WebSocket连接已关闭', 'info')
        }
      } catch (e) {
        this.addMessage('连接失败: ' + e.message, 'error')
      }
    },

    // 断开WebSocket
    disconnectWebSocket() {
      if (this.socket) {
        this.socket.close()
      }
    },

    // 处理气候数据
    processClimateData(data) {
      // 更新当前数据
      this.currentData = {
        ...data,
        timestamp: new Date().toLocaleString()
      }

      // 添加到图表数据
      const now = new Date()
      let timeLabel = this.currentData.collectTime
      let date = new Date(timeLabel);

      if (isNaN(date.getTime())) {
        console.error("Invalid date string:", timeLabel);
      } else {
        // 提取时分秒
        let hours = date.getHours().toString().padStart(2, "0"); // 补零，确保两位数
        let minutes = date.getMinutes().toString().padStart(2, "0");
        let seconds = date.getSeconds().toString().padStart(2, "0");

        // 拼接成 HH:mm:ss 格式
        timeLabel = `${hours}:${minutes}:${seconds}`
      }

      // 限制数据点数量
      if (this.chartData.time.length >= 12) {
        this.chartData.time.shift()
        this.chartData.airTemperature.shift()
        this.chartData.airHumidity.shift()
        this.chartData.atmosphericPressure.shift()
      }

      this.chartData.time.push(timeLabel)
      this.chartData.airTemperature.push(data.airTemperature)
      this.chartData.airHumidity.push(data.airHumidity)
      this.chartData.atmosphericPressure.push(data.atmosphericPressure)

      this.updateChart()

      // 记录消息
      const messageType = data.status === 'ALERT' ? 'alert' : 'data'
      // this.addMessage(`收到${data.stationId}的数据: 温度${data.airTemperature}°C, 湿度${data.airHumidity}%, 收集时间${timeLabel}`, messageType)

      if (messageType === 'alert') {
        this.alertCount++
      }
    },

    // 获取最新数据
    async fetchLatestData() {

      if (!this.currentDevice) return
      this.loading = true
      // 获取当前时间作为结束时间
      let endCollectTime = new Date().toLocaleString();

// 获取当前时间的24小时前作为开始时间
      let startCollectTime = new Date();
      startCollectTime.setHours(startCollectTime.getHours() - 24); // 减去24小时
      startCollectTime = startCollectTime.toLocaleString();

      try {
        const res = await listClimateDataByDeviceIds([this.currentDevice], {
          pageSize: 100,
          startCollectTime,
          endCollectTime
        })
        this.chartData = {
          time: [],
          airTemperature: [],
          airHumidity: [],
          atmosphericPressure: []
        };
        this.currentData = '';
        res.rows.forEach((row) => {
          this.processClimateData(row)
        })
        this.addMessage(`手动获取${this.currentDevice}的最新数据成功`, 'success')
      } catch (error) {
        this.addMessage(`获取${this.currentDevice}数据失败: ${error.message}`, 'error')
      }
      this.loading = false
    },

    // 添加消息到日志
    addMessage(content, type) {
      this.messageLog.unshift({
        time: new Date().toLocaleTimeString(),
        content: content,
        type: type
      })

      // 限制日志数量
      if (this.messageLog.length > 100) {
        this.messageLog.pop()
      }
    },

    // 获取消息样式类
    getMessageClass(type) {
      return {
        'alert-message': type === 'alert',
        'error-message': type === 'error',
        'success-message': type === 'success',
        'info-message': type === 'info',
        'data-message': type === 'data'
      }
    },

    // 获取状态标签类型
    getStatusTagType(status) {
      return status === 'ALERT' ? 'danger' : status === 'WARNING' ? 'warning' : 'success'
    }
  }
}
</script>

<style lang="scss" scoped>
.climate-monitor-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;

  .header {
    text-align: center;
    margin-bottom: 20px;
    padding-bottom: 20px;
    border-bottom: 1px solid #ebeef5;

    h1 {
      font-size: 24px;
      color: #333;
      margin-bottom: 10px;
    }

    p {
      color: #909399;
      font-size: 14px;
    }
  }

  .connection-status {
    margin: 0 auto 20px;
    max-width: 300px;
    padding: 10px;
    border-radius: 4px;
    text-align: center;
    font-weight: bold;
    font-size: 14px;

    &.connected {
      background-color: #f0f9eb;
      color: #67c23a;
      border: 1px solid #e1f3d8;
    }

    &.disconnected {
      background-color: #fef0f0;
      color: #f56c6c;
      border: 1px solid #fde2e2;
    }
  }

  .control-panel {
    margin-bottom: 20px;

    ::v-deep .el-form-item {
      margin-bottom: 0;
    }
  }

  .data-display {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    margin-bottom: 20px;

    .data-card {
      flex: 1;
      min-width: 300px;

      ::v-deep .el-descriptions__body {
        background-color: #f8f8f8;
      }
    }

    .chart-card {
      flex: 2;
      min-width: 600px;

      .chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .chart-container {
        height: 400px;
      }
    }
  }

  .message-log {
    .log-content {
      max-height: 300px;
      overflow-y: auto;
      padding: 10px;
      background-color: #f8f8f8;
      border-radius: 4px;
    }

    .log-item {
      margin-bottom: 8px;
      padding: 8px;
      border-radius: 4px;
      background-color: #fff;
      border-left: 4px solid #ddd;

      .log-time {
        font-size: 12px;
        color: #909399;
        margin-bottom: 4px;
      }

      .log-message {
        font-size: 14px;
      }

      &.alert-message {
        border-left-color: #f56c6c;
        background-color: #fef0f0;
      }

      &.error-message {
        border-left-color: #e6a23c;
        background-color: #fdf6ec;
      }

      &.success-message {
        border-left-color: #67c23a;
        background-color: #f0f9eb;
      }

      &.info-message {
        border-left-color: #909399;
        background-color: #f4f4f5;
      }

      &.data-message {
        border-left-color: #409eff;
        background-color: #ecf5ff;
      }
    }
  }
}

@media (max-width: 992px) {
  .climate-monitor-container {
    .data-display {
      flex-direction: column;

      .data-card, .chart-card {
        min-width: 100%;
      }
    }
  }
}
</style>
