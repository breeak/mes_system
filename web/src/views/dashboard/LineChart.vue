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
      type: Object,
      required: true
    },
    showtype: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.initChart()
      }
    },
    showtype: {
      deep: true,
      handler(val) {
        this.initChart()
      }
    }

  },
  mounted() {
    this.$nextTick(() => {
      //this.initChart()
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
      this.setOptions(this.chartData,this.showtype)
    },
    setOptions(chartData,showtype) {
      this.chart.setOption({
        xAxis: {
          data: chartData["时间列表"]["日期"],
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: false
          }
        },
        legend: {
          data: ['早班', '中班','晚班','总计'],
          selected:{
            '总计':false
          }
        },
        series: [{
          name: '早班', itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          data: chartData[showtype]['早班'],
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: '中班',
          smooth: true,
          type: 'line',
          itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2
              },
              areaStyle: {
                color: '#f3f8ff'
              }
            }
          },
          data: chartData[showtype]['中班'],
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        },
          {
            name: '晚班',
            smooth: true,
            type: 'line',
            itemStyle: {
              normal: {
                color: '#ffcb8c',
                lineStyle: {
                  color: '#ffcb8c',
                  width: 2
                },
                areaStyle: {
                  color: '#f3f8ff'
                }
              }
            },
            data: chartData[showtype]['晚班'],
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          },
          {
            name: '总计',
            smooth: true,
            type: 'line',
            itemStyle: {
              normal: {
                color: '#c8b2f4',
                lineStyle: {
                  color: '#c8b2f4',
                  width: 2
                },
                areaStyle: {
                  color: '#f3f8ff'
                }
              }
            },
            data: chartData[showtype]['总计'],
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          }]
      })
    }
  }
}
</script>
