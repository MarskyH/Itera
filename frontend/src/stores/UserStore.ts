import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { models } from 'src/@types'

interface UserModel extends models.UserModel { }

interface State {
  user: UserModel,
  users: UserModel[]
}

const userDefault: UserModel = {
  id: '',
  name: '',
  email: '',
  login: '',
  password: '',
  userRole: ''
}

export const useUserStore = defineStore('User', {
  state: (): State => ({
    user: { ...userDefault },
    users: []
  }),

  actions: {
    async fetchUsers() {
      const response = await Api.request({
        method: 'get',
        route: 'user'
      })

      if (response?.status === 200) {
        this.users = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            name: elem.name,
            email: elem.email,
            login: elem.username,
            userRole: elem.userRole
          }
        })
      }
    },
  }
})