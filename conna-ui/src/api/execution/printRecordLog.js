
import request from '@/utils/request';

// 执行录制的接口 创建并录制流量
export function getTaskExecutionByRecord (data) {
  return request({
    url: '/api/1.0/taskExecution?task_id='+data,
    method: 'post',
  })
}
// 根据录制id查询日志
export function getLogByRecord (data) {
  return request({
    url: '/api/1.0/admin/record/log',
    method: 'get',
    params:data
  })
}
// 查看接口进度条
export function getProgressByRecord (data) {
  return request({
    url: '/api/1.0/admin/record/progress',
    method: 'get',
    params:data
  })
}
// 查询流量详情内容
export function getDetailByRecord (data) {
  return request({
    url: '/api/1.0/admin/record',
    method: 'get',
    params:data
  })
}