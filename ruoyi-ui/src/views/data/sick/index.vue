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

          <el-table-column label="省">
            <template slot-scope="{ row }">
              <span>{{ row.province }}</span>
            </template>
          </el-table-column>

          <el-table-column label="市">
            <template slot-scope="{ row }">
              <span>{{ row.city }}</span>
            </template>
          </el-table-column>

          <el-table-column
            label="县">
            <template slot-scope="{ row }">
              <span>{{ row.district }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="监测面积(亩)">
            <template slot-scope="{ row }">
              <span>{{ row.area }}</span>
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
              <el-tag v-else :type="getStatusTagType(row.isPredictionPeriod)">
                {{ row.isPredictionPeriod ? '预测期' : '非预测期' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total>0"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
          :total="total"
          style="float:right;">
        </pagination>
      </el-card>
    </div>
  </div>

</template>

<script>
import { listDevice } from '@/api/device/device';
import { listDisease } from "@/api/disease/disease";
export default {
  data() {
    return {
      loading: false,
      formInline: {
        ID: '',
        startDate: '',
        endDate: '',
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      deviceData: [],
      total: 0,
    };
  },
  async mounted() {
    await this.getList();
  },
  methods: {
    onSubmit() {

    },
    async getList() {
      this.loading = true;
      const res = await listDisease(this.queryParams);
      const device = await listDevice({
        pageSize: 100
      });
      this.deviceData = res.rows;
      this.total = res.total;
      this.deviceData = this.deviceData.map(item => {
        for (let i = 0; i < device.rows.length; i++) {
          if (device.rows[i].deviceId === item.deviceId) {
            item.province = device.rows[i].province;
            item.city = device.rows[i].city;
            item.district = device.rows[i].district;
            break;
          }
        }
        return item;
      })
      this.loading = false;
    },
    // 状态标签样式
    getStatusTagType(status) {
      status = status ? '预测期' : '非预测期';
      const map = {
        '非预测期': 'info',
        '预测中': 'warning',
        '已完成': 'success'
      };
      return map[status] || '';
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

.el-tag {
  font-size: 14px;
  padding: 0 8px;
}
</style>
