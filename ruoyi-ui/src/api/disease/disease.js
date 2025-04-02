import request from "@/utils/request";

export function listDisease(query) {
  return request({
    url: '/models/relation/prediction/list',
    method: 'get',
    params: query
  })
}
export function listDiseaseModel(query) {
  return request({
    url: 'models/disease-model/list',
    method: 'get',
    params: query
  })
}
