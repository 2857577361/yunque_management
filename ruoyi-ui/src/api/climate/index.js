import request from '@/utils/request'

// 查询设备列表列表
export function listAlert(query) {
  return request({
    url: '/climate/alert/list',
    method: 'get',
    params: query
  })
}
export function updateAlert(data) {
  return request({
    url: '/climate/alert',
    method: 'put',
    data: data
  })
}
