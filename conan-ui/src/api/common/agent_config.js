import request from '@/utils/request'

// 查询work机管理列表
export function listAgent_config(query) {
  return request({
    url: '/api/1.0/common/agent_config/list',
    method: 'get',
    params: query
  })
}

// 查询work机管理详细
export function getAgent_config(id) {
  return request({
    url: '/api/1.0/common/agent_config/' + id,
    method: 'get'
  })
}

// 新增work机管理
export function addAgent_config(data) {
  return request({
    url: '/api/1.0/common/agent_config',
    method: 'post',
    data: data
  })
}

// 修改work机管理
export function updateAgent_config(data) {
  return request({
    url: '/api/1.0/common/agent_config',
    method: 'put',
    data: data
  })
}

// 删除work机管理
export function delAgent_config(id) {
  return request({
    url: '/api/1.0/common/agent_config/' + id,
    method: 'delete'
  })
}

// 导出work机管理
export function exportAgent_config(query) {
  return request({
    url: '/api/1.0/common/agent_config/export',
    method: 'get',
    params: query
  })
}