<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="菜单名" label="菜单名" style="width: 200px;" class="filter-item" clearable maxlength="15" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">新增</el-button>
    </div>

    <tree-table v-loading="loading" :data="list" :expand-all="true" :columns="columns" border size="small">
      <el-table-column prop="icon" label="图标" align="center" width="80px">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="sort" align="center" width="100px" label="排序">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.sort }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" prop="path" label="链接地址" />
      <el-table-column :show-overflow-tooltip="true" prop="component" label="组件路径" />
      <el-table-column label="操作" width="150px" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </tree-table>

    <data_form ref="dataForm" @fetchData="fetchData" />
  </div>
</template>

<script>
import { menuList, remove } from '@/api/system/menu'
import waves from '@/directive/waves' // 水波纹指令
import treeTable from '@/components/TreeTable'
import data_form from './components/form'

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
    fetchData() {
      this.listLoading = true
      menuList(this.listQuery).then(response => {
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
