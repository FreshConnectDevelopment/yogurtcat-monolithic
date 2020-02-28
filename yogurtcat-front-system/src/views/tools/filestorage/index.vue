<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input
        v-model="listQuery.name"
        placeholder="名称"
        label="名称"
        style="width: 200px;"
        class="filter-item"
        clearable
        maxlength="20"
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
        @click="addFile"
      >新增</el-button>
      <!-- <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-folder"
        @click="fileInVisible = true"
      >导入</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-folder-opened"
        @click="outFile"
      >导出</el-button> -->
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
          label="名称"
          width="180"
        >
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="文件类型"
          width="150"
        >
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.type }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="业务类型"
          min-width="200"
        >
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.businessType }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="参数"
          min-width="200"
        >
          <template slot-scope="scope">
            <!-- <span style="margin-left: 10px">{{ scope.row.param.join(", ") }}</span> -->
            <span
              v-if="scope.row.type === '本地存储'"
              style="margin-left: 10px"
            >{{ "服务器IP地址:" + scope.row.ip }}</span>
            <span
              v-else
              style="margin-left: 10px"
            >bucketname: {{ scope.row.bucketname }}, endpoint: {{ scope.row.endpoint }}, accessKeyId: {{ scope.row.accessKeyId }}, accessKeySecret: {{ scope.row.accessKeySecret }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column
          align="center"
          label="排序"
          width="150"
          class-name="small-padding fixed-width"
        /> -->
        <el-table-column
          label="操作"
          width="160"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleEdit(scope.$index, scope.row)"
            >编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.limit"
        @pagination="getInfo"
      />
    </div>

    <!-- 文件新增/编辑弹窗 -->
    <div class="add">
      <el-dialog
        :title="isTitle ? '添加文件存储' : '编辑文件存储'"
        :visible.sync="dialogVisible"
        :before-close="handleClose"
        width="50%"
      >
        <div class="dialog-body">
          <el-form
            ref="fileForm"
            :model="fileForm"
            :rules="rules"
            label-width="150px"
            class="demo-fileForm"
          >
            <el-form-item
              label="存储类型"
              prop="type"
            >
              <el-select
                v-model="fileForm.type"
                placeholder="请选择存储类型"
              >
                <el-option
                  v-for="item in storageType"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item
              label="名称"
              prop="name"
            >
              <el-input
                v-model="fileForm.name"
                placeholder="请输入文件名称"
              />
            </el-form-item>
            <el-form-item
              label="业务类型"
              prop="business"
            >
              <el-select
                v-model="fileForm.business"
                multiple
                placeholder="请选择业务类型"
              >
                <el-option
                  v-for="item in businessType"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <div v-if="fileForm.type">
              <div class="param">参数：</div>
              <template v-if="fileForm.type === '本地存储'">
                <el-form-item
                  label="服务器IP地址"
                  prop="ip"
                >
                  <el-input
                    v-model="fileForm.ip"
                    placeholder="请输入服务器IP地址"
                  />
                </el-form-item>
              </template>
              <template v-else>
                <el-form-item
                  label="bucketname："
                  prop="bucketname"
                >
                  <el-input
                    v-model="fileForm.bucketname"
                    placeholder="请输入bucketname"
                  />
                </el-form-item>
                <el-form-item
                  label="endpoint："
                  prop="endpoint"
                >
                  <el-input
                    v-model="fileForm.endpoint"
                    placeholder="请输入endpoint"
                  />
                </el-form-item>
                <el-form-item
                  label="accessKeyId："
                  prop="accessKeyId"
                >
                  <el-input
                    v-model="fileForm.accessKeyId"
                    placeholder="请输入accessKeyId"
                  />
                </el-form-item>
                <el-form-item
                  label="accessKeySecret："
                  prop="accessKeySecret"
                >
                  <el-input
                    v-model="fileForm.accessKeySecret"
                    placeholder="请输入accessKeySecret"
                  />
                </el-form-item>
              </template>
            </div>
          </el-form>
        </div>
        <span
          slot="footer"
          class="dialog-footer"
        >
          <el-button
            type="primary"
            :disabled="isDisable"
            @click="isTitle ? submitForm('fileForm') : updateForm('fileForm')"
          >
            <span v-if="isTitle">立即创建</span>
            <span v-else>确定修改</span>
          </el-button>
          <el-button @click="resetForm('fileForm')">重置</el-button>
        </span>
      </el-dialog>
    </div>

    <!-- 导入弹窗 -->
    <div>
      <el-dialog
        title="导入"
        :visible.sync="fileInVisible"
        width="30%"
        :before-close="handleClose"
      >
        <div class="down">
          <div>1、下载模板文件根据模板编辑excle文件</div>
          <div class="downitem">
            <span><i class="el-icon-document" /> muban.xlsx</span>
            <el-link
              :underline="false"
              icon="el-icon-download"
              @click="downFile"
            >点 击 下 载 Excle 模 板</el-link>
            <!-- <a class='download' :href='downloadhttp' download=""  title="下载" >点 击 下 载 Excle 模 板 </a> -->
          </div>
          <div>2、选取编辑好的excle文件上传</div>
          <div class="downitem">
            <el-upload
              ref="upload"
              :limit="1"
              :file-list="fileList"
              :on-change="filechange"
              :on-remove="removeUpload"
              :http-request="uploadFile"
              action=""
              accept="application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            >
              <el-button
                :disabled="isBtnShow===false"
                size="small"
                type="primary"
              >点击选取文件</el-button>
            </el-upload>
          </div>
        </div>
        <div
          slot="footer"
          class="dialog-footer"
        >
          <el-button @click="fileInVisible = false">取 消</el-button>
          <el-button
            type="primary"
            @click="uploadSubmit"
          >确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import {
  getFileInfo,
  addFileStorage,
  updateFileStorage,
  removeFileStorage,
  importExcel,
  downloadTemp,
  exportExcel
} from '@/api/systools/filestorage'
import Pagination from '@/components/Pagination'
import waves from '@/directive/waves' // 水波纹指令

export default {
  directives: {
    waves
  },
  components: { Pagination },
  data() {
    return {
      // downloadhttp: 'http://localhost:9528/static/fonts/element-icons.535877f5.woff',
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        name: ''
      },
      listLoading: true,
      storageType: ['本地存储', '阿里云OSS'],
      businessType: ['通用', '音频', '视频', '机密', '其它'],
      dialogVisible: false,
      isTitle: true,
      tableData: [],
      fileForm: {
        name: '',
        type: '',
        business: [],
        bucketname: '',
        endpoint: '',
        accessKeyId: '',
        accessKeySecret: ''
      },
      rules: {
        type: [
          { required: true, message: '请选择存储类型', trigger: 'change' }
        ],
        name: [
          { required: true, message: '请输入存储名称', trigger: 'blur' },
          { min: 3, max: 64, message: '存储名称长度为3到64个字符哦~', trigger: 'change' }
        ],
        business: [
          {
            required: true,
            message: '请选择业务类型(可多选)',
            trigger: 'change'
          }
        ],
        bucketname: [
          { required: true, message: '请填写bucketname参数', trigger: 'blur' },
          { min: 3, max: 64, message: '请填写bucketname参数长度为3到64个字符哦~', trigger: 'change' }
        ],
        endpoint: [
          { required: true, message: '请填写参数', trigger: 'blur' },
          { min: 3, max: 64, message: 'endpoint长度为3到64个字符哦~', trigger: 'change' }
        ],
        ip: [{ required: true, message: '请填写服务器IP', trigger: 'blur' }],
        accessKeyId: [
          { required: true, message: '请填写accessKeyId参数', trigger: 'blur' },
          { min: 3, max: 64, message: 'accessKeyId长度为3到64个字符哦~', trigger: 'change' }
        ],
        accessKeySecret: [
          {
            required: true,
            message: '请填写accessKeySecret参数',
            trigger: 'blur'
          },
          { min: 3, max: 64, message: 'accessKeySecret长度为3到64个字符哦~', trigger: 'change' }
        ]
      },
      fileInVisible: false,
      isBtnShow: true,
      fileList: [],
      upload: {},
      upload_flag: false,
      isDisable: false
    }
  },
  mounted() {
    // 获取页面数据
    this.getInfo()
  },
  methods: {
    // 获取页面信息
    getInfo() {
      this.listLoading = false
      getFileInfo(this.listQuery).then(res => {
        // 将返回的数据赋给tableData、fileType、resourceType
        this.tableData = res.data.content
        this.total = res.data.totalElements
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    // 搜索
    handleFilter() {
      this.getInfo()
    },
    // 打开添加文件存储弹窗
    addFile() {
      // 设置弹窗可见，标题切换
      this.isDisable = false
      this.dialogVisible = true
      this.isTitle = true
      this.resetForm()
      this.$nextTick(() => {
        this.$refs['fileForm'].clearValidate()
      })
    },
    // 打开编辑文件存储弹窗
    handleEdit(index, row) {
      // 设置弹窗可见，标题切换
      var that = this

      var fileForm = Object.assign({}, row)
      fileForm.business = fileForm.businessType.split(',')
      that.fileForm = fileForm
      this.isDisable = false
      that.dialogVisible = true
      that.isTitle = false
      this.$nextTick(() => {
        this.$refs['fileForm'].clearValidate()
      })
    },
    // 删除
    handleDelete(index, row) {
      this.$confirm('此操作将永久删除该行, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          removeFileStorage(row).then(res => {
            this.tableData.splice(index, 1)
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },
    // 关闭弹窗
    handleClose(done) {
      done()
      this.resetForm()
    },
    // 创建
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.isDisable = true
          this.fileForm.businessType = this.fileForm.business.join(',')
          addFileStorage(this.fileForm).then(res => {
            this.tableData.push(res.data)
            this.dialogVisible = false
            // 添加成功提示
            this.$notify({
              title: '成功',
              message: '添加成功',
              type: 'success'
            })
            // 清空弹窗
            this.$refs[formName].resetFields()
          }).catch(response => {
            this.isDisable = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    // 修改
    updateForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.isDisable = true
          // 拷贝源对象
          const fileData = Object.assign({}, this.fileForm)
          fileData.businessType = fileData.business.join(',')
          updateFileStorage(fileData).then(res => {
            // 遍历tableData
            this.tableData.forEach((item, index) => {
              if (item.id === this.fileForm.id) {
                this.tableData.splice(index, 1, res.data)
              }
            })
            // 关闭弹窗
            this.dialogVisible = false
            // 添加成功提示
            this.$notify({
              title: '成功',
              message: '修改成功',
              type: 'success'
            })
            // 清空弹窗
            this.$refs[formName].resetFields()
          }).catch(response => {
            this.isDisable = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    // 重置
    resetForm(formName) {
      this.fileForm = {
        name: '',
        type: '',
        business: [],
        bucketname: '',
        endpoint: '',
        accessKeyId: '',
        accessKeySecret: ''
      }
    },
    // 导出
    outFile() {
      exportExcel().then(res => {
        this.downloads(res, '存储.xls')
      })
    },
    // 点击下载
    downFile() {
      var that = this
      downloadTemp().then(res => {
        that.downloads(res, '模板.xls')
      })
    },
    // 创建模板下载链接
    downloads(data, name) {
      if (!data) {
        return
      }
      const url = window.URL.createObjectURL(new Blob([data], {
        type:
          'application/vnd.ms-excel;charset=utf-8'
      })) // 创建下载的链接
      const link = document.createElement('a')
      link.style.display = 'none'
      link.href = url
      link.setAttribute('download', name) // 下载后文件名
      document.body.appendChild(link)
      link.click() // 点击下载
      document.body.removeChild(link) // 下载完成移除元素
      window.URL.revokeObjectURL(url) // 释放掉blob对象
    },
    filechange(file, fileList) {
      // 如果没有选取文件，可以加个字段来控制流程
      if (file.name) {
        this.upload_flag = true
      }
    },
    // 上传文件
    uploadFile: function(param) {
      this.upload = param
      this.isBtnShow = false
    },
    // 移除上传的文件
    removeUpload() {
      this.isBtnShow = true
    },
    // 确定上传
    uploadSubmit() {
      if (this.upload_flag) {
        var uploadData = new FormData()
        uploadData.append(
          this.upload.filename,
          this.upload.file,
          this.upload.file.name
        )
        importExcel(uploadData).then(res => {
          this.$notify({
            title: '提示',
            message: '上传成功',
            type: 'success'
          })
          this.getInfo()
          this.fileInVisible = false
          this.removeUpload()
          this.fileList = []
          this.upload_flag = false
        })
      } else {
        this.$notify({
          title: '提示',
          message: '请选取文件后再点击确定按钮',
          type: 'warning'
        })
      }
    }
  }
}
</script>
<style>
.file-wrap {
  padding: 30px 5%;
}
.el-input-group__prepend {
  width: 165px;
}

.add .el-input {
  width: 300px;
}

.param {
  width: 100px;
  text-align: center;
  line-height: 40px;
  color: #606266;
  font-weight: 700;
}

.down {
  line-height: 30px;
}

.downitem {
  padding: 0 20px;
}

.downitem a {
  margin-left: 50px;
  text-decoration: underline;
  color: #46a6ff;
}
</style>

