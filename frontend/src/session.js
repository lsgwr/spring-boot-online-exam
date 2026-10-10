import { defineStore } from 'pinia'

export const useSession = defineStore('session', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || 'null')
  }),
  getters: {
    roleId: (s) => s.user?.roleId || 0,
    admin: (s) => s.user?.roleId === 1,
    staff: (s) => s.user?.roleId === 1 || s.user?.roleId === 2
  },
  actions: {
    save(auth) {
      this.token = auth.token
      this.user = auth.user
      localStorage.setItem('token', auth.token)
      localStorage.setItem('user', JSON.stringify(auth.user))
    },
    clear() {
      this.token = ''
      this.user = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})
