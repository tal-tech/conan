import request from '@/utils/request';

// 根据schemaErrorid
export function getReplaySchemaErrorById(schemaErrorId) {
  return request({
    url: '/api/1.0/common/replaySchemaError/'+schemaErrorId ,
    method: 'get'
  })
}
// 查看列表信息
export function getReplaySchemaErrorList(data) {
  return request({
    url: '/api/1.0/common/replaySchemaError/list' ,
		method: 'get',
		params:data
  })
}
// 删除信息
export function delReplaySchemaError(schemaErrorIds) {
  return request({
    url: '/api/1.0/common/replaySchemaError/' + schemaErrorIds,
    method: 'delete'
  })
}
// 错误接口统计信息
export function getReplaySchemaErrorNumber(data) {
  return request({
    url: '/api/1.0/common/replaySchemaError/response/getResponseInterfaceNumber' ,
		method: 'get',
		params:data
  })
}