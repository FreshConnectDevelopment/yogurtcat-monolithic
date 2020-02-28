import request from '@/utils/request'

export function getRabbitConfig(params) {
  return request({
    url: '/rabbit/list',
    method: 'get',
    params
  })
}

export function saveRabbit(data) {
  return request({
    url: '/rabbit/update',
    method: 'post',
    data
  })
}

export function removeRabbit(data) {
  return request({
    url: '/rabbit/delete',
    method: 'post',
    data
  })
}
