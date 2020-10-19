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
  <el-dialog  width="800px" :visible.sync="isShowInfo" title="生产详情" >
      <!--TODO  查看织机情况-->
    <el-form :model="machine" :inline="true"  label-width="100px" >
      <el-form-item label="织机编号" >
        <el-input v-model="machine.maccode" ></el-input>
      </el-form-item>
      <el-form-item label="织机类型" >
        <el-input v-model="machine.mactype" ></el-input>
      </el-form-item>
      <el-form-item label="效率" >
        <el-input v-model="machine.macefficiency" ></el-input>
      </el-form-item>
      <el-form-item label="车速" >
        <el-input v-model="machine.macspeed" ></el-input>
      </el-form-item>
      <el-form-item label="状态" >
        <el-input v-model="machine.macstatus" ></el-input>
      </el-form-item>
      <el-form-item label="品种编号" >
        <el-input v-model="machine.pdtcode" ></el-input>
      </el-form-item>
      <el-form-item label="织轴编号" >
        <el-input v-model="machine.shaftcode" ></el-input>
      </el-form-item>
      <el-form-item label="纬密根/10cm" >
        <el-input v-model="machine.weftdensity" ></el-input>
      </el-form-item>
    </el-form>
    <el-form :model="shaft" :inline="true"  label-width="100px" >
      <el-form-item label="织轴总长" >
        <el-input v-model="shaft.shaftlength" ></el-input>
      </el-form-item>
      <el-form-item label="剩余长度" >
        <el-input v-model="shaft.shaftremainlength" ></el-input>
      </el-form-item>
      <el-form-item label="已织长度" >
        <el-input v-model="shaft.clothlength" ></el-input>
      </el-form-item>
      <el-form-item label="上机时间" >
        <el-input v-model="shaft.actstart" ></el-input>
      </el-form-item>
      <el-form-item label="预了时间" >
        <el-input v-model="shaft.planend" ></el-input>
      </el-form-item>
      <el-form-item label="织造进度" >
        <el-input v-model="spanwidth" ></el-input>
      </el-form-item>
    </el-form>
  </el-dialog>
  </div>
</template>

<script>
  import { listShift} from "@/api/manufacture/shift";
  import { listShaft} from "@/api/manufacture/shaft";
  export default {
    props:{
      shaftList:{
        type:Array,
      }
    },
    data() {
      return {
        machine:Object,
        mftshift:{},
        shaft:{},
        mac_common_status:null,
        isShowInfo:false,
        backgroundColor: "#",
        progress:0,
        spanwidth:0,
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
          }
        })
      },

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
        this.isShowInfo=true;
      },

    }
  }
</script>
<style lang="scss" scoped>
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
</style>
