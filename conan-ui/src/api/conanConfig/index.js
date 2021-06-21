import request from '@/utils/request'
import conf from '../conf';

// 获取产品线名称及id
export function RuleInfo(query) {
  return request({
    url: conf.baseUrl+'/rule/product/line/info',
    method: 'get',
    params: query
  })
}

// 产品线规则信息
export function LineRuleInfo(query) {
  return request({
    url: conf.baseUrl+'/rule/product/line/rule_info',
    method: 'get',
    params: query
  })
}
