import request from '@/utils/request'

export function getMessageConfig(params) {
  return request({
    url: '/message/list',
    method: 'get',
    params
  })
}

export function saveMessage(data) {
  return request({
    url: '/message/update',
    method: 'post',
    data
  })
}

export function sendMessage(data) {
  return request({
    url: '/message/test',
    method: 'post',
    data
  })
}
