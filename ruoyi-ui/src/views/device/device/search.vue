<template>
  <div class="cascade-disease-model">
    <el-form-item label="关联模型:" prop="diseaseModels">
      <el-cascader
        v-model="selectedOptions"
        :options="cascadeOptions"
        :props="cascaderProps"
        clearable
        filterable
        :show-all-levels="true"
        placeholder="请选择病害和模型"
        style="width: 100%"
        @change="handleChange"
      ></el-cascader>
    </el-form-item>
  </div>
</template>

<script>
import { listDiseaseModel } from "@/api/disease/disease";

import { listDiseaseModel as list } from "@/api/model/index"

export default {
  name: 'CascadeDiseaseModel',
  props: {
    deviceId: {
      type: [String, Number],
      default: null
    },
  },
  data() {
    return {
      selectedOptions: [],
      cascadeOptions: [],
      cascaderProps: {
        multiple: true,
        checkStrictly: true,
        value: 'uniqueId',
        label: 'name',
        children: 'children',
        emitPath: true,
        disabled: 'disabled' // 添加disabled属性映射
      },
      diseaseModelData: null,
      relatedOptions: [] // 存储需要禁用的选项
    };
  },
  async mounted() {
    await this.loadData();
  },
  methods: {
    async loadData() {
      try {
        this.diseaseModelData = await listDiseaseModel({
          pageSize: 1000
        });

        // 如果有设备ID，加载关联数据
        if (this.deviceId) {
          await this.loadRelatedData(this.deviceId);
        }

        // 格式化选项数据（添加disabled标记）
        this.formatCascadeOptions();
      } catch (error) {
        console.error('数据加载失败:', error);
        this.$message.error('数据加载失败');
      }
    },

    async loadRelatedData(deviceId) {
      try {
        const res = await list({
          id: deviceId
        });

        if (res.code === 200) {
          const relatedData = this.convertToDiseaseModelStructure(res.data);
          this.relatedOptions = this.generateDisabledOptions(relatedData);
          this.selectedOptions = this.generateSelectedOptions(relatedData);
          this.$emit('input', this.selectedOptions);
        }
      } catch (error) {
        console.error('获取关联数据失败:', error);
      }
    },

    // 生成需要禁用的选项唯一标识
    generateDisabledOptions(relatedData) {
      return relatedData.reduce((acc, disease) => {
        // 添加病害节点
        acc.push(`disease_${disease.diseaseId}`);

        // 添加模型节点
        disease.modelList.forEach(model => {
          acc.push(`disease_${disease.diseaseId}_model_${model.modelId}`);
        });

        return acc;
      }, []);
    },

    // 格式化级联选项，标记需要禁用的选项
    formatCascadeOptions() {
      this.cascadeOptions = this.diseaseModelData.rows.map(disease => {
        const isDiseaseDisabled = this.relatedOptions.includes(`disease_${disease.diseaseId}`);

        return {
          uniqueId: `disease_${disease.diseaseId}`,
          name: disease.diseaseName,
          disabled: isDiseaseDisabled, // 禁用整个病害节点
          children: disease.modelList.map(model => {
            const isModelDisabled = this.relatedOptions.includes(
              `disease_${disease.diseaseId}_model_${model.modelId}`
            );

            return {
              uniqueId: `disease_${disease.diseaseId}_model_${model.modelId}`,
              name: model.modelName,
              disabled: isModelDisabled // 禁用特定模型
            };
          })
        };
      });
    },

    convertToDiseaseModelStructure(data) {
      const diseaseMap = new Map();
      data.forEach(item => {
        if (!diseaseMap.has(item.diseaseId)) {
          diseaseMap.set(item.diseaseId, {
            diseaseId: item.diseaseId,
            diseaseName: item.diseaseName,
            modelList: []
          });
        }
        diseaseMap.get(item.diseaseId).modelList.push({
          modelId: item.modelId,
          modelName: item.modelName
        });
      });
      return Array.from(diseaseMap.values());
    },

    generateSelectedOptions(relatedData) {
      return relatedData.reduce((acc, disease) => {
        // 添加病害节点
        acc.push([`disease_${disease.diseaseId}`]);

        // 添加模型节点
        disease.modelList.forEach(model => {
          acc.push([`disease_${disease.diseaseId}`, `disease_${disease.diseaseId}_model_${model.modelId}`]);
        });

        return acc;
      }, []);
    },
    handleChange(value) {
      // 过滤掉被禁用的选项（防止通过API强制修改）
      const enabledOptions = value.filter(optionPath => {
        const lastId = optionPath[optionPath.length - 1];
        return !this.relatedOptions.includes(lastId);
      });

      // 合并已禁用的选项（保持它们始终被选中）
      this.selectedOptions = [...this.generateSelectedOptionsFromDisabled(), ...enabledOptions];
      this.$emit('input', this.selectedOptions);
    },

    // 从禁用选项中生成选中项（保持禁用项始终被选中）
    generateSelectedOptionsFromDisabled() {
      return this.relatedOptions.reduce((acc, optionId) => {
        if (optionId.includes('_model_')) {
          // 模型节点路径 [病害ID, 模型ID]
          const [diseasePart, modelPart] = optionId.split('_model_');
          const diseaseId = diseasePart.replace('disease_', '');
          acc.push([`disease_${diseaseId}`, optionId]);
        } else {
          // 病害节点路径 [病害ID]
          acc.push([optionId]);
        }
        return acc;
      }, []);
    }
  }
};
</script>

<style scoped>
.cascade-disease-model {
  margin-bottom: 20px;
}
</style>
