import { defineStore } from 'pinia'
import { models } from 'src/@types'
import Api from 'src/services/api'

interface Priority extends models.Priority { }

interface State {
  priorities: Priority[]
}

export const usePriorityStore = defineStore('Priority', {
  state: (): State => ({
    priorities: []
  }),

  actions: {
    async fetchPriorities() {
      const response = await Api.request({
        method: 'get',
        route: 'priority',
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