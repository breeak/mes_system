<template>
  <div class="dashboard-editor-container">

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <machine  :machineList="machineList" :shaftList="shaftList" :allRow="allRow" :allColumn="allColumn" :mac_common_status="mac_common_status"/>
    </el-row>

    <panel-group @handleSetLineChartData="handleSetLineChartData" :avgSpeed="avgSpeed" :avgEfficiency="avgEfficiency" :allWeavingLength="allWeavingLength" :stopMacNum="stopMacNum" />

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <div><b>{{showtype}}</b></div>
      <line-chart :chart-data="lineData" :showtype="showtype"/>
    </el-row>

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <raddar-chart :chart-data = "raddarData" />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart :chart-data="pieData"  />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <div style="text-align: center">
          <el-button style="margin-top: -10px;" size="mini" :type="barType=='macspeed'?'primary':''" round @click="changeBarSearch('macspeed')">车速排行</el-button>
          <el-button style="margin-top: -10px;" size="mini" :type="barType=='macefficiency'?'primary':''" round @click="changeBarSearch('macefficiency')">效率排行</el-button>
          <el-button style="margin-top: -10px;" size="mini" :type="barType=='maclength'?'primary':''" round @click="changeBarSearch('maclength')">产量排行</el-button>
          </div>
          <bar-chart :chart-data="barData"/>
        </div>
      </el-col>
    </el-row>

    
  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import RaddarChart from './dashboard/RaddarChart'
import PieChart from './dashboard/PieChart'
import BarChart from './dashboard/BarChart'
import machine from './components/machine/machine'

import { listMachine } from "@/api/manufacture/machine";
import { listShift,listShiftRecent } from "@/api/manufacture/shift";
import { listShaft } from "@/api/manufacture/shaft";

