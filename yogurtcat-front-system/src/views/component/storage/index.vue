<template>
  <div>
    <div class="btm">
      <label>单个图片上传</label>
      <singleImage :file="o.url" path="通用/a" @input="setVlaue" />
    </div>
    <div class="btm">
      <label>单个文件上传</label>
      <singleImage :file="o.file" list-type="text" @input="setFile" />
    </div>
    <div class="btm">
      <label>多个上传</label>
      <multipleImage :file-list="o.urls" @input="setVlaues" />
    </div>
    <div class="btm">
      <label>多个上传列表形式</label>
      <multipleImage :file-list="o.files" :file-list-type="2" list-type="text" @input="setFiles" />
    </div>
  </div>
</template>

<script>
import singleImage from './singleImage'
import multipleImage from './multipleImage'

export default {
  components: { singleImage, multipleImage },
  data() {
    return {
      o: {
        url: 'https://yogurtcat.oss-cn-shenzhen.aliyuncs.com/unfiled/9794a8b51ba34d66ae31d7eb2d6d8e64.jpg',
        file: { name: 'test.jpg', url: 'https://yogurtcat.oss-cn-shenzhen.aliyuncs.com/unfiled/9794a8b51ba34d66ae31d7eb2d6d8e64.jpg' },
        urls: /* [] */['https://yogurtcat.oss-cn-shenzhen.aliyuncs.com/unfiled/9794a8b51ba34d66ae31d7eb2d6d8e64.jpg'],
        files: /* [] */[{ name: 'test.jpg', url: 'https://yogurtcat.oss-cn-shenzhen.aliyuncs.com/unfiled/9794a8b51ba34d66ae31d7eb2d6d8e64.jpg' }]
      }
    }
  },
  methods: {
    setVlaues: function(value) {
      this.o.urls = value
    },
    setVlaue: function(value) {
      this.o.url = value.url
    },
    setFiles: function(value) {
      this.o.files = value
    },
    setFile: function(value) {
      this.o.file = value
    }
  }
}
</script>

<style>
.btm {
  margin: 0 0 20px 20px;
  line-height: 40px;
}
</style>
