<template>
  <div>
    <div class="model_title">
      <span>病害预测总览</span>
    </div>
    <div class="content-container">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <div class="model_form">
          <el-form-item label="设备ID" style="margin-left: 20px;">
            <el-input v-model="formInline.ID" placeholder="请输入设备ID" size="small" style="width: 180px"></el-input>
          </el-form-item>
          <el-form-item label="素材名">
            <el-input v-model="formInline.content" placeholder="请输入素材名" size="small" style="width: 180px"></el-input>
          </el-form-item>
          <el-form-item label="推送标题">
            <el-input v-model="formInline.title" placeholder="请输入推送标题" size="small" style="width: 180px"></el-input>
          </el-form-item>
          <el-form-item label="病害种类">
            <el-select v-model="formInline.type" placeholder="请选择" size="small" style="width: 180px">
              <el-option label="条锈病" value="shanghai"></el-option>
              <el-option label="赤霉病" value="beijing"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="年份">
            <el-select v-model="formInline.year" placeholder="请选择" size="small" style="width: 180px">
              <el-option label="2024" value="shanghai"></el-option>
              <el-option label="2025" value="beijing"></el-option>
            </el-select>
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
        <el-table :data="deviceData" stripe style="width: 100%" v-loading="loading">
          <el-table-column prop="deviceId" label="设备ID" width="140" align="center"></el-table-column>
          <el-table-column prop="diseaseName" label="病害类型" width="120"></el-table-column>
          <el-table-column prop="predictedValueDescription" label="预测结果描述" align="center"></el-table-column>
          <el-table-column prop="selectedModelId" label="选中模型 ID" width="80" align="center"></el-table-column>

          <el-table-column label="预测信息" align="center">
            <el-table-column prop="predictionDataTime" label="预测日期" width="160"></el-table-column>
            <el-table-column prop="predictedValue" label="预测值" width="80">
              <template slot-scope="scope">
                <el-tag type="warning">{{ scope.row.predictedValue }}</el-tag>
              </template>
            </el-table-column>
          </el-table-column>

          <el-table-column label="验证信息" align="center">
            <el-table-column prop="verifyDate" label="验证日期" width="160"></el-table-column>
            <el-table-column prop="actualValue" label="实验值" width="80">
              <template slot-scope="scope">
                <el-tag :type="getValueType(scope.row)">{{ scope.row.actualValue || '暂无' }}</el-tag>
              </template>
            </el-table-column>
          </el-table-column>

          <el-table-column label="偏差率" width="100" align="center">
            <template slot-scope="scope">
              {{ calculateDeviation(scope.row) }}
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
import { listDevice } from '@/api/device/device';
import { listDisease } from "@/api/disease/disease";
<script>
import {listDisease} from "@/api/disease/disease";
import {listDevice} from "@/api/device/device";

export default {
  data() {
    return {
      loading: false,
      currentPage: 1,
      pageSize: 10,
      deviceData: [
        {
          index: 1,
          deviceId: 'ZVDN0001',
          diseaseType: '小麦赤霉病',
          year: 2024,
          predictDate: '2024/12/10 09:00',
          predictValue: '22.50%',
          verifyDate: '2024/12/10 10:00',
          actualValue: '25.00%'
        },
        // 添加其他9条数据...
      ],
      formInline: {
        ID: '',
        content: '',
        title: '',
        type: '',
        year: ''
      }
    }
  },
  async mounted() {
    this.loading = true;
    const res = await listDisease();
    const device = await listDevice();
    this.deviceData = res.rows;
    this.deviceData = this.deviceData.map(item => {
      for (let i = 0; i < device.rows.length; i++) {
        if (device.rows[i].id === item.id) {
          item.province = device.rows[i].province;
          item.city = device.rows[i].city;
          item.county = device.rows[i].district;
          break;
        }
      }
      return item;
    })
    this.loading = false;
  },
  methods: {
    calculateDeviation(row) {
      const predict = parseFloat(row.predictedValue);
      const actual = parseFloat(row.actualValue);
      if (!actual) {
        return '暂无'
      }
      return ((actual - predict) / predict * 100).toFixed(1) + '%';
    },
    getValueType(row) {
      const predict = parseFloat(row.predictValue);
      const actual = parseFloat(row.actualValue);
      return actual >= predict ? 'success' : 'danger';
    },
    onSubmit() {
      console.log('submit!');
    },
    handleSizeChange(newSize) {
      console.log(`每页 ${newSize} 条`, newSize);
    },
    handleCurrentChange(newPage) {
      console.log(`当前页码 ${newPage}`, newPage);
    },
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
