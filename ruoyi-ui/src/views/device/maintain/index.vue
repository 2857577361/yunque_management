<template>
  <div>
    <div class="model_title">
      <span>设备运行状态监测</span>
    </div>
    <div class="content-container">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <div class="model_form">
          <el-form-item label="设备ID" style="margin-left: 20px">
            <el-input v-model="formInline.ID" placeholder="请输入设备ID" size="small" style="width: 180px; "></el-input>
          </el-form-item>
          <el-form-item label="负责人">
            <el-input v-model="formInline.ID" placeholder="请输入负责人" size="small" style="width: 180px;"></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="city">
            <el-select v-model="formInline.ID" placeholder="请选择状态">
              <el-option
                key="value"
                label="online"
                value="online"
              ></el-option>
              <el-option
                key="value2"
                label="offline"
                value="offline"
              ></el-option>
            </el-select>
            </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="formInline.ID" placeholder="请输入地址" size="small" style="width: 180px;"></el-input>
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
          :data="deviceList"
          border
          stripe
          style="width: 100%"
        >
          <!-- 工单序号 -->
          <el-table-column
            type="index"
            label="工单序号"
            width="100"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="id"
            label="工单编号"
            align="center"
          ></el-table-column>
          <!-- 设备编号 -->
          <el-table-column
            prop="deviceId"
            label="设备编号"
            align="center"
          ></el-table-column>

          <!-- 检修人员 -->
          <el-table-column
            prop="repairPerson"
            label="检修人员"
          ></el-table-column>

          <!-- 检修地点 -->
          <el-table-column
            prop="repairLocation"
            label="检修地点"
          ></el-table-column>

          <!-- 创建日期 -->
          <el-table-column
            prop="createTime"
            label="创建日期"
            align="center"
          >
            <template slot-scope="scope">
              {{ scope.row.createDate }}
            </template>
          </el-table-column>

          <!-- 质保时间 -->
          <el-table-column
            prop="warrantyTime"
            label="质保时间"
            align="center"
          >
            <template slot-scope="scope">
              {{ scope.row.warrantyTime }}
            </template>
          </el-table-column>

          <!-- 检修内容 -->
          <el-table-column
            prop="repairContent"
            label="检修内容"
          ></el-table-column>

          <!-- 操作列 -->
          <el-table-column
            label="操作"
            width="150"
            align="center"
            fixed="right"
          >
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
              <el-input v-model="editForm.principal"></el-input>
            </el-form-item>

            <el-form-item label="告警手机号" prop="alertPhone">
              <el-input v-model="editForm.alertPhone"
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
// import dayjs from 'dayjs'
// import { listOrders, delOrders } from '@/api/device/orders'

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
          maintainer: '李明',
          location: '长武县植保站',
          createTime: '2022-06-30 15:22',
          warrantyTime: '2022-06-30 15:22',
          content: '更换电池'
        }
        // 可添加更多工单数据
      ]
    }
  },
  // async mounted() {
  //   const res = await listOrders()
  //   this.deviceList = res.rows
  // },
  methods: {
    // 时间格式化
    // formatTime(time) {
    //   return dayjs(time).format('YYYY-MM-DD HH:mm')
    // },
    // 编辑操作
    handleEdit(row) {
      console.log('编辑数据:', row)
    },
    // 删除操作
    handleDelete(row) {
      console.log('删除数据:', row)
      // delOrders(row.id)
    },
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
