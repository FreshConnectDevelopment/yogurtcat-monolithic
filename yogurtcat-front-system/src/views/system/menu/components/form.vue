<template>
  <el-dialog :visible.sync="dialogFormVisible" :title="textMap[dialogStatus]" append-to-body width="600px">
    <el-form ref="el_data_form" :model="formModel" :rules="rules" size="small" label-width="80px">
      <el-form-item label="菜单图标">
        <el-popover
          placement="bottom-start"
          width="460"
          trigger="click"
          @show="$refs['iconSelect'].reset()"
        >
          <IconSelect ref="iconSelect" @selected="selected" />
          <el-input slot="reference" v-model="formModel.icon" style="width: 460px;" placeholder="点击选择图标" readonly>
            <svg-icon v-if="formModel.icon" slot="prefix" :icon-class="formModel.icon" class="el-input__icon" style="height: 32px;width: 16px;" />
            <i v-else slot="prefix" class="el-icon-search el-input__icon" />
          </el-input>
        </el-popover>
      </el-form-item>
      <el-form-item label="菜单名称" prop="name">
        <el-input v-model="formModel.name" placeholder="名称" style="width: 460px;" />
      </el-form-item>
      <el-form-item label="菜单排序" prop="sort">
        <el-input v-model.number="formModel.sort" placeholder="序号越小越靠前" style="width: 460px;" maxlength="3" />
      </el-form-item>
      <el-form-item label="链接地址">
        <el-input v-model="formModel.path" placeholder="菜单路径" style="width: 460px;" />
      </el-form-item>
      <el-form-item label="组件路径">
        <el-input v-model="formModel.component" placeholder="组件路径" style="width: 460px;" />
      </el-form-item>
      <el-form-item label="上级类目">
        <treeselect v-model="formModel.pid" :options="menus" style="width: 460px;" placeholder="选择上级类目" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialogFormVisible-footer">
      <el-button @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="dialogStatus==='create'?createData():updateData()">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { create, update, menuOptionlist } from '@/api/system/menu'
import Treeselect from '@riophae/vue-treeselect'
import IconSelect from '@/components/IconSelect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  components: { Treeselect, IconSelect },
  props: {
  },
  data() {
    return {
      loading: false,
      listQuery: {},
      menus: [],
      formModel: {
        name: '',
        sort: '',
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
        sort: [
          {
            required: true,
            message: '请输入正确的序号',
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
      menuOptionlist(this.listQuery).then(response => {
        this.menus = []
        const menu = { id: 0, label: '顶级类目', children: [] }
        menu.children = response.data
        this.menus.push(menu)
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
            this.$emit('fetchData')
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
            this.$emit('fetchData')
          })
        }
      })
    },
    resetForm() {
      this.formModel = {
        name: '',
        sort: '',
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
