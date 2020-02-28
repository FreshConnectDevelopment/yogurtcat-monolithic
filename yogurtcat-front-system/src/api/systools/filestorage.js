import request from '@/utils/request'

export function getFileInfo(params) {
  return request({
    url: '/storage/list',
    method: 'get',
    params
  })
}

export function addFileStorage(data) {
  return request({
    url: '/storage/create',
    method: 'post',
    data
  })
}

export function updateFileStorage(data) {
  return request({
    url: '/storage/update',
    method: 'post',
    data
  })
}

export function removeFileStorage(data) {
  return request({
    url: '/storage/delete',
    method: 'post',
    data
  })
}

export function importExcel(data) {
  return request({
    url: '/storage/importExcel',
    method: 'post',
    data
  })
}

export function downloadTemp(params) {
  return request({
    url: '/storage/exportTemp',
    method: 'post',
    responseType: 'blob',
    params
  })
}

export function exportExcel(params) {
  return request({
    url: '/storage/exportExcel',
    method: 'post',
    responseType: 'blob',
    params
  })
}

export function imgUploadTinymce(data) {
  return request({
    url: '/resource/upload/通用/tinymce',
    method: 'post',
    data
  })
}
