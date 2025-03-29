<template>
  <div class="weather-data-container">
    <!-- 标题 -->
    <div class="model_title">
      <span>气象数据可视化分析</span>
    </div>

    <!-- 查询表单 -->
    <el-card shadow="hover" class="query-card">
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="监测站点">
          <el-select
            v-model="queryForm.stationId"
            placeholder="请选择设备ID"
            clearable
            style="width: 180px"
          >
            <el-option
              v-for="station in stationOptions"
              :key="station"
              :label="station"
              :value="station"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="时间范围">
          <el-date-picker
            v-model="queryForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            style="width: 280px"
          />
        </el-form-item>

        <el-form-item label="气象指标">
          <el-select
            v-model="queryForm.metric"
            placeholder="选择指标"
            style="width: 180px"
          >
            <el-option-group
              v-for="group in metricOptions"
              :key="group.label"
              :label="group.label"
            >
              <el-option
                v-for="item in group.options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-option-group>
          </el-select>
        </el-form-item>

        <el-form-item label="数据粒度">
          <el-select
            v-model="queryForm.granularity"
            placeholder="选择粒度"
            style="width: 120px"
          >
            <el-option
              v-for="item in granularityOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="handleQuery"
            :loading="loading"
            icon="el-icon-search"
          >查询</el-button>
          <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据图表 -->
    <el-card shadow="hover" class="chart-card" v-loading="loading">
      <div slot="header" class="chart-header">
        <span>{{ chartTitle }}</span>
        <div class="chart-actions">
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
      </div>

      <div v-loading="chartLoading" class="chart-container">
        <div ref="chart" class="chart" style="height: 400px"></div>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {listClimateDataByDeviceIds, listDeviceByRole} from '@/api/device/device'

