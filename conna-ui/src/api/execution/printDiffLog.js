
import request from '@/utils/request';


// 执行比对按钮接口
export function getToDiff (data) {
  return request({
    url: '/api/1.0/common/diff/start',
    method: 'get',
    params:data
  })
}
// 根据比对记录Id得到日志内容
export function getDiffInfo (data) {
  return request({
    url: '/api/1.0/common/diff/log',
    method: 'get',
    params:data
  })
}
//查询diff列表图表信息
export function getDiffInfoList (data) {
  return request({
    url: '/api/1.0/common/diff/list',
    method: 'get',
    params:data
  })
}
// 执行比对执行接口单接口那个
export function getDiffDetailList (data) {
  return request({
    url: '/api/1.0/common/diff/detail',
    method: 'get',
    params:data
  })
}
// 进度条
export function getDiffProgress (data) {
  return request({
    url: '/api/1.0/common/diff/progress',
    method: 'get',
    params:data
  })
}

// setBaseLineByTaskExecutionID值为基准

export function setBaseLineByTaskExecutionID (data) {
  return request({
    url: '/api/1.0/admin/replay/setBaseLine',
    method: 'get',
    params:data
  })
}

// 点击比对结果按钮查比对id
export function getDiffIdByReplayId (data) {
  return request({
    url: '/api/1.0/admin/replay/getDiffIdByReplayId',
    method: 'get',
    params:data
  })
}
