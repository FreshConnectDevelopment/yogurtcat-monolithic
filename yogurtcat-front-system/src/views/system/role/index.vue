<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="名称" label="名称" style="width: 200px;" class="filter-item" clearable maxlength="15" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">新增</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      size="small"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="createTime" label="创建日期">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="fetchData" />

    <el-row :gutter="20" style="margin-top: 5px;">
      <!--权限分配-->
      <el-col :span="12">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span class="role-span">权限分配</span>
            <el-button
              v-if="showButton"
              :loading="permissionLoading"
              icon="el-icon-check"
              size="mini"
              style="float: right; padding: 4px 10px"
              type="info"
              @click="savePermission"
            >保存</el-button>
          </div>
          <div :style="'min-height: 200px;max-height:' + height + 'overflow-y: auto;'">
            <el-tree
              ref="permission"
              :data="permissions"
              :default-checked-keys="permissionIds"
              :props="defaultProps"
              show-checkbox
              node-key="id"
            />
          </div>
        </el-card>
      </el-col>
      <!--菜单分配-->
      <el-col :span="12">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span class="role-span">菜单分配</span>
            <el-button
              v-if="showButton"
              :loading="menuLoading"
              icon="el-icon-check"
              size="mini"
              style="float: right; padding: 4px 10px"
              type="info"
              @click="saveMenu"
            >保存</el-button>
          </div>
          <div :style="'min-height: 207px;max-height:' + height + 'overflow-y: auto;'">
            <el-tree
              ref="menu"
              :data="menus"
              :default-checked-keys="menuIds"
              :props="defaultProps"
              show-checkbox
              node-key="id"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="角色名" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="temp.description" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" :disabled="isDisable" @click="dialogStatus==='create'?createData():updateData()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { permissionOptionlist } from '@/api/system/permission'
import { menuOptionlist } from '@/api/system/menu'
import {
  editPermission,
  editMenu,
  list,
  create,
  update,
  remove,
  get
} from '@/api/system/role'
import waves from '@/directive/waves' // 水波纹指令
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils/index'

export default {
  directives: {
    waves
  },
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined
      },
      temp: {
        id: undefined,
        name: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增'
      },
      dialogPvVisible: false,
      rules: {
        name: [
          { required: true, message: '角色名不能为空', trigger: 'change' },
          { min: 0, max: 25, message: '角色名不能超过25个字符哦~', trigger: 'blur' },
          { pattern: /\s\S+|S+\s|\S/, message: '角色名不能只是空格' }
        ],
        description: [
          { pattern: /\s\S+|S+\s|\S/, message: '描述不能只是空格' }
        ]
      },
      UserStatus: ['待激活', '正常', '禁用'],
      list: [],
      permissionLoading: false,
      menuLoading: false,
      showButton: false,
      height: document.documentElement.clientHeight - 94.5 - 260 + 'px;',
      total: 0,
      permissions: [],
      permissionIds: [],
      menus: [],
      menuIds: [],
      currentRoleId: null,
      isDisable: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    parseTime,
    fetchData() {
      this.listLoading = true
      list(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = response.data.totalElements
        this.listLoading = false
      })

      this.listLoading = true
      menuOptionlist(this.listQuery).then(response => {
        this.menus = response.data
        this.listLoading = false
      })

      this.listLoading = true
      permissionOptionlist(this.listQuery).then(response => {
        this.permissions = response.data
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.fetchData()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: undefined
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.isDisable = false
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.isDisable = true
          create(this.temp).then(response => {
            this.fetchData()
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
          }).catch(response => {
            this.isDisable = false
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
      this.isDisable = false
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.isDisable = true
          const tempData = Object.assign({}, this.temp)
          update(tempData).then(response => {
            for (const v of this.list) {
              if (v.id === this.temp.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, response.data)
                break
              }
            }
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          }).catch(response => {
            this.isDisable = false
          })
        }
      })
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
    },
    handleCurrentChange(row) {
      if (!row) return
      const _this = this
      this.$refs.permission.setCheckedKeys([])
      this.$refs.menu.setCheckedKeys([])
      this.currentRoleId = row.id
      this.showButton = true
      this.menuIds = []
      this.permissionIds = []
      row.menus.forEach(function(data, index) {
        let add = true
        for (let i = 0; i < row.menus.length; i++) {
          if (data.id === row.menus[i].pid) {
            add = false
            break
          }
        }
        if (add) {
          _this.menuIds.push(data.id)
        }
      })
      // 处理权限数据
      row.permissions.forEach(function(data, index) {
        _this.permissionIds.push(data.id)
      })
    },
    savePermission() {
      this.permissionLoading = true
      const role = { id: this.currentRoleId, permissions: [] }
      this.$refs.permission.getCheckedKeys().forEach(function(data, index) {
        const permission = { id: data }
        role.permissions.push(permission)
      })
      editPermission(role)
        .then(res => {
          this.$notify({
            title: '保存成功',
            type: 'success',
            duration: 2500
          })
          this.permissionLoading = false
          this.update()
        })
        .catch(err => {
          this.permissionLoading = false
          console.log(err.response.data.message)
        })
    },
    saveMenu() {
      this.menuLoading = true
      const role = { id: this.currentRoleId, menus: [] }
      // 得到半选的父节点数据，保存起来
      this.$refs.menu.getHalfCheckedNodes().forEach(function(data, index) {
        const permission = { id: data.id }
        role.menus.push(permission)
      })
      // 得到已选中的 key 值
      this.$refs.menu.getCheckedKeys().forEach(function(data, index) {
        const permission = { id: data }
        role.menus.push(permission)
      })
      editMenu(role)
        .then(res => {
          this.$notify({
            title: '保存成功',
            type: 'success',
            duration: 2500
          })
          this.menuLoading = false
          this.update()
        })
        .catch(err => {
          this.menuLoading = false
          console.log(err.response.data.message)
        })
    },
    update() {
      get(this.currentRoleId).then(res => {
        for (let i = 0; i < this.list.length; i++) {
          if (res.data.id === this.list[i].id) {
            this.list[i] = res.data
            break
          }
        }
      })
    }
  }
}
</script>
