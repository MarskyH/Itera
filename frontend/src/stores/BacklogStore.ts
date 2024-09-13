import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
import { M } from 'vite/dist/node/types.d-jgA8ss1A'
interface BacklogRequirement extends models.BacklogRequirement { }

interface State {
  backlogRequirement: BacklogRequirement
  backlogRequirements: BacklogRequirement[]
}

const backlogRequirementDefault = {
  id: "",
  idRequirement: "",
  title: "",
  priority: "",
  effort: 0,
  sizeRequirement: 0,
  progressiveBar: 0,
  orderRequirement: 0,
  checkCancelled: false
}

export const useBacklogStore = defineStore('Backlog', {
  state: (): State => ({
    backlogRequirement: { ...backlogRequirementDefault },
    backlogRequirements: [],
  }),

  actions: {
    setBacklog(backlogData: BacklogRequirement) {
      this.backlogRequirement = backlogData
    },

    async fetchBacklog(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${id}/backlog`
      })

      if (response?.status === 200) {
        this.backlogRequirements = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            idRequirement: elem.idRequirement,
            title: elem.title,
            effort: elem.effort,
            sizeRequirement: elem.sizeRequirement,
            priority: elem.priority,
            progressiveBar: elem.progressiveBar,
            orderRequirement: elem.orderRequirement,
            checkCancelled: elem.checkCancelled
          }
        })
      }
    },

    async updateBacklogRequirement(requirementId: string, futureIndex: number) {
      const response = await Api.request({
        method: 'put',
        route: `/requirement/${requirementId}`,
        body: { orderRequirement: futureIndex + 1 }
      })

      return response?.status === 200
    },

    async updateBacklogRequirementIteration(requirementId: string, iterationId: string | undefined) {
      console.log(requirementId, iterationId)
      const response = await Api.request({
        method: 'put',
        route: `/requirement/${requirementId}`,
        body: { iterationId: iterationId ? iterationId : '0' }
      })

      return response?.status === 200
    }
  }
})