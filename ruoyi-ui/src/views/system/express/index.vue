<script>
export default {
  data() {
    return {
      active: 0,
      form: {
        username: "",
        city: "",
        date: "",
      },
      cities: [
        { value: "shanghai", label: "上海" },
        { value: "beijing", label: "北京" },
      ],
      rules: {
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" }
        ],
        city: [
          { required: true, message: "请选择城市", trigger: "change" }
        ],
        date: [
          { type: "date", required: true, message: "请选择日期", trigger: "change" }
        ]
      }
    };
  },

  methods: {
    next() {
      if (this.active++ > 2) this.active = 0;
    },
    submitForm() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          // 调用接口提交数据
          console.log("表单数据:", this.form);
        } else {
          this.$message.error("请填写必填项");
          return false;
        }
      });
    },
    resetForm() {
      this.$refs.formRef.resetFields();
    }
  }
}
</script>

<template>
  <div>
    <div class="model_title">
      <span>病害预测总览</span>
    </div>
    <div class="express-container">
      <el-steps :active="active" finish-status="success" class="express-steps" align-center>
        <el-step title="基础信息"></el-step>
        <el-step title="病害选择"></el-step>
        <el-step title="快报类型"></el-step>
      </el-steps>
    </div>
    <div class="express-container">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" style="width: 35%; margin: 0 auto;">
        <!-- 选择器 -->
        <el-form-item label="省" prop="city">
          <el-select v-model="form.city" placeholder="请选择省">
            <el-option
              v-for="item in cities"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="市" prop="city">
          <el-select v-model="form.city" placeholder="请选择市">
            <el-option
              v-for="item in cities"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="区" prop="city">
          <el-select v-model="form.city" placeholder="请选择区">
            <el-option
              v-for="item in cities"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="详细地址" prop="username" style="width: 350px">
          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="username" style="width: 350px">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入内容"
            v-model="form.username">
          </el-input>
        </el-form-item>
        <!-- 提交按钮 -->
        <el-form-item>
          <el-button type="primary" @click="submitForm">下一步</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="express-container">

    </div>

  </div>

</template>

<style scoped lang="scss">
.model_title {
  padding: 20px 0 0 20px;
  span {
    font-weight: bold;
  }
}
.express-steps {
  margin: 0 auto;
  width: 40%;

}
.express-container {
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  margin: 20px 20px 20px 20px;
  padding: 20px;
}
</style>
