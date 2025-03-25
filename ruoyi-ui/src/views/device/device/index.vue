<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备ID" prop="deviceId">
        <el-input
          v-model="queryParams.deviceId"
          placeholder="请输入设备ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="负责人" prop="responsiblePerson">
        <el-input
          v-model="queryParams.responsiblePerson"
          placeholder="请输入负责人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="质保时间">
        <el-date-picker
          v-model="daterangeWarrantyTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['device:device:add']"
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
          v-hasPermi="['device:device:edit']"
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
          v-hasPermi="['device:device:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['device:device:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deviceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" />
      <el-table-column label="设备ID" align="center" prop="deviceId" />
      <el-table-column label="详细地址" align="center" prop="address" />
      <el-table-column label="部门" align="center" prop="department" />
      <el-table-column label="负责人" align="center" prop="responsiblePerson" />
      <el-table-column label="告警手机号" align="center" prop="alarmPhone" />
      <el-table-column label="经纬度" align="center" prop="coordinates" />
      <el-table-column label="质保时间" align="center" prop="warrantyTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.warrantyTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="状态" align="center" prop="status" />-->
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '运行中' ? 'success' : 'danger'" style="border-radius: 50%; margin-right: 5px">
          {{scope.row.status}}
        </el-tag>
          <el-switch
            v-model="scope.row.status"
            active-value="运行中"
            inactive-value="已停用"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['device:device:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['device:device:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改设备列表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-input v-model="form.department" placeholder="请输入部门" />
        </el-form-item>
        <el-form-item label="负责人" prop="responsiblePerson">
          <el-input v-model="form.responsiblePerson" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="告警手机号" prop="alarmPhone">
          <el-input v-model="form.alarmPhone" placeholder="请输入告警手机号" />
        </el-form-item>
        <el-form-item label="经纬度" prop="coordinates">
          <el-input v-model="form.coordinates" placeholder="请输入经纬度" />
        </el-form-item>
        <el-form-item label="质保时间" prop="warrantyTime">
          <el-date-picker clearable
            v-model="form.warrantyTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择质保时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="国别选择" prop="count" v-show="isAdd">
          <el-select
            v-model="form.country"
            placeholder="请选择国家类型"
            class="custom-select"
            @change="handleCountryChange"
          >
            <el-option
              v-for="item in countryOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <!-- 动态区域选择 -->
        <template v-if="form.country === 'CN' && isAdd">
          <el-form-item label="设备位置" prop="region">
            <el-cascader
              v-model="form.region"
              :options="regionOptions"
              placeholder="省/市/区三级选择"
              class="custom-cascader"
              clearable
            />
          </el-form-item>
        </template>
        <template v-else-if="form.country === 'FRN'">
          <el-form-item label="外国地址" prop="foreignAddress">
            <el-input
              v-model="form.foreignAddress"
              placeholder="请输入国家/城市/街道信息"
              class="custom-input"
              clearable
            />
          </el-form-item>
        </template>
        <el-form-item label="拥有者类型" prop="deptType">
          <el-select v-model="form.deptType" placeholder="请选择拥有者类型" @change="handleOwnerTypeChange">
            <el-option label="私有企业" value="private"></el-option>
            <el-option label="国有地区管理" value="state"></el-option>
          </el-select>
        </el-form-item>

        <!-- 私有企业的动态表单 -->
        <div v-if="form.deptType === 'private'">
          <el-form-item label="拥有者名称" prop="deptName">
            <el-select v-model="form.deptName" placeholder="请选择拥有者名称">
              <el-option v-for="(item,index) in deptOptions" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否公开" prop="isOpened">
            <el-select v-model="form.isOpened" placeholder="请选择是否公开">
              <el-option label="是" value="true"></el-option>
              <el-option label="否" value="false"></el-option>
            </el-select>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDevice, getDevice, delDevice, addDevice, updateDevice, listCity, listDept, listDeviceByRole } from "@/api/device/device";
import { getOwner } from "@/api/device/owner";

