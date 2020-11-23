<template>
  <div>
    <div v-show="machine.maccode!=null"  :style="styleObject"  class="card-panel" @click="arrangeThis(machine.id,machine.maccode)">
      <div  class="card-panel-header" >
        {{machine.maccode}}
      </div>
      <div class="card-panel-content">
        <div v-for="worker in workerList" class="card-panel-text">
          {{worker.workername}}
        </div>
      </div>
    </div>
  </div>
</template>

<script>

  export default {
    props:{
      oldworker:{
        type: Number,
      },
      newworker:{
        type: Number,
      },
    },
    data() {
      return {
        machine:Object,
        flag:true,
        workerList:Array,
        backgroundColor: "#fff",
        oldbackgroundColor: "#fff",
      }
    },
    computed:{
      //监视status的改变
      styleObject(){
        return {backgroundColor:this.backgroundColor}
      },
    },
    watch:{
      // 监听是否有员工安排 这是某一个织机的
      workerList:{
        handler(val, oldVal){
          if (val!=undefined){
            this.workerList.forEach((worker, index, array) =>{
              if (this.oldworker==worker.id){//如果员工是当前员工就改变颜色
                this.oldbackgroundColor="#fef";
                this.backgroundColor=this.oldbackgroundColor;
              }else {
                this.oldbackgroundColor="#fff";
                this.backgroundColor=this.oldbackgroundColor;
              }
              if (this.newworker!=null && this.newworker==worker.id){//如果员工是当前员工就改变颜色
                this.backgroundColor="#FFB6C1";
              }
            })
          }else{
            this.oldbackgroundColor="#fff";
            this.backgroundColor=this.oldbackgroundColor;
          }
        },
        deep:true,
      },
      // 监听员工的改变  默认是function类型的要注意
      oldworker:function(val,oldVal){
        if (this.workerList!=undefined && typeof this.workerList!="function"){
          this.workerList.forEach((worker, index, array) =>{
            if (this.oldworker==worker.id){//如果员工是当前员工就改变颜色
              this.oldbackgroundColor="#fef";
              this.backgroundColor=this.oldbackgroundColor;
            }else{
              this.oldbackgroundColor="#fff";
              this.backgroundColor=this.oldbackgroundColor;
            }
          })
        }else{
          this.oldbackgroundColor="#fff";
          this.backgroundColor=this.oldbackgroundColor;
        }
      },
      // 监听员工的改变  默认是function类型的要注意
      newworker:function(val,oldVal){
        if (this.workerList!=undefined && typeof this.workerList!="function"){
          this.workerList.forEach((worker, index, array) =>{
            if (this.newworker==worker.id){//如果员工是当前员工就改变颜色
              this.backgroundColor="#FFB6C1";
            }else{
              this.oldbackgroundColor="#fff";
              this.backgroundColor=this.oldbackgroundColor;
            }
          })
        }else{
          this.oldbackgroundColor="#fff";
          this.backgroundColor=this.oldbackgroundColor;
        }
      },
      backgroundColor:function (val,oldval) {
        console.log(val);
        if (val=="#FFB6C1") {
          this.$emit("update", this.machine.id,this.machine.maccode,true);
        }else{
          this.$emit("update", this.machine.id,this.machine.maccode,false);
        }
      }
    },
    created(){
    },
    updated(){
    },
    methods:{
      arrangeThis(id,macCode){
        if (this.backgroundColor!="#FFB6C1"){
          this.backgroundColor="#FFB6C1";
        } else{
          this.backgroundColor=this.oldbackgroundColor;
        }
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

</style>
