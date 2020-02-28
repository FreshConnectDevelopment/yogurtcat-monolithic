const vue2json = (vue) => {
  if (vue) {
    return JSON.parse(JSON.stringify(vue))
  }
}

const GetUrlRelativePath = (url) => {
  if (url) {
    var arrUrl = url.split('//')

    var start = arrUrl[1].indexOf('/')
    var relUrl = arrUrl[1].substring(start)

    if (relUrl.indexOf('?') !== -1) {
      relUrl = relUrl.split('?')[0]
    }
    return relUrl
  }
}

export default {
  vue2json,
  GetUrlRelativePath
}
