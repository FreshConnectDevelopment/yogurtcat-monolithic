<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="名称" label="名称" style="width: 200px;" class="filter-item" maxlength="15" clearable @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">新增</el-button>
    </div>

    <tree-table v-loading="loading" :data="list" :expand-all="true" :columns="columns" border size="small">
      <el-table-column prop="createTime" label="创建日期">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150px" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </tree-table>

    <data_form ref="dataForm" @fatherMethod="fetchData" />
  </div>
</template>

<script>
import { permissionList, remove } from '@/api/system/permission'
import waves from '@/directive/waves' // 水波纹指令
import treeTable from '@/components/TreeTable'
import data_form from './components/form'
import { parseTime } from '@/utils/index'

export default {
  directives: {
    waves
  },
  components: { treeTable, data_form },
  data() {
    return {
      columns: [
        {
          text: '名称',
          value: 'name'
        },
        {
          text: '别名',
          value: 'alias'
        }
      ],
      list: [],
      listLoading: true,
      listQuery: {
        name: undefined
      },
      delLoading: false,
      loading: false,
      rules: {
        name: [
          { required: true, message: 'name is required', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    parseTime,
    fetchData() {
      this.listLoading = true
      permissionList(this.listQuery).then(response => {
        this.list = response.data
        this.listLoading = false
      })
    },
    handleFilter() {
      this.fetchData()
    },
    handleCreate() {
      this.$refs.dataForm.handleCreate()
    },
    handleUpdate(row) {
      this.$refs.dataForm.handleUpdate(row)
    },
    handleDelete(row) {
      this.$confirm('确认删除？')
        .then(_ => {
          remove(row).then(response => {
            const index = this.list.indexOf(row)
            this.list.splice(index, 1)
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
          })
        })
        .catch(_ => {})
    }
  }
}
</script>
