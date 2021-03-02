import request from '@/utils/request'

// 查询接口Schema规则列表
export function listRule(query) {
  return request({
    url: '/api/1.0/admin/api/rule/list',
    method: 'get',
    params: { apiId:query }
  })
}

// 查询接口Schema规则详细信息
export function getRule(id) {
  return request({
    url: '/api/1.0/admin/api/rule/' + id,
    method: 'get'
  })
}

// 新增接口Schema规则
export function addRule(data) {
  return request({
    url: '/api/1.0/admin/api/rule',
    method: 'post',
    data: data
  })
}

// 修改接口Schema规则
export function updateRule(data) {
  return request({
    url: '/api/1.0/admin/api/rule',
    method: 'put',
    data: data
  })
}

// 删除接口Schema规则
export function delRule(id) {
  return request({
    url: '/api/1.0/admin/api/rule/' + id,
    method: 'delete'
  })
}

// 导出接口Schema规则
export function exportRule(query) {
  return request({
    url: '/api/1.0/admin/api/rule/export',
    method: 'get',
    params: query
  })
}