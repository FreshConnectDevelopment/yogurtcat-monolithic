<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.username"
        placeholder="账号名"
        label="账号名"
        style="width: 200px;"
        class="filter-item"
        maxlength="15"
        clearable
        @keyup.enter.native="handleFilter"
      />
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >搜索</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >新增</el-button>
    </div>

    <el-table
      v-if="checkPermission(['ADMIN'])"
      v-loading="listLoading"
      :data="list"
      size="small"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="账号名">
        <template slot-scope="scope">{{ scope.row.username }}</template>
      </el-table-column>
      <el-table-column align="center" label="头像">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" class="el-avatar">
        </template>
      </el-table-column>
      <el-table-column label="邮箱" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="注册时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ parseTime(scope.row.registrationTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="270" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          <el-button style="width:80px" type="primary" size="mini" plain @click="handleChangePassword(scope.row)">修改密码</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="fetchData"
    />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="70px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="账号名" prop="username">
          <el-input v-model="temp.username" />
        </el-form-item>
        <el-form-item v-if="dialogStatus==='create'" label="密码" prop="password">
          <el-input v-model="temp.password" type="password" show-password auto-complete="new-password" />
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <singleImage :file="temp.avatar" path="通用/systemUser" @input="setVlaue" />
        </el-form-item>
        <!-- <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option v-for="item in UserStatus" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item> -->
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="temp.email" />
        </el-form-item>
        <el-form-item style="margin-bottom: 0px;" label="角色" prop="roleIds">
          <el-select v-model="temp.roleIds" class="filter-item" placeholder="请选择角色" multiple>
            <el-option v-for="item in roles" :key="item.id" :label="item.label" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" :disabled="isDisable" @click="dialogStatus==='create'?createData():updateData()">确定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改密码" :visible.sync="dialogPass">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="80px"
        style="margin-left:10px;margin-right:10px;"
      >
        <el-form-item label="新密码" prop="password">
          <el-input v-model="temp.password" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="passwordtwo">
          <el-input v-model="temp.passwordtwo" type="password" show-password />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogPass = false">取消</el-button>
        <el-button type="primary" :disabled="isDisable" @click="updateData()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getList, createUser, updateUser, deleteUser } from '@/api/system/user'
import { roleOptionlist } from '@/api/system/role'
import waves from '@/directive/waves' // 水波纹指令
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils/index'
import checkPermission from '@/utils/permission'
import singleImage from '../../component/storage/singleImage'

export default {
  directives: {
    waves
  },
  components: { Pagination, singleImage },
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
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.temp.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
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
        roles: '',
        avatar: '',
        passwordtwo: ''
      },
      total: 0,
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增'
      },
      dialogPvVisible: false,
      rules: {
        username: [
          { required: true, message: '用户名不能为空!', trigger: ['blur', 'change'] },
          { min: 0, max: 10, message: '用户名不能超过10个字符哦~', trigger: ['blur', 'change'] },
          { pattern: /\s\S+|S+\s|\S/, message: '用户名不能只是空格' }
        ],
        password: [
          { required: true, message: '密码不能为空!', trigger: ['blur', 'change'] },
          { min: 6, max: 12, message: '密码必须是6到12位哦~', trigger: ['blur', 'change'] },
          { pattern: /\s\S+|S+\s|\S/, message: '密码不能只是空格' }
        ],
        avatar: [
          { required: true, message: '至少要上传一张美照哦', trigger: ['blur', 'change'] }
        ],
        email: [
          { pattern: /^[_a-z0-9-\.]+@([-a-z0-9]+\.)+[a-z]{2,}$/i, message: '请输入正确的邮箱!' }
          // /^[_a-z0-9-\.]+@([-a-z0-9]+\.)+[a-z]{2,}$/i
          // /^([0-9A-Za-z\-_\.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g
        ],
        roleIds: [
          { required: true, message: '至少要选一个角色哦~', trigger: ['blur', 'change'] }
        ],
        passwordtwo: [
          { required: true, message: '确认密码不能为空!', trigger: ['blur', 'change'] },
          { min: 6, max: 12, message: '确认密码必须是6到12位哦~', trigger: ['blur', 'change'] },
          { validator: validatePass, trigger: ['blur', 'change'] },
          { pattern: /\s\S+|S+\s|\S/, message: '确认密码不能只是空格' }
        ]
      },
      roles: {
        type: Array,
        required: true
      },
      UserStatus: ['待激活', '正常', '禁用'],
      dialogPass: false,
      isDisable: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    parseTime,
    checkPermission,
    fetchData() {
      this.listLoading = true
      getList(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = response.data.totalElements
        this.listLoading = false
      })

      this.listLoading = true
      roleOptionlist(this.listQuery).then(response => {
        this.roles = response.data
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.fetchData()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: undefined,
        avatar: ''
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
          createUser(this.temp).then(response => {
            this.list.unshift(response.data)
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
    handleChangePassword(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.isDisable = false
      this.dialogPass = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
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
          updateUser(tempData).then(response => {
            for (const v of this.list) {
              if (v.id === this.temp.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, response.data)
                break
              }
            }
            this.dialogFormVisible = false
            this.dialogPass = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            this.resetTemp()
          }).catch(response => {
            this.isDisable = false
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除？')
        .then(_ => {
          deleteUser(row).then(response => {
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
    setVlaue: function(value) {
      this.temp.avatar = value.url
    }
  }
}
</script>
