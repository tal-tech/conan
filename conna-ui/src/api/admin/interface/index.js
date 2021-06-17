import request from '@/utils/request'

// 查询接口管理列表
export function listApi(query) {
  return request({
    url: '/api/1.0/admin/api/list',
    method: 'get',
    params: query
  })
}

// 查询接口管理详细
export function getApi(id) {
  return request({
    url: '/api/1.0/admin/api/' + id,
    method: 'get'
  })
}

// 新增接口管理
export function addApi(data) {
  return request({
    url: '/api/1.0/admin/api',
    method: 'post',
    data: data
  })
}

// 修改接口管理
export function updateApi(data) {
  return request({
    url: '/api/1.0/admin/api',
    method: 'put',
    data: data
  })
}

// 删除接口管理
export function delApi(id) {
  return request({
    url: '/api/1.0/admin/api/' + id,
    method: 'delete'
  })
}

// 导出接口管理
export function exportApi(query) {
  return request({
    url: '/api/1.0/admin/api/export',
    method: 'get',
    params: query
  })
}