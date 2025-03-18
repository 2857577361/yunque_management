<template>
  <div class="smart-form-container">
    <el-form
      :model="form"
      :rules="rules"
      ref="formRef"
      label-width="110px"
      label-position="right"
      class="dynamic-form"
    >
      <el-row :gutter="30">
        <!-- 左侧主要信息列 -->
        <el-col>
          <el-form-item label="设备编号" prop="deviceNumber">
            <el-input
              v-model="form.deviceNumber"
              placeholder="请输入12位设备编号"
              clearable
              class="custom-input"
            >
              <i slot="prefix" class="el-icon-s-platform"></i>
            </el-input>
          </el-form-item>

          <el-form-item label="所属单位" prop="organization">
            <el-input
              v-model="form.organization"
              placeholder="请输入完整单位名称"
              clearable
              class="custom-input"
            >
              <i slot="prefix" class="el-icon-office-building"></i>
            </el-input>
          </el-form-item>

          <el-form-item label="负责人" prop="principal">
            <el-input
              v-model="form.principal"
              placeholder="请输入负责人姓名"
              clearable
              class="custom-input"
            >
              <i slot="prefix" class="el-icon-user"></i>
            </el-input>
          </el-form-item>

          <el-form-item label="联系电话" prop="contact">
            <el-input
              v-model="form.contact"
              placeholder="请输入11位手机号码"
              clearable
              class="custom-input"
            >
              <i slot="prefix" class="el-icon-phone"></i>
            </el-input>
          </el-form-item>

          <el-form-item label="详细地址" prop="address">
            <el-input
              v-model="form.address"
              placeholder="请输入设备安装详细地址"
              clearable
              class="custom-input"
            >
              <i slot="prefix" class="el-icon-location"></i>
            </el-input>
          </el-form-item>
        </el-col>

        <!-- 右侧配置信息列 -->
        <el-col>
          <el-form-item label="国别选择" prop="country">
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
          <template v-if="form.country === 'CN'">
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

          <el-form-item label="过期时间" prop="expireTime">
            <el-date-picker
              v-model="form.expireTime"
              type="datetime"
              placeholder="选择设备失效时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              class="custom-datepicker"
            />
          </el-form-item>

          <el-form-item label="绑定病害" prop="disease">
            <el-select
              v-model="form.disease"
              placeholder="请选择关联病害"
              class="custom-select"
              clearable
            >
              <el-option
                v-for="item in diseaseOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 表单操作 -->
      <div class="form-actions">
        <el-button
          type="primary"
          class="submit-btn"
          @click="handleSubmit"
        >
          <i class="el-icon-check"></i> 提交配置
        </el-button>
        <el-button
          class="reset-btn"
          @click="handleReset"
        >
          <i class="el-icon-refresh-right"></i> 重置表单
        </el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        deviceNumber: '',
        organization: '',
        principal: '',
        contact: '',
        address: '',
        country: 'CN',
        region: [],
        foreignAddress: '',
        expireTime: '',
        disease: ''
      },
      rules: {
        deviceNumber: [
          { required: true, message: '设备编号不能为空', trigger: 'blur' },
          { pattern: /^\d{12}$/, message: '请输入12位数字编号', trigger: 'blur' }
        ],
        organization: [
          { required: true, message: '所属单位不能为空', trigger: 'blur' }
        ],
        principal: [
          { required: true, message: '负责人不能为空', trigger: 'blur' }
        ],
        contact: [
          { required: true, message: '联系电话不能为空', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '详细地址不能为空', trigger: 'blur' }
        ],
        region: [
          { type: 'array', required: true, message: '请选择完整地区信息', trigger: 'change' }
        ],
        foreignAddress: [
          { required: true, message: '请填写完整地址信息', trigger: 'blur' }
        ]
      },
      countryOptions: [
        { label: '中国', value: 'CN' },
        { label: '外国', value: 'FRN' }
      ],
      diseaseOptions: [
        { label: '稻瘟病', value: 'disease-01' },
        { label: '小麦赤霉病', value: 'disease-02' }
      ],
      regionOptions: [
        {
          value: 'shaanxi',
          label: '陕西省',
          children: [
            {
              value: 'xianyang',
              label: '咸阳市',
              children: [
                { value: 'yangling', label: '杨凌区' }
              ]
            }
          ]
        },
        {
          value: 'beijing',
          label: '北京市',
          children: [
            {
              value: 'haidian',
              label: '海淀区',
              children: [
                { value: 'wudaokou', label: '五道口' }
              ]
            },
            {
              value: 'chaoyang',
              label: '朝阳区',
              children: [
                { value: 'guomao', label: '国贸' }
              ]
            }
          ]
        },
        {
          value: 'guangdong',
          label: '广东省',
          children: [
            {
              value: 'shenzhen',
              label: '深圳市',
              children: [
                { value: 'nanshan', label: '南山区' }
              ]
            },
            {
              value: 'guangzhou',
              label: '广州市',
              children: [
                { value: 'tianhe', label: '天河区' }
              ]
            }
          ]
        }
      ],
      cascaderProps: {
        expandTrigger: 'hover',
        checkStrictly: true
      }
    };
  },
  methods: {
    handleCountryChange(value) {
      if (value === 'CN') {
        this.form.foreignAddress = '';
      } else {
        this.form.region = [];
      }
    },
    handleSubmit() {
      this.$refs.formRef.validate(valid => {
        if (valid) {
          this.$message.success('表单验证通过');
          // 此处添加提交逻辑
        } else {
          this.$message.warning('请完善表单信息');
        }
      });
    },
    handleReset() {
      this.$refs.formRef.resetFields();
      this.form.country = 'CN';
    }
  }
};
</script>

<style lang="scss" scoped>
.smart-form-container {
  padding: 25px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);

  .dynamic-form {
    ::v-deep .el-form-item__label {
      color: #606266;
      font-weight: 500;
    }

    .custom-input {
      ::v-deep .el-input__inner {
        border-radius: 4px;
        transition: all 0.3s ease;

        &:hover {
          border-color: #409EFF;
        }

        &:focus {
          box-shadow: 0 0 5px rgba(64, 158, 255, 0.2);
        }
      }
    }

    .custom-select {
      width: 100%;
    }

    .custom-cascader {
      width: 100%;
    }

    .custom-datepicker {
      width: 100%;
    }

    .form-actions {
      margin-top: 25px;
      text-align: center;

      .submit-btn {
        padding: 12px 30px;
        border-radius: 20px;
        background: linear-gradient(135deg, #409EFF, #60aefc);
        border: none;
      }

      .reset-btn {
        padding: 12px 30px;
        border-radius: 20px;
        border-color: #dcdfe6;
        color: #606266;

        &:hover {
          background-color: #f5f7fa;
        }
      }
    }
  }
}
</style>
