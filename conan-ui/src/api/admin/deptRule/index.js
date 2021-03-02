import request from '@/utils/request'

// 查询部门Schema规则配置列表
export function listDeptRule(query) {
  return request({
    url: '/api/1.0/admin/deptRule/list',
    method: 'get',
    params: query
  })
}

// 查询部门Schema规则配置详细
export function getDeptRule(id) {
  return request({
    url: '/api/1.0/admin/deptRule/' + id,
    method: 'get'
  })
}

// 新增部门Schema规则配置
export function addDeptRule(data) {
  return request({
    url: '/api/1.0/admin/deptRule',
    method: 'post',
    data: data
  })
}

// 修改部门Schema规则配置
export function updateDeptRule(data) {
  return request({
    url: '/api/1.0/admin/deptRule',
    method: 'put',
    data: data
  })
}

// 删除部门Schema规则配置
export function delDeptRule(id) {
  return request({
    url: '/api/1.0/admin/deptRule/' + id,
    method: 'delete'
  })
}

// 导出部门Schema规则配置
export function exportDeptRule(query) {
  return request({
    url: '/api/1.0/admin/deptRule/export',
    method: 'get',
    params: query
  })
}