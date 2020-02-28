import request from '@/utils/request'

export function getPayment(params) {
  return request({
    url: '/payment/list',
    method: 'get',
    params
  })
}

export function savePayment(data) {
  return request({
    url: '/payment/update',
    method: 'post',
    data
  })
}
