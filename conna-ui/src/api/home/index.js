import request from '@/utils/request';
import conf from '../conf';


// 获取首页方框数据信息（6个）
export function getAllcountHome () {
  return request({
    url: '/api/1.0/admin/home/getAllCount',
    method: 'get',
  })
}
//获取核心监控数据
export function getHomeDataWithTimeHome () {
  return request({
    url: '/api/1.0/admin/home/getImportantData',
    method: 'get',
  })
}


// 获取部门监控数据
export function getProductLineDataHome (data) {
  return request({
    url: '/api/1.0/admin/home/getDepartmentData',
    method: 'get',
    params: data || {}
  })
}
// 获取任务排名数据
export function getTaskRankHome (data) {
  return request({
    url: '/api/1.0/admin/home/getTaskRank',
    method: 'get',
    params: data || {}
  })
}
//获取机器执行状态agent
export function getAgentHome (data) {
  return request({
    url:'/api/1.0/admin/home/getAgentNode',
    method: 'get',
    params: data || {}
  })
}
// 留言板
export function messagesHome (data) {
  return request({
    url:'/api/1.0/messages',
    method: 'get',
    params: {
      size:data
    }
  })
}
// 添加留言板
export function messagesAddHome (username,content) {
  return request({
    url:'/api/1.0/messages/add?content='+content+'&username='+username,
    method: 'post'
  })
}