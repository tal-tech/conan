import request from '@/utils/request';

//  查询任务
export function GetTaskSchedule (data) {
  return request({
    url: '/api/1.0/taskSchedule',
    method: 'get',
    params:data
  })
}
//   任务创建
export function CreateTaskSchedule (data,task_id) {
  return request({
    url: '/api/1.0/taskSchedule',
    method: 'post',
    data:data
  })
}
//    单个定时任务详情
export function TaskScheduleByTaskId (task_id) {
  return request({
    url: '/api/1.0/taskSchedule/'+task_id,
    method: 'get'
  })
}
// {} 检查cron表达式
export function TaskScheduleByCronExpression (cron_expression) {
  return request({
    url: '/api/1.0/taskSchedule/cronValid/'+cron_expression,
    method: 'get'
  })
}
// {task_id} 删除任务
export function deleteTaskById (id) {
  return request({
    url: '/api/1.0/taskSchedule/deleteTask/'+id,
    method: 'post'
  })
}
// {task_id} 暂停或恢复定时任务
export function execTaskById (task_id) {
  return request({
    url: '/api/1.0/taskSchedule/execTask/'+task_id,
    method: 'post',
  })
}
// 任务更新
export function updateTaskById (data,task_id) {
  return request({
    url: '/api/1.0/taskSchedule/updateTask/'+task_id,
    method: 'post',
    data:data
  })
}