<template>
  <div>
    <div v-show="machine.maccode!=null"  :style="styleObject"  class="card-panel" @click="lookInfo('newVisitis')">
      <div  class="card-panel-progress">

      </div>
      <div  class="card-panel-header" >
        {{machine.maccode}}
      </div>

      <div class="card-panel-content">
        <div class="card-panel-text">
          车速：{{machine.macspeed}}
        </div>
        <div class="card-panel-text">
          效率：{{machine.macefficiency}}
        </div>
        <div class="card-panel-text">
          品种：{{machine.pdtcode}}
        </div>
      </div>
    </div>
  <!-- 用户导入对话框 -->
  <el-dialog  width="400px" :visible.sync="isShowInfo" append-to-body>
      <!--TODO  查看织机情况-->
      查看详情{{machine.maccode}}
  </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        machine:Object,
        mac_common_status:null,
        isShowInfo:false,
        backgroundColor: "#"
      }
    },
    computed:{
      //监视status的改变
      styleObject(){
        /*this.machine.mac_common_status.forEach((statusList, index, array) =>{
          if (statusList.dictValue==this.machine.macstatus){
            if(statusList.dictLabel=="运转"){
              this.backgroundColor="#67C23A"
            }else if(statusList.dictLabel=="经停"){
              this.backgroundColor="#F56C6C"
            }else if(statusList.dictLabel=="纬停"){
              this.backgroundColor="#E6A23C"
            }else if(statusList.dictLabel=="其他停"){
              this.backgroundColor="#909399"
            }
          }
        })*/
        return {backgroundColor:this.backgroundColor}
      }
    },
    watch:{
      //改变颜色
      mac_common_status:{
        handler(mac_common_status, oldVal){
          this.mac_common_status.forEach((statusList, index, array) =>{
            if (statusList.dictValue==this.machine.macstatus){
              this.backgroundColor=statusList.remark
            }
          })
        },
        deep:true,
      }
    },
    methods:{
      lookInfo(){
        this.isShowInfo=true
      }
    }
  }
</script>
<style lang="scss" scoped>
  .card-panel {
    margin-bottom: 5px;
    height: 60px;
    cursor: pointer;
    font-size: 12px;

    position: relative;
    overflow: hidden;
    color: #666;
    background: #DCDFE6;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

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
