import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/project/list',
    method: 'get',
    params
  })
}

export function createProject(data) {
  return request({
    url: '/project/create',
    method: 'post',
    data
  })
}

export function updateProject(data) {
  return request({
    url: '/project/update',
    method: 'post',
    data
  })
}

export function deleteProject(data) {
  return request({
    url: '/project/delete',
    method: 'post',
    data
  })
}
