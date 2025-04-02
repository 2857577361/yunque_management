import request from '@/utils/request'

// 查询上报数据列表
export function listModelData(query) {
  return request({
    url: '/models/model/list',
    method: 'get',
    params: query
  })
}
export function listDiseaseModel(query) {
  return request({
    url: `/models/relation/device/${query.id}`,
    method: 'get'
  })
}
export function updataDiseaseModelByDevideId(data) {
  return request({
    url: `/models/relation/update`,
    method: 'post',
    data: data
  })
}
export function updataDiseaseModelRelation(data) {
  return request({
    url: `/models/disease-model`,
    method: 'put',
    data: data
  })
}
export function delDeviceDiseaseModel(data) {
  return request({
    url: `/models/relation/delete`,
    method: 'delete',
    data: data
  })
}
