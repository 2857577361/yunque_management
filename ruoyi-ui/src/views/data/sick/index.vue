<template>
  <div>
    <div class="model_title">
      <span>设备运行状态监测</span>
    </div>
    <div class="content-container">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <div class="model_form">
          <el-form-item label="设备ID" style="margin-left: 20px">
            <el-input v-model="formInline.ID" placeholder="请输入设备ID" size="small" style="width: 280px;"></el-input>
          </el-form-item>
          <el-form-item label="素材名">
            <el-input v-model="formInline.ID" placeholder="请输入素材名" size="small" style="width: 280px;"></el-input>
          </el-form-item>
          <el-form-item label="推送标题">
            <el-input v-model="formInline.ID" placeholder="请输入推送标题" size="small" style="width: 280px;"></el-input>
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
        <el-table :data="deviceData" border style="width: 100%" v-loading="loading">
          <!-- 序号 -->
          <el-table-column type="index" label="序号" width="80" align="center"></el-table-column>

          <!-- 设备号（不可编辑） -->
          <el-table-column prop="deviceId" label="设备号" align="center"></el-table-column>

          <!-- 省（可编辑输入） -->
          <el-table-column label="省">
            <template slot-scope="{ row }">
              <el-input
                v-if="row.isEditing"
                v-model="row.province"
                size="mini"
              ></el-input>
              <span v-else>{{ row.province }}</span>
            </template>
          </el-table-column>

          <!-- 市（可编辑输入） -->
          <el-table-column label="市">
            <template slot-scope="{ row }">
              <el-input
                v-if="row.isEditing"
                v-model="row.city"
                size="mini"
              ></el-input>
              <span v-else>{{ row.city }}</span>
            </template>
          </el-table-column>

          <!-- 县（可编辑输入） -->
          <el-table-column label="县">
            <template slot-scope="{ row }">
              <el-input
                v-if="row.isEditing"
                v-model="row.county"
                size="mini"
              ></el-input>
              <span v-else>{{ row.county }}</span>
            </template>
          </el-table-column>
          <el-table-column label="病害名称">
            <template slot-scope="{ row }">
              <el-input
                v-if="row.isEditing"
                v-model="row.diseaseName"
                size="mini"
              ></el-input>
              <span v-else>{{ row.diseaseName }}</span>
            </template>
          </el-table-column>

          <!-- 今日状态（可编辑下拉选择） -->
          <el-table-column label="今日" width="140">
            <template slot-scope="{ row }">
              <el-select
                v-if="row.isEditing"
                v-model="row.status"
                size="mini"
                placeholder="请选择"
              >
                <el-option label="非预测期" value="非预测期"></el-option>
                <el-option label="预测中" value="预测中"></el-option>
                <el-option label="已完成" value="已完成"></el-option>
              </el-select>
              <el-tag v-else :type="getStatusTagType(row.status)">
                {{ row.isPredictionPeriod === 0 ? '非预测期' : '预测期' }}
              </el-tag>
            </template>
          </el-table-column>

          <!-- 操作列（编辑/保存切换） -->
          <el-table-column label="操作" width="150" align="center">
            <template slot-scope="{ row }">
              <el-button
                v-if="!row.isEditing"
                type="primary"
                size="mini"
                @click="startEdit(row)"
              >编辑</el-button>

              <div v-else>
                <el-button
                  size="mini"
                  @click="saveEdit(row)"
                >保存</el-button>
                <el-button
                  size="mini"
                  @click="cancelEdit(row)"
                >取消</el-button>
              </div>
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
// import { listDevice } from '@/api/device/device';
// import { listDisease } from "@/api/disease/disease";
export default {
  data() {
    return {
      loading: false,
      formInline: {
        ID: '',
        startDate: '',
        endDate: '',
      },
      currentPage: 1,
      pageSize: 10,
      deviceData: []
    };
  },
  // async mounted() {
  //   this.loading = true;
  //   const res = await listDisease();
  //   const device = await listDevice();
  //   // this.deviceData = device.data;
  //   console.log(device);
  //   this.deviceData = res.rows;
  //   this.deviceData = this.deviceData.map(item => {
  //       for (let i = 0 ; i < device.rows.length; i++) {
  //         if (device.rows[i].id === item.id) {
  //           item.province = device.rows[i].province;
  //           item.city = device.rows[i].city;
  //           item.county = device.rows[i].district;
  //           break;
  //         }
  //       }
  //       return item;
  //     })
  //   this.loading = false;
  //   console.log(this.deviceData);
  // },
  methods: {
    handleSizeChange(newSize) {
      console.log(`每页 ${newSize} 条`, newSize);
    },
    handleCurrentChange(newPage) {
      console.log(`当前页码 ${newPage}`, newPage);
    },
    startEdit(row) {
      row.originalData = { ...row }; // 深拷贝原始数据
      row.isEditing = true;
    },

    // 保存修改
    saveEdit(row) {
      row.isEditing = false;
      // 此处可调用API保存数据
      console.log('保存数据:', row);
    },

    // 取消修改
    cancelEdit(row) {
      Object.assign(row, row.originalData); // 恢复原始数据
      row.isEditing = false;
    },

    // 状态标签样式
    getStatusTagType(status) {
      const map = {
        '非预测期': 'info',
        '预测中': 'warning',
        '已完成': 'success'
      };
      return map[status] || '';
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
