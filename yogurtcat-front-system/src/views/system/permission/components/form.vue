<template>
  <el-dialog :visible.sync="dialogFormVisible" :title="textMap[dialogStatus]" append-to-body width="600px">
    <el-form ref="el_data_form" :model="formModel" :rules="rules" size="small" label-width="80px">
      <el-form-item label="名称" prop="name">
        <el-input v-model="formModel.name" placeholder="名称" style="width: 460px;" />
      </el-form-item>
      <el-form-item label="别名" prop="alias">
        <el-input v-model.number="formModel.alias" style="width: 460px;" />
      </el-form-item>
      <el-form-item label="上级类目">
        <treeselect v-model="formModel.pid" :options="options" style="width: 460px;" placeholder="选择上级类目" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialogFormVisible-footer">
      <el-button @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="dialogStatus==='create'?createData():updateData()">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { create, update, permissionOptionlist } from '@/api/system/permission'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  components: { Treeselect },
  props: {
  },
  data() {
    return {
      loading: false,
      listQuery: {},
      options: [],
      formModel: {
        name: '',
        sort: 999,
        path: '',
        component: '',
        iframe: 'false',
        roles: [],
        pid: 0,
        icon: ''
      },
      rules: {
        name: [{ required: true, message: '请输入名称', trigger: 'blur' },
          { min: 0, max: 25, message: '名称不能超过25个字符哦~', trigger: 'blur' },
          { pattern: /\s\S+|S+\s|\S/, message: '名称不能只是空格' }
        ],
        alias: [{ required: true, message: '请输入别名,配置时只会显示别名', trigger: 'blur' },
          { min: 0, max: 25, message: '别名不能超过25个字符哦~', trigger: 'blur' },
          { pattern: /\s\S+|S+\s|\S/, message: '别名不能只是空格' }
        ],
        sort: [
          {
            required: true,
            message: '请输入序号',
            trigger: 'blur',
            type: 'number'
          }
        ],
        iframe: [{ required: true, message: '请选择菜单类型', trigger: 'blur' }]
      },
      dialogStatus: '',
      dialogFormVisible: false,
      listLoading: true,
      textMap: {
        update: '编辑',
        create: '新增'
      }
    }
  },
  methods: {
    fetchData() {
      this.listLoading = true
      permissionOptionlist(this.listQuery).then(response => {
        this.options = []
        const permission = { id: 0, label: '顶级类目', children: [] }
        permission.children = response.data
        this.options.push(permission)
        this.listLoading = false
      })
    },
    cancel() {
      this.dialogFormVisible = false
      // this.resetForm()
    },
    handleCreate() {
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.resetForm()
      this.fetchData()
      this.$nextTick(() => {
        this.$refs['el_data_form'].clearValidate()
      })
    },
    createData() {
      this.$refs['el_data_form'].validate(valid => {
        if (valid) {
          create(this.formModel).then(response => {
            this.dialogFormVisible = false
            this.$emit('fatherMethod')
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.formModel = Object.assign({}, row) // copy obj
      this.formModel.parent = null
      this.formModel.children = null
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.fetchData()
      this.$nextTick(() => {
        this.$refs['el_data_form'].clearValidate()
      })
    },
    updateData() {
      this.$refs['el_data_form'].validate(valid => {
        if (valid) {
          update(this.formModel).then(response => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            this.$emit('fatherMethod')
          })
        }
      })
    },
    resetForm() {
      this.formModel = {
        name: '',
        sort: 999,
        path: '',
        component: '',
        roles: [],
        pid: 0,
        icon: ''
      }
    },
    selected(name) {
      this.formModel.icon = name
    }
  }
}
</script>
