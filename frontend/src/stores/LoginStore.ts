import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
import LocalStorage from 'src/services/localStorage'
import CookieService from 'src/services/cookieService'
interface LoginModel extends models.LoginModel {}
interface UserData extends models.UserData {}

const storage = new LocalStorage()
const cookieService = new CookieService()

export const useLoginStore = defineStore('LoginStore', {
  state: () => ({
    loggedIn: storage.getToken() !== undefined,
    userData: cookieService.get('Token') !== undefined ? storage.parseToken(cookieService.get('Token')) : null,
    isFirstAccess: storage.getToken() === undefined ? 'empty' : 'no'
  }),

  actions: {
    async userLogin (login: string, password: string) {
      const data: LoginModel = {
        login,
        password
      }

      const response = await Api.request({
        method: 'post',
        route: '/auth/login',
        body: data
      })

      if (response?.status === 200) {
        this.loggedIn = true
        storage.setLoggedUser(response.data?.token)
        this.setUserData(storage.parseToken(response.data?.token))
      }

      return {
        status: (response?.status) !== undefined ? response.status : 500,
        data: (response?.data !== undefined) ? response?.data : null
      }
    },

    userLogout () {
      this.loggedIn = false
      storage.remove('loggedUser')
      cookieService.remove('Token')
      this.userData = null
      this.isFirstAccess = 'empty'
      if (storage.has('loggedEgresso')) {
        storage.remove('loggedEgresso')
      }
    },

    getUserData (): UserData | null {
      if (this.userData !== undefined && cookieService.get('Token') !== undefined) return this.userData
      return null
    },

    setUserData (userData: UserData | null): void {
      this.userData = userData
    }
  }
})