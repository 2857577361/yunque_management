<template>
  <div class="device-disease-model-container">
    <!-- 标题和返回按钮 -->
    <div class="page-header">
      <h3>设备病害模型关联管理 - {{ deviceId }}</h3>
    </div>

    <!-- 基本信息卡片 -->
    <el-card shadow="hover" class="base-info-card">
      <el-descriptions title="设备基本信息" :column="3" border>
        <el-descriptions-item label="设备ID">{{ deviceDetails.deviceId }}</el-descriptions-item>
        <el-descriptions-item label="设备位置">{{ deviceDetails.location || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="设备状态">
          <el-tag :type="deviceDetails.status === '运行中' ? 'success' : 'danger'" size="small">
            {{ deviceDetails.status === '运行中' ? '在线' : '离线' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="总检测面积">{{ deviceDetails.totalArea }} m²</el-descriptions-item>
        <el-descriptions-item label="安装日期">{{ deviceInfo.installDate || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="质保时间">{{ deviceDetails.warrantyTime || '未知' }}</el-descriptions-item>
      </el-descriptions>

      <!-- 农作物检测面积表格 -->
      <div class="crop-table">
        <h4>农作物检测面积统计</h4>
        <el-table
          :data="deviceDetails.cropDetails"
          border
          size="small"
          style="width: 100%; margin-top: 15px"
        >
          <el-table-column prop="crop_name" label="农作物名称" width="150" />
          <el-table-column prop="area" label="检测面积(m²)" min-width="120" />
          <el-table-column prop="lastDetectionTime" label="最后检测时间" min-width="220" />
          <el-table-column prop="diseaseCount" label="病害数量" width="180" />
          <el-table-column label="操作" width="180">
            <template #default="{row}">
              <el-button type="text" size="mini" @click="viewCropDetail(row.cropId)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 关联病害和模型管理 -->
    <el-tabs v-model="activeTab" type="card">
      <!-- 关联病害管理 -->
      <el-tab-pane label="关联病害" name="diseases">
        <el-card shadow="hover" class="table-card">
          <el-table
            :data="deviceDetails.diseases"
            border
            v-loading="loading"
            style="width: 100%"
          >
            <el-table-column prop="diseaseId" label="病害ID" width="120" />
            <el-table-column prop="name" label="病害名称" width="150" />
            <el-table-column prop="description" label="病害描述" min-width="150" />
            <el-table-column label="操作" width="180">
              <template #default="{row}">
                <template>
                  <el-button
                    type="danger"
                    size="mini"
                    icon="el-icon-delete"
                    circle
                    @click="handleRemove({diseaseId: row.diseaseId})"
                  />
                </template>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <!-- 关联模型管理 -->
      <el-tab-pane label="关联模型" name="models">
        <el-card shadow="hover" class="table-card">
          <el-table
            :data="deviceDetails.models"
            border
            v-loading="loading"
            style="width: 100%"
          >
            <el-table-column prop="modelId" label="模型ID" width="120" />
            <el-table-column prop="modelName" label="模型名称" width="250" />
            <el-table-column prop="diseaseName" label="关联病害" width="150" />
            <el-table-column label="预测时间范围" min-width="320">
              <template #default="{row}">
                <template v-if="row.editing">
                  <el-date-picker
                    v-model="row.predictionTimeRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    value-format="yyyy-MM-dd HH:MM:SS"
                  />
                </template>
                <template v-else>
                  {{ row.predictionTimeRange ? row.predictionTimeRange.join(' 至 ') : '未设置' }}
                </template>
              </template>
            </el-table-column>
            <el-table-column label="防治面积(m²)" width="120">
              <template #default="{row}">
                <template v-if="row.editing">
                 <el-input v-model="row.area" />
                </template>
                <template v-else>
                  {{ row.area }}
                </template>
              </template>
            </el-table-column>
            <el-table-column label="是否是预测期" width="120">
              <template #default="{row}">
                <template v-if="row.editing">
                  <el-switch v-model="row.isPredictionEnabled" />
                </template>
                <template v-else>
                  {{ row.isPredictionEnabled ? '是' : '否'}}
                </template>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180">
              <template #default="{row}">
                <template v-if="row.editing">
                  <el-button
                    type="success"
                    size="mini"
                    icon="el-icon-check"
                    circle
                    @click="saveModelEdit(row)"
                  />
                  <el-button
                    type="info"
                    size="mini"
                    icon="el-icon-close"
                    circle
                    @click="cancelModelEdit(row)"
                  />
                </template>
                <template v-else>
                  <el-button
                    type="primary"
                    size="mini"
                    icon="el-icon-edit"
                    circle
                    @click="editModel(row)"
                  />
                  <el-button
                    type="danger"
                    size="mini"
                    icon="el-icon-delete"
                    circle
                    @click="handleRemove({modelId: row.modelId, diseaseId: row.diseaseId})"
                  />
                </template>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { listModelData, listDiseaseModel, delDeviceDiseaseModel } from "@/api/model";
import { listDevice, getDeviceArea } from "@/api/device/device";
import { updataDiseaseModelByDevideId } from "@/api/model";
import { updateDiseasePrediction, listDisease } from '@/api/disease/disease';

export default {
  name: 'DeviceDiseaseModel',
  data() {
    return {
      deviceId: this.$route.query.deviceId || '',
      activeTab: 'diseases',
      loading: false,
      diseaseList: [],
      deviceInfo: [],
      modelList: [],
      modelOptions: [],
      availableDiseases: [],
      addDiseaseDialogVisible: false,
      addDiseaseForm: {
        diseaseId: '',
        isPredictionEnabled: true,
        modelId: ''
      },
      cropDetectionData: [],
      totalDetectionArea: 0,
      deviceDetails: [],
      // 用于存储编辑前的数据
      diseaseEditCache: {},
      modelEditCache: {}
    }
  },
  created() {
    this.getDeviceList()
  },
  methods: {
    async getDeviceList() {
      const diseaseData = await listDisease({deviceId: this.$route.query.deviceId});

      const res = await listDevice({
        deviceId: this.$route.query.deviceId
      });
      const areaData = await getDeviceArea(this.$route.query.deviceId);
      const totalArea = areaData.data.totalArea;
      const cropDetails = JSON.parse(areaData.data.cropDetails).map(v => ({
        ...v,
        lastDetectionTime : '2025-1-30',
        diseaseCount : '200'
      }));

      this.deviceDetails = await Promise.all(
        res.rows.map(async (device) => {
          try {
            const data = await listDiseaseModel({ id: device.deviceId }) || {};

            const disease = Array.from(
              new Map(
                (data.data || []).map(disease => [disease.diseaseName, {
                  diseaseId: disease.diseaseId,
                  name: disease.diseaseName || "未知病害",
                  level: disease.diseaseSeries,
                  description: disease.diseaseDescription,
                  modelId: disease.modelId || '',
                  editing: false
                }])
              ).values()
            );

            const model = (data.data || []).map((model) => ({
              modelId: model.modelId || "未知",
              modelName: model.modelName || "未知模型",
              diseaseName: model.diseaseName,
              diseaseId: model.diseaseId,
              isPredictionEnabled: model.isPredictionEnabled || false,
              area: model.area || 0,
              predictionTimeRange: model.predictionStartTime && model.predictionEndTime
                ? [model.predictionStartTime, model.predictionEndTime]
                : null,
              editing: false
            }));

            return {
              ...device,
              totalArea,
              cropDetails,
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
              totalArea,
              cropDetails,
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
      this.deviceDetails = this.deviceDetails[0]
    },
    // 模型编辑相关方法
    editModel(row) {
      this.modelEditCache = {
        ...row,
        predictionTimeRange: row.predictionTimeRange ? [...row.predictionTimeRange] : null
      };
      row.editing = true;
    },

    cancelModelEdit(row) {
      Object.assign(row, this.modelEditCache);
      row.editing = false;
      this.modelEditCache = {};
    },

    async saveModelEdit(row) {
      try {
        await updataDiseaseModelByDevideId({
          deviceId: this.deviceId,
          diseaseModels: [{
            diseaseId: row.diseaseId,
            modelId: row.modelId,
            predictionStartTime: row.predictionTimeRange ? row.predictionTimeRange[0] : null,
            predictionEndTime: row.predictionTimeRange ? row.predictionTimeRange[1] : null,
            area: row.area,
            isPredictionEnabled: row.isPredictionEnabled
          }]
        });
        this.$message.success('模型信息更新成功');
        row.editing = false;
        this.modelEditCache = {};
      } catch (error) {
        console.error('更新模型信息失败:', error);
        this.$message.error('更新模型信息失败');
      }
    },

    handleRemove(queryObj) {
      this.$confirm(`确定要删除此关联吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        await delDeviceDiseaseModel({
          deviceId: this.deviceDetails.deviceId,
          diseaseModels: [queryObj]
        })
        this.$message.success('删除成功')
        this.getDeviceList();
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },

    viewCropDetail(cropId) {
      this.$router.push({
        path: '/crop/detail',
        query: { cropId, deviceId: this.deviceId }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.device-disease-model-container {
  padding: 0 20px 0 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  align-items: center;

  h3 {
    line-height: 1;
  }

  .el-button {
    margin-right: 10px;
  }
}

.base-info-card {
  margin-bottom: 20px;

  .el-descriptions {
    margin-bottom: 20px;
  }

  .crop-table {
    margin-top: 20px;

    h4 {
      margin: 0 0 15px 0;
      font-size: 16px;
      color: #606266;
    }
  }
}

.table-card {
  margin-top: 15px;

  .table-header {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 15px;
  }
}

.el-date-editor.el-range-editor {
  width: 100%;
}

.el-select {
  width: 100%;
}

.el-button + .el-button {
  margin-left: 8px;
}
</style>
