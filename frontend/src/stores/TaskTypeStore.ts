import { defineStore } from 'pinia'
import { models } from 'src/@types'
import Api from 'src/services/api'

interface TaskType extends models.TaskType { }

interface State {
  types: TaskType[]
}

export const useTaskTypeStore = defineStore('TaskType', {
  state: (): State => ({
    types: []
  }),

  actions: {
    async fetchTaskTypes() {
      const response = await Api.request({
        method: 'get',
        route: 'tasktype',
      })
      if (response?.status === 200) {
        this.types = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            name: elem.name
          }
        })
      }
    }
  }
})