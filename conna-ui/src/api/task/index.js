import request from '@/utils/request';

// 获取执行列表数据
export function listTask (data) {
  return request({
    url: '/api/1.0/admin/task/list',
    method: 'get',
    params:data
  })
}


// 查询域名信息详细
export function getTask(id) {
  return request({
    url: '/api/1.0/admin/task/' + id,
    method: 'get'
  })
}

// 新增域名信息
export function addTask(data) {
  return request({
    url: '/api/1.0/admin/task',
    method: 'post',
    data: data
  })
}

// 修改域名信息
export function updateTask(data) {
  return request({
    url: '/api/1.0/admin/task',
    method: 'put',
    data: data
  })
}

// 删除域名信息
export function delTask(id) {
  return request({
    url: '/api/1.0/admin/task/' + id,
    method: 'delete'
  })
}

// 导出域名信息
export function exportTask(query) {
  return request({
    url: '/api/1.0/admin/task/export',
    method: 'get',
    params: query
  })
}

// 任务相关信息
export function findTaskInfo(query) {
  return request({
    url: '/api/1.0/admin/task/info',
    method: 'get',
    params: query
  })
}