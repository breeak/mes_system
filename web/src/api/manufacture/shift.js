import request from '@/utils/request'

// 查询班次效率列表
export function listShift(query) {
  return request({
    url: '/manufacture/shift/list',
    method: 'get',
    params: query
  })
}
// 更新任意日期的班次
export function updateShiftAny(query) {
  return request({
    url: '/manufacture/shift/updateShiftAny',
    method: 'get',
    params: query
  })
}
// 查询班次最近几天的数据统计
export function listShiftRecent(days,maccode) {
  return request({
    url: '/manufacture/shift/recent/'+days+'/'+maccode,
    method: 'get',
    params: days
  })
}

// 查询班次效率详细
export function getShift(id) {
  return request({
    url: '/manufacture/shift/' + id,
    method: 'get'
  })
}

// 新增班次效率
export function addShift(data) {
  return request({
    url: '/manufacture/shift',
    method: 'post',
    data: data
  })
}

// 修改班次效率
export function updateShift(data) {
  return request({
    url: '/manufacture/shift',
    method: 'put',
    data: data
  })
}

// 删除班次效率
export function delShift(id) {
  return request({
    url: '/manufacture/shift/' + id,
    method: 'delete'
  })
}

// 导出班次效率
export function exportShift(query) {
  return request({
    url: '/manufacture/shift/export',
    method: 'get',
    params: query
  })
}
