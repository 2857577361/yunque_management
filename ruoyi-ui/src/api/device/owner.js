import request from '@/utils/request'

export function getOwner(id) {
  return request({
    url: '/owner/owner/' + id,
    method: 'get'
  })
}
