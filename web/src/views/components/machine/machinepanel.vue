<template>
  <div>
    <div v-show="machine.maccode!=null"  :style="styleObject"  class="card-panel" @click="lookInfo(machine.id)">
      <div  class="card-panel-progress">
        <span :style="spanWidth">{{spanwidth}}</span>
      </div>
      <div  class="card-panel-header" >
        {{machine.maccode}}
      </div>

      <div class="card-panel-content">
        <div class="card-panel-text">
          车速：{{machine.macspeed}}
        </div>
        <div class="card-panel-text">
          效率：{{machine.macefficiency}} %
        </div>
        <div class="card-panel-text">
          品种：{{machine.pdtcode}}
        </div>
      </div>
    </div>
  <!-- 用户导入对话框 -->
  <el-dialog  width="1000px" :visible.sync="isShowInfo" :title="'生产详情：'+machine.maccode" >
      <!--TODO  查看织机情况-->
    <div class="dashboard-editor-container">

      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-form :model="machine" :inline="true"  label-width="200px" >
          <el-form-item label="品种编号/织轴编号/品种纬密" >
            <el-input v-model="machine.pdtcode" size="mini" style="width: 150px" ></el-input>
            <el-input v-model="machine.shaftcode" size="mini" style="width: 150px"  ></el-input>
            <el-input v-model="machine.weftdensity" size="mini" style="width: 150px"  ></el-input>
          </el-form-item>
          <br>
          <el-form-item label="现在产量/织轴总长/织造进度" >
            <el-input v-model="machine.clothlength" size="mini" style="width: 150px" ></el-input>
            <el-input v-model="machine.shaftlength" size="mini" style="width: 150px"></el-input>
            <el-input v-model="spanwidth" size="mini" style="width: 150px"></el-input>
          </el-form-item>
          <el-form-item label="上机时间/预了时间/剩余小时" >
            <el-input v-model="shaft.actstart" size="mini" style="width: 150px" ></el-input>
            <el-input v-model="shaft.planend" size="mini" style="width: 150px"></el-input>
            <el-input v-model="spanwidth" size="mini" style="width: 150px"></el-input> <!--TODO 计算剩余小时数-->
          </el-form-item>
          <el-form-item label="车速/效率/状态" >
            <el-input v-model="machine.macspeed" size="mini" style="width: 150px" ></el-input>
            <el-input v-model="machine.macefficiency" size="mini" style="width: 150px"></el-input>
            <el-input v-model="macstate" size="mini" style="width: 150px"></el-input>
          </el-form-item>
        </el-form>
      </el-row>
      <panel-group @handleSetLineChartData="handleSetLineChartData" :avgSpeed="avgSpeed" :avgEfficiency="avgEfficiency" :allWeavingLength="allWeavingLength" :stopMacNum="stopMacNum" />
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <div><b>{{showtype}}</b></div>
        <line-chart :chart-data="lineData" :showtype="showtype"/>
      </el-row>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">

        <el-col :xs="14" :sm="14" :lg="14">
          <div style="margin-bottom: 20px">
            <b>日期：{{shiftDate}}</b>&nbsp;&nbsp;&nbsp;&nbsp;
            <el-button  size="mini" round @click="changeDate(shiftDate,-1)">前一天</el-button>
            <el-button  size="mini" round @click="changeDate(shiftDate,+1)">后一天</el-button>
          </div>
          <profile-chart :chart-data="profileData"  :mac_common_status="mac_common_status" :shiftTypes="shiftTypes"/>

        </el-col>
        <el-col :xs="10" :sm="10" :lg="10">
          <div><b>数据统计</b></div>
          <div style="text-align: center"> <!--应该是查询班次数量类别的-->
            <span v-for="item in shiftTypes"   class="card-panel-col" style="margin-left:10px">
              <el-button  size="mini" :type="shiftType==item?'primary':''" round @click="changeShift(item)">{{item}}</el-button>
            </span>
          </div>
          <el-form :model="mftshift" :inline="true"  label-width="150px" >
            <el-form-item label="运行时长/停机时长" >
              <el-input v-model="mftshift.runtime" size="mini" style="width: 100px" ></el-input>
              <el-input v-model="mftshift.stoptime" size="mini" style="width: 100px"  ></el-input>
            </el-form-item>
            <br>
            <el-form-item label="经停时长/经停次数" >
              <el-input v-model="mftshift.warpstoptime" size="mini" style="width: 100px" ></el-input>
              <el-input v-model="mftshift.warpstopnum" size="mini" style="width: 100px"></el-input>
            </el-form-item>
            <el-form-item label="纬停时长/纬停次数" >
              <el-input v-model="mftshift.otherstoptime" size="mini" style="width: 100px" ></el-input>
              <el-input v-model="mftshift.weftstopnum" size="mini" style="width: 100px"></el-input>
            </el-form-item>
            <el-form-item label="其他停时长/次数" >
              <el-input v-model="mftshift.otherstoptime" size="mini" style="width: 100px" ></el-input>
              <el-input v-model="mftshift.otherstopnum" size="mini" style="width: 100px"></el-input>
            </el-form-item>
            <el-form-item label="织造长度/打纬次数" >
              <el-input v-model="mftshift.shiftlength" size="mini" style="width: 100px" ></el-input>
              <el-input v-model="mftshift.picknum" size="mini" style="width: 100px"></el-input>
            </el-form-item>
            <el-form-item label="平均车速/平均效率" >
              <el-input v-model="mftshift.macspeed" size="mini" style="width: 100px" ></el-input>
              <el-input v-model="mftshift.macefficiency" size="mini" style="width: 100px"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>

  </el-dialog>
  </div>
