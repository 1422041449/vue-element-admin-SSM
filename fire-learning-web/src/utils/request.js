import axios from 'axios'
import { Loading, Message, MessageBox } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import md5 from 'js-md5'

let loadingInstance
// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

/**
 * 获取指定长度随机字符串
 */
function nonceStr(len) {
  len = len || 32
  var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678'
  /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
  var maxPos = $chars.length
  var pwd = ''
  for (let i = 0; i < len; i++) {
    pwd += $chars.charAt(Math.floor(Math.random() * maxPos))
  }
  return pwd
}

/**
 * 请求拦截器
 */
service.interceptors.request.use(
  config => {
    //调用接口时弹出请稍等等待
    if (config.timeout <= 100000) {
      // timer = setTimeout(() => {
      loadingInstance = Loading.service({
        fullscreen: true,
        text: '请稍等'
      })
      // }, 200)
    }

    let data
    //进行MD5加密
    if (config.method == 'get') {
      console.log('get请求的config：', config)
      data = config.params
      data.nonceStr = nonceStr(6)
      data.timestamp = new Date().getTime()
      let str = `content=${JSON.stringify(data.content)}&nonceStr=${
        data.nonceStr
      }&signType=md5&timestamp=${data.timestamp}&version=${
        data.version
      }&key=md5ownkeyvalue001`
      console.log('待签名数据：' + str)
      data.sign = md5(str)
      data.sign = data.sign.toLocaleUpperCase()
    } else {
      if (typeof config.data == 'string') {
        data = JSON.parse(config.data)
        data.nonceStr = nonceStr(6)
        data.timestamp = new Date().getTime()
        let str = `content=${JSON.stringify(data.content)}&nonceStr=${
          data.nonceStr
        }&signType=md5&timestamp=${data.timestamp}&version=${
          data.version
        }&key=md5ownkeyvalue001`
        console.log('待签名数据：' + str)
        data.sign = md5(str)
        data.sign = data.sign.toLocaleUpperCase()
        config.data = JSON.stringify(data)
      } else {
        data = config.data
        data.nonceStr = nonceStr(6)
        data.timestamp = new Date().getTime()
        let str = `content=${JSON.stringify(data.content)}&nonceStr=${
          data.nonceStr
        }&signType=md5&timestamp=${data.timestamp}&version=${
          data.version
        }&key=md5ownkeyvalue001`
        console.log('待签名数据：' + str)
        data.sign = md5(str)
        data.sign = data.sign.toLocaleUpperCase()
      }
      console.log(config)
    }

    // do something before request is sent
    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['token'] = getToken()
    }
    console.log(config)
    return config
  },
  error => {
    if (loadingInstance) {
      loadingInstance.close()
    }
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

/**
 * 返回拦截器
 */
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data
    // if the custom code is not 20000, it is judged as an error.
    if (loadingInstance) {
      loadingInstance.close()
    }
    if (res.code !== 10000) {
      Message({
        message: res.msg,
        type: 'error',
        duration: 5 * 1000
      })

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('userinfo/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    if (loadingInstance) {
      loadingInstance.close()
    }
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
