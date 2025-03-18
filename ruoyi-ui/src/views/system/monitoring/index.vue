<template>
  <div>
    <div class="model_title">
      <span>设备运行状态监测</span>
    </div>
    <div class="content-container">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <div class="model_form">
          <el-form-item label="设备ID" style="margin-left: 20px;">
            <el-input v-model="formInline.ID" placeholder="请输入设备ID" size="small" style="width: 280px; margin-right: 20px"></el-input>
          </el-form-item>
          <el-form-item label="数据查询间隔">
            <el-select v-model="formInline.type" placeholder="请选择间隔" size="small" style="width: 280px">
              <el-option label="5" value="shanghai"></el-option>
              <el-option label="6" value="beijing"></el-option>
            </el-select>
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
        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="序号" label="序号"></el-table-column>
          <el-table-column prop="设备ID" label="设备ID"></el-table-column>
          <el-table-column prop="设备地址" label="设备地址"></el-table-column>
          <el-table-column prop="设备号" label="设备号"></el-table-column>
          <el-table-column prop="备注" label="备注"></el-table-column>
        </el-table>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="10"
          :small="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="tableData.length"
          style="float:right;">
        </el-pagination>
      </el-card>
    </div>
  </div>

</template>

<script>
export default {
  data() {
    return {
      formInline: {
        ID: '',
        type: ''
      },
      currentPage: 1,
      pageSize: 10,
      tableData: [
        {序号: 1, 设备ID: 'BZYDN0001', 设备地址: '小麦锈病', 设备号: 'BZYDN0001', 备注: '无'},
        {序号: 2, 设备ID: 'BZYDN0002', 设备地址: '水稻稻瘟病', 设备号: 'BZYDN0002', 备注: '无'},
        // 添加更多模拟数据...
        {序号: 10, 设备ID: 'BZYDN0010', 设备地址: '马铃薯环腐病', 设备号: 'BZYDN0010', 备注: '无'},
        {序号: 1, 设备ID: 'BZYDN0001', 设备地址: '小麦锈病', 设备号: 'BZYDN0001', 备注: '无'},

      ],
    };
  },
  methods: {
    handleSizeChange(newSize) {
      console.log(`每页 ${newSize} 条`, newSize);
    },
    handleCurrentChange(newPage) {
      console.log(`当前页码 ${newPage}`, newPage);
    },
    onSubmit() {
      console.log('submit!');
    }
  },
  computed: {
    filteredData() {
      // 这里可以添加过滤数据的逻辑，例如根据页码和每页大小返回数据子集
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.tableData.slice(start, end);
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
