import request from '@/utils/request'

// 查询员工划片列表
export function listMacworker(query) {
  return request({
    url: '/hr/macworker/list',
    method: 'get',
    params: query
  })
}

// 查询员工划片详细
export function getMacworker(id) {
  return request({
    url: '/hr/macworker/' + id,
    method: 'get'
  })
}

// 新增员工划片
export function addMacworker(data) {
  return request({
    url: '/hr/macworker',
    method: 'post',
    data: data
  })
}

// 修改员工划片
export function updateMacworker(data) {
  return request({
    url: '/hr/macworker',
    method: 'put',
    data: data
  })
}

// 删除员工划片
export function delMacworker(id) {
  return request({
    url: '/hr/macworker/' + id,
    method: 'delete'
  })
}

// 导出员工划片
export function exportMacworker(query) {
  return request({
    url: '/hr/macworker/export',
    method: 'get',
    params: query
  })
}
