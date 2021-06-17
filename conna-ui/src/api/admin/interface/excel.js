import request from '@/utils/request'

// 解析excel导入API接口
export function excelExportList(data) {
  return request({
    url: '/api/1.0/excel',
    method: 'post',
    data: data
  })
}
//  删除文件
export function excelDeleteFileByDomain (data) {
  return request({
    url: '/api/1.0/excel/deleteFileByDomain',
    method: 'get',
    params:{domain:data} 
  })
}
// 根据domainName下载文件(不删除生成到文件)
export function excelDownloadFileNoDeleteByDomain (data) {
  return request({
    url: '/api/1.0/excel/downloadFileNoDeleteByDomain',
    method: 'get',
    params:{domain:data} 
  })
}

//获取文件列表
export function excelGetFileNameList (data) {
  return request({
    url: '/api/1.0/excel/getFileNameList',
    method: 'get',
    params:data 
  })
}
//  通过域名下载流量
export function excelDownloadByDomain (data) {
  return request({
    url: '/api/1.0/admin/record/downloadByDomain',
    method: 'get',
    params:data 
  })
}