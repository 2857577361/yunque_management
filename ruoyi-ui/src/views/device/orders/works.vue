<template>
  <div class="app-container">
    <!-- 工单基本信息 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>工单信息</span>
      </div>
      <el-form :model="orderInfo" label-width="100px">
        <el-form-item label="工单ID">
          <el-input v-model="orderInfo.workOrderId" :disabled="true" />
        </el-form-item>
        <el-form-item label="设备ID">
          <el-input v-model="orderInfo.deviceId" :disabled="true" />
        </el-form-item>
        <el-form-item label="创建日期">
          <el-input v-model="orderInfo.createDate" :disabled="true" />
        </el-form-item>
        <el-form-item label="故障描述">
          <el-input v-model="orderInfo.repairContent" :disabled="true" />
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 故障上报与维修信息 -->
    <el-card class="box-card" style="margin-top: 20px;">
      <div slot="header" class="clearfix">
        <span>维修信息</span>
      </div>
      <el-form :model="repairForm" :rules="rules" ref="repairForm" label-width="100px">
        <el-form-item label="故障类型" prop="faultType">
          <el-select v-model="repairForm.faultType" placeholder="请选择故障类型" :disabled="isCompleted">
            <el-option label="硬件故障" value="硬件故障" />
            <el-option label="软件故障" value="软件故障" />
            <el-option label="网络故障" value="网络故障" />
            <el-option label="其他故障" value="其他故障" />
          </el-select>
        </el-form-item>
        <el-form-item label="维修方式" prop="repairMethod">
          <el-select v-model="repairForm.repairMethod" placeholder="请选择维修方式" :disabled="isCompleted">
            <el-option label="现场维修" value="现场维修" />
            <el-option label="远程维修" value="远程维修" />
            <el-option label="更换设备" value="更换设备" />
          </el-select>
        </el-form-item>
        <el-form-item label="维修备注" prop="repairNote">
          <el-input
            v-model="repairForm.repairNote"
            type="textarea"
            placeholder="请输入维修备注"
            :disabled="isCompleted"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitRepairInfo" :disabled="isCompleted">提交维修信息</el-button>
          <el-button type="success" @click="completeRepair" :disabled="isCompleted">完成维修</el-button>
          <el-button type="success" @click="handleBack">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
// import { getOrderDetail, updateOrderRepairInfo, completeOrderRepair } from "@/api/device/orders";
import { upDateOrdersRepairDetail, listOrdersDetail, updateOrders } from "@/api/device/orders";

export default {
  name: "OrderRepair",
  data() {
    return {
      // 工单基本信息
      orderInfo: {
        workOrderId: "",
        deviceId: "",
        createDate: "",
        repairContent: "",
      },
      // 维修信息表单
      repairForm: {
        faultType: "",
        repairMethod: "",
        repairNote: "",
      },
      // 表单校验规则
      rules: {
        faultType: [
          { required: true, message: "请选择故障类型", trigger: "change" }
        ],
        repairMethod: [
          { required: true, message: "请选择维修方式", trigger: "change" }
        ],
        repairNote: [
          { required: true, message: "请输入维修备注", trigger: "blur" }
        ]
      },
      // 是否已完成维修
      isCompleted: false
    };
  },
  created() {
    if (this.$route.query.repairStatus === '已完成') {
      this.isCompleted = true;
      this.getOrderDetail();
    }
  },
  mounted() {
    this.orderInfo = this.$route.query;
    console.log(this.orderInfo);
  },
  methods: {
    /** 获取工单详情 */
    getOrderDetail() {
      const id = this.$route.query.id; // 从路由中获取工单ID
      listOrdersDetail(id).then(response => {
        this.repairForm = { ...response.data };
      });
    },
    /** 提交维修信息 */
    submitRepairInfo() {
      this.$refs.repairForm.validate(valid => {
        if (valid) {
          const id = this.$route.query.id;
          upDateOrdersRepairDetail({
            id,
            ...this.repairForm
          }).then(() => {
            this.$message.success("维修信息提交成功");
          });
        }
      });
    },
    /** 完成维修 */
    completeRepair() {
      const id = this.$route.query.id;
      const form = {
        id,
        repairStatus: '已完成',
        repairCompletionDate: Date.now(),
      }
      updateOrders(form).then(() => {
        this.$message.success("维修完成");
        this.isCompleted = true;
        this.$router.push({ path: "/tool/orders" });
      });
    },
    handleBack() {
      this.$router.push({ path: "/tool/orders" });
    }
  }
};
</script>
<style scoped>
.app-container {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.clearfix {
  font-size: 18px;
  font-weight: bold;
}
</style>
