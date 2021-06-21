import request from '@/utils/request'

// 查询域名鉴权列表
export function listDomainAuth(query) {
  return request({
    url: '/api/1.0/common/domainAuth/list',
    method: 'get',
    params: query
  })
}

// 查询域名鉴权详细
export function getDomainAuth(id) {
  return request({
    url: '/api/1.0/common/domainAuth/' + id,
    method: 'get'
  })
}

// 新增域名鉴权
export function addDomainAuth(data) {
  return request({
    url: '/api/1.0/common/domainAuth',
    method: 'post',
    data: data
  })
}

// 修改域名鉴权
export function updateDomainAuth(data) {
  return request({
    url: '/api/1.0/common/domainAuth',
    method: 'put',
    data: data
  })
}

// 删除域名鉴权
export function delDomainAuth(id) {
  return request({
    url: '/api/1.0/common/domainAuth/' + id,
    method: 'delete'
  })
}

// 导出域名鉴权
export function exportDomainAuth(query) {
  return request({
    url: '/api/1.0/common/domainAuth/export',
    method: 'get',
    params: query
  })
}