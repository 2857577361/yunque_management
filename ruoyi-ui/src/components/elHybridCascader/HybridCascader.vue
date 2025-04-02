<template>
  <div class="hybrid-cascader">
    <el-cascader
      ref="cascaderRef"
      v-model="selectedValue"
      :options="options"
      :props="props"
      @change="handleChange"
      @visible-change="handleVisibleChange"
    />

    <!-- 自定义下拉面板 -->
    <div v-if="showCustomPanel" class="custom-panel">
      <div class="panel-header">
        <span v-for="(item, index) in currentLabels" :key="index">
          {{ item }}
        </span>
      </div>

      <div class="panel-content">
        <!-- 第一级多选 -->
        <div v-if="currentLevel === 0" class="level-container">
          <el-checkbox-group v-model="level1Selected">
            <el-checkbox
              v-for="item in options"
              :key="item.value"
              :label="item.value"
            >
              {{ item.label }}
            </el-checkbox>
          </el-checkbox-group>
        </div>

        <!-- 第二级多选 -->
        <div v-if="currentLevel === 1" class="level-container">
          <el-checkbox-group v-model="level2Selected">
            <el-checkbox
              v-for="item in level2Options"
              :key="item.value"
              :label="item.value"
            >
              {{ item.label }}
            </el-checkbox>
          </el-checkbox-group>
        </div>

        <!-- 第三级文本输入 -->
        <div v-if="currentLevel === 2" class="level-container">
          <el-input
            v-model="inputValue"
            placeholder="请输入内容"
            clearable
          />
        </div>
      </div>

      <div class="panel-footer">
        <el-button size="small" @click="handleCancel">取消</el-button>
        <el-button type="primary" size="small" @click="handleConfirm">确定</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    options: {
      type: Array,
      required: true
    },
    value: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      selectedValue: [],
      showCustomPanel: false,
      currentLevel: 0,
      currentLabels: [],
      level1Selected: [],
      level2Selected: [],
      level2Options: [],
      inputValue: ''
    };
  },
  computed: {
    props() {
      return {
        expandTrigger: 'hover',
        emitPath: false,
        multiple: false
      };
    }
  },
  methods: {
    handleVisibleChange(visible) {
      if (visible) {
        this.showCustomPanel = true;
        this.currentLevel = 0;
        this.currentLabels = [];
        this.level1Selected = [];
        this.level2Selected = [];
        this.inputValue = '';
      } else {
        this.showCustomPanel = false;
      }
    },

    handleChange(value) {
      // 阻止原生change事件
      this.$emit('input', this.selectedValue);
    },

    handleLevel1Confirm() {
      if (this.level1Selected.length === 0) return;

      const selectedItem = this.options.find(item => item.value === this.level1Selected[0]);
      this.currentLabels = [selectedItem.label];
      this.currentLevel = 1;

      // 获取第二级选项
      this.level2Options = selectedItem.children || [];
    },

    handleLevel2Confirm() {
      if (this.level2Selected.length === 0) return;

      const selectedItem = this.level2Options.find(item => item.value === this.level2Selected[0]);
      this.currentLabels = [...this.currentLabels, selectedItem.label];
      this.currentLevel = 2;
    },

    handleConfirm() {
      if (this.currentLevel === 0) {
        this.handleLevel1Confirm();
      } else if (this.currentLevel === 1) {
        this.handleLevel2Confirm();
      } else {
        // 最终确认
        this.selectedValue = [
          ...this.level1Selected,
          ...this.level2Selected,
          this.inputValue
        ];
        this.showCustomPanel = false;
        this.$emit('input', this.selectedValue);
        this.$emit('change', this.selectedValue);
      }
    },

    handleCancel() {
      this.showCustomPanel = false;
      this.$refs.cascaderRef.dropDownVisible = false;
    }
  }
};
</script>

<style scoped>
.hybrid-cascader {
  position: relative;
}

.custom-panel {
  position: absolute;
  z-index: 2000;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 400px;
}

.panel-header {
  padding: 10px;
  border-bottom: 1px solid #e4e7ed;
  font-size: 14px;
}

.panel-header span {
  margin-right: 10px;
  color: #409eff;
}

.panel-content {
  padding: 10px;
}

.level-container {
  max-height: 300px;
  overflow-y: auto;
}

.panel-footer {
  padding: 10px;
  text-align: right;
  border-top: 1px solid #e4e7ed;
}
</style>
