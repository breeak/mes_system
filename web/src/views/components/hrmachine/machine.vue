<template>
  <el-main >
    <el-row v-for="i in allColumn"  :gutter="10"  type="flex" justify="space-between"  >
      <el-col v-for="j in allRow" :style="styleObject"  class="card-panel-col">
        <machine-base :ref="i+'-'+j" v-on:update="receiveData" :oldworker="oldworker" :newworker="newworker" ></machine-base>
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
      macworkerMap: {
        type: Object,
      },
      allColumn:{
        type: Number,
        required: true
      },
      allRow:{
        type: Number,
        required: true
      },
      oldworker:{
        type: Number,
      },
      newworker:{
        type: Number,
      },
    },
    data() {
      return {
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
        if (this.macworkerMap!=undefined && this.machineList!=undefined) {
          this.machineList.forEach((machine, index, array) => {
            if (this.$refs[`${machine.maccolumn + '-' + machine.macrow}`]) {
              this.$refs[`${machine.maccolumn + '-' + machine.macrow}`][0].machine = machine;
              this.$refs[`${machine.maccolumn + '-' + machine.macrow}`][0].workerList = this.macworkerMap[machine.id];
            }
          })
        }
      },
      macworkerMap:function(val,oldVal){
        if (this.macworkerMap!=undefined && this.machineList!=undefined){
          this.machineList.forEach((machine, index, array) =>{
            if (this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`]){
              this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].machine = machine;
              this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].workerList = this.macworkerMap[machine.id];
            }
          })
        }
      },
      newworker:function(val,oldVal){
          console.log(this.newworker);
      },
    },
    updated:function(){
      this.machineList.forEach((machine, index, array) =>{
        if (this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`]){
          this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].machine = machine;
          this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].workerList = this.macworkerMap[machine.id];
        }
      })
    },
    methods: {
      receiveData:function (macid,macCode,flag) {
        this.$emit("update", macid, macCode,flag);
      }
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
