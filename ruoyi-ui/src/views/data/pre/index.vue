<template>
  <div>
    <div class="model_title">
      <span>病害模型关联管理</span>
    </div>
    <div class="content-container">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <div class="model_form">
          <el-form-item label="病害ID" style="margin-left: 20px">
            <el-input v-model="formInline.diseaseId" placeholder="请输入病害ID" size="small" style="width: 280px;"></el-input>
          </el-form-item>
          <el-form-item label="病害名称">
            <el-input v-model="formInline.diseaseName" placeholder="请输入病害名称" size="small" style="width: 280px;"></el-input>
          </el-form-item>
          <el-form-item label="模型名称">
            <el-input v-model="formInline.modelName" placeholder="请输入模型名称" size="small" style="width: 280px;"></el-input>
          </el-form-item>
          <div class="model_button">
            <el-form-item>
              <el-button type="primary" @click="onSubmit">查询</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="info" @click="resetForm">重置</el-button>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <el-card class="table-card">
        <el-table :data="filteredData" border style="width: 100%" v-loading="loading">

          <!-- 病害ID -->
          <el-table-column prop="diseaseId" label="病害ID" align="center" width="120"></el-table-column>

          <!-- 病害名称 -->
          <el-table-column prop="diseaseName" label="病害名称" align="center"></el-table-column>

          <!-- 关联模型列表 -->
          <el-table-column label="关联模型" align="center">
            <template slot-scope="{ row }">
              <el-select
                v-if="isEdit && activeDiseaseId === row.diseaseId"
                v-model="row.selectedModels"
                multiple
                filterable
                placeholder="请选择关联模型"
                style="width: 100%"
                @change="handleModelChange(row)"
              >
                <el-option
                  v-for="model in allModels"
                  :key="model.modelId"
                  :label="model.modelName"
                  :value="model.modelId"
                ></el-option>
              </el-select>
              <div v-else-if="row.modelList && row.modelList.length > 0" style="margin-top: 10px">
                <el-tag
                  v-for="model in row.modelList"
                  :key="model.modelId"
                  type="info"
                  style="margin-right: 5px; margin-bottom: 5px"
                >
                  {{ model.modelName }}
                </el-tag>
              </div>
              <div v-else style="color: #999; margin-top: 5px">暂无关联模型</div>
            </template>
          </el-table-column>

          <!-- 操作列 -->
          <el-table-column label="操作" width="180" align="center">
            <template slot-scope="{ row }">
              <el-button
                type="info"
                size="mini"
                @click="handleEdit(row)"
                :loading="row.saving"
              >编辑</el-button>
              <el-button
                :disabled="!isEdit"
                type="primary"
                size="mini"
                @click="saveRelation(row)"
                :loading="row.saving"
              >保存</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="pageSize"
          :small="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          style="float:right; margin-top: 20px">
        </el-pagination>
      </el-card>
    </div>
  </div>
</template>

<script>
import { updataDiseaseModelRelation, listModelData } from '@/api/model';
import {listDiseaseModel} from "@/api/disease/disease";

export default {
  data() {
    return {
      loading: false,
      formInline: {
        diseaseId: '',
        diseaseName: '',
        modelName: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      diseaseData: [],
      allModels: [], // 所有可用模型列表
      originalData: [], // 原始数据备份
      isEdit: false,
      activeDiseaseId: '',
    };
  },
  async created() {
    await this.fetchData();
    await this.fetchAllModels();
  },
  methods: {
    handleEdit (row) {
      this.isEdit = true;
      this.activeDiseaseId = row.diseaseId;
    },
    // 获取病害模型关联数据
    async fetchData() {
      this.loading = true;
      try {
        const params = {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          ...this.formInline
        };
        const res = await listDiseaseModel(params);
        this.diseaseData = res.rows.map(item => {
          return {
            ...item,
            selectedModels: item.modelList.map(model => model.modelId),
            saving: false
          };
        });
        this.total = res.total;
        this.originalData = JSON.parse(JSON.stringify(this.diseaseData));
      } catch (error) {
        console.error('获取数据失败:', error);
      } finally {
        this.loading = false;
      }
    },

    // 获取所有模型数据
    async fetchAllModels() {
      try {
        const res = await listModelData();
        this.allModels = res.rows || res.data;
      } catch (error) {
        console.error('获取模型列表失败:', error);
      }
    },

    // 查询
    onSubmit() {
      this.currentPage = 1;
      this.fetchData();
    },

    // 重置查询表单
    resetForm() {
      this.formInline = {
        diseaseId: '',
        diseaseName: '',
        modelName: ''
      };
      this.currentPage = 1;
      this.fetchData();
    },

    // 模型选择变化
    handleModelChange(row) {
      // 可以在这里添加一些逻辑处理
      console.log('模型选择变化:', row.diseaseName, row.selectedModels);
    },

    // 保存关联关系
    async saveRelation(row) {
      row.saving = true;
      try {
        const params = {
          diseaseId: row.diseaseId,
          modelIds: row.selectedModels
        };
        await updataDiseaseModelRelation(params);
        this.$message.success('保存成功');
        // 重新获取数据以确保数据一致性
        await this.fetchData();
      } catch (error) {
        console.error('保存失败:', error);
        this.$message.error('保存失败');
        // 恢复原始选择
        const original = this.originalData.find(item => item.diseaseId === row.diseaseId);
        if (original) {
          row.selectedModels = [...original.selectedModels];
        }
      } finally {
        row.saving = false;
        this.isEdit = false;
      }
    },

    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.fetchData();
    },

    handleCurrentChange(newPage) {
      this.currentPage = newPage;
      this.fetchData();
    }
  },
  computed: {
    filteredData() {
      // 这里可以根据需要添加前端过滤逻辑
      return this.diseaseData;
    }
  }
}
</script>

<style scoped>
.model_title {
  padding: 20px 0 0 20px;
}
.model_title span {
  font-weight: bold;
  font-size: 16px;
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
</style>
