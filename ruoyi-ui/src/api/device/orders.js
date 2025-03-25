import request from '@/utils/request'

// 查询工单列表
export function listOrders(query) {
  return request({
    url: '/device/orders/list',
    method: 'get',
    params: query
  })
}

// 查询维修详情
export function listOrdersDetail(id) {
  return request({
    url: `/device/orders/getRepairInfo/${id}`,
    method: 'get'
  })
}
export function upDateOrdersRepairDetail(query) {
  return request({
    url: 'device/orders/uploadRepairInfo',
    method: 'put',
    data: query
  })
}

// 查询工单详细
export function getOrders(id) {
  return request({
    url: '/device/orders/' + id,
    method: 'get'
  })
}

// 新增工单
export function addOrders(data) {
  return request({
    url: '/device/orders',
    method: 'post',
    data: data
  })
}

// 修改工单
export function updateOrders(data) {
  return request({
    url: '/device/orders',
    method: 'put',
    data: data
  })
}

// 删除工单
export function delOrders(id) {
  return request({
    url: '/device/orders/' + id,
    method: 'delete'
  })
}
