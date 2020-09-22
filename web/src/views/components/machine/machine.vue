<template>
  <el-main >
    <el-row :gutter="10"   type="flex" align="middle"  justify="space-between" class="card-panel-tittle" >
      <div class="card-panel-header">织造产实时监控</div>
      <el-row type="flex" align="middle"  justify="end">
        <div class="card-panel-flag" v-for="mac_status in mac_common_status" :style="{backgroundColor:mac_status.remark}">{{mac_status.dictLabel}}</div>
      </el-row>
    </el-row>
    <el-row v-for="i in allColumn"  :gutter="10"  type="flex" justify="space-between"  >
      <el-col v-for="j in allRow" :style="styleObject"  class="card-panel-col">
        <machine-base :ref="i+'-'+j" ></machine-base>
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
    data() {
      return {
        macCode1:'',
        macCode2:'',
        machineList:[],
        allColumn:0,
        allRow:0,
        mac_common_status:[],
        styleObject:{
          width:'10%',

        }
      }
    },
    beforeCreate(){

    },
    created() {
        listMachine().then(response => {
          this.machineList = response.rows;
        });
      this.getDicts("mac_common_status").then(response => {
        this.mac_common_status=response.data;
      });
      this.getDicts("mac_common_arrangement").then(response => {
        this.allRow = Number(response.data[0].dictValue);
        this.allColumn = Number(response.data[1].dictValue);
      });

    },
    computed: {

    },
    mounted(){
      /*this.timer = setInterval(function(){
        listMachine(this.queryParams).then(response => {
          this.machineList = response.rows;
        });
      }, 300000)*/
    },
    watch:{
      /*machineList:function(val,oldVal){
        console.log(val)
        console.log(oldVal)
        console.log(this.$refs)
        this.machineList.forEach((machine, index, array) =>{
          console.log(this.$refs[1-1])
          console.log(this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`])
          console.log(`${machine.maccolumn +'-'+ machine.macrow}`)
          this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].machine = machine
        })
      },
      mac_common_status(val,oldVal){
        console.log(val)
        console.log(oldVal)
      }*/

      /*this.machineList.forEach((machine, index, array) =>{
        this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].mac_common_status = this.mac_common_status
        console.log(this.mac_common_status)
        this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].machine = machine
      })*/
    },
    updated:function(){
      this.machineList.forEach((machine, index, array) =>{
        this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].machine = machine
        this.$refs[`${machine.maccolumn +'-'+ machine.macrow}`][0].mac_common_status = this.mac_common_status
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
