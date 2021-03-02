import request from '@/utils/request'

// 查询esConditionSetting域名下ES 查询条件配置列表
export function listEsConditionSetting(query) {
  return request({
    url: '/api/1.0/common/esConditionSetting/list',
    method: 'get',
    params: query
  })
}

// 查询esConditionSetting域名下ES 查询条件配置详细
export function getEsConditionSetting(esSettingId) {
  return request({
    url: '/api/1.0/common/esConditionSetting/' + esSettingId,
    method: 'get'
  })
}

// 新增esConditionSetting域名下ES 查询条件配置
export function addEsConditionSetting(data) {
  return request({
    url: '/api/1.0/common/esConditionSetting',
    method: 'post',
    data: data
  })
}

// 修改esConditionSetting域名下ES 查询条件配置
export function updateEsConditionSetting(data) {
  return request({
    url: '/api/1.0/common/esConditionSetting',
    method: 'put',
    data: data
  })
}

// 删除esConditionSetting域名下ES 查询条件配置
export function delEsConditionSetting(esSettingId) {
  return request({
    url: '/api/1.0/common/esConditionSetting/' + esSettingId,
    method: 'delete'
  })
}

// 导出esConditionSetting域名下ES 查询条件配置
export function exportEsConditionSetting(query) {
  return request({
    url: '/api/1.0/common/esConditionSetting/export',
    method: 'get',
    params: query
  })
}