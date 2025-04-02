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
          <div class="table-header">
            <el-button
              type="primary"
              icon="el-icon-plus"
              @click="showAddDiseaseDialog"
            >
              添加病害
            </el-button>
          </div>

          <el-table
            :data="deviceDetails.diseases"
            border
            v-loading="loading"
            style="width: 100%"
          >
            <el-table-column prop="diseaseId" label="病害ID" width="120" />
            <el-table-column prop="name" label="病害名称" width="150" />
            <el-table-column prop="description" label="病害描述" min-width="150" />
            <el-table-column label="是否预测期" width="120">
              <template #default="{row}">
                <el-switch
                  v-model="row.isPredictionEnabled"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  @change="handlePredictionChange(row)"
                />
              </template>
            </el-table-column>
            <el-table-column label="选中关联模型" width="200">
              <template #default="{row}">
                <el-select
                  v-model="row.modelId"
                  placeholder="选择模型"
                  clearable
                  filterable
                  @change="handleModelChange(row)"
                >
                  <el-option
                    v-for="model in modelOptions"
                    :key="model.modelId"
                    :label="model.modelName"
                    :value="model.modelId"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="{row}">
                <el-button
                  type="danger"
                  size="mini"
                  icon="el-icon-delete"
                  circle
                  @click="handleRemoveDisease(row.diseaseId)"
                />
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
                <el-date-picker
                  v-model="row.predictionTimeRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  value-format="yyyy-MM-dd"
                  @change="handleTimeRangeChange(row)"
                />
              </template>
            </el-table-column>
            <el-table-column prop="area" label="防治面积(m²)" width="120" />
            <el-table-column label="操作" width="200">
              <template #default="{row}">
                <el-button
                  type="danger"
                  size="mini"
                  icon="el-icon-delete"
                  circle
                  @click="handleRemoveModel(row.modelId, row.diseaseId)"
                />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加病害对话框 -->
    <el-dialog
      title="添加病害"
      :visible.sync="addDiseaseDialogVisible"
      width="50%"
    >
      <el-form :model="addDiseaseForm" label-width="120px">
        <el-form-item label="选择病害">
          <el-select
            v-model="addDiseaseForm.diseaseId"
            placeholder="请选择病害"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="disease in availableDiseases"
              :key="disease.diseaseId"
              :label="disease.diseaseName"
              :value="disease.diseaseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否预测期">
          <el-switch
            v-model="addDiseaseForm.isPredictionEnabled"
            active-color="#13ce66"
            inactive-color="#ff4949"
          />
        </el-form-item>
        <el-form-item label="关联模型">
          <el-select
            v-model="addDiseaseForm.modelId"
            placeholder="请选择模型"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="model in modelOptions"
              :key="model.modelId"
              :label="model.modelName"
              :value="model.modelId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDiseaseDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAddDisease">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listModelData, listDiseaseModel, delDeviceDiseaseModel } from "@/api/model";
