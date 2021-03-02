import request from '@/utils/request'

// 查询Task任务(根据部门)
export function getInfoByDeptIdTaskApiRelation(id) {
  return request({
    url: '/api/1.0/common/taskApiRelation/deptId/' + id,
    method: 'get',
  })
}
// 查询Task任务(根据域名)
export function getInfoByDomainIdTaskApiRelation(id) {
  return request({
    url: '/api/1.0/common/taskApiRelation/domainId/' + id,
    method: 'get',
  })
}
// 查询Task任务(根据接口名称)
export function getInfoByApiNameAndDomainNameTaskApiRelation(data) {
  return request({
    url: '/api/1.0/common/taskApiRelation/getInfo' ,
    method: 'get',
    params:data
  })
}
// 查询Task任务与接口关联关系详细（通过id）
export function getTaskApiRelation(id) {
  return request({
    url: '/api/1.0/common/taskApiRelation/taskId/' + id,
    method: 'get'
  })
}

// 新增Task任务与接口关联关系
export function addTaskApiRelation(data) {
  return request({
    url: '/api/1.0/common/taskApiRelation',
    method: 'post',
    data: data
  })
}




