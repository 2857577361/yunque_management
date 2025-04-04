import request from "@/utils/request";

export function listDisease(query) {
  return request({
    url: '/models/relation/prediction/list',
    method: 'get',
    params: query
  })
}
export function addDiseasePrediction(data) {
  return request({
    url: '/models/relation/prediction',
    method: 'post',
    data: data
  })
}
export function updateDiseasePrediction(data) {
  return request({
    url: '/models/relation/prediction',
    method: 'put',
    data: data
  })
}
export function listDiseaseModel(query) {
  return request({
    url: 'models/disease-model/list',
    method: 'get',
    params: query
  })
}