</template>

<script>
import PanelGroup from '../../dashboard/PanelGroup'
import LineChart from '../../dashboard/LineChart'
import ProfileChart from '../../dashboard/ProfileChart'
  // import RaddarChart from './dashboard/RaddarChart'
  // import PieChart from './dashboard/PieChart'
  // import BarChart from './dashboard/BarChart'

import { listShift,listShiftRecent } from "@/api/manufacture/shift";
  import { listShaft} from "@/api/manufacture/shaft";

  export default {
    props:{
      shaftList:{
        type:Array,
      },
      shiftTypes:{
        type:Array,
      },
    },
    components: {
      PanelGroup,
      LineChart,
      ProfileChart,
      /*           RaddarChart,
            PieChart,
            BarChart,*/
    },
    data() {
      return {
        machine:Object,
        mftshift:{},
        shaft:{},
        mac_common_status:[],
        isShowInfo:false,
        backgroundColor: "#",
        progress:0,
        spanwidth:0,
        remainhour:0,
        macstate:"未知",
        //图表
        lineData: {},
        avgSpeed:0,
        avgEfficiency:0,
        allWeavingLength:0,
        showtype:"速度统计",
        stopMacNum:0, //TODO 这里要改成停台次数
        shiftType:'早班',//显示详情的类别默认
        profileData:[],
        shiftDate:this.parseTime(new Date(),'{y}-{m}-{d}')
      }
    },
    computed:{
      //监视status的改变
      styleObject(){
        return {backgroundColor:this.backgroundColor}
      },
      spanWidth(){
        return {"width":this.spanwidth,
        }
      }
    },
    watch:{
      //改变颜色状态指定颜色
      mac_common_status:{
        handler(mac_common_status, oldVal){
          this.mac_common_status.forEach((statusList, index, array) =>{
            if (statusList.dictValue==this.machine.macstatus){
              this.backgroundColor=statusList.remark
              this.macstate = statusList.dictLabel;
            }
          })
        },
        deep:true,
      },
      shaftList:function(val,oldVal){
        this.shaftList.forEach((shaft, index, array) =>{
          if (shaft.actmaccode==this.machine.maccode){
            this.shaft = shaft;
            this.progress = (shaft.shaftlength-shaft.shaftremainlength)*100/shaft.shaftlength
            this.spanwidth = this.progress + "%";
            this.remainhour = ((shaft.planend.getTime()-shaft.actstart.getTime())/(1000*60*60)).toFixed(2);
          }
        })
      },
      ProfileData:function(val,oldVal){
        this.ProfileData.forEach(shift=>{
          if (shift.shifttype==this.shiftType){
            this.mftshift = shift;
          }
        });
      },
      machine: function (val,oldVal) {
      }
    },
    updated(){
      this.shaftList.forEach((shaft, index, array) =>{
        if (shaft.actmaccode==this.machine.maccode){
          this.shaft = shaft;
          this.progress = (shaft.shaftlength-shaft.shaftremainlength)*100/shaft.shaftlength
          this.spanwidth = this.progress + "%";
        }
      })
    },
    methods:{
      lookInfo(id){
        // 有织机传过来了 查这个织机的当前班次情况，shiftnow=1应该只有一个；
        var shiftDatester = this.parseTime(new Date(),'{y}-{m}-{d}')//查当天的情况
        listShift({"shiftdate":shiftDatester,"maccode":this.machine.maccode,"groupby":true}).then(response=>{
          if (response.rows.length>0){// 可能大于1的
            //计算平均车速等
            response.rows.forEach(shift=>{
              if (shift.shiftnow==1){//今天应该必定有当前时刻
                this.mftshift = shift;
                this.avgSpeed = this.mftshift.macspeed;
                this.avgEfficiency = this.mftshift.macefficiency;
                this.stopMacNum=this.mftshift.warpstopnum+this.mftshift.weftstopnum+this.mftshift.otherstopnum;
                this.allWeavingLength = this.mftshift.shiftlength;
              }
            });
          }
          //列出最近7天的情况
          listShiftRecent(7,this.machine.maccode).then(response=>{
            if (response.msg=="操作成功"){
              this.lineData=response.data;
            }
            //列出指定时间的生产情况
            listShift({"shiftdate":this.shiftDate,"maccode":this.machine.maccode,"groupby":true}).then(response=>{
              this.ProfileData = response.rows;
            });
          });
        });
        this.isShowInfo=true;
      },
      //点击切换显示内容
      handleSetLineChartData(type) {
        this.showtype = type;
      },
      //点击切换班次显示内容
      changeShift(type) {
        this.shiftType = type;
        this.ProfileData.forEach(shift=>{
          if (shift.shifttype==this.shiftType){
            this.mftshift = shift;
          }
        });
      },
      //点击切换日期
      changeDate(datestr,type) {
        var newday = this.parseTime(new Date(new Date(datestr).getTime()+60*1000*60*24*type),'{y}-{m}-{d}');
        this.shiftDate = newday;
        //列出指定时间的生产情况
        listShift({"shiftdate":this.shiftDate,"maccode":this.machine.maccode,"groupby":true}).then(response=>{
          this.ProfileData = response.rows;
        });
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

  .card-panel {
    margin-bottom: 5px;
    height: 70px;
    cursor: pointer;
    font-size: 12px;
    border-radius: 5px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #DCDFE6;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    .card-panel-progress {
      width: 100%;
      height: 10px;
      border-radius: 2px;
      overflow: hidden;  /*注意这里*/
      text-align: left;
      background: white;
      opacity: 0.7;
      span{
        display: inline-block;
        height: 100%;
        background: #98FB98;
        text-align: center;
      }
    }

    .card-panel-header {
      font-size: 15px;
      font-weight: bold;
      text-align: center;
    }


    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }

  /*@media (max-width:550px) {
    .card-panel-description {
      display: none;
    }

    .card-panel-icon-wrapper {
      float: none !important;
      width: 100%;
      height: 100%;
      margin: 0 !important;

      .svg-icon {
        display: block;
        margin: 14px auto !important;
        float: none !important;
      }
    }
  }*/

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>
