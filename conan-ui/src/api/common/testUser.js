import request from '@/utils/request'

// 查询测试账号管理列表
export function listTestUser(query) {
  return request({
    url: '/api/1.0/common/testUser/list',
    method: 'get',
    params: query
  })
}

// 查询测试账号管理详细
export function getTestUser(id) {
  return request({
    url: '/api/1.0/common/testUser/' + id,
    method: 'get'
  })
}

// 新增测试账号管理
export function addTestUser(data) {
  return request({
    url: '/api/1.0/common/testUser',
    method: 'post',
    data: data
  })
}

// 修改测试账号管理
export function updateTestUser(data) {
  return request({
    url: '/api/1.0/common/testUser',
    method: 'put',
    data: data
  })
}

// 删除测试账号管理
export function delTestUser(id) {
  return request({
    url: '/api/1.0/common/testUser/' + id,
    method: 'delete'
  })
}

// 导出测试账号管理
export function exportTestUser(query) {
  return request({
    url: '/api/1.0/common/testUser/export',
    method: 'get',
    params: query
  })
}
