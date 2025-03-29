import request from '@/utils/request'

// 查询设备列表列表
export function listDevice(query) {
  return request({
    url: '/device/device/list',
    method: 'get',
    params: query
  })
}
export function listDeviceByRole(query) {
  // console.log(query)
  return request({
    url: '/device/device/listByRoleAndDept',
    method: 'get',
    params: query
  })
}
export function listClimateDataByDeviceIds(deviceList, query) {
  // console.log(query)
  return request({
    url: '/device/sensor/queryClimateDataByDeviceIds',
    method: 'post',
    data: deviceList,
    params: query
  })
}
export function listDept() {
  return request({
    url: '/system/dept/names?parentId=101',
    method: 'get'
  })
}
export function listCity() {
  return request({
    url: '/system/city/tree',
    method: 'get'
  })
}
// 查询设备列表详细
export function getDevice(id) {
  return request({
    url: '/device/device/' + id,
    method: 'get'
  })
}



// 新增设备列表
export function addDevice(data) {
  return request({
    url: '/device/device',
    method: 'post',
    data: data
  })
}

// 修改设备列表
export function updateDevice(data) {
  return request({
    url: '/device/device',
    method: 'put',
    data: data
  })
}

// 删除设备列表
export function delDevice(id) {
  return request({
    url: '/device/device/' + id,
    method: 'delete'
  })
}
