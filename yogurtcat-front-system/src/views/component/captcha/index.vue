<template>
  <div class="app-container">
    <verify />

    <slideVerify
      :slider-text="text"
      @success="onSuccess"
      @fail="onFail"
      @refresh="onRefresh"
    />
    <div>{{ msg }}</div>
  </div>
</template>
<script>
import verify from './verify'
import slideVerify from './slideVerify'
export default {
  components: {
    verify,
    slideVerify
  },
  data() {
    return {
      msg: '',
      text: '拖动左边滑块完成上方拼图'
    }
  },
  methods: {
    onSuccess() {
      this.msg = '验证成功'
    },
    onFail() {
      this.msg = ''
    },
    onRefresh() {
      this.msg = ''
    }
  }
}
</script>
