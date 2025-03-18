<template>
  <div>
    <div class="model_title">
      <span>设备运行状态监测</span>
    </div>
    <div class="content-container">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <div class="model_form">
          <el-form-item label="模型ID" style="margin-left: 20px;">
            <el-input v-model="formInline.ID" placeholder="请输入模型ID" size="small" style="width: 280px; margin-right: 20px"></el-input>
          </el-form-item>
          <el-form-item label="开始时间" prop="date">
            <el-date-picker
              v-model="formInline.startDate"
              type="date"
              placeholder="请选择开始时间"
              style="width: 280px;"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间" prop="date">
            <el-date-picker
              v-model="formInline.endDate"
              type="date"
              placeholder="请选择结束时间"
              style="width: 280px;"
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
        <el-table
          :data="modelData"
          stripe
          style="width: 100%"
          v-loading="loading"
          border
        >
          <!-- 序号列 -->
          <el-table-column
            type="index"
            label="序号"
            width="80"
            align="center"
          ></el-table-column>

          <!-- 基础信息列 -->
          <el-table-column prop="id" label="模型ID" width="120" align="center"></el-table-column>
          <el-table-column prop="name" label="模型名称" width="150"></el-table-column>
          <el-table-column prop="formulaDefinition" label="公式定义"></el-table-column>

          <!-- 时间范围（嵌套表头） -->
          <el-table-column label="计算时间" align="center">
            <el-table-column
              prop="startTime"
              label="开始时间"
              width="160"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="endTime"
              label="结束时间"
              width="160"
              align="center"
            ></el-table-column>
          </el-table-column>

          <!-- 误差范围 -->
          <el-table-column label="误差范围" width="120" align="center">
            <template slot-scope="scope">
              <el-tag type="info">{{ `${scope.row.errorRange}` }}</el-tag>
            </template>
          </el-table-column>

          <!-- 设备列表（多标签展示） -->
          <el-table-column label="设备列表">
            <template slot-scope="scope">
              <el-tag
                v-for="device in scope.row.deviceList"
                :key="device"
                style="margin-right: 4px"
              >{{ device }}</el-tag>
            </template>
          </el-table-column>

          <!-- 操作列 -->
          <el-table-column label="操作" width="150" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                @click="handleEdit(scope.row)"
              >编辑</el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.row)"
              >删除</el-button>
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
          :total="modelData.length"
          style="float:right;">
        </el-pagination>
      </el-card>
    </div>
  </div>

</template>

<script>
import { listModelData } from "@/api/model";

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
      loading: false,
      modelData: [
        {
          modelId: 'M001',
          modelName: '温湿度模型',
          formula: 'T = (a * X) + b',
          startTime: '2024/12/17 8:00',
          endTime: '2024/12/17 9:00',
          errorRange: 2.0,
          deviceList: ['BZYDN0001', 'BZYDN0002', 'BZYDN0003']
        }
        // 可添加更多测试数据
      ]
    };
  },
  async mounted() {
    const res = await listModelData();
    console.log(res);
    this.modelData = res.rows;
  },
  methods: {
    handleSizeChange(newSize) {
      console.log(`每页 ${newSize} 条`, newSize);
    },
    handleCurrentChange(newPage) {
      console.log(`当前页码 ${newPage}`, newPage);
    },
    handleEdit(row) {
      console.log('编辑操作', row)
    },
    handleDelete(row) {
      console.log('删除操作', row)
    }
  },
  computed: {
    filteredData() {
      // 这里可以添加过滤数据的逻辑，例如根据页码和每页大小返回数据子集
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.modelData.slice(start, end);
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
