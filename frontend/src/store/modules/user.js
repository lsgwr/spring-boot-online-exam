import Vue from 'vue'
import { login, getInfo, logout } from '../../api/login'
import { ACCESS_TOKEN } from '../../store/mutation-types'
import { welcome } from '../../utils/util'

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '',
    roles: [],
    info: {}
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, { name, welcome }) => {
      state.name = name
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(response => {
          if (response.code === 0) {
            const token = response.data
            // 把接口返回的token字段的值设置到localStorage的token键值对中，token的有效期是1天,Vue.ls中的ls是localStorage的意思
            Vue.ls.set(ACCESS_TOKEN, token, 24 * 60 * 60 * 1000)
            // 设置token事件,修改全局变量state中的token值，讲mutations中的SET_TOKEN事件
            commit('SET_TOKEN', token)
            resolve()
          } else {
            // 自定义错误
            reject(new Error('用户名或密码错误'))
          }
        }).catch(error => {
          console.log(error)
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          console.log('/user/info的响应如下：')
          console.log(response)
          const result = response.data // 取出响应体

          if (result.role && result.role.permissions.length > 0) { // 如果权限
            const role = result.role
            role.permissions = result.role.permissions // permissions是给页面行为设置权限
            role.permissions.map(per => {
              if (per.actionEntitySet != null && per.actionEntitySet.length > 0) {
                const action = per.actionEntitySet.map(action => {
                  return action.action
                })
                per.actionList = action
              }
            })
            role.permissionList = role.permissions.map(permission => { // permissionList是从permissions中遍历解析得来的
              return permission.permissionId
            })

            // 这些设置都在Vuex的getters里面了
            commit('SET_ROLES', result.role) // 在store中设置用户的权限
            commit('SET_INFO', result) // 在store中设置用户信息
          } else {
            reject(new Error('getInfo: roles must be a non-null array !'))
          }

          // 这些设置都在Vuex的getters里面了
          commit('SET_NAME', { name: result.name, welcome: welcome() }) // 设置用户名称
          commit('SET_AVATAR', result.avatar) // 设置用户头像

          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    Logout ({ commit, state }) {
      return new Promise((resolve) => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        Vue.ls.remove(ACCESS_TOKEN)

        logout(state.token).then(() => {
          resolve()
        }).catch(() => {
          resolve()
        })
      })
    }

  }
}

export default user
