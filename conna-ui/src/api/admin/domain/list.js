import request from '@/utils/request'

// 查询域名信息列表
export function listDomain(query) {
  return request({
    url:'/api/1.0/admin/domain/list',
    method: 'get',
    params: query
  })
}

// 查询域名信息详细
export function getDomain(id) {
  return request({
    url: '/api/1.0/admin/domain/' + id,
    method: 'get'
  })
}

// 新增域名信息
export function addDomain(data) {
  return request({
    url: '/api/1.0/admin/domain',
    method: 'post',
    data: data
  })
}

// 修改域名信息
export function updateDomain(data) {
  return request({
    url: '/api/1.0/admin/domain',
    method: 'put',
    data: data
  })
}

// 删除域名信息
export function delDomain(id) {
  return request({
    url: '/api/1.0/admin/domain/' + id,
    method: 'delete'
  })
}

// 导出域名信息
export function exportDomain(query) {
  return request({
    url: '/api/1.0/admin/domain/export',
    method: 'get',
    params: query
  })
}