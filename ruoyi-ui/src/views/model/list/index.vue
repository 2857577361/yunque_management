<template>
  <div class="monitoring-container">
    <!-- 标题区域 -->
    <div class="model-title">
      <span>设备运行状态监测</span>
    </div>

    <!-- 查询表单 -->
    <el-card shadow="hover" class="query-card">
      <el-form :inline="true" :model="formInline" class="query-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="模型ID">
              <el-input v-model="formInline.modelId" placeholder="请输入模型ID" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="开始时间">
              <el-date-picker
                v-model="formInline.startPredictionTime"
                type="date"
                placeholder="选择开始时间"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="结束时间">
              <el-date-picker
                v-model="formInline.endPredictionTime"
                type="date"
                placeholder="选择结束时间"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6" class="form-actions">
            <el-button type="primary" @click="onSubmit">查询</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="hover" class="data-card">
      <el-table
        :data="modelData"
        v-loading="loading"
        style="width: 100%"
        stripe
        border
      >
        <!-- 表格列定义 -->
        <el-table-column prop="modelId" label="模型ID" width="120" align="center" />
        <el-table-column prop="modelName" label="模型名称" width="150" />
        <el-table-column prop="formulaDefinition" label="公式定义" />

        <el-table-column label="计算时间" align="center">
          <el-table-column prop="startPredictionTime" label="开始时间" width="160" align="center" />
          <el-table-column prop="endPredictionTime" label="结束时间" width="160" align="center" />
        </el-table-column>

        <el-table-column label="误差范围" width="120" align="center">
          <template #default="{row}">
            <el-tag type="info" effect="dark">±{{ row.errorRange }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="设备列表">
          <template #default="{row}">
            <div class="device-tags">
              <el-tag
                v-for="device in row.deviceIds"
                :key="device"
                class="device-tag"
                effect="plain"
                @click="showDeviceDetails(device)"
              >
                <i class="el-icon-cpu"></i> {{ device }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 设备列表弹窗 - 使用相对定位 -->
    <div class="dialog-container">
      <el-dialog
        title="设备列表详情"
        :visible.sync="deviceDialogVisible"
        width="80%"
        custom-class="device-dialog"
        :append-to-body="false"
      >
        <div class="device-grid">
          <el-card
            v-for="device in deviceDetails"
            :key="device.deviceId"
            shadow="hover"
            class="device-card"
            :class="{'online': device.status === 'online'}"
          >
            <div class="device-header">
              <div class="device-id">
                <i class="el-icon-cpu"></i>
                <span>{{ device.deviceId }}</span>
              </div>
              <div class="device-status">
                <el-tag
                  :type="device.status === 'online' ? 'success' : 'danger'"
                  size="mini"
                  effect="plain"
                >
                  {{ device.status === 'online' ? '在线' : '离线' }}
                </el-tag>
              </div>
            </div>
            <div class="device-actions">
              <el-button
                type="primary"
                size="mini"
                @click="showDeviceDetailModal(device)"
              >
                查看详情
              </el-button>
            </div>
          </el-card>
        </div>

        <div class="dialog-footer">
          <span class="total-count">共 {{ deviceTotal }} 条记录</span>
          <el-pagination
            small
            layout="prev, pager, next"
            :total="deviceTotal"
            :page-size="deviceQueryParams.pageSize"
            @current-change="handleDevicePageChange"
          />
        </div>
      </el-dialog>
    </div>

    <!-- 设备详情弹窗 - 使用相对定位 -->
    <div class="dialog-container">
      <el-dialog
        :title="`设备详情 - ${selectedDevice.deviceId || ''}`"
        :visible.sync="deviceDetailModalVisible"
        width="60%"
        custom-class="device-detail-dialog"
        :append-to-body="false"
        @close="getList"
      >
        <div class="detail-container">
          <div class="detail-left">
<!--            <img src="@/assets/images/detail.png" />-->
            <div class="device-image-large">
              <img src="@/assets/images/detail.png" />
              <div class="device-status-badge">
                <el-tag
                  :type="selectedDevice.status === '运行中' ? 'success' : 'danger'"
                  effect="dark"
                >
                  {{ selectedDevice.status === '运行中' ? '在线' : '离线' }}
                </el-tag>
              </div>
            </div>
          </div>

          <div class="detail-right">
            <el-tabs v-model="activeTab">
              <el-tab-pane label="基本信息" name="basic">
                <div class="info-section">
                  <div class="info-row">
                    <span class="info-label">设备ID:</span>
                    <span class="info-value">{{ selectedDevice.deviceId }}</span>
                  </div>
                  <div class="info-row">
                    <span class="info-label">设备类型:</span>
                    <span class="info-value">{{ selectedDevice.type || '未知' }}</span>
                  </div>
                  <div class="info-row">
                    <span class="info-label">设备地址:</span>
                    <span class="info-value">{{ selectedDevice.location || '未记录' }}</span>
                  </div>
                  <div class="info-row">
                    <span class="info-label">质保时间:</span>
                    <span class="info-value">{{ selectedDevice.warrantyTime || '未知' }}</span>
                  </div>
                </div>
              </el-tab-pane>

              <el-tab-pane label="关联病害" name="diseases">
                <el-table
                  :data="selectedDevice.diseases || []"
                  border
                  size="mini"
                  empty-text="暂无关联病害"
                >
                  <el-table-column prop="name" label="病害名称" width="120" />
                  <el-table-column prop="level" label="严重程度" width="100">
                    <template #default="{row}">
                      <el-tag :type="getDiseaseLevelTag(row.level)">
                        {{ row.level }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="description" label="病害描述" />
                  <el-table-column label="操作" width="80">
                    <template #default="{row}">
                      <el-button
                        type="danger"
                        size="mini"
                        icon="el-icon-delete"
                        circle
                        @click="confirmDeleteModel({ diseaseId: row.diseaseId })"
                      />
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>

              <el-tab-pane label="关联模型" name="models">
                <el-table
                  :data="selectedDevice.models || []"
                  border
                  size="mini"
                  empty-text="暂无关联模型"
                >
                  <el-table-column prop="modelId" label="模型ID" width="120" />
                  <el-table-column prop="diseaseName" label="病害名称" width="150" />
                  <el-table-column prop="modelName" label="模型名称" width="150" />
                  <el-table-column prop="area" label="防治面积" width="160" />
                  <el-table-column label="操作" width="80">
                    <template #default="{row}">
                      <el-button
                        type="danger"
                        size="mini"
                        icon="el-icon-delete"
                        circle
                        @click="confirmDeleteModel({ diseaseId: row.diseaseId, modelId: row.modelId } )"
                      />
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { listModelData, listDiseaseModel, delDeviceDiseaseModel } from "@/api/model";
import { listDevice } from "@/api/device/device";

export default {
  name: 'DeviceMonitoring',
  data() {
    return {
      formInline: {
        modelId: '',
        startPredictionTime: '',
        endPredictionTime: ''
      },
      loading: false,
      modelData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },

      // 设备列表弹窗相关
      deviceDialogVisible: false,
      deviceDetails: [],
      deviceTotal: 0,
      deviceQueryParams: {
        pageNum: 1,
        pageSize: 8, // 每页8个设备，与UI设计一致
        deviceId: ''
      },

      // 设备详情弹窗相关
      deviceDetailModalVisible: false,
      selectedDevice: {},
      activeTab: 'basic',
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.queryParams,
          ...this.formInline
        }
        const res = await listModelData(params)
        this.modelData = res.rows
        this.total = res.total
      } finally {
        this.loading = false
      }
    },

    onSubmit() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    resetForm() {
      this.formInline = {
        modelId: '',
        startPredictionTime: '',
        endPredictionTime: ''
      }
      this.onSubmit()
    },

    // 显示设备列表弹窗
    async showDeviceDetails(deviceId) {
      this.deviceQueryParams.deviceId = deviceId
      this.deviceQueryParams.pageNum = 1
      await this.getDeviceList()
      // this.deviceDialogVisible = true
      this.showDeviceDetailModal(this.deviceDetails[0])
    },

    async getDeviceList() {
      const res = await listDevice(this.deviceQueryParams);

      this.deviceDetails = await Promise.all(
        res.rows.map(async (device) => {
          try {
            const data = await listDiseaseModel({ id: device.deviceId }) || {};  // 处理 data 为 null 或 undefined

            const disease = Array.from(
              new Map(
                (data.data || []).map(disease => [disease.diseaseName, {
                  diseaseId: disease.diseaseId,
                  name: disease.diseaseName || "未知病害",
                  level: disease.diseaseSeries,
                  description: disease.diseaseDescription
                }])
              ).values()
            );


            const model = (data.data || []).map((model) => ({
              modelId: model.modelId || "未知",
              modelName: model.modelName || "未知模型",
              diseaseName: model.diseaseName,
              diseaseId: model.diseaseId,
              area: model.area || 383
            }));

            return {
              ...device,
              type: ["sensor", "camera", "gateway"][Math.floor(Math.random() * 3)],
              location: device.address || "未知地址",
              warrantyTime: device.warrantyTime || "未知",
              status: device.status || "未知",
              diseases: disease,
              models: model,
            };
          } catch (error) {
            console.error(`获取设备 ${device.deviceId} 的病害模型失败:`, error);
            return {
              ...device,
              type: ["sensor", "camera", "gateway"][Math.floor(Math.random() * 3)],
              location: device.address || "未知地址",
              warrantyTime: device.warrantyTime || "未知",
              status: device.status || "未知",
              diseases: [],
              models: [],
            };
          }
        })
      );

      this.deviceTotal = res.total;
    },

    handleDevicePageChange(page) {
      this.deviceQueryParams.pageNum = page
      this.getDeviceList()
    },

    // 显示设备详情弹窗
    showDeviceDetailModal(device) {
      this.selectedDevice = device
      this.activeTab = 'basic'
      this.deviceDetailModalVisible = true
    },

    // 病害等级标签样式
    getDiseaseLevelTag(level) {
      const map = {
        '轻微': 'success',
        '中等': 'warning',
        '严重': 'danger'
      }
      return map[level] || 'info'
    },

    // 确认删除模型关联
    confirmDeleteModel(queryObj) {
      this.$confirm(`确定要删除此关联吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        await delDeviceDiseaseModel({
          deviceId: this.selectedDevice.deviceId,
          diseaseModels: [queryObj]
        })
        this.$message.success('删除成功')
        if (queryObj.modelId) {
          const map = new Map()
          this.selectedDevice.models = this.selectedDevice.models.filter(m => {
            if (m.diseaseId !== queryObj.diseaseId || m.modelId !== queryObj.modelId){
              map.set(m.diseaseId, 1)
            }
            return m.diseaseId !== queryObj.diseaseId || m.modelId !== queryObj.modelId
          })
          this.selectedDevice.diseases = this.selectedDevice.diseases.filter(m => {
            return map.has(m.diseaseId);
          })


        } else {
          const index = this.selectedDevice.diseases.findIndex(d => {
            return d.diseaseId === queryObj.diseaseId
          })
          if (index !== -1) {
            this.selectedDevice.diseases.splice(index, 1)
          }
          this.selectedDevice.models = this.selectedDevice.models.filter(m => {
            return m.diseaseId !== queryObj.diseaseId
          })
        }
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.monitoring-container {
  padding: 20px;
  background-color: #f5f7fa;
  position: relative; /* 为弹窗提供定位上下文 */
}

.model-title {
  margin-bottom: 20px;
  span {
    font-size: 18px;
    font-weight: bold;
    color: #303133;
  }
}

.query-card {
  margin-bottom: 20px;
  ::v-deep .el-card__body {
    padding: 20px;
  }
}

.query-form {
  ::v-deep .el-form-item {
    margin-bottom: 0;
    width: 100%;
  }

  ::v-deep .el-form-item__content {
    width: calc(100% - 80px); /* 根据label宽度调整 */
  }

  ::v-deep .el-input,
  ::v-deep .el-date-editor {
    width: 100%;
  }
}

.data-card {
  ::v-deep .el-card__body {
    padding: 0;
  }
}

.device-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.device-tag {
  cursor: pointer;
  transition: all 0.3s;
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
}

// 弹窗容器
.dialog-container {
  position: relative;
}

// 设备列表弹窗样式
.device-dialog {
  position: relative;
  margin-top: 20vh;

  .device-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    padding: 10px 0;
  }

  .device-card {
    transition: all 0.3s;
    border-radius: 8px;

    &.online {
      border-left: 4px solid #67C23A;
    }

    &:not(.online) {
      border-left: 4px solid #F56C6C;
    }

    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    .device-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;

      .device-id {
        display: flex;
        align-items: center;
        font-weight: bold;

        i {
          margin-right: 5px;
          font-size: 16px;
          color: #409EFF;
        }
      }
    }

    .device-image {
      height: 100px;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-bottom: 10px;

      img {
        max-height: 100%;
        max-width: 100%;
        object-fit: contain;
      }
    }

    .device-actions {
      text-align: center;
    }
  }

  .dialog-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
    padding-top: 10px;
    border-top: 1px solid #ebeef5;

    .total-count {
      color: #909399;
      font-size: 14px;
    }
  }
}

// 设备详情弹窗样式
.device-detail-dialog {
  position: relative;
  margin-top: 15vh;

  .detail-container {
    display: flex;
    gap: 20px;
  }

  .detail-left {
    width: 200px;

    .device-image-large {
      position: relative;
      width: 100%;
      height: 180px;
      background-color: #f5f7fa;
      border-radius: 4px;
      overflow: hidden;
      margin-bottom: 15px;

      img {
        width: 100%;
        height: 100%;
        object-fit: contain;
      }

      .device-status-badge {
        position: absolute;
        top: 10px;
        right: 10px;
      }
    }
  }

  .detail-right {
    flex: 1;

    .info-section {
      padding: 10px;

      .info-row {
        display: flex;
        margin-bottom: 12px;
        line-height: 1.5;

        .info-label {
          width: 80px;
          color: #909399;
        }

        .info-value {
          flex: 1;
          color: #303133;
        }
      }
    }
  }
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding-top: 2px; /* 对齐按钮 */
}
</style>
