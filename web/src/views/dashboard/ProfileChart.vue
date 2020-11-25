<template>
    <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Array,
      required: true
    },
    mac_common_status:{
      type: Array,
      required: true
    },
    shiftTypes:{
      type: Array,
      required: true
    }
  },
  data() {
    return {
      chart: null,
      // 各状态的颜色
      colors : [],
      state : [],
      statevalue : [],
      mac_common_shift:[],
      profileData:null,
      alldata:[]
    }
  },
  created(){
    if (this.mac_common_status!=undefined){
      this.colors=[];
      this.state=[];
      this.mac_common_status.forEach((statusList, index, array) =>{
        this.colors.push(statusList.remark);
        this.state.push(statusList.dictLabel);
        this.statevalue.push(statusList.dictValue);
      });
    };
  },
  updated(){

  },
  watch: {
    chartData: {
      deep: true,
      handler(val,oldval) {
        /*{
          itemStyle: { normal: { color: this.colors[0] } },
          name: '正常',
            value: [0, 0, 10]
        },*/
        if (val!=undefined && val.length>0){
          var start=val[0].shiftstarttime.getTime()/1000;//开始的时间戳 转换到秒
          var end=val[0].shiftendtime.getTime()/1000;
          var during = (end-start)/(60*60).toFixed(0);//转化到小时
          this.shiftTypes.forEach((shifttype, index, array) =>{
            if (val[0].shifttype==shifttype) {
              var shiftindex = index;
            }
          });
          val.forEach(shift=>{
            var alldatalist = shift.alldatalist;
            if (alldatalist!=undefined && alldatalist.length>0) {
              //遍历内部数据
              alldatalist.forEach(item=>{
                var data = new Object();
                this.statevalue.forEach((state, index, array) =>{
                  if (item.state==state) {
                    data.name = this.state[index];
                    data.itemStyle={"normal": { color: this.colors[index]}};
                  }
                });
                var value = new Array();
                value.push(shiftindex);
                var starttime = (item.starttime-start)/(60*60).toFixed(2);//转化到小时
                value.push(starttime);
                var endtime = (item.endtime-start)/(60*60).toFixed(2);//转化到小时
                value.push(endtime);
                value.push(item.startdate);
                value.push(item.enddate);
                value.push((item.dusec/60).toFixed(2));//持续时间使用分钟  5
                value.push(item.pdtcode);//品种 6
                value.push(item.length);
                value.push(item.output);//打纬数
                data.value=value;
                this.alldata.push(data);
              });
            }
          });
        }
        this.initChart();
      }
    },
    showtype: {
      deep: true,
      handler(val) {
        this.initChart()
      }
    },
    shiftTypes: {
      deep: true,
      handler(val) {
        this.initChart()
      }
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions()
    },
    setOptions() {
      this.chart.setOption({
        color: this.colors,
        tooltip: {
          formatter: function (params) {
            return '状态:'+params.name + '<br>起止:' + params.value[3] + '~' + params.value[4]+ '<br>持续:' + params.value[5]+ '<br>品种:' + params.value[6]+ '<br>长度:' + params.value[7]+ '<br>纬数:' + params.value[8];
          }
        },
        legend: {
          data: this.state,
          bottom: '1%',
          selectedMode: false, // 图例设为不可点击
          textStyle: {
            color: '#000'
          }
        },
        grid: {
          left: '3%',
          right: '3%',
          top: '1%',
          bottom: '10%',
          containLabel: true
        },
        xAxis: {
          min: 0 // x轴零刻度对应的实际值
        },
        yAxis: {
          data: this.shiftTypes
        },
        series: [
          // 用空bar来显示四个图例
          {name: this.state[0], type: 'bar', data: []},
          {name: this.state[1], type: 'bar', data: []},
          {name: this.state[2], type: 'bar', data: []},
          {name: this.state[3], type: 'bar', data: []},
          {name: this.state[4], type: 'bar', data: []}, //TODO 应该是遍历状态获得的
          {
            type: 'custom',
            renderItem: function (params, api) {
              var categoryIndex = api.value(0);
              var start = api.coord([api.value(1), categoryIndex]);
              var end = api.coord([api.value(2), categoryIndex]);
              var height = 24;

              return {
                type: 'rect',
                shape: echarts.graphic.clipRectByRect({
                  x: start[0],
                  y: start[1] - height / 2,
                  width: end[0] - start[0],
                  height: height
                }, {
                  x: params.coordSys.x,
                  y: params.coordSys.y,
                  width: params.coordSys.width,
                  height: params.coordSys.height
                }),
                style: api.style()
              };
            },
            encode: {
              x: [1, 2],
              y: 0
            },
            data: this.alldata
          }
        ]
      })
    }
  }
}
</script>
