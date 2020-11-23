import request from '@/utils/request'

// 查询班次排班列表
export function listShiftarrange(query) {
  return request({
    url: '/hr/shiftarrange/list',
    method: 'get',
    params: query
  })
}

// 查询班次排班详细
export function getShiftarrange(id) {
  return request({
    url: '/hr/shiftarrange/' + id,
    method: 'get'
  })
}

// 新增班次排班
export function addShiftarrange(data) {
  return request({
    url: '/hr/shiftarrange',
    method: 'post',
    data: data
  })
}

// 修改班次排班
export function updateShiftarrange(data) {
  return request({
    url: '/hr/shiftarrange',
    method: 'put',
    data: data
  })
}

// 删除班次排班
export function delShiftarrange(id) {
  return request({
    url: '/hr/shiftarrange/' + id,
    method: 'delete'
  })
}

// 导出班次排班
export function exportShiftarrange(query) {
  return request({
    url: '/hr/shiftarrange/export',
    method: 'get',
    params: query
  })
}