import request from '@/utils/request'

// 查询上报数据列表
export function listModelData(query) {
  return request({
    url: '/system/model/list',
    method: 'get',
    params: query
  })
}
