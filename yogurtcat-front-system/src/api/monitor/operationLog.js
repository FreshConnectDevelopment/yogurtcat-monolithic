import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/operationLog/list',
    method: 'get',
    params
  })
}
