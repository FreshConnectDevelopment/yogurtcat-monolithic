import request from '@/utils/request'

export function permissionOptionlist(params) {
  return request({
    url: '/permission/optionlist',
    method: 'get',
    params
  })
}

export function permissionList(params) {
  return request({
    url: '/permission/list',
    method: 'get',
    params
  })
}

export function create(data) {
  return request({
    url: '/permission/create',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/permission/update',
    method: 'post',
    data
  })
}

export function remove(data) {
  return request({
    url: '/permission/' + data,
    method: 'delete'
  })
}
