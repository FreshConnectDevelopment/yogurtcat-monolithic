<template>
  <div class="app-container">
    <div class="item">
      <div class="itemhead">
        <span>RabbitMQ配置</span>
      </div>
      <div class="itembody">
        <el-form
          ref="rabbit"
          :model="rabbit"
          :rules="rules"
          label-width="130px"
        >
          <el-form-item label="地址" prop="host">
            <el-input v-model="rabbit.host" placeholder="请输入地址" />
          </el-form-item>
          <el-form-item label="端口" prop="port">
            <el-input v-model.number="rabbit.port" placeholder="请输入端口号" maxlength="8" />
          </el-form-item>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="rabbit.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="rabbit.password" placeholder="请输入密码" />
          </el-form-item>
          <el-form-item label="是否重试">
            <el-switch v-model="rabbit.enabled" />
          </el-form-item>
          <el-form-item label="重试间隔（s）" prop="interval">
            <el-input v-model.number="rabbit.interval" placeholder="请输入重试间隔" maxlength="8" />
          </el-form-item>
          <el-form-item align="right">
            <el-button
              type="primary"
              @click="rabbitSubmit"
            >保存</el-button>
            <el-button
              plain
              @click="rabbitReset"
            >重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
import { getRabbitConfig, saveRabbit, removeRabbit } from '@/api/systools/rabbit'
export default {
  data() {
    return {
      rabbit: {
        host: '',
        port: '',
        username: '',
        password: '',
        enabled: false,
        interval: ''
      },
      rules: {
        host: [
          { required: true, message: '请输入地址', trigger: 'blur' },
          { min: 0, max: 25, message: '地址不能超过25个字符哦~', trigger: 'blur' },
          { pattern: /\s\S+|S+\s|\S/, message: '地址不能只是空格' }
        ],
        port: [
          { required: true, type: 'number', message: '请输入端口，仅支持数字', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 0, max: 25, message: '用户名不能超过25个字符哦~', trigger: 'blur' },
          { pattern: /\s\S+|S+\s|\S/, message: '用户名不能只是空格' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 0, max: 25, message: '密码不能超过25个字符哦~', trigger: 'blur' },
          { pattern: /\s\S+|S+\s|\S/, message: '密码不能只是空格' }
        ],
        interval: [
          { required: false, type: 'number', message: '请输入数字, 重试间隔仅支持数字', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.getRabbitConfig()
  },
  methods: {
    getRabbitConfig() {
      var that = this
      getRabbitConfig().then(res => {
        var data = res.data
        if (data) {
          that.rabbit = data
        }
      })
    },
    rabbitSubmit() {
      this.$refs.rabbit.validate(valid => {
        if (valid) {
          var that = this
          saveRabbit(this.rabbit).then(res => {
            that.rabbit = res.data
            this.$notify({
              title: '成功',
              message: '保存成功',
              type: 'success'
            })
          })
        }
      })
    },
    rabbitReset() {
      this.$confirm('此操作将删除配置, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        var that = this
        removeRabbit(this.rabbit).then(res => {
          that.rabbit = {
            host: '',
            port: '',
            username: '',
            password: '',
            enabled: false,
            interval: ''
          }
        })
      }).catch(() => {
      })
    }
  }
}
</script>

<style lang="scss">
  @import './../index.scss';
</style>