export default {
  name: "Device",
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
      // 设备列表表格数据
      deviceList: [],
      deptOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      isAdd: true,
      // 是否公开时间范围
      daterangeWarrantyTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceId: null,
        responsiblePerson: null,
        warrantyTime: null,
        status: null,
      },
      countryOptions: [
        { label: '中国', value: 'CN' },
        { label: '外国', value: 'FRN' }
      ],
      regionOptions: [],
      user: {
        deptName: '',
        roleName: ''
      },
      form: {},
      // 表单参数
      // form: {},
      // 表单校验
      rules: {
        deviceId: [
          { required: true, message: "设备ID不能为空", trigger: "blur" }
        ],
        address: [
          { required: true, message: "详细地址不能为空", trigger: "blur" }
        ],
        department: [
          { required: true, message: "部门不能为空", trigger: "blur" }
        ],
        responsiblePerson: [
          { required: true, message: "负责人不能为空", trigger: "blur" }
        ],
        alarmPhone: [
          { required: true, message: "告警手机号不能为空", trigger: "blur" }
        ],
        coordinates: [
          { required: true, message: "经纬度不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "修改时间不能为空", trigger: "blur" }
        ],
        warrantyTime: [
          { required: true, message: "质保时间不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.user.roleName = this.$store.state.user.roleName;
    this.user.deptName = this.$store.state.user.deptName;
    this.getList();
  },
  async mounted() {
    const res = await listCity()
    const deptRes = await listDept()
    this.regionOptions = res
    this.deptOptions = deptRes
  },
  methods: {
    handleStatusChange(row) {
      this.$modal.confirm('确认要切换状态吗？').then(() => {
        this.reset();
        const id = row.id || this.ids;
        getDevice(id).then(async response => {
          this.form.id = response.data.id;
          this.form.address = response.data.address;
          this.form.coordinates = response.data.coordinates;
          this.form.department = response.data.department;
          this.form.responsiblePerson = response.data.responsiblePerson;
          this.form.alarmPhone = response.data.alarmPhone;
          this.form.warrantyTime = response.data.warrantyTime;
          this.form.deviceId = response.data.deviceId;
          this.form.status = row.status;
          await this.getUserDetails(row.ownerId);
          this.form.isOpened = row.isOpened ? "true" : "false";
          if (this.form.id != null) {
            updateDevice(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          }
        });
      }).catch(() => {
        this.getList();
        // 用户取消操作
        this.$modal.msgInfo("已取消状态切换");
      });
    },
    handleOwnerTypeChange(value) {
      // 清空私有企业的相关字段
      if (value !== 'private') {
        this.form.deptName = '';
        this.form.isOpened = '';
      }
      console.log(value)
    },
    handleCountryChange(value) {
      if (value === 'CN') {
        this.form.foreignAddress = '';
      } else {
        this.form.region = [];
      }
    },
    /** 查询设备列表列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      console.log(this.user);
      console.log(this.user.roleName);
      this.queryParams.roleName= this.user.roleName;
      this.queryParams.deptName= this.user.deptName ? this.user.deptName : "陕西省";
      if (null != this.daterangeWarrantyTime && '' != this.daterangeWarrantyTime) {
        this.queryParams.params["beginWarrantyTime"] = this.daterangeWarrantyTime[0];
        this.queryParams.params["endWarrantyTime"] = this.daterangeWarrantyTime[1];
      }
      console.log(this.queryParams);
      listDeviceByRole(this.queryParams).then(response => {
        this.deviceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        deviceId: null,
        address: null,
        department: null,
        responsiblePerson: null,
        alarmPhone: null,
        coordinates: null,
        updateTime: null,
        warrantyTime: null,
        status: null,
        country: null,
        province: null,
        city: null,
        district: null,
        ownerId: null,
        isOpened: null,
        deptName: null,
        deptType: null,
        region: null,
        foreignAddress: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeWarrantyTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.isAdd = true;
      this.open = true;
      this.title = "添加设备列表";
    },
    async getUserDetails(id) {
      const res = await getOwner(id);
      this.form.deptName = res.data.deptName;
      this.form.deptType = res.data.deptType;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDevice(id).then(async response => {
        this.form.id = response.data.id;
        this.form.address = response.data.address;
        this.form.coordinates = response.data.coordinates;
        this.form.department = response.data.department;
        this.form.responsiblePerson = response.data.responsiblePerson;
        this.form.alarmPhone = response.data.alarmPhone;
        this.form.warrantyTime = response.data.warrantyTime;
        this.form.deviceId = response.data.deviceId;
        await this.getUserDetails(row.ownerId);
        this.form.isOpened = row.isOpened ? "true" : "false";
        this.isAdd = false;
        this.open = true;
        this.title = "修改设备列表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      console.log(this.form);
      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log('111111111111111');
          if (this.form.id != null) {
            updateDevice(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.region = this.form.region.toString();
            addDevice(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除设备列表编号为"' + ids + '"的数据项？').then(function() {
        return delDevice(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('device/device/export', {
        ...this.queryParams
      }, `device_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
