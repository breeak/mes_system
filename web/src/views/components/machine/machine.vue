<template>
  <el-main >
    <el-row :gutter="10"   type="flex" align="middle"  justify="space-between" class="card-panel-tittle" >
      <div class="card-panel-header">织造生产实时监控</div>
      <el-row type="flex" align="middle"  justify="end">
        <div class="card-panel-flag" v-for="mac_status in mac_common_status" :style="{backgroundColor:mac_status.remark}">{{mac_status.dictLabel}}</div>
      </el-row>
    </el-row>
    <el-row v-for="i in allColumn"  :gutter="10"  type="flex" justify="space-between"  >
      <el-col v-for="j in allRow" :style="styleObject"  class="card-panel-col">
        <machine-base :ref="i+'-'+j" :shaftList="shaftList"  ></machine-base>
      </el-col>
    </el-row>
  </el-main>
</template>

<script>
  import CountTo from 'vue-count-to'
  import machineBase from "./machinepanel"

  import { listMachine } from "@/api/manufacture/machine";

  export default {
    components: {
      CountTo,
      machineBase
    },
    props: {
      machineList: {
        type: Array,
        required: true
      },
      shaftList: {
        type: Array,
      },
      allColumn:{
        type: Number,
        required: true
      },
      allRow:{
        type: Number,
        required: true
      },
      mac_common_status:{
        type: Array,
        required: true
      },
    },
    data() {
      return {
        macCode1:'',
        macCode2:'',

        styleObject:{
          width:'10%',
        }
      }
    },
    beforeCreate(){
    },
    created() {
    },
    computed: {
    },
    mounted(){
    },
    watch:{
      machineList:function(val,oldVal){
        this.machineList.forEach((machine, index, array) =>{
          if (this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`]){
            this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].machine = machine
            this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].mac_common_status = this.mac_common_status
          }
        })
      },
    },
    updated:function(){
      this.machineList.forEach((machine, index, array) =>{
        if (this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`]){
          this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].machine = machine
          this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].mac_common_status = this.mac_common_status
        }
      })
    },
    methods: {

    }
  }
</script>

<style lang="scss" scoped>
  .card-panel-tittle{
    background-color: #F2F6FC;
    margin-bottom: 10px;

    .card-panel-header {
      font-weight: bold;
      margin-left: 10px;
      font-size: 20px;
    }

    .card-panel-flag {
      margin-top: 15px;
      margin-bottom: 15px;
      margin-left: 10px;
      padding: 10px;
      width: 80px;
      text-align: center;
    }

  }
  .card-panel-col {
    margin-bottom: 10px;
  }

</style>
