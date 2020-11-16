import request from '@/utils/request'

// 查询织轴列表列表
export function listShaft(query) {
  return request({
    url: '/manufacture/shaft/list',
    method: 'get',
    params: query
  })
}

// 查询织轴列表详细
export function getShaft(id) {
  return request({
    url: '/manufacture/shaft/' + id,
    method: 'get'
  })
}

// 新增织轴列表
export function addShaft(data) {
  return request({
    url: '/manufacture/shaft',
    method: 'post',
    data: data
  })
}

// 修改织轴列表
export function updateShaft(data) {
  return request({
    url: '/manufacture/shaft',
    method: 'put',
    data: data
  })
}

// 织轴上轴
export function doShangZhou(data) {
  return request({
    url: '/manufacture/shaft/doShangZhou',
    method: 'put',
    data: data
  })
}
// 织轴上轴
export function doShangZhouAll(data) {
  return request({
    url: '/manufacture/shaft/doShangZhouAll',
    method: 'put',
    data: data
  })
}
// 删除织轴列表
export function delShaft(id) {
  return request({
    url: '/manufacture/shaft/' + id,
    method: 'delete'
  })
}

// 导出织轴列表
export function exportShaft(query) {
  return request({
    url: '/manufacture/shaft/export',
    method: 'get',
    params: query
  })
}
