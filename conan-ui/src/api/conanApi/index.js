import request from '@/utils/request'
import conf from '../conf';

// 查询部门Schema规则配置列表
export function getDebug(data) {
  return request({
    url: conf.url+'/debug',
    method: 'post',
    data: data
  })
}
