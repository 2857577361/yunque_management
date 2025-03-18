<template>
  <div>
    <div class="model_title">
      <span>设备运行状态监测</span>
    </div>
    <div class="content-container">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <div class="model_form">
          <el-form-item label="设备ID" style="margin-left: 20px;">
            <el-input v-model="formInline.ID" placeholder="请输入设备ID" size="small" style="width: 200px;"></el-input>
          </el-form-item>
          <el-form-item label="开始时间" prop="date">
            <el-date-picker
              v-model="formInline.startDate"
              type="date"
              placeholder="请选择开始时间"
              style="width: 200px;"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间" prop="date">
            <el-date-picker
              v-model="formInline.endDate"
              type="date"
              placeholder="请选择结束时间"
              style="width: 200px;"
            ></el-date-picker>
          </el-form-item>
          <div class="model_button">
            <el-form-item>
              <el-button type="primary" @click="onSubmit">查询</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="info" @click="onSubmit">重置</el-button>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <el-card class="table-card">
        <el-table :data="deviceData" border style="width: 100%">
          <!-- 设备ID（不可编辑） -->
          <el-table-column prop="deviceId" label="设备ID" align="center" fixed></el-table-column>

          <!-- 采集时间（可编辑时间选择器） -->
          <el-table-column label="采集时间" align="center">
            <template slot-scope="{ row }">
              <el-date-picker
                v-if="row.isEditing"
                v-model="row.time"
                type="datetime"
                size="mini"
                value-format="yyyy/MM/dd HH:mm"
              ></el-date-picker>
<!--              <span v-else>{{ formatTime(row.time) }}</span>-->
            </template>
          </el-table-column>

          <!-- 数值型指标（可编辑输入框） -->
          <el-table-column
            v-for="col in numericColumns"
            :key="col.prop"
            :label="col.label"
            align="center"
          >
            <template slot-scope="{ row }">
              <div v-if="row.isEditing">
                <el-input
                  v-model.number="row[col.prop]"
                  size="mini"
                  :type="col.type || 'number'"
                  :step="col.step"
                  :min="col.min"
                ></el-input>
              </div>
              <div v-else :class="getValueStyle(col.prop, row[col.prop])">
                {{ row[col.prop] }} {{ col.unit }}
              </div>
            </template>
          </el-table-column>

          <!-- 操作列 -->
          <el-table-column label="操作" width="150" align="center" fixed="right">
            <template slot-scope="{ row }">
              <template v-if="!row.isEditing">
                <el-button type="primary" size="mini" @click="startEdit(row)">编辑</el-button>
              </template>
              <template v-else>
                <el-button size="mini" type="success" @click="saveEdit(row)">保存</el-button>
                <el-button size="mini" @click="cancelEdit(row)">取消</el-button>
              </template>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="10"
          :small="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="deviceData.length"
          style="float:right;">
        </el-pagination>
      </el-card>
    </div>
  </div>

</template>

<script>
// import dayjs from 'dayjs'
export default {
  data() {
    return {
      formInline: {
        ID: '',
        startDate: '',
        endDate: '',
      },
      currentPage: 1,
      pageSize: 10,
      deviceData: [{
        deviceId: "BZYDN0001",
        time: "2024/12/17 8:00",
        temperature: 22.5,        // 空气温度
        humidity: 80,             // 相对湿度
        dewPoint: 19.5,           // 露点温度
        dayRainfall: 0,           // 天降雨量
        hourRainfall: 0,          // 小时降雨量
        voltage: 3.6,             // 电池电压
        wetnessDuration: 120,     // 叶面湿润时间
        sunshineHours: 5.2,       // 日照时数
        soilMoisture: 18.7        // 土壤含水量
      }],
      // 数值型字段配置（含单位）
      numericColumns: [
        { prop: "temperature", label: "空气温度", unit: "°C" },
        { prop: "humidity", label: "相对湿度", unit: "%" },
        { prop: "dewPoint", label: "露点温度", unit: "°C" },
        { prop: "dayRainfall", label: "天降雨量", unit: "mm" },
        { prop: "hourRainfall", label: "小时降雨量", unit: "mm" },
        { prop: "voltage", label: "电池电压", unit: "V" },
        { prop: "wetnessDuration", label: "叶面湿润时间", unit: "s" },
        { prop: "sunshineHours", label: "日照时数", unit: "h" },
        { prop: "soilMoisture", label: "土壤含水量", unit: "%" }
      ]
    };
  },
  methods: {
    handleSizeChange(newSize) {
      console.log(`每页 ${newSize} 条`, newSize);
    },
    handleCurrentChange(newPage) {
      console.log(`当前页码 ${newPage}`, newPage);
    },
    // formatTime(time) {
    //   return dayjs(time).format("YYYY/MM/DD HH:mm")
    // },

    // 启动编辑模式
    startEdit(row) {
      row.originalData = JSON.parse(JSON.stringify(row))
      row.isEditing = true
    },

    // 保存修改
    saveEdit(row) {
      if (this.validateData(row)) {
        row.isEditing = false
        console.log('修改已保存:', row)
      }
    },

    // 取消修改
    cancelEdit(row) {
      Object.assign(row, row.originalData)
      row.isEditing = false
    },

    // 数据验证
    validateData(row) {
      if (row.voltage < 3.0) {
        this.$message.error("电池电压不应低于3.0V")
        return false
      }
      if (row.humidity < 0 || row.humidity > 100) {
        this.$message.error("湿度值应在0-100%范围内")
        return false
      }
      return true
    },

    // 数值预警样式
    getValueStyle(field, value) {
      const rules = {
        voltage: value < 3.4 ? 'warning-text' : '',
        temperature: value > 35 ? 'danger-text' : '',
        humidity: value > 90 ? 'warning-text' : ''
      }
      return rules[field] || ''
    }
  },
  computed: {
    filteredData() {
      // 这里可以添加过滤数据的逻辑，例如根据页码和每页大小返回数据子集
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.deviceData.slice(start, end);
    }
  }
}
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
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
.model_button {
  width: 160px;
  margin-left: auto;
  margin-right: 0;
}
.table-card {
  margin: 0 20px 20px 20px;
  padding-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
.el-table {
  margin-top: 15px;
}
.el-tag {
  font-size: 14px;
  padding: 0 8px;
}
</style>
