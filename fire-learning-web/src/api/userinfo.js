import request from '@/utils/request'

let dataObj = {
  version: '1.0',
  nonceStr: 'jlwnoncestr',
  timestamp: '2021-10-24',
  signType: 'md5',
  content: { object: {} },
  sign: 'dafdsf'
}

export function login(data) {
  let obj = JSON.parse(JSON.stringify(dataObj))
  if (data) {
    obj.content = data
  }
  return request({
    url: '/userinfo/login',
    method: 'post',
    data: obj
  })
}

export function getInfo(data) {
  let obj = JSON.parse(JSON.stringify(dataObj))
  if (data) {
    obj.content = data
  }
  return request({
    url: '/userinfo/getInfo',
    method: 'get',
    params: obj
  })
}

export function logout(data) {
  let obj = JSON.parse(JSON.stringify(dataObj))
  if (data) {
    obj.content = data
  }
  return request({
    url: '/userinfo/logout',
    method: 'get',
    params: obj
  })
}
