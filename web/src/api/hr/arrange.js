import request from '@/utils/request'



// 新增排班信息
export function addArrangePart(data) {
  return request({
    url: '/hr/arrange/part',
    method: 'post',
    data: data
  })
}

// 修改排班信息
export function updateArrangePart(data) {
  return request({
    url: '/hr/arrange/part',
    method: 'put',
    data: data
  })
}

// 查询排班信息列表
export function listArrange(query) {
  return request({
    url: '/hr/arrange/list',
    method: 'get',
    params: query
  })
}

// 查询排班信息详细
export function getArrange(id) {
  return request({
    url: '/hr/arrange/' + id,
    method: 'get'
  })
}

// 新增排班信息
export function addArrange(data) {
  return request({
    url: '/hr/arrange',
    method: 'post',
    data: data
  })
}

// 修改排班信息
export function updateArrange(data) {
  return request({
    url: '/hr/arrange',
    method: 'put',
    data: data
  })
}

// 删除排班信息
export function delArrange(id) {
  return request({
    url: '/hr/arrange/' + id,
    method: 'delete'
  })
}

// 导出排班信息
export function exportArrange(query) {
  return request({
    url: '/hr/arrange/export',
    method: 'get',
    params: query
  })
}
