<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input v-model="listQuery.username" class="filter-item" placeholder="用户名" label="用户名" style="width: 200px;" maxlength="15" clearable @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      size="small"
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="操作账号">
        <template slot-scope="scope">
          {{ scope.row.username }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作终端IP">
        <template slot-scope="scope">
          {{ scope.row.ip }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.module }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作内容">
        <template slot-scope="scope">
          <el-tooltip effect="light" placement="top-start">
            <div slot="content" style="width:300px">{{ scope.row.content }}</div>
            <span>{{ scope.row.content.substring(0, 20) + '...' }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作结果">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.result }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="fetchData" />
  </div>
</template>

<script>
import { getList } from '@/api/monitor/operationLog'
import waves from '@/directive/waves' // 水波纹指令
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils/index'

export default {
  directives: {
    waves
  },
  components: { Pagination },
  data() {
    return {
      list: null,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined
      },
      temp: {
        id: undefined,
        name: '',
        roles: ''
      },
      total: 0
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    parseTime,
    fetchData() {
      this.listLoading = true
      getList(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = response.data.totalElements
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.fetchData()
    }
  }
}
</script>
