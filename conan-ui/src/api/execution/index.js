import request from '@/utils/request';
import conf from '../conf';

// 获取执行列表数据
export function getTaskExecution (data) {
  return request({
    url: '/api/1.0/taskExecution/list',
    method: 'get',
    params:data
  })
}
// // 点击查看详情获取流量接口/api/1.0/record
// export function getRecord (data) {
//   return request({
//     url: conf.publicUrl+'/record',
//     method: 'get',
//     params:data
//   })
// }
// // replay 查看详情要获取的接口数据
// export function getReplay (data) {
//   return request({
//     url: conf.publicUrl+'/replay',
//     method: 'get',
//     params:data
//   })
// }
// // 置为基准的接口
// export function setBaseLineByTaskExecutionID (task_execution_id,replay_id) {
//   return request({
//     url: conf.publicUrl+ "/diff/setBaseLine?task_execution_id=" +  task_execution_id +  "&replay_id=" +  replay_id,
//     method: 'post',
//     data:  {}
//   })
// }
// // diffByTaskExecutionID 执行比对确认比对按钮
// export function diffByTaskExecutionID (task_execution_id,record_id,replay_id) {
//   return request({
//     url: conf.publicUrl+ "/diff?task_execution_id=" +  task_execution_id +  "&record_id=" + record_id + "&replay_id=" + replay_id,
//     method: 'post',
//     data:  {}
//   })
// }

// // 执行比对后面接口数据 （每分钟「info」数据）
// export function diffByTaskReplay (data) {
//   return request({
//     url: conf.publicUrl+ "/log/diff",
//     method: 'get',
//     params: data,
//   })
// }
