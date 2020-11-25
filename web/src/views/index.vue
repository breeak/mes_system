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
      timer:"",
      avgSpeed:0,
      avgEfficiency:0,
      avgEfficiencyList:[],
      allWeavingLength:0,
      runMacNum:0,
      stopMacNum:0,
      weftStopNum:0,
      warpStopNum:0,
      otherStopNum:0,
      offLineNum:0,
      machineList:[],
      allRow:0,
      allColumn:0,
      shaftList:[],
      shiftList:{},
      mac_common_status:[],
      macStatusMap:new Map(),
      pieData:[],
      raddarData:[],
      barData:{},
      barType:"macspeed",
      showtype:"速度统计",
      lineData: {},
      lastDay:new Date()
    }
  },
  watch: {
    avgEfficiencyList: {
      deep: true,
      handler(val) {
        var avg = 0;
        for(let i = 0; i<val.length;i++){
          avg = avg + val[i];
        }
        this.avgEfficiency = avg/val.length;
      }
    },
  },
  created(){
    //列出所有织机  定时执行轮旬进行 不断的查询织机状态
    this.updateStatus();
    //列出最近的生产情况
    listShiftRecent(7,null).then(response=>{
      if (response.msg=="操作成功"){
        this.lineData=response.data;
      }
    });
    //计算前一天是哪  并列出那一天的所有班次 分组排序好
    this.lastday = this.parseTime(new Date(new Date().getTime()-60*1000*60*24),'{y}-{m}-{d}');
    listShift({"shiftdate":this.lastday}).then(response=>{
      if (response.rows.length>0){
        let lastShiftGroup = this.listGroupBy(response,"shifttype");
        lastShiftGroup.forEach(arr=>{
          let shiftInfo = this.calcShift(arr);
          this.avgEfficiencyList.push(shiftInfo.avgEfficiency)
        });
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
  mounted(){
    this.timer = setInterval(this.updateStatus, 60000);//
  },
  beforeDestroy() {
    clearTimeout(this.timer);
  },
  methods: {
    handleSetLineChartData(type) {
      this.showtype = type;
    },
    updateStatus(){
      listMachine({"sortProp":"macspeed","sortOrder":"desc"}).then(response => {
        console.log(1)
        this.machineList = response.rows;
        //计算平均车速
        this.avgSpeed = 0;
        //this.avgEfficiency = 0;
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
          let num = 0
          this.machineList.forEach((machine, index, array) =>{
            if (machine.macspeed!=0){
              this.avgSpeed = this.avgSpeed + machine.macspeed;
            }
            //this.avgEfficiency = this.avgEfficiency + machine.macefficiency;
            if (machine.macstatus ==  this.macStatusMap.get("运转") ) {
              num = num +1;
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
          //this.avgEfficiency = this.avgEfficiency/this.machineList.length;
          console.log(num)
          if(num!=0){
            this.avgSpeed = this.avgSpeed/num;
          }else{
            this.avgSpeed = 0;
          }
          this.pieData = [
            { value: this.runMacNum, name: '运转' },
            { value: this.warpStopNum, name: '经停' },
            { value: this.weftStopNum, name: '纬停' },
            { value: this.otherStopNum, name: '其他停' },
            { value: this.offLineNum, name: '离线' }
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
      return {"avgSpeed":avgSpeed.toFixed(2),"avgEfficiency":avgEffiency,"allLength":allLength,"avgRuntime":avgRuntime.toFixed(2),"avgStoptime":avgStoptime.toFixed(2),"avgStopNum":avgStopNum.toFixed(2)};
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
