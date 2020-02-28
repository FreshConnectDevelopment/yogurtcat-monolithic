<template>
  <div>
    <el-form
      ref="form"
      label-width="80px"
    >
      <el-form-item
        label="验证码"
        style="width: 570px"
      >
        <el-input
          id="code_input"
          v-model="value"
          placeholder="请输入验证码"
          size="small"
          style="width: 187px;float: left;"
          maxlength="5"
        />

        <div class="img-verify">
          <canvas
            ref="verify"
            :width="width"
            :height="height"
            @click="handleDraw"
          />
        </div>

        <el-button
          type="primary"
          @click="yanzheng"
        >提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      pool: 'ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890', // 字符串
      width: 160, // 图形宽度
      height: 40, // 图形高度
      imgCode: '', // 图形生成的字符串
      value: '', // 输入框输入的字符串
      strLen: 5, // 生成的字符串长度, 如需更多的字符，需要同时设置更大的宽度
      disturbLine: 5, // 生成的干扰线
      disturbDot: 40 // 生成的干扰点
    }
  },
  mounted() {
    this.draw()
  },
  methods: {
    // 1.随机数
    randomNum(min, max) {
      return parseInt(Math.random() * (max - min) + min)
    },
    // 2.随机颜色
    randomColor(min, max) {
      const r = this.randomNum(min, max)
      const g = this.randomNum(min, max)
      const b = this.randomNum(min, max)
      return `rgb(${r},${g},${b})`
    },
    // 点击图片重新绘制
    handleDraw() {
      this.draw()
    },
    // 绘制图片
    draw() {
      // 3.填充背景颜色，背景颜色要浅一点
      const ctx = this.$refs.verify.getContext('2d')
      // 填充颜色
      ctx.fillStyle = this.randomColor(180, 230)
      // 填充的位置
      ctx.fillRect(0, 0, this.width, this.height)
      // 定义paramText
      let imgCode = ''
      // 4.随机产生字符串，并且随机旋转
      for (let i = 0; i < this.strLen; i++) {
        // 随机的this.strLen个字
        const text = this.pool[this.randomNum(0, this.pool.length)]
        imgCode += text
        // 随机的字体大小
        const fontSize = this.randomNum(18, 40)
        // 字体随机的旋转角度
        const deg = this.randomNum(-30, 30)
        /*
         * 绘制文字并让这几个文字在不同的位置显示的思路 :
         * 1、定义字体
         * 2、定义对齐方式
         * 3、填充不同的颜色
         * 4、保存当前的状态（以防止以上的状态受影响）
         * 5、平移translate()
         * 6、旋转 rotate()
         * 7、填充文字
         * 8、restore出栈
         * */
        ctx.font = fontSize + 'px Simhei' // 1
        ctx.textBaseline = 'top' // 2
        ctx.fillStyle = this.randomColor(80, 150) // 3
        /*
         * save() 方法把当前状态的一份拷贝压入到一个保存图像状态的栈中。
         * 这就允许您临时地改变图像状态，
         * 然后，通过调用 restore() 来恢复以前的值。
         * save是入栈，restore是出栈。
         * 用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。 restore：用来恢复Canvas之前保存的状态。防止save后对Canvas执行的操作对后续的绘制有影响。
         *
         * */
        ctx.save() // 4
        ctx.translate(30 * i + 15, 15) // 5
        ctx.rotate((deg * Math.PI) / 180) // 6
        // fillText() 方法在画布上绘制填色的文本。文本的默认颜色是黑色。
        // 请使用 font 属性来定义字体和字号，并使用 fillStyle 属性以另一种颜色/渐变来渲染文本。
        // context.fillText(text,x,y,maxWidth);
        ctx.fillText(text, -15 + 5, -15) // 7
        ctx.restore() // 8
      }
      // 5.随机产生5条干扰线,干扰线的颜色要浅一点
      for (let i = 0; i < this.disturbLine; i++) {
        ctx.beginPath()
        ctx.moveTo(
          this.randomNum(0, this.width),
          this.randomNum(0, this.height)
        )
        ctx.lineTo(
          this.randomNum(0, this.width),
          this.randomNum(0, this.height)
        )
        ctx.strokeStyle = this.randomColor(180, 230)
        ctx.closePath()
        ctx.stroke()
      }
      // 6.随机产生40个干扰的小点
      for (let i = 0; i < this.disturbDot; i++) {
        ctx.beginPath()
        ctx.arc(
          this.randomNum(0, this.width),
          this.randomNum(0, this.height),
          1,
          0,
          2 * Math.PI
        )
        ctx.closePath()
        ctx.fillStyle = this.randomColor(150, 200)
        ctx.fill()
      }
      // 将生成的五个字传递给父组件
      // this.$emit('imgCode', imgCode)

      this.imgCode = imgCode
    },
    // 验证
    yanzheng() {
      if (this.imgCode.toLowerCase() === this.value.toLowerCase()) {
        this.$notify({
          title: '成功',
          message: '验证码正确',
          type: 'success'
        })
      } else {
        this.$notify.error({
          title: '错误',
          message: '验证码错误'
        })
      }
    }
  }
}
</script>
<style type="text/css">
.img-verify {
  width: 160px;
  height: 40px;
  float: left;
  margin: 0 20px;
}

.img-verify canvas {
  cursor: pointer;
}
</style>
