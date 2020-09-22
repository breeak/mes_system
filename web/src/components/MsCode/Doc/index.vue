<template>
  <div>
    <svg-icon icon-class="link" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'MsCodeDoc',
  data() {
    return {
      url: 'https://www.github.com'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
