<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="文件名" label="文件名" style="width: 200px;" class="filter-item" clearable maxlength="100" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
    </div>

    <!-- 表格区域 -->
    <div class="table-wrap">
      <el-table
        v-loading="listLoading"
        :data="tableData"
        size="small"
        fit
        border
        style="width: 100%"
      >
        <el-table-column type="index" />
        <el-table-column
          label="上传文件名"
        >
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="存储名"
        >
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.keyName }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="文件类型"
          width="100"
        >
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.type }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="文件大小"
          width="80"
        >
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.size }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="存储路径"
        >
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.url }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="存储方式"
        >
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.storage.type }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="存储方式名"
        >
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.storage.name }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="业务类型"
          width="100"
        >
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.businessType }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="创建日期"
        >
          <template slot-scope="scope">
            <i class="el-icon-time" />
            <span>{{ parseTime(scope.row.createDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getResourceData" />
    </div>

  </div>
</template>
<script>
import { getResourceInfo, removeResource } from '@/api/system/resource'
import Pagination from '@/components/Pagination'
import waves from '@/directive/waves' // 水波纹指令
import { parseTime } from '@/utils/index'
export default {
  directives: {
    waves
  },
  components: { Pagination },
  data() {
    return {
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        name: ''
      },
      listLoading: true,
      tableData: []
    }
  },
  mounted() {
    this.getResourceData()
  },
  methods: {
    parseTime,
    // 获取数据
    getResourceData() {
      this.listLoading = false
      getResourceInfo(this.listQuery).then(res => {
        // 将返回的数据赋给tableData、fileType、resourceType
        this.tableData = res.data.content
        this.total = res.data.totalElements
        this.listLoading = false
      })
    },
    // 搜索
    handleFilter() {
      this.getResourceData()
    },
    // 删除
    handleDelete(index, row) {
      this.$confirm('此操作将永久删除该行, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeResource(row).then(res => {
          this.tableData.splice(index, 1)
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }
}
</script>
<style>

</style>

