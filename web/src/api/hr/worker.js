import request from '@/utils/request'

// 查询员工列表列表
export function listWorker(query) {
  return request({
    url: '/hr/worker/list',
    method: 'get',
    params: query
  })
}

// 查询员工列表详细
export function getWorker(id) {
  return request({
    url: '/hr/worker/' + id,
    method: 'get'
  })
}

// 新增员工列表
export function addWorker(data) {
  return request({
    url: '/hr/worker',
    method: 'post',
    data: data
  })
}

// 修改员工列表
export function updateWorker(data) {
  return request({
    url: '/hr/worker',
    method: 'put',
    data: data
  })
}

// 删除员工列表
export function delWorker(id) {
  return request({
    url: '/hr/worker/' + id,
    method: 'delete'
  })
}

// 导出员工列表
export function exportWorker(query) {
  return request({
    url: '/hr/worker/export',
    method: 'get',
    params: query
  })
}