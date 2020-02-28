<template>
  <div :class="{fullscreen:fullscreen}" class="tinymce-container editor-container" :style="{width:containerWidth}">
    <textarea :id="tinymceId" class="tinymce-textarea" />
  </div>
</template>

<script>
import plugins from './plugins'
import toolbar from './toolbar'
import load from './dynamicLoadScript'
import { imgUploadTinymce } from '@/api/systools/filestorage'

const tinymceCDN = 'https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/tinymce.min.js'

export default {
  name: 'Tinymce',
  props: {
    id: {
      type: String,
      default: function() {
        return (
          'vue-tinymce-' +
            +new Date() +
            ((Math.random() * 1000).toFixed(0) + '')
        )
      }
    },
    value: {
      type: String,
      default: ''
    },
    toolbar: {
      type: Array,
      required: false,
      default() {
        return []
      }
    },
    menubar: {
      type: String,
      default: 'file edit insert view format table'
    },
    height: {
      type: [Number, String],
      required: false,
      default: 360
    },
    width: {
      type: [Number, String],
      required: false,
      default: 'auto'
    }
  },
  data() {
    return {
      hasChange: false,
      hasInit: false,
      tinymceId: this.id,
      fullscreen: false,
      languageTypeList: {
        'en': 'en',
        'zh': 'zh_CN',
        'es': 'es_MX',
        'ja': 'ja'
      },
      checkerTimeout: null
    }
  },
  computed: {
    language() {
      return this.languageTypeList[this.$store.getters.language]
    },
    containerWidth() {
      const width = this.width
      if (/^[\d]+(\.[\d]+)?$/.test(width)) { // matches `100`, `'100'`
        return `${width}px`
      }
      return width
    }
  },
  watch: {
    value(val) {
      if (!this.hasChange && this.hasInit) {
        this.$nextTick(() =>
          window.tinymce.get(this.tinymceId).setContent(val || '')
        )
      }
    },
    language() {
      this.destroyTinymce()
      this.$nextTick(() => this.initTinymce())
    }
  },
  mounted() {
    this.init()
  },
  activated() {
    if (window.tinymce) {
      this.initTinymce()
    }
  },
  deactivated() {
    this.destroyTinymce()
  },
  destroyed() {
    this.destroyTinymce()
  },
  methods: {
    init() {
      // dynamic load tinymce from cdn
      load(tinymceCDN, (err) => {
        if (err) {
          this.$message.error(err.message)
          return
        }
        this.initTinymce()
      })
    },
    initTinymce() {
      const _this = this
      window.tinymce.init({
        language: this.language,
        selector: `#${this.tinymceId}`,
        height: this.height,
        body_class: 'panel-body ',
        object_resizing: false,
        toolbar: this.toolbar.length > 0 ? this.toolbar : toolbar,
        menubar: this.menubar,
        plugins: plugins,
        end_container_on_empty_block: true,
        powerpaste_word_import: 'clean',
        code_dialog_height: 450,
        code_dialog_width: 1000,
        advlist_bullet_styles: 'square',
        advlist_number_styles: 'default',
        imagetools_cors_hosts: ['www.tinymce.com', 'codepen.io'],
        default_link_target: '_blank',
        link_title: false,
        nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
        image_title: false,
        automatic_uploads: true,
        file_picker_types: 'file image media',
        file_picker_callback: function(cb, value, meta) {
          var input = document.createElement('input')
          input.setAttribute('type', 'file')
          input.onchange = function() {
            var file = this.files[0]

            var reader = new FileReader()
            reader.onload = function() {
              var id = 'blobid' + new Date().getTime()
              var blobCache =
                  window.tinymce.activeEditor.editorUpload.blobCache
              var base64 = reader.result.split(',')[1]
              var blobInfo = blobCache.create(id, file, base64)
              blobCache.add(blobInfo)
              const formData = new FormData()
              formData.append('file', blobInfo.blob(), blobInfo.filename())
              imgUploadTinymce(formData).then(response => {
                cb(response.data, { title: file.name })
              })
            }
            reader.readAsDataURL(file)
          }

          input.click()
        },
        init_instance_callback: editor => {
          if (_this.value) {
            editor.setContent(_this.value)
          }
          _this.hasInit = true
          editor.on('NodeChange Change KeyUp SetContent', () => {
            this.hasChange = true
            if (this.checkerTimeout != null) {
              clearTimeout(this.checkerTimeout)
            }
            this.checkerTimeout = setTimeout(() => {
              this.hasChange = false
            }, 300)

            this.$emit('input', editor.getContent())
          })
        },
        setup(editor) {
          editor.on('FullscreenStateChanged', e => {
            _this.fullscreen = e.state
          })
        }
      })
    },
    destroyTinymce() {
      if (window.tinymce.get(this.tinymceId)) {
        window.tinymce.get(this.tinymceId).destroy()
      }
    },
    setContent(value) {
      window.tinymce.get(this.tinymceId).setContent(value)
    },
    getContent() {
      window.tinymce.get(this.tinymceId).getContent()
    },
    imageSuccessCBK(arr) {
      const _this = this
      arr.forEach(v => {
        window.tinymce
          .get(_this.tinymceId)
          .insertContent(`<img class="wscnph" src="${v.url}" >`)
      })
    },
    imageToolsProxy(value) {
      alert(value)
    }
  }
}
</script>

<style scoped>
  .tinymce-container {
    position: relative;
  }
  .tinymce-container >>> .mce-fullscreen {
    z-index: 10000;
  }
  .tinymce-textarea {
    visibility: hidden;
    z-index: -1;
  }
  .editor-upload-btn {
    display: inline-block;
  }
</style>
