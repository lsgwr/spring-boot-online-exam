// 自己的借口呀:用户的注册和登录等服务，注意所有的接口应该都现在index.js里面注册，方便统一管理

import api from './index'
import { axios } from '../utils/request'

export function login (parameter) {
  return axios({
    url: api.UserLogin,
    method: 'post',
    data: parameter
  })
}

export function register (parameter) {
  return axios({
    url: api.UserRegister,
    method: 'post',
    data: parameter
  })
}
