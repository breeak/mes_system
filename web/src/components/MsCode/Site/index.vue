<template>
  <div>
    <svg-icon icon-class="guide" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'MsCodeSite',
  data() {
    return {
      url: 'https://www.jiangnan.edu.cn/'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