export default {
  name: 'WeatherDataAnalysis',

  data() {
    return {
      loading: false,
      chartLoading: false,
      chartType: 'line',
      chartInstance: null,
      rawData: [],
      processedData: [],
      deviceIds: null,

      queryForm: {
        stationId: '',
        dateRange: [],
        metric: 'airTemperature',
        granularity: 'month' // hour/day/month
      },

      stationOptions: [],
      granularityOptions: [
        { label: '按小时', value: 'hour' },
        { label: '按天', value: 'day' },
        { label: '按月', value: 'month' }
      ],

      metricOptions: [
        {
          label: '大气指标',
          options: [
            { label: '气温(℃)', value: 'airTemperature' },
            { label: '湿度(%)', value: 'airHumidity' },
            { label: '露点温度(℃)', value: 'dewPointHumidity' }
          ]
        },
        {
          label: '降水指标',
          options: [
            { label: '小时降雨量(mm)', value: 'hourlyRainfall' },
            { label: '日降雨量(mm)', value: 'daylyRainfall' }
          ]
        },
        {
          label: '土壤指标',
          options: [
            { label: '土壤含水量1', value: 'n1SoilWaterContent' },
            { label: '土壤含水量2', value: 'n2SoilWaterContent' },
            { label: '土壤含水量3', value: 'n3SoilWaterContent' }
          ]
        }
      ]
    }
  },

  computed: {
    chartTitle() {
      const station = this.queryForm.stationId;
      const metric = this.metricOptions
        .flatMap(g => g.options)
        .find(m => m.value === this.queryForm.metric)?.label || '气温'
      const granularity = this.granularityOptions.find(g => g.value === this.queryForm.granularity)?.label || '小时'

      return `${station} - ${metric}变化趋势 (${granularity})`
    }
  },

  async mounted() {
    const queryForm = {
      roleName: this.$store.state.user.roleName,
      deptName: this.$store.state.user.deptName || '陕西省',
      pageSize: 100
    }
    console.log(this.$store)
    const deviceList = await listDeviceByRole(
      queryForm
    )
    this.deviceIds = deviceList.rows.map(device => device.deviceId);
    this.stationOptions = this.deviceIds
    this.queryForm.stationId = this.deviceIds[0];
    this.initChart()
    this.fetchData()
  },
  beforeDestroy() {
    if (this.chartInstance) {
      this.chartInstance.dispose()
    }
    window.removeEventListener('resize', this.resizeChart)
  },
  // watch: {
  //   // 监听简单属性
  //     queryForm(newvalue) {
  //       console.log(newvalue)
  //       if (!newvalue.stationId) {
  //           this.queryForm.stationId = this.deviceIds[0];
  //       }
  //   },
  //   deep: true,
  // },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await listClimateDataByDeviceIds([this.queryForm.stationId], {
          pageSize: 1000000,
          startCollectTime: this.queryForm.dateRange[0],
          endCollectTime: this.queryForm.dateRange[1],
          startUploadTime: this.queryForm.dateRange[0],
          endUploadTime: this.queryForm.dateRange[1],
        })
        this.rawData = res.rows
        // this.stationOptions = [...new Set(res.rows.map(item => item.stationId))]
        this.processData()
      } catch (error) {
        console.error('获取数据失败:', error)
        this.$message.error('数据加载失败')
      } finally {
        this.loading = false
      }
    },

    async processData() {
      let data = [...this.rawData]

      // 按站点筛选
      // if (this.queryForm.stationId) {
      //   data = data.filter(item => item.stationId === this.queryForm.stationId)
      // }

      // 按时间范围筛选
      // if (this.queryForm.dateRange && this.queryForm.dateRange.length === 2) {
      //   const [start, end] = this.queryForm.dateRange
      //   data = data.filter(item => {
      //     const date = item.collectTime.split('T')[0]
      //     return date >= start && date <= end
      //   })
      // }

      // 按粒度处理数据
      const metric = this.queryForm.metric
      const granularity = this.queryForm.granularity

      const groupedData = {}

      data.forEach(item => {
        const date = new Date(item.collectTime)
        let key

        if (granularity === 'hour') {
          // 按小时粒度，显示具体时间
          key = `${date.getMonth() + 1}/${date.getDate()} ${date.getHours()}:00`
        } else if (granularity === 'day') {
          // 按天粒度，显示日期
          key = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`
        } else {
          // 按月粒度，显示年月
          key = `${date.getFullYear()}/${date.getMonth() + 1}`
        }

        if (!groupedData[key]) {
          groupedData[key] = {
            count: 0,
            sum: 0,
            max: -Infinity,
            min: Infinity,
            values: []
          }
        }

        const value = item[metric]
        if (value !== null && value !== undefined) {
          groupedData[key].count++
          groupedData[key].sum += value
          groupedData[key].values.push(value)
          groupedData[key].max = Math.max(groupedData[key].max, value)
          groupedData[key].min = Math.min(groupedData[key].min, value)
        }
      })

      // 转换为图表需要的数据格式
      this.processedData = Object.keys(groupedData).map(key => {
        const stats = groupedData[key]
        return {
          date: key,
          avg: stats.count > 0 ? stats.sum / stats.count : 0,
          max: stats.max !== -Infinity ? stats.max : 0,
          min: stats.min !== Infinity ? stats.min : 0,
          values: stats.values
        }
      })

      // 按日期排序
      this.processedData.sort((a, b) => {
        return new Date(a.date) - new Date(b.date)
      })

      this.renderChart()
    },

    initChart() {
      this.chartInstance = echarts.init(this.$refs.chart)
      window.addEventListener('resize', this.resizeChart)
    },

    resizeChart() {
      if (this.chartInstance) {
        this.chartInstance.resize()
      }
    },

    renderChart() {
      if (!this.processedData.length) {
        this.chartInstance.clear()
        return
      }

      this.chartLoading = true

      try {
        // 准备图表数据
        const xData = this.processedData.map(item => item.date)
        const yData = this.processedData.map(item => item.avg)
        const maxData = this.processedData.map(item => item.max)
        const minData = this.processedData.map(item => item.min)

        // 获取单位
        const unit = this.getUnit(this.queryForm.metric)

        // 图表配置
        const option = {
          backgroundColor: '#fff',
          tooltip: {
            trigger: 'axis',
            formatter: params => {
              const data = params[0]
              const index = data.dataIndex
              const detail = this.processedData[index]
              let html = `<div style="font-weight:bold;margin-bottom:5px">${data.name}</div>`

              html += `<div>平均值: ${detail.avg.toFixed(2)}${unit}</div>`
              html += `<div>最大值: ${detail.max.toFixed(2)}${unit}</div>`
              html += `<div>最小值: ${detail.min.toFixed(2)}${unit}</div>`

              if (detail.values.length > 1) {
                html += `<div style="margin-top:5px;color:#999">共 ${detail.values.length} 个数据点</div>`
              }

              return html
            }
          },
          legend: {
            data: ['平均值', '最大值', '最小值'],
            top: 10
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xData,
            axisLabel: {
              rotate: this.queryForm.granularity === 'hour' ? 30 : 0,
              fontSize: 10,
              formatter: value => {
                // 根据粒度调整显示格式
                if (this.queryForm.granularity === 'month') {
                  return value.split('/')[0] + '年' + value.split('/')[1] + '月'
                }
                return value
              }
            }
          },
          yAxis: {
            type: 'value',
            name: unit,
            nameTextStyle: {
              padding: [0, 30, 0, 0]
            }
          },
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
              name: '平均值',
              type: this.chartType,
              smooth: true,
              data: yData,
              symbol: 'circle',
              symbolSize: 6,
              itemStyle: {
                color: '#5B8FF9'
              },
              lineStyle: {
                width: 2,
                color: '#5B8FF9'
              },
              areaStyle: this.chartType === 'line' ? {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  {offset: 0, color: 'rgba(91, 143, 249, 0.3)'},
                  {offset: 1, color: 'rgba(91, 143, 249, 0.1)'}
                ])
              } : undefined
            },
            {
              name: '最大值',
              type: this.chartType,
              smooth: true,
              data: maxData,
              symbol: 'circle',
              symbolSize: 6,
              itemStyle: {
                color: '#FF6B6B'
              },
              lineStyle: {
                width: 1,
                color: '#FF6B6B',
                type: 'dashed'
              }
            },
            {
              name: '最小值',
              type: this.chartType,
              smooth: true,
              data: minData,
              symbol: 'circle',
              symbolSize: 6,
              itemStyle: {
                color: '#69D2A0'
              },
              lineStyle: {
                width: 1,
                color: '#69D2A0',
                type: 'dashed'
              }
            }
          ]
        }

        this.chartInstance.setOption(option, true)
      } catch (error) {
        console.error('渲染图表失败:', error)
      } finally {
        this.chartLoading = false
      }
    },

    getUnit(metric) {
      const units = {
        airTemperature: '℃',
        airHumidity: '%',
        dewPointHumidity: '℃',
        hourlyRainfall: 'mm',
        daylyRainfall: 'mm',
        n1SoilWaterContent: '',
        n2SoilWaterContent: '',
        n3SoilWaterContent: ''
      }
      return units[metric] || ''
    },

    handleQuery() {
      this.fetchData()
    },

    resetQuery() {
      this.queryForm = {
        stationId: this.deviceIds[0],
        dateRange: [],
        metric: 'airTemperature',
        granularity: 'month'
      }
      this.fetchData()
    },

    changeChartType(type) {
      this.chartType = type
      this.renderChart()
    }
  }
}
</script>

<style lang="scss" scoped>
.weather-data-container {
  padding: 20px;
  background-color: #f5f7fa;

  .model_title {
    margin-bottom: 20px;

    span {
      font-size: 18px;
      font-weight: bold;
      color: #333;
    }
  }

  .query-card {
    margin-bottom: 20px;
    border-radius: 4px;

    .query-form {
      display: flex;
      flex-wrap: wrap;
      align-items: center;

      ::v-deep .el-form-item {
        margin-bottom: 10px;
        margin-right: 20px;
      }
    }
  }

  .chart-card {
    border-radius: 4px;

    .chart-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 16px;
      font-weight: 500;
    }

    .chart-container {
      padding: 10px 0;

      .chart {
        width: 100%;
      }
    }
  }
}

@media (max-width: 768px) {
  .weather-data-container {
    padding: 10px;

    .query-card {
      ::v-deep .el-form-item {
        width: 100%;
        margin-right: 0;

        .el-form-item__content {
          width: 100%;

          .el-select, .el-date-editor {
            width: 100% !important;
          }
        }
      }
    }
  }
}
</style>