export default {
  name: 'Index',
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart,
    machine
  },
  data() {
    return {
      avgSpeed:0,
      avgEfficiency:0,
      allWeavingLength:0,
      runMacNum:0,
      stopMacNum:0,
      weftStopNum:0,
      warpStopNum:0,
      otherStopNum:0,
      offLineNum:0,
      machineList:[],
      shaftList:[],
      shiftList:{},
      mac_common_status:[],
      macStatusMap:new Map(),
      pieData:[],
      raddarData:[],
      barData:{},
      barType:"macspeed",
      showtype:"速度统计",
      allRow:0,
      allColumn:0,
      lineData: {}
    }
  },
  created(){
    //列出所有织机
    listMachine({"sortProp":"macspeed","sortOrder":"desc"}).then(response => {
      this.machineList = response.rows;
      //计算平均车速
      this.avgSpeed = 0;
      this.avgEfficiency = 0;
      this.stopMacNum=0,
      this.weftStopNum=0,
      this.warpStopNum=0,
      this.otherStopNum=0,
      this.offLineNum=0,
      this.runMacNum=0,
      this.allWeavingLength = 0;
      this.getDicts("mac_common_status").then(response => {
        this.mac_common_status=response.data;
        // 获取状态对应value
        this.mac_common_status.forEach((status, index, array) =>{
          this.macStatusMap.set(status.dictLabel,status.dictValue);
        })
        // 统计数据
        this.machineList.forEach((machine, index, array) =>{
          this.avgSpeed = this.avgSpeed + machine.macspeed;
          this.avgEfficiency = this.avgEfficiency + machine.macefficiency;
          if (machine.macstatus ==  this.macStatusMap.get("运转") ) {
            this.runMacNum = this.runMacNum + 1;
          }else if (machine.macstatus == this.macStatusMap.get("离线") ) {
            this.offLineNum = this.offLineNum + 1;
          }else if (machine.macstatus == this.macStatusMap.get("经停") ) {
            this.warpStopNum = this.warpStopNum + 1;
            this.stopMacNum = this.stopMacNum + 1;
          }else if (machine.macstatus == this.macStatusMap.get("纬停") ) {
            this.weftStopNum = this.weftStopNum + 1;
            this.stopMacNum = this.stopMacNum + 1;
          }else if (machine.macstatus == this.macStatusMap.get("其他停") ) {
            this.otherStopNum = this.otherStopNum + 1;
            this.stopMacNum = this.stopMacNum + 1;
          }
        })
        this.avgEfficiency = this.avgEfficiency/this.machineList.length;
        this.avgSpeed = this.avgSpeed/this.machineList.length;
        this.pieData = [
          { value: this.runMacNum.toFixed(2), name: '运转' },
          { value: this.warpStopNum.toFixed(2), name: '经停' },
          { value: this.weftStopNum.toFixed(2), name: '纬停' },
          { value: this.otherStopNum.toFixed(2), name: '其他停' },
          { value: this.offLineNum.toFixed(2), name: '离线' }
        ];
        if (this.machineList.length>10){
          this.barData={"name":[],"value":[]}
          for(let i=0;i<7;i++){
            this.barData.name.push(this.machineList[i].maccode);
            this.barData.value.push(this.machineList[i].macspeed);
          }
        }
      });
    });
    //列出最近的生产情况
    listShiftRecent(7).then(response=>{
      if (response.msg=="操作成功"){
        this.lineData=response.data;
      }
    });
    //列出所有当前班次
    listShift({shiftnow:"1"}).then(response=>{
      if (response.rows.length>0){
        var shiftInfo = this.calcShift(response.rows);
        this.allWeavingLength =shiftInfo.allLength;
      }
    });
    // 列出前一班
    listShift({shiftnow:"-1"}).then(response=>{
      if (response.rows.length>0){
        this.calcShift(response.rows);
      }
    });
    // 列出再前一班
    listShift({shiftnow:"-2"}).then(response=>{
      if (response.rows.length>0){
        this.calcShift(response.rows);
      }
    });


    //列出所有正在上轴的织轴
    listShaft({shaftstatus:"1"}).then(response=>{
      if (response.rows.length>0){
          this.shaftList = response.rows;
      }
    });
    this.getDicts("mac_common_arrangement").then(response => {
      this.allRow = Number(response.data[0].dictValue);
      this.allColumn = Number(response.data[1].dictValue);
    });
  },

  methods: {
    handleSetLineChartData(type) {
      this.showtype = type;
    },
    calcShift(shiftList){
      var avgSpeed = 0;
      var avgEffiency = 0;
      var avgRuntime = 0;
      var avgStoptime= 0;
      var avgStopNum = 0;
      var allLength =0;
      shiftList.forEach((shift, index, array) =>{
        allLength = allLength + shift.shiftlength;
        avgSpeed = avgSpeed + shift.macspeed;
        avgEffiency = avgEffiency + shift.macefficiency;
        avgRuntime = avgRuntime + shift.runtime;
        avgStoptime = avgStoptime + shift.stoptime;
        avgStopNum = avgStopNum + shift.otherstopnum + shift.warpstopnum + shift.weftstopnum;
      })
      var allNum =shiftList.length
      avgSpeed = avgSpeed/allNum;
      avgEffiency = avgEffiency/allNum;
      avgRuntime = avgRuntime/allNum;
      avgStoptime = avgStoptime/allNum;
      avgStopNum = avgStopNum/allNum;
      var raddarObject = {"name":shiftList[0].shifttype,"value":[avgSpeed.toFixed(2),avgEffiency.toFixed(2),allLength.toFixed(2),avgRuntime.toFixed(2),avgStoptime.toFixed(2),avgStopNum.toFixed(2)]}
      this.raddarData.push(raddarObject)
      return {"avgSpeed":avgSpeed.toFixed(2),"avgEffiency":avgEffiency.toFixed(2),"allLength":allLength,"avgRuntime":avgRuntime.toFixed(2),"avgStoptime":avgStoptime.toFixed(2),"avgStopNum":avgStopNum.toFixed(2)};
    },
    // 柱状图内容的更新
    changeBarSearch(type){
      this.barType=type;
      if (type=="macspeed"){
        listMachine({"sortProp":"macspeed","sortOrder":"desc"}).then(response=>{
          if (response.rows.length>0){
            this.machineList = response.rows;
            if (this.machineList.length>10){
              this.barData={"name":[],"value":[]}
              for(let i=0;i<7;i++){
                this.barData.name.push(this.machineList[i].maccode);
                this.barData.value.push(this.machineList[i].macspeed);
              }
            }
          }
        });
      } else if (type=="macefficiency"){
        listMachine({"sortProp":"macefficiency","sortOrder":"desc"}).then(response=>{
          if (response.rows.length>0){
            this.machineList = response.rows;
            if (this.machineList.length>10){
              this.barData={"name":[],"value":[]}
              for(let i=0;i<7;i++){
                this.barData.name.push(this.machineList[i].maccode);
                this.barData.value.push(this.machineList[i].macefficiency.toFixed(2));
              }
            }
          }
        });
      } else if(type=="maclength"){
        listShift({"sortProp":"shiftLength","sortOrder":"desc","shiftnow":"1"}).then(response=>{
          if (response.rows.length>0){
            this.shiftList = response.rows;
            console.log(this.shiftList)
            if (this.shiftList.length>10){
              this.barData={"name":[],"value":[]}
              for(let i=0;i<7;i++){
                this.barData.name.push(this.shiftList[i].maccode);
                this.barData.value.push(this.shiftList[i].shiftlength);
              }
            }
          }
        });
      }
    },
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
