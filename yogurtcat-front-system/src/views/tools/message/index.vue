<template>
  <div class="app-container">
    <div class="item">
      <div class="itemhead">
        <span>阿里云短信服务</span>
      </div>
      <div class="itembody">
        <el-form
          ref="message"
          :model="message"
          :rules="rules"
          label-width="130px"
        >
          <el-form-item label="accessKeyId" prop="accessKeyId">
            <el-input v-model="message.accessKeyId" placeholder="请输入accessKeyId" />
          </el-form-item>
          <el-form-item label="accessKeySecret" prop="accessKeySecret">
            <el-input v-model="message.accessKeySecret" placeholder="请输入accessKeySecret" />
          </el-form-item>
          <el-form-item align="right">
            <el-button
              type="primary"
              @click="messageSubmit"
            >保存</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="item">
      <div class="itemhead">
        <span>测试</span>
      </div>
      <div class="itembody">
        <el-form
          ref="example"
          :model="example"
          :rules="exam"
          label-width="130px"
        >
          <el-form-item label="手机号" prop="phoneNumber">
            <el-input v-model="example.phoneNumber" placeholder="请输入手机号，多个英文逗号隔开" />
          </el-form-item>
          <el-form-item label="短信签名" prop="signName">
            <el-input v-model="example.signName" placeholder="请输入signName" />
          </el-form-item>
          <el-form-item label="短信模板ID" prop="templateCode">
            <el-input v-model="example.templateCode" placeholder="请输入短信模板ID" />
          </el-form-item>
          <el-form-item label="ccc">
            <div v-for="(list,index) in lists" :key="list.id" class="flex">
              <el-input v-model="list.title" placeholder="模板变量名" />
              <span>-</span>
              <el-input v-model="list.value" placeholder="模板变量值" />
              <div class="add-wrap">
                <span class="add" title="点击添加" @click="addItem">+</span>
                <span v-if="index !== 0" class="add" title="点击删除" @click="lists.splice(index, 1)">-</span>
              </div>
            </div>
          </el-form-item>
          <el-form-item align="right">
            <el-button
              type="primary"
              @click="sendMessageInfo"
            >发送</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
import { getMessageConfig, saveMessage, sendMessage } from '@/api/systools/message'
export default {
  data() {
    return {
      message: {
        accessKeyId: '',
        accessKeySecret: ''
      },
      rules: {
        accessKeyId: [
          { required: true, message: '请输入accessKeyId', trigger: 'blur' },
          { min: 0, max: 25, message: 'accessKeyId不能超过25个字符哦~', trigger: 'blur' },
          { pattern: /\s\S+|S+\s|\S/, message: 'accessKeyId不能只是空格' }
        ],
        accessKeySecret: [
          { required: true, message: '请输入accessKeySecret', trigger: 'blur' },
          { min: 0, max: 100, message: 'accessKeySecret不能超过100个字符哦~', trigger: 'blur' },
          { pattern: /\s\S+|S+\s|\S/, message: 'accessKeySecret不能只是空格' }
        ]
      },
      exam: {
        phoneNumber: [
          { required: true, message: '手机号不能为空', trigger: 'blur' },
          { pattern: /^1[3456789]\d{9}$/i, message: '请输入正确的手机号' }
        ],
        signName: [{ required: true, message: '请输入短信签名', trigger: 'blur' }],
        templateCode: [{ required: true, message: '请输入短信模板ID', trigger: 'blur' }]
      },
      example: {
        phoneNumber: '',
        signName: '',
        templateCode: ''
      },
      lists: [
        {
          title: '',
          value: ''
        }
      ]
    }
  },
  computed: {
  },
  mounted() {
    this.getMessageConfig()
  },
  methods: {
    getMessageConfig() {
      var that = this
      getMessageConfig().then(res => {
        var data = res.data
        if (data) {
          for (const d of data) {
            if (d.type === '短信') {
              that.message = d
            }
          }
        }
      })
    },
    messageSubmit() {
      this.$refs.message.validate(valid => {
        if (valid) {
          var that = this
          that.message.type = '短信'
          saveMessage(this.message).then(res => {
            that.message = res.data
            that.$notify({
              title: '成功',
              message: '保存成功',
              type: 'success'
            })
          })
        }
      })
    },
    sendMessageInfo() {
      var obj = Object.assign({}, this.example)
      obj.paramMap = {}
      this.lists.forEach(item => {
        if (item.title && item.value) {
          var title = item.title
          var value = item.value
          obj.paramMap[title] = value
        }
      })
      this.$refs.example.validate(valid => {
        if (valid) {
          var that = this
          that.message.type = '短信'
          sendMessage(obj).then(res => {
            that.$notify({
              title: '成功',
              message: '发送成功',
              type: 'success'
            })
          })
        }
      })
    },
    addItem() {
      this.lists.push({
        title: '',
        value: ''
      })
    }
  }
}
</script>

<style lang="scss">
  @import './../index.scss';
</style>
<style scoped>
.flex {
  display: flex;
  margin-bottom: 10px;
}

.flex span {
  display: inline-block;
  width: 40px;
  text-align: center;
}

.flex .add-wrap {
  width: 200px;
}

.flex .add {
  font-size: 22px;
  cursor: pointer;
  float: left;
}

.flex .add:hover {
  background: #eee;
}
</style>

