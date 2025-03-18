import request from "@/utils/request";

export function listDisease(query) {
  return request({
    url: '/system/deviceDisease/list',
    method: 'get',
    params: query
  })
}
