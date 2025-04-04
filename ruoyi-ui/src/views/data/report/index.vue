<template>
  <div>
    <div class="model_title">
      <span>设备运行状态监测</span>
    </div>
    <div class="content-container">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <div class="model_form">
          <el-form-item label="设备ID" style="margin-left: 20px;">
            <el-input v-model="formInline.deviceId" placeholder="请输入设备ID" size="small" style="width: 200px;"></el-input>
          </el-form-item>
          <el-form-item label="开始时间" prop="date">
            <el-date-picker
              v-model="formInline.startDate"
              type="datetime"
              placeholder="请选择开始时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 200px;"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间" prop="date">
            <el-date-picker
              v-model="formInline.endDate"
              type="datetime"
              placeholder="请选择结束时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 200px;"
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
      <el-card class="table-card">
        <el-table :data="deviceData" border style="width: 100%" v-loading="loading">
          <!-- 设备ID -->
          <el-table-column prop="stationId" label="设备ID" align="center" fixed></el-table-column>

          <!-- 采集时间 -->
          <el-table-column prop="collectTime" label="采集时间" align="center">
            <template slot-scope="{ row }">
              {{ formatTime(row.collectTime) }}
            </template>
          </el-table-column>

          <!-- 上传时间 -->
          <el-table-column prop="uploadTime" label="上传时间" align="center">
            <template slot-scope="{ row }">
              {{ formatTime(row.uploadTime) }}
            </template>
          </el-table-column>

          <!-- 数值型指标 -->
          <el-table-column
            v-for="col in numericColumns"
            :key="col.prop"
            :label="col.label"
            align="center"
          >
            <template slot-scope="{ row }">
              <div :class="getValueStyle(col.prop, row[col.prop])">
                {{ row[col.prop] }} {{ col.unit }}
              </div>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pageSize"
          :small="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          style="float:right;"
        ></el-pagination>
      </el-card>
    </div>
  </div>
</template>

<script>
import { listClimateDataByDeviceIds, listDeviceByRole } from '@/api/device/device';
import dayjs from 'dayjs'; // 引入 dayjs 进行时间格式化

export default {
  data() {
    return {
      formInline: {
        deviceId: '',
        startDate: '',
        endDate: '',
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      deviceData: [],
      deviceIds: [], // 设备ID列表
      queryParams: {
        params: {}, // 初始化 queryParams
        roleName: '',
        deptName: '',
      },
      user: {
        deptName: '',
        roleName: ''
      },
      loading: false, // 加载状态
      // 数值型字段配置（含单位）
      numericColumns: [
        {prop: "airTemperature", label: "空气温度", unit: "°C"},
        {prop: "airHumidity", label: "相对湿度", unit: "%"},
        {prop: "dewPointHumidity", label: "露点温度", unit: "°C"},
        {prop: "hourlyRainfall", label: "小时降雨量", unit: "mm"},
        {prop: "daylyRainfall", label: "天降雨量", unit: "mm"},
        {prop: "wettingTimeOfLeafSurface", label: "叶面湿润时间", unit: "s"},
        {prop: "hoursSunshine", label: "日照时数", unit: "h"},
        {prop: "n1SoilWaterContent", label: "土壤含水量（第1层）", unit: "%"},
        {prop: "n2SoilWaterContent", label: "土壤含水量（第2层）", unit: "%"},
        {prop: "n3SoilWaterContent", label: "土壤含水量（第3层）", unit: "%"},
        {prop: "n4SoilWaterContent", label: "土壤含水量（第4层）", unit: "%"},
        {prop: "n5SoilWaterContent", label: "土壤含水量（第5层）", unit: "%"},
      ],
    };
  },
  async mounted() {
    // 获取设备ID列表
    await this.fetchDeviceIds();
    // 默认查询第一页数据
    await this.fetchClimateData();
  },
  methods: {
    // 获取设备列表
    async getList() {
      this.queryParams.roleName = this.user.roleName;
      this.queryParams.deptName = this.user.deptName ? this.user.deptName : "陕西省";
      const response = await listDeviceByRole(this.queryParams);
      this.queryParams.pageSize = response.total;
      const response2 = await listDeviceByRole(this.queryParams);
      return response2;
    },
    // 获取设备ID列表
    async fetchDeviceIds() {
      this.user.roleName = this.$store.state.user.roleName;
      this.user.deptName = this.$store.state.user.deptName;
      const response = await this.getList();
      this.deviceIds = response.rows.map(device => device.deviceId);
    },
    // 查询气候数据
    async fetchClimateData() {
      this.loading = true; // 开启加载状态
      try {
        let deviceIds = this.formInline.deviceId ? [this.formInline.deviceId] : this.deviceIds;
        if (this.formInline.deviceId) {
          deviceIds = this.deviceIds.filter(id => id.includes(this.formInline.deviceId));
        }
        const params = {
          startCollectTime: this.formInline.startDate,
          endCollectTime: this.formInline.endDate,
          startUploadTime: this.formInline.startDate,
          endUploadTime: this.formInline.endDate,
          pageNum: this.currentPage,
          pageSize: this.pageSize,
        };
        const response = await listClimateDataByDeviceIds(deviceIds, params);
        this.deviceData = response.rows;
        this.total = response.total;
      } catch (error) {
        console.error("Failed to fetch climate data:", error);
        this.$message.error("数据加载失败，请稍后重试");
      } finally {
        this.loading = false; // 关闭加载状态
      }
    },
    // 查询按钮
    onSubmit() {
      this.currentPage = 1;
      this.fetchClimateData();
    },
    // 重置按钮
    onReset() {
      this.formInline = {
        deviceId: '',
        startDate: '',
        endDate: '',
      };
      this.currentPage = 1;
      this.fetchClimateData();
    },
    // 分页大小改变
    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.fetchClimateData();
    },
    // 当前页码改变
    handleCurrentChange(newPage) {
      this.currentPage = newPage;
      this.fetchClimateData();
    },
    // 数值预警样式
    getValueStyle(field, value) {
      const rules = {
        airTemperature: value > 35 ? 'danger-text' : '',
        airHumidity: value > 90 ? 'warning-text' : '',
      };
      return rules[field] || '';
    },
    // 时间格式化
    formatTime(time) {
      return dayjs(time).format("YYYY-MM-DD HH:mm:ss");
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
}

.model_button {
  width: 160px;
  margin-left: auto;
  margin-right: 0;
}

.table-card {
  margin: 0 20px 20px 20px;
  padding-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-table {
  margin-top: 15px;
}

.danger-text {
  color: #ff4d4f;
}

.warning-text {
  color: #faad14;
}
</style>
