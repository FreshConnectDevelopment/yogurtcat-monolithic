<!-- 单文件上传，可回显 -->
<template>
  <el-upload
    v-loading="loading"
    :action="uploadPath"
    :headers="headers"
    :on-success="handleGalleryUrl"
    :on-remove="handleRemove"
    :show-file-list="listType === 'picture-card' ? false : true"
    :accept="accept"
    :list-type="listType"
    :file-list="galleryFileList"
    :before-upload="beforeUpload"
    class="singleWidth"
  >
    <img v-if="fileOject.url && listType === 'picture-card'" :src="fileOject.url" class="uploadimg">
    <i v-else-if="listType === 'picture-card'" class="el-icon-plus" />
    <el-button v-else :style="{display: isShow}" size="small" type="primary">点击上传</el-button>
    <span v-if="fileOject.url && listType === 'picture-card'" class="deleteimg" @click.stop="handleRemove"><i class="el-icon-delete" /></span>
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
    // 限制上传格式
    accept: {
      type: String,
      default: '.*'
    },
    // 回显
    file: {
      type: [String, Object],
      default: null
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
      fileOject: {
        url: ''
      },
      uploadPath: process.env.VUE_APP_BASE_API + '/resource/upload/通用/unfiled',
      isShow: 'block',
      loading: false
    }
  },
  watch: {
    file() {
      this.watchFile()
    }
  },
  mounted() {
    this.uploadPath = upload(encodeURI(this.path))
    this.watchFile()
  },
  methods: {
    watchFile() {
      if (this.file) {
        if (typeof this.file === 'string') {
          this.fileOject.url = this.file
          this.galleryFileList = [{ url: this.file }]
        } else if (typeof this.file === 'object') {
          if (this.file.url) {
            this.fileOject = this.file
            this.galleryFileList = [this.file]
            this.isShow = 'none'
          } else {
            this.isShow = 'block'
          }
        }
      } else {
        this.fileOject = {
          url: ''
        }
      }
    },
    emitInput(val) {
      this.$emit('input', val)
    },
    handleGalleryUrl(response, file, fileList) {
      if (response.code === 20000) {
        this.fileOject.url = response.data
        this.fileOject.name = file.name

        this.isShow = 'none'
        this.emitInput(this.fileOject)
        this.loading = false
      } else {
        this.$message.error(response.message)
        this.loading = false
      }
    },
    handleRemove: function(file, fileList) {
      this.fileOject = {
        url: ''
      }
      this.isShow = 'block'
      this.emitInput(this.fileOject)
    },
    beforeUpload(file) {
      this.loading = true
      const isLt = file.size / 1024 / 1024 < this.fileLt

      if (!isLt) {
        this.$message.error('上传文件大小不能超过 ' + this.fileLt + 'MB!')
        this.loading = false
      }
      return isLt
    }
  }
}
</script>

<style>
.el-upload {
  position: relative;
}
.uploadimg {
  width: 100%;
  height: 100%;
}
.deleteimg {
  display: block;
  width: 28px;
  height: 28px;
  line-height: 26px;
  background: rgba(0,0,0,.3);
  position: absolute;
  top: 0;
  right: 0;
  z-index: 2;
}
.deleteimg i {
  font-size: 14px;
  color: #fff;
}

/* 限制上传组件宽度，方便loading图标显示 */
.singleWidth div:first-child {
  width: 150px;
}
</style>
