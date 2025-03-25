<template>
  <div class="app-container">
    <!-- 搜索条件 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="工单ID" prop="workOrderId">
        <el-input
          v-model="queryParams.workOrderId"
          placeholder="请输入工单ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备ID" prop="deviceId">
        <el-input
          v-model="queryParams.deviceId"
          placeholder="请输入设备ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="检修人员" prop="repairPerson">
        <el-input
          v-model="queryParams.repairPerson"
          placeholder="请输入检修人员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建日期">
        <el-date-picker
          v-model="daterangeCreateDate"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['device:orders:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['device:orders:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['device:orders:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['device:orders:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <!-- Tab 切换 -->
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="进行中" name="pending">
        <el-table
          v-loading="loading"
          :data="pendingOrdersList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="工单ID" align="center" prop="workOrderId" />
          <el-table-column label="设备ID" align="center" prop="deviceId" />
          <el-table-column label="检修人员" align="center" prop="repairPerson" />
          <el-table-column label="检修地点" align="center" prop="repairLocation" />
          <el-table-column label="创建日期" align="center" prop="createDate" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createDate, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="检修内容" align="center" prop="repairContent" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-info"
                @click="handleInfo(scope.row)"
                v-hasPermi="['device:orders:edit']"
              >详情</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['device:orders:edit']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['device:orders:remove']"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="已完成" name="completed">
        <el-table
          v-loading="loading"
          :data="completedOrdersList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="工单ID" align="center" prop="workOrderId" />
          <el-table-column label="设备ID" align="center" prop="deviceId" />
          <el-table-column label="检修人员" align="center" prop="repairPerson" />
          <el-table-column label="检修地点" align="center" prop="repairLocation" />
          <el-table-column label="创建日期" align="center" prop="createDate" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createDate, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="完成日期" align="center" prop="repairCompletionDate" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.repairCompletionDate, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="检修内容" align="center" prop="repairContent" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-info"
                @click="handleInfo(scope.row)"
                v-hasPermi="['device:orders:edit']"
              >详情</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['device:orders:edit']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['device:orders:remove']"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加/修改工单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工单ID" prop="workOrderId">
          <el-input v-model="form.workOrderId" placeholder="请输入工单ID" />
        </el-form-item>
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="检修人员" prop="repairPerson">
          <el-input v-model="form.repairPerson" placeholder="请输入检修人员" />
        </el-form-item>
        <el-form-item label="检修地点" prop="repairLocation">
          <el-input v-model="form.repairLocation" placeholder="请输入检修地点" />
        </el-form-item>
        <el-form-item label="创建日期" prop="createDate">
          <el-date-picker
            v-model="form.createDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择创建日期"
          />
        </el-form-item>
        <el-form-item label="完成日期" prop="repairCompletionDate">
          <el-date-picker
            v-model="form.repairCompletionDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择完成日期"
          />
        </el-form-item>
        <el-form-item label="检修内容" prop="repairContent">
          <el-input
            v-model="form.repairContent"
            type="textarea"
            placeholder="请输入检修内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { listOrders, getOrders, delOrders, addOrders, updateOrders } from "@/api/device/orders";

export default {
  name: "Orders",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 当前激活的 Tab
      activeTab: "pending",
      // 进行中的工单列表
      pendingOrdersList: [],
      // 已完成的工单列表
      completedOrdersList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 创建日期范围
      daterangeCreateDate: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        workOrderId: null,
        deviceId: null,
        repairPerson: null,
        createDate: null,
        repairStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        workOrderId: [
          { required: true, message: "工单ID不能为空", trigger: "blur" }
        ],
        deviceId: [
          { required: true, message: "设备ID不能为空", trigger: "blur" }
        ],
        repairPerson: [
          { required: true, message: "检修人员不能为空", trigger: "blur" }
        ],
        repairLocation: [
          { required: true, message: "检修地点不能为空", trigger: "blur" }
        ],
        createDate: [
          { required: true, message: "创建日期不能为空", trigger: "blur" }
        ],
        repairContent: [
          { required: true, message: "检修内容不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询工单列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      console.log(this.activeTab);
      this.queryParams.repairStatus =  this.activeTab === 'pending' ? '进行中' : '已完成';
      if (null != this.daterangeCreateDate && '' != this.daterangeCreateDate) {
        this.queryParams.params["beginCreateDate"] = this.daterangeCreateDate[0];
        this.queryParams.params["endCreateDate"] = this.daterangeCreateDate[1];
      }
      listOrders(this.queryParams).then(response => {
        if (this.activeTab === "pending") {
          this.pendingOrdersList = response.rows;
        } else {
          this.completedOrdersList = response.rows;
        }
        // this.pendingOrdersList = response.rows.filter(item => item.repairStatus === "进行中");
        // this.completedOrdersList = response.rows.filter(item => item.repairStatus === "已完成");
        this.total = response.total;
        this.loading = false;
      });
    },
    /** Tab 切换事件 */
    handleTabClick(tab) {
      console.log(tab)
      this.activeTab = tab.name;
      this.getList();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateDate = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加工单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getOrders(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改工单";
      });
    },
    /**
     * 详情按钮操作
     */
    handleInfo (row) {
      this.$router.push({
        path: "/system/works",
        query: {
          id: row.id,
          workOrderId: row.workOrderId,
          repairPerson: row.repairPerson,
          createDate: row.createDate,
          repairStatus: row.repairStatus,
          deviceId: row.deviceId,
          repairLocation: row.repairLocation,
          repairContent: row.repairContent
        }
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            if (this.activeTab === "pending" && this.form.repairCompletionDate !== null) {
              this.form.repairStatus = '已完成'
            }
            updateOrders(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOrders(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除工单编号为"' + ids + '"的数据项？').then(() => {
        return delOrders(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('device/orders/export', {
        ...this.queryParams
      }, `orders_${new Date().getTime()}.xlsx`);
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: null,
        workOrderId: null,
        deviceId: null,
        repairPerson: null,
        repairLocation: null,
        createDate: null,
        repairContent: null,
        repairCompletionDate: null,
        repairStatus: null
      };
      this.resetForm("form");
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    }
  }
};
</script>
<style scoped>
.app-container {
  padding: 20px;
}
</style>
