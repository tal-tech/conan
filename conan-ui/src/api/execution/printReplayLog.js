
import request from '@/utils/request';

// 根据任务id执行回放

export function getToReplay (replay_env,replay_type,task_execution_id) {
  return request({
    url: '/api/1.0/admin/replay?replay_env='+replay_env+'&replay_type='+replay_type+'&task_execution_id='+task_execution_id,
    method: 'post'
  })
}
//  根据任务执行ID查询流量回放记录列表
export function getReplay (data) {
  return request({
    url: '/api/1.0/admin/replay',
    method: 'get',
    params:data
  })
}
//根据回放ID查询回放日志
export function getLogByReplay (data) {
  return request({
    url: '/api/1.0/admin/replay/log',
    method: 'get',
    params:data
  })
}
// 根据回放ID查询回放进度
export function getProgressByReplay (data) {
  return request({
    url: '/api/1.0/admin/replay/progress',
    method: 'get',
    params:data
  })
}
// 根据id查看细节详情
export function getDetailByReplay (data) {
  return request({
    url: '/api/1.0/admin/replay/detail',
    method: 'get',
    params:data
  })
}
// apiid和replayid查看单接口回放数据
export function searchReplayDetailByApi (data) {
  return request({
    url: '/api/1.0/admin/replay/oneApiDetail',
    method: 'get',
    params:data
  })
}