<template>
  <div class="app-container">
    <div class="item">
      <div class="itemhead">
        <span>微信支付</span>
      </div>
      <div class="itembody">
        <el-form
          ref="wechatPay"
          :rules="rules"
          :model="wechatPay"
          label-width="120px"
        >
          <el-form-item label="服务号appID" prop="appId">
            <el-input v-model="wechatPay.appId" placeholder="请输入服务号appID" />
          </el-form-item>
          <el-form-item label="商户号" prop="mchId">
            <el-input v-model="wechatPay.mchId" placeholder="请输入商户号" />
          </el-form-item>
          <el-form-item label="商户apiSecret" prop="apiSecret">
            <el-input v-model="wechatPay.apiSecret" placeholder="请输入商户apiSecret" />
          </el-form-item>
          <el-form-item label="上传商户证书">
            <el-upload
              :file-list="fileList"
              :on-remove="removeFile"
              :http-request="uploadFile"
              action=""
            > <!--此处使用自定义上传实现http-request-->
              <el-button v-if="isBtnShow" size="small" type="success">点击上传证书</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item align="right">
            <el-button
              type="primary"
              @click="wechatPaySubmit"
            >保存</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
import { savePayment, getPayment } from '@/api/systools/payment'
export default {
  data() {
    return {
      fileList: [],
      wechatPay: {
        appId: '',
        mchId: '',
        apiSecret: '',
        fileObj: {}
      },
      rules: {
        appId: [
          { required: true, message: '服务号appID不能为空', trigger: ['blur', 'change'] },
          { min: 0, max: 25, message: '服务号appID不能超过25个字符哦~', trigger: ['blur', 'change'] },
          { pattern: /\s\S+|S+\s|\S/, message: '服务号appID不能只是空格' }
        ],
        mchId: [
          { required: true, message: '商户号不能为空', trigger: ['blur', 'change'] },
          { min: 0, max: 25, message: '商户号不能超过25个字符哦~', trigger: ['blur', 'change'] },
          { pattern: /\s\S+|S+\s|\S/, message: '商户号不能只是空格' }
        ],
        apiSecret: [
          { required: true, message: '商户apiSecret不能为空', trigger: ['blur', 'change'] },
          { min: 0, max: 100, message: '商户apiSecret不能超过100个字符哦~', trigger: ['blur', 'change'] },
          { pattern: /\s\S+|S+\s|\S/, message: '商户apiSecret不能只是空格' }
        ]
      },
      roles: {
        type: Array,
        required: true
      },
      isBtnShow: true
    }
  },
  computed: {
  },
  mounted() {
    this.getPaymentInfo()
  },
  methods: {
    getPaymentInfo() {
      var that = this
      getPayment().then(res => {
        var data = res.data.content
        if (data) {
          for (const d of data) {
            if (d.type === '微信支付') {
              that.wechatPay = d
              if (d.certName) {
                that.fileList.push({ name: d.certName })
                that.isBtnShow = false
              }
            }
          }
        }
      })
    },
    wechatPaySubmit() {
      var formData = new FormData()
      if (this.wechatPay) {
        Object.keys(this.wechatPay).forEach(key => {
          if (key === 'fileObj') {
            return
          }
          formData.append(key, this.wechatPay[key])
        })
        if (this.wechatPay.fileObj && this.wechatPay.fileObj.file) {
          formData.append(this.wechatPay.fileObj.filename, this.wechatPay.fileObj.file, this.wechatPay.fileObj.file.name)
          this.wechatPay.certName = this.wechatPay.fileObj.file.name
        }
        formData.append('type', '微信支付')
      }
      this.$refs['wechatPay'].validate(valid => {
        if (valid) {
          savePayment(formData).then(response => {
            this.$notify({
              title: '提示',
              message: '保存成功',
              type: 'success'
            })
          })
        }
      })
    },
    uploadFile: function(param) {
      this.wechatPay.fileObj = param
      this.isBtnShow = false
    },
    removeFile() {
      this.wechatPay.certName = ''
      this.isBtnShow = true
    }
  }
}
</script>

<style lang="scss">
  @import './../index.scss';
</style>

