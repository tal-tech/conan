import request from '@/utils/request'

// 查询ES 数据源配置，域名需要绑定ES数据源列表
export function listEsDataSource(query) {
  return request({
    url: '/api/1.0/common/esDataSource/list',
    method: 'get',
    params: query
  })
}

// 查询ES 数据源配置，域名需要绑定ES数据源详细
export function getEsDataSource(esSourceId) {
  return request({
    url: '/api/1.0/common/esDataSource/' + esSourceId,
    method: 'get'
  })
}

// 新增ES 数据源配置，域名需要绑定ES数据源
export function addEsDataSource(data) {
  return request({
    url: '/api/1.0/common/esDataSource',
    method: 'post',
    data: data
  })
}

// 修改ES 数据源配置，域名需要绑定ES数据源
export function updateEsDataSource(data) {
  return request({
    url: '/api/1.0/common/esDataSource',
    method: 'put',
    data: data
  })
}

// 删除ES 数据源配置，域名需要绑定ES数据源
export function delEsDataSource(esSourceId) {
  return request({
    url: '/api/1.0/common/esDataSource/' + esSourceId,
    method: 'delete'
  })
}

// 导出ES 数据源配置，域名需要绑定ES数据源
export function exportEsDataSource(query) {
  return request({
    url: '/api/1.0/common/esDataSource/export',
    method: 'get',
    params: query
  })
}