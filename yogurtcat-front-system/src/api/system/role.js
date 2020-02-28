import request from '@/utils/request'

export function roleOptionlist(params) {
  return request({
    url: '/role/optionlist',
    method: 'get',
    params
  })
}

export function list(params) {
  return request({
    url: '/role/list',
    method: 'get',
    params
  })
}

export function create(data) {
  return request({
    url: '/role/create',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/role/update',
    method: 'post',
    data
  })
}

export function remove(data) {
  return request({
    url: '/role/' + data,
    method: 'delete'
  })
}

export function editPermission(data) {
  return request({
    url: '/role/permission',
    method: 'put',
    data
  })
}

export function editMenu(data) {
  return request({
    url: '/role/menu',
    method: 'put',
    data
  })
}

export function get(id) {
  return request({
    url: '/role/' + id,
    method: 'get'
  })
}
