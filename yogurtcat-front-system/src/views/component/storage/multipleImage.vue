<!-- 多文件上传，可回显 -->
<template>
  <el-upload
    :action="uploadPath"
    :headers="headers"
    :limit="limit"
    :file-list="galleryFileList"
    :on-exceed="uploadOverrun"
    :on-success="handleGalleryUrl"
    :on-remove="handleRemove"
    :accept="accept"
    :list-type="listType"
    :before-upload="beforeUpload"
    :class="{disabled: fileList.length === 9 ? 1 : 0}"
    multiple
  >
    <i v-if="listType == 'picture-card'" class="el-icon-plus" />
    <el-button v-else size="small" type="primary">点击上传</el-button>
  </el-upload>
</template>

<script>
import { upload } from '@/api/system/resource'
import { getToken } from '@/utils/auth'

export default {
  props: {
    // 文件存储相对路径
    path: {
      type: String,
      default: '通用/unfiled'
    },
    // 头信息，如token，格式 {'X-Token': getToken()}
    headers: {
      type: Object,
      default() {
        return { 'X-Token': getToken() }
      }
    },
    // 上传数量限制
    limit: {
      type: Number,
      default: 100
    },
    // 返回格式
    // 1:url集合
    // 2:{name:'',url:''}对象集合，需要获取文件名情形
    // 本想兼顾图片获取宽高音视频获取时长，能力不够再说吧，可外部根据返回数据依业务获取
    fileListType: {
      type: Number,
      default: 1
    },
    // 回显集合，同上
    fileList: {
      type: Array,
      default() {
        return []
      }
    },
    // 限制上传格式
    accept: {
      type: String,
      default: '.*'
    },
    // 回显形式
    listType: {
      type: String,
      default: 'picture-card'
    },
    // 限制上传文件大小（最大可上传尺寸）
    fileLt: {
      type: Number,
      default: 3
    }
  },
  data() {
    return {
      galleryFileList: [],
      uploadPath: process.env.VUE_APP_BASE_API + '/resource/upload/通用/unfiled',
      // 不初始化标识
      initFlag: true
    }
  },
  watch: {
    fileList() {
      // 最多只做一次的初始化
      if (this.galleryFileList.length === 0 && this.initFlag) {
        this.watchFilelist()
      }
    }
  },
  mounted() {
    this.uploadPath = upload(this.path)
    this.watchFilelist()
  },
  methods: {
    watchFilelist() {
      this.galleryFileList = []
      if (this.fileList) {
        if (this.fileListType === 1) {
          for (var i = 0; i < this.fileList.length; i++) {
            this.galleryFileList.push({
              url: this.fileList[i]
            })
          }
        } else {
          this.galleryFileList = this.fileList
        }
      }
    },
    emitInput(val) {
      this.$emit('input', val)
    },
    uploadOverrun: function() {
      this.$message({
        type: 'error',
        message: '上传文件个数超出限制!最多上传' + this.limit + '个文件!'
      })
    },
    handleGalleryUrl(response, file, fileList) {
      if (response.code === 20000) {
        if (this.fileListType === 1) {
          this.fileList.push(response.data)
        } else {
          var o = {}
          o.name = file.name
          o.url = response.data
          this.fileList.push(o)
        }
        this.initFlag = false
        this.emitInput(this.fileList)
      } else {
        // 不回显 上传失败的图片
        for (var i in fileList) {
          if (fileList[i].response !== undefined) {
            if (fileList[i].response.code !== 20000) {
              fileList.splice(i, 1)
            }
          }
        }
        this.$message.error(response.message)
        this.loading = false
      }
    },
    handleRemove: function(file, fileList) {
      for (var i = 0; i < this.fileList.length; i++) {
        // 这里存在两种情况
        // 1. 如果所删除图片是刚刚上传的图片，那么图片地址是file.response.data.url
        //    此时的file.url虽然存在，但是是本机地址，而不是远程地址。
        // 2. 如果所删除图片是后台返回的已有图片，那么图片地址是file.url
        var url
        if (file.response === undefined) {
          url = file.url
        } else {
          url = file.response.data
        }

        if (this.fileListType === 1) {
          if (this.fileList[i] === url) {
            this.fileList.splice(i, 1)
          }
        } else {
          if (this.fileList[i].url === url) {
            this.fileList.splice(i, 1)
          }
        }
      }
      this.emitInput(this.fileList)
    },
    beforeUpload(file) {
      const isLt = file.size / 1024 / 1024 < this.fileLt

      if (!isLt) {
        this.$message.error('上传文件大小不能超过 ' + this.fileLt + 'MB!')
      }
      return isLt
    }
  }
}
</script>

<style>
.el-upload-list {
    width: 400px;
}
.disabled .el-upload--picture-card {
  display: none;
}
</style>
