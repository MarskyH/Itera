import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface BacklogRequirement extends models.BacklogRequirement { }

interface State {
  backlogRequirement: BacklogRequirement
  backlogRequirements: BacklogRequirement[]
}

const backlogRequirementDefault = {
  id: 0,
  idRequirement: "",
  title: "",
  priority: "",
  progressiveBar: 0,
  orderRequirement: 0
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
        console.log(response.data)
        this.backlogRequirements = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            idRequirement: elem.idRequirement,
            title: elem.title,
            priority: elem.priority,
            progressiveBar: elem.progressiveBar,
            orderRequirement: elem.orderRequirement
          }
        })
      }
    },

    async updateBacklogRequirement(requirementData: BacklogRequirement) {
      const response = await Api.request({
        method: 'put',
        route: `/requirement/${requirementData.id}`,
        body: { orderRequirement: requirementData.orderRequirement }
      })

      return response?.status === 200
    }
  }
})