import request from '@/utils/request'

export function getResourceInfo(params) {
  return request({
    url: '/resource/list',
    method: 'get',
    params
  })
}

export function removeResource(data) {
  return request({
    url: '/resource/delete',
    method: 'post',
    data
  })
}

export function upload(path) {
  return process.env.VUE_APP_BASE_API + '/resource/upload/' + path
}
