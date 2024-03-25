import { defineStore } from 'pinia'
import { models } from 'src/@types'
import Api from 'src/services/api'

interface Degree extends models.Degree { }

interface State {
  degrees: Degree[]
}

export const useDegreeStore = defineStore('Degree', {
  state: (): State => ({
    degrees: []
  }),

  actions: {
    async fetchDegrees() {
      const response = await Api.request({
        method: 'get',
        route: 'degree',
      })
      if (response?.status === 200) {
        this.degrees = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            name: elem.name
          }
        })
      }
    }
  }
})