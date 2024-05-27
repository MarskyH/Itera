import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface Iteration extends models.Iteration { }

interface State {
  iteration: Iteration
  iterations: Iteration[]
}

const iterationDefault = {
  id: "",
  number: 0,
  startDate: "",
  endDate: "",
  active: false,
  project_id: "",
  requirements:[]
}

export const useIterationStore = defineStore('Iteration', {
  state: (): State => ({
    iteration: { ...iterationDefault },
    iterations: [],
  }),

  actions: {
    setIteration(iterationData: Iteration) {
      this.iteration = iterationData
    },

    async fetchIterations(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${id}/iteration`
      })

      if (response?.status === 200) {
        console.log(response.data)

        this.iterations = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            number: elem.number,
            startDate: elem.startDate,
            endDate: elem.endDate,
            active: elem.active,
            project_id: elem.project_id,
            requirements: elem.requirements
          }
        })
      }
    },

    async createIterations(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${id}/backlog`
      })

      if (response?.status === 200) {
        this.iterations = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            number: elem.number,
            startDate: elem.startDate,
            endDate: elem.endDate,
            project_id: elem.project_id
          }
        })
      }
    },

    /*
    async updateBacklogRequirement(requirementData: Iteration) {
      const response = await Api.request({
        method: 'put',
        route: `/requirement/${requirementData.id}`,
        body: { orderRequirement: requirementData.orderRequirement }
      })

      return response?.status === 200
    }*/
  }
})