<template>
  <div class="dashboard-container">
    <!-- 标题 -->
    <div class="header">
      <h2>农业病虫害监测平台</h2>
      <el-divider></el-divider>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6" v-for="(item,index) in stats" :key="index">
        <el-card class="stat-card" :style="{background: item.color}">
          <div class="card-content" @click="handleClick(item.event)" style="cursor: pointer;">
            <i :class="item.icon" style="font-size: 40px; color: #fff;"></i>
            <div class="text">
              <h3 class="title">{{ item.title }}</h3>
              <p class="value">{{ item.value }}</p>
              <span class="tip">{{ item.tip }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="query-card">
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="作物类型">
          <el-select v-model="queryForm.crop" placeholder="请选择">
            <el-option
              v-for="item in crops"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="日期范围">
          <el-date-picker
            v-model="queryForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <div class="table-header">
        <span class="title">病虫害监测记录</span>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增记录</el-button>
      </div>

      <el-table
        :data="tableData"
        stripe
        v-loading="loading"
        style="width: 100%">
        <el-table-column prop="date" label="日期" width="150"></el-table-column>
        <el-table-column prop="crop" label="作物类型" width="120"></el-table-column>
        <el-table-column prop="disease" label="病虫害类型"></el-table-column>
        <el-table-column prop="area" label="发生面积(亩)" width="120"></el-table-column>
        <el-table-column prop="level" label="危害等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.level | levelColor">{{ scope.row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        class="pagination">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import { listAlert, updateAlert } from "@/api/climate";

export default {
  data() {
    return {
      // 统计卡片数据
      stats: [
        {
          title: '采集数据总量',
          value: '811.23万条',
          tip: '较昨日 +2.3%',
          color: '#409EFF',
          icon: 'el-icon-collection'
        },
        {
          title: '待处理任务',
          value: '23项',
          tip: '今日新增5项',
          color: '#E6A23C',
          icon: 'el-icon-alarm-clock'
        },
        {
          title: '防控面积',
          value: '12.7万亩',
          tip: '覆盖98个区域',
          color: '#67C23A',
          icon: 'el-icon-map-location'
        },
        {
          title: '预警事件',
          value: '3起',
          tip: '1起未处理',
          color: '#F56C6C',
          icon: 'el-icon-warning',
          event: 'alert'
        }
      ],

      // 查询表单
      queryForm: {
        crop: '',
        dateRange: []
      },

      // 分页配置
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },

      // 表格数据
      tableData: [
        {
          date: '2024-03-01',
          crop: '水稻',
          disease: '稻瘟病',
          area: 235,
          level: '高'
        },
        {
          date: '2024-03-02',
          crop: '小麦',
          disease: '赤霉病',
          area: 156,
          level: '中',
        }
      ],

      // 作物类型选项
      crops: [
        { value: 'rice', label: '水稻' },
        { value: 'wheat', label: '小麦' },
        { value: 'corn', label: '玉米' }
      ],

      loading: false
    }
  },

  filters: {
    levelColor(level) {
      const map = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return map[level] || 'info'
    }
  },
  async created() {
    const res = await listAlert({
      pageSize: 200
    });
    this.stats[3].value = `${res.total} 起`;
    let count = 0;
    res.rows.forEach((item, index) => {
      if (item.status === 0) {
        count++;
      }
    })
    this.stats[3].tip = `${count} 起未处理`
  },

  methods: {
    handleClick(item) {
      if (item === 'alert'){
        this.$router.push({ path: '/data/handleAlert' })
      }
    },
    // 查询操作
    handleQuery() {
      this.pagination.current = 1
      this.loadTableData()
    },

    // 重置表单
    handleReset() {
      this.queryForm = {
        crop: '',
        dateRange: []
      }
    },

    // 加载表格数据
    async loadTableData() {
      this.loading = true
      // 这里模拟API调用
      setTimeout(() => {
        this.pagination.total = 100
        this.loading = false
      }, 500)
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadTableData()
    },

    // 页码改变
    handlePageChange(page) {
      this.pagination.current = page
      this.loadTableData()
    },

    // 新增记录
    handleAdd() {
      console.log('打开新增弹窗')
    },

    // 编辑记录
    handleEdit(row) {
      console.log('编辑记录', row)
    },

    // 删除记录
    handleDelete(row) {
      this.$confirm('确认删除该记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  background: #f5f7fa;

  .header {
    margin-bottom: 20px;
    h2 {
      color: #303133;
      margin: 0;
    }
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      color: #fff;

      .card-content {
        display: flex;
        align-items: center;

        .text {
          margin-left: 15px;

          .title {
            font-size: 16px;
            margin: 0 0 5px 0;
          }

          .value {
            font-size: 24px;
            margin: 5px 0;
          }

          .tip {
            font-size: 12px;
            opacity: 0.9;
          }
        }
      }
    }
  }

  .query-card {
    margin-bottom: 20px;

    ::v-deep .el-form-item {
      margin-bottom: 0;
    }
  }

  .table-card {
    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;

      .title {
        font-size: 16px;
        font-weight: bold;
      }
    }

    .pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>
