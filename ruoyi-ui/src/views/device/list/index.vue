<template>
  <div>
    <div class="model_title">
      <span>设备运行状态监测</span>
    </div>
    <div class="content-container">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <div class="model_form">
          <el-form-item label="设备ID" style="margin-left: 20px">
            <el-input v-model="formInline.ID" placeholder="请输入设备ID" size="small" style="width: 280px; margin-right: 20px"></el-input>
          </el-form-item>
          <el-form-item label="素材名">
            <el-input v-model="formInline.ID" placeholder="请输入素材名" size="small" style="width: 200px; margin-right: 20px"></el-input>
          </el-form-item>
          <el-form-item label="推送标题">
            <el-input v-model="formInline.ID" placeholder="请输入推送标题" size="small" style="width: 200px; margin-right: 20px"></el-input>
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
        <el-table :data="deviceList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
          <el-table-column prop="deviceId" label="设备ID" width="120" align="center"></el-table-column>
          <el-table-column prop="address" label="地址"></el-table-column>
          <el-table-column prop="department" label="部门" width="180"></el-table-column>
          <el-table-column prop="responsiblePerson" label="负责人"></el-table-column>
          <el-table-column prop="alarmPhone" label="告警手机号" width="120"></el-table-column>
          <el-table-column prop="coordinates" label="经纬度" width="150"></el-table-column>
          <el-table-column prop="updateTime" label="修改时间" width="155"></el-table-column>
          <el-table-column prop="warrantyTime" label="质保时间" width="105"></el-table-column>
          <el-table-column label="状态">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === '运行中' ? 'success' : 'danger'">
                {{scope.row.status}}
              </el-tag>
            </template>
          </el-table-column>

          <!-- 操作列 -->
          <el-table-column label="操作" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" @click="openEditDialog(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 编辑弹窗 -->
        <el-dialog title="设备信息编辑" :visible.sync="dialogVisible" width="600px">
          <el-form :model="editForm" label-width="100px">
            <el-form-item label="设备ID">
              <el-input v-model="editForm.deviceId" disabled></el-input>
            </el-form-item>

            <el-form-item label="地址">
              <el-input v-model="editForm.address"></el-input>
            </el-form-item>

            <el-form-item label="部门">
              <el-input v-model="editForm.department"></el-input>
            </el-form-item>

            <el-form-item label="负责人">
              <el-input v-model="editForm.responsiblePerson"></el-input>
            </el-form-item>

            <el-form-item label="告警手机号" prop="alertPhone">
              <el-input v-model="editForm.alarmPhone"
                        placeholder="请输入11位手机号"
                        maxlength="11"></el-input>
            </el-form-item>

            <el-form-item label="经纬度">
              <el-input v-model="editForm.coordinates"
                        placeholder="格式：纬度,经度"></el-input>
            </el-form-item>

            <el-form-item label="状态">
              <el-select v-model="editForm.status">
                <el-option label="运行中" value="运行中"></el-option>
                <el-option label="已停止" value="已停止"></el-option>
                <el-option label="维护中" value="维护中"></el-option>
              </el-select>
            </el-form-item>
          </el-form>

          <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </span>
        </el-dialog>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="10"
          :small="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="deviceList.length"
          style="float:right;">
        </el-pagination>
      </el-card>
    </div>
  </div>

</template>

<script>
// import { listDevice } from "@/api/device/device";

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
      dialogVisible: false,
      editForm: {}, // 当前编辑的数据副本
      deviceList: [
        {
          deviceId: 'BZYDN0001',
          address: '长武县',
          department: '长武县农业技术推广中心',
          principal: '李明',
          alertPhone: '13812345678',
          coordinates: '34.2665,108.9607',
          updateTime: '2022-06-30 15:22',
          warrantyTime: '2022-06-30 15:22',
          status: '运行中'
        }
      ]
    };
  },
  // async mounted() {
  //   const res = await listDevice();
  //   this.deviceList = res.rows;
  // },
  methods: {
    handleSizeChange(newSize) {
      console.log(`每页 ${newSize} 条`, newSize);
    },
    handleCurrentChange(newPage) {
      console.log(`当前页码 ${newPage}`, newPage);
    },
    openEditDialog(row) {
      this.editForm = JSON.parse(JSON.stringify(row)) // 深拷贝数据
      this.dialogVisible = true
    },

    // 保存修改
    saveEdit() {
      const index = this.deviceList.findIndex(
        item => item.deviceId === this.editForm.deviceId
      )
      if (index !== -1) {
        // 更新数据（实际开发中此处应调用API）
        this.$set(this.deviceList, index, this.editForm)
        this.dialogVisible = false
        this.$message.success('修改成功！')
      }
    }
  },
  computed: {
    filteredData() {
      // 这里可以添加过滤数据的逻辑，例如根据页码和每页大小返回数据子集
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.deviceList.slice(start, end);
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
