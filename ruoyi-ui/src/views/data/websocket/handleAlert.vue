<template>
  <div class="app-container">
    <el-card>
      <div class="filter-container">
        <el-form :inline="true" :model="queryParams" class="demo-form-inline">
          <el-form-item label="设备ID">
            <el-input
              v-model="queryParams.deviceId"
              placeholder="请输入设备ID"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="异常类型">
            <el-select
              v-model="queryParams.alertType"
              placeholder="请选择异常类型"
              clearable
            >
              <el-option
                v-for="item in alertTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="queryParams.status"
              placeholder="请选择状态"
              clearable
            >
              <el-option
                v-for="item in statusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="异常时间">
            <el-date-picker
              v-model="dateRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd HH:mm:ss"
              align="right"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">搜索</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        v-loading="loading"
        :data="alertList"
        border
        fit
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column label="ID" prop="id" width="80" align="center" />
        <el-table-column label="设备ID" prop="deviceId" width="120" />
        <el-table-column label="异常类型">
          <template slot-scope="{ row }">
            <el-tag :type="getAlertTypeTag(row.alertType)">
              {{ formatAlertType(row.alertType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="指标值" width="120">
          <template slot-scope="{ row }">
            {{ row.metricValue }} {{ getMetricUnit(row.metricType) }}
          </template>
        </el-table-column>
        <el-table-column label="阈值" width="120">
          <template slot-scope="{ row }">
            {{ row.thresholdValue }} {{ getMetricUnit(row.metricType) }}
          </template>
        </el-table-column>
        <el-table-column label="异常级别" width="100">
          <template slot-scope="{ row }">
            <el-tag :type="getLevelTag(row.alertLevel)">
              {{ formatLevel(row.alertLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="异常时间">
          <template slot-scope="{ row }">
            {{ row.alertTime }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ formatStatus(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="180"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="{ row }">
            <el-button
              size="mini"
              type="primary"
              @click="handleDetail(row)"
              v-hasPermi="['climate:alert:query']"
            >
              详情
            </el-button>
            <el-button
              size="mini"
              type="success"
              @click="handleProcess(row)"
              v-hasPermi="['climate:alert:edit']"
              :disabled="row.status === 1"
            >
              处理
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog
      :title="'异常详情 - ' + currentAlert.deviceId"
      :visible.sync="detailVisible"
      width="50%"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="异常ID">
          {{ currentAlert.id }}
        </el-descriptions-item>
        <el-descriptions-item label="设备ID">
          {{ currentAlert.deviceId }}
        </el-descriptions-item>
        <el-descriptions-item label="异常类型">
          <el-tag :type="getAlertTypeTag(currentAlert.alertType)">
            {{ formatAlertType(currentAlert.alertType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="指标类型">
          {{ formatMetricType(currentAlert.metricType) }}
        </el-descriptions-item>
        <el-descriptions-item label="当前值">
          {{ currentAlert.metricValue }} {{ getMetricUnit(currentAlert.metricType) }}
        </el-descriptions-item>
        <el-descriptions-item label="阈值">
          {{ currentAlert.thresholdValue }} {{ getMetricUnit(currentAlert.metricType) }}
        </el-descriptions-item>
        <el-descriptions-item label="异常级别">
          <el-tag :type="getLevelTag(currentAlert.alertLevel)">
            {{ formatLevel(currentAlert.alertLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="异常时间">
          {{ currentAlert.alertTime }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(currentAlert.status)">
            {{ formatStatus(currentAlert.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理人" v-if="currentAlert.status === 1">
          {{ currentAlert.processedBy }}
        </el-descriptions-item>
        <el-descriptions-item label="处理时间" v-if="currentAlert.status === 1">
          {{ currentAlert.processedTime }}
        </el-descriptions-item>
        <el-descriptions-item label="处理备注" v-if="currentAlert.status === 1">
          {{ currentAlert.processedComment }}
        </el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 处理对话框 -->
    <el-dialog
      :title="'处理异常 - ' + currentAlert.deviceId"
      :visible.sync="processVisible"
      width="50%"
    >
      <el-form
        ref="processForm"
        :model="processForm"
        :rules="processRules"
        label-width="100px"
      >
        <el-form-item label="异常ID" prop="id">
          <el-input v-model="processForm.id" disabled />
        </el-form-item>
        <el-form-item label="设备ID">
          <el-input v-model="currentAlert.deviceId" disabled />
        </el-form-item>
        <el-form-item label="异常类型">
          <el-input
            :value="formatAlertType(currentAlert.alertType)"
            disabled
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="processForm.status" placeholder="请选择状态">
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="处理人" prop="processedBy">
          <el-input
            v-model="processForm.processedBy"
            placeholder="请输入处理人"
          />
        </el-form-item>
        <el-form-item label="处理备注" prop="processedComment">
          <el-input
            v-model="processForm.processedComment"
            type="textarea"
            placeholder="请输入处理备注"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="processVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitProcess">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { listAlert, updateAlert } from "@/api/climate";
import { getToken } from "@/utils/auth";

export default {
  name: "AlertList",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 异常数据表格数据
      alertList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceId: undefined,
        alertType: undefined,
        status: undefined,
        beginAlertTime: undefined,
        endAlertTime: undefined,
      },
      // 日期范围
      dateRange: [],
      // 异常类型选项
      alertTypeOptions: [
        { value: "TEMPERATURE_HIGH", label: "温度过高" },
        { value: "TEMPERATURE_LOW", label: "温度过低" },
        { value: "HUMIDITY_HIGH", label: "湿度过高" },
        { value: "HUMIDITY_LOW", label: "湿度过低" },
        { value: "PRESSURE_HIGH", label: "气压过高" },
        { value: "PRESSURE_LOW", label: "气压过低" },
      ],
      // 状态选项
      statusOptions: [
        { value: 0, label: "未处理" },
        { value: 1, label: "已处理" },
      ],
      // 详情对话框
      detailVisible: false,
      // 处理对话框
      processVisible: false,
      // 当前异常数据
      currentAlert: {},
      // 处理表单
      processForm: {
        id: undefined,
        status: 1,
        processedBy: getToken("nickname") || "admin",
        processedComment: "",
        processedTime: new Date(),
      },
      // 处理表单验证规则
      processRules: {
        status: [
          { required: true, message: "请选择状态", trigger: "change" },
        ],
        processedBy: [
          { required: true, message: "请输入处理人", trigger: "blur" },
        ],
        processedComment: [
          { required: true, message: "请输入处理备注", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询异常数据列表 */
    getList() {
      this.loading = true;
      // 处理日期范围
      if (this.dateRange && this.dateRange.length === 2) {
        this.queryParams.beginAlertTime = this.dateRange[0];
        this.queryParams.endAlertTime = this.dateRange[1];
      } else {
        this.queryParams.beginAlertTime = undefined;
        this.queryParams.endAlertTime = undefined;
      }

      listAlert(this.queryParams).then((response) => {
        this.alertList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        deviceId: undefined,
        alertType: undefined,
        status: undefined,
        beginAlertTime: undefined,
        endAlertTime: undefined,
      };
      this.handleQuery();
    },
    /** 查看详情 */
    handleDetail(row) {
      this.currentAlert = row;
      this.detailVisible = true;
    },
    /** 处理异常 */
    handleProcess(row) {
      this.currentAlert = row;
      this.processForm = {
        id: row.id,
        status: 1,
        processedBy: getToken("nickname") || "admin",
        processedComment: "",
        processedTime: new Date(),
      };
      this.processVisible = true;
      this.$nextTick(() => {
        this.$refs.processForm.clearValidate();
      });
    },
    /** 提交处理 */
    submitProcess() {
      this.$refs.processForm.validate((valid) => {
        if (valid) {
          updateAlert(this.processForm).then((response) => {
            this.$modal.msgSuccess("处理成功");
            this.processVisible = false;
            this.getList();
          });
        }
      });
    },
    /** 格式化异常类型 */
    formatAlertType(type) {
      const map = {
        TEMP_HIGH: "温度过高",
        TEMP_LOW: "温度过低",
        HUMIDITY_HIGH: "湿度过高",
        HUMIDITY_LOW: "湿度过低",
        PRESSURE_HIGH: "气压过高",
        PRESSURE_LOW: "气压过低",
      };
      return map[type] || type;
    },
    /** 格式化指标类型 */
    formatMetricType(type) {
      const map = {
        temperature: "温度",
        humidity: "湿度",
        pressure: "气压",
      };
      return map[type] || type;
    },
    /** 获取指标单位 */
    getMetricUnit(type) {
      const map = {
        temperature: "°C",
        humidity: "%",
        pressure: "hPa",
      };
      return map[type] || "";
    },
    /** 格式化异常级别 */
    formatLevel(level) {
      const map = {
        1: "一般",
        2: "严重",
        3: "紧急",
      };
      return map[level] || level;
    },
    /** 格式化状态 */
    formatStatus(status) {
      const map = {
        0: "未处理",
        1: "已处理",
      };
      return map[status] || status;
    },
    /** 获取异常类型标签样式 */
    getAlertTypeTag(type) {
      const map = {
        TEMPERATURE_HIGH: "danger",
        TEMPERATURE_LOW: "primary",
        HUMIDITY_HIGH: "warning",
        HUMIDITY_LOW: "info",
        PRESSURE_HIGH: "danger",
        PRESSURE_LOW: "primary",
      };
      return map[type] || "";
    },
    /** 获取异常级别标签样式 */
    getLevelTag(level) {
      const map = {
        1: "info",
        2: "warning",
        3: "danger",
      };
      return map[level] || "";
    },
    /** 获取状态标签样式 */
    getStatusTag(status) {
      const map = {
        0: "danger",
        1: "success",
      };
      return map[status] || "";
    },
  },
};
</script>

<style scoped>
.filter-container {
  margin-bottom: 20px;
}
</style>
