import request from '@/utils/request'

// 查询织机列表列表
export function listMachine(query) {
  return request({
    url: '/manufacture/machine/list',
    method: 'get',
    params: query
  })
}

// 查询织机列表详细
export function getMachine(id) {
  return request({
    url: '/manufacture/machine/' + id,
    method: 'get'
  })
}

// 新增织机列表
export function addMachine(data) {
  return request({
    url: '/manufacture/machine',
    method: 'post',
    data: data
  })
}

// 修改织机列表
export function updateMachine(data) {
  return request({
    url: '/manufacture/machine',
    method: 'put',
    data: data
  })
}

// 删除织机列表
export function delMachine(id) {
  return request({
    url: '/manufacture/machine/' + id,
    method: 'delete'
  })
}

// 导出织机列表
export function exportMachine(query) {
  return request({
    url: '/manufacture/machine/export',
    method: 'get',
    params: query
  })
}