import { listDevice, getDeviceArea } from "@/api/device/device";

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
    }
  },
  created() {
    this.getDeviceList()
    // await this.getDeviceArea()
  },
  methods: {
    async getDeviceArea() {
      const res = await getDeviceArea(this.$route.query.deviceId);
      this.deviceDetails.totalArea = res.data.totalArea;
      const cropDetails = JSON.parse(res.data.cropDetails);
      this.deviceDetails.cropDatas = cropDetails;
      // this.deviceDetails.cropData = res.data.cropTypes.split(',').map((item) => {
      //   return {
      //     cropType: item,
      //     detail: cropDetails.filter((ele) => {
      //       return ele.crop_name === item
      //     })[0],
      //   }
      // });
      console.log(this.deviceDetails);
    },

    async getDeviceList() {
      const res = await listDevice({
        deviceId: this.$route.query.deviceId
      });
      const areaData = await getDeviceArea(this.$route.query.deviceId);
      const totalArea = areaData.data.totalArea;
      const cropDetails = JSON.parse(areaData.data.cropDetails);

      this.deviceDetails = await Promise.all(
        res.rows.map(async (device) => {
          try {
            const data = await listDiseaseModel({ id: device.deviceId }) || {};  // 处理 data 为 null 或 undefined

            const disease = Array.from(
              new Map(
                (data.data || []).map(disease => [disease.diseaseName, {
                  diseaseId: disease.diseaseId,
                  name: disease.diseaseName || "未知病害",
                  level: "中等",
                  description: "暂无"
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
      console.log(this.deviceDetails)
    },
    showAddDiseaseDialog() {
      if (this.availableDiseases.length === 0) {
        this.$message.warning('没有可添加的病害')
        return
      }
      this.addDiseaseForm = {
        diseaseId: this.availableDiseases[0].diseaseId,
        isPredictionEnabled: true,
        modelId: ''
      }
      this.addDiseaseDialogVisible = true
    },

    async handleAddDisease() {
      if (!this.addDiseaseForm.diseaseId) {
        this.$message.warning('请选择病害')
        return
      }

      try {
        const params = {
          deviceId: this.deviceId,
          diseaseModels: [{
            diseaseId: this.addDiseaseForm.diseaseId,
            modelId: this.addDiseaseForm.modelId,
            isPredictionEnabled: this.addDiseaseForm.isPredictionEnabled
          }]
        }

        await addDeviceDiseaseModel(params)
        this.$message.success('添加成功')
        this.addDiseaseDialogVisible = false
        this.fetchData() // 刷新数据
      } catch (error) {
        console.error('添加病害失败:', error)
      }
    },

    async handleRemoveDisease(diseaseId) {
      try {
        await this.$confirm('确定要删除此病害关联吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        await deleteDeviceDiseaseModel({
          deviceId: this.deviceId,
          diseaseModels: [{diseaseId}]
        })

        this.$message.success('删除成功')
        this.fetchData() // 刷新数据
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除病害失败:', error)
        }
      }
    },

    async handleRemoveModel(modelId, diseaseId) {
      try {
        await this.$confirm('确定要删除此模型关联吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        await deleteDeviceDiseaseModel({
          deviceId: this.deviceId,
          diseaseModels: [{diseaseId, modelId}]
        })

        this.$message.success('删除成功')
        this.fetchData() // 刷新数据
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除模型失败:', error)
        }
      }
    },

    async handlePredictionChange(row) {
      try {
        await updateDeviceDiseaseModel({
          deviceId: this.deviceId,
          diseaseModels: [{
            diseaseId: row.diseaseId,
            isPredictionEnabled: row.isPredictionEnabled
          }]
        })
        this.$message.success('更新成功')
      } catch (error) {
        console.error('更新预测期状态失败:', error)
        // 恢复原状态
        row.isPredictionEnabled = !row.isPredictionEnabled
      }
    },

    async handleModelChange(row) {
      if (!row.modelId) return

      try {
        await updateDeviceDiseaseModel({
          deviceId: this.deviceId,
          diseaseModels: [{
            diseaseId: row.diseaseId,
            modelId: row.modelId
          }]
        })
        this.$message.success('模型关联更新成功')
        this.fetchData() // 刷新数据
      } catch (error) {
        console.error('更新模型关联失败:', error)
      }
    },

    async handleTimeRangeChange(row) {
      if (!row.predictionTimeRange || row.predictionTimeRange.length !== 2) return

      try {
        await updateDeviceDiseaseModel({
          deviceId: this.deviceId,
          diseaseModels: [{
            diseaseId: row.diseaseId,
            modelId: row.modelId,
            startPredictionTime: row.predictionTimeRange[0],
            endPredictionTime: row.predictionTimeRange[1]
          }]
        })
        this.$message.success('预测时间更新成功')
      } catch (error) {
        console.error('更新预测时间失败:', error)
      }
    },

    viewCropDetail(cropId) {
      // 跳转到农作物详情页面
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
</style>
