import request from '@/utils/request'

export function menuOptionlist(params) {
  return request({
    url: '/menu/optionlist',
    method: 'get',
    params
  })
}

export function menuList(params) {
  return request({
    url: '/menu/list',
    method: 'get',
    params
  })
}

export function menuListForSidebar(params) {
  return request({
    url: '/menu/listForSidebar',
    method: 'get',
    params
  })
}

export function create(data) {
  return request({
    url: '/menu/create',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/menu/update',
    method: 'post',
    data
  })
}

export function remove(data) {
  return request({
    url: '/menu/' + data,
    method: 'delete'
  })
}
