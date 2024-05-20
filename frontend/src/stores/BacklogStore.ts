import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface Backlog extends models.Backlog { }

interface State {
  backlog: Backlog
  items: Backlog[]
}

const itemBacklogDefault = {
  id: 0,
  idRequirement: "",
  title: "",
  priority: "",
  progressiveBar: 0
}

export const useBacklogStore = defineStore('Backlog', {
  state: (): State => ({
    backlog: { ...itemBacklogDefault },
    items: [],
  }),

  actions: {
    setBacklog(backlogData: Backlog) {
      this.backlog = backlogData
    },

    async fetchBacklog(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${id}/backlog`
      })

      if (response?.status === 200) {
        this.items = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            idRequirement: elem.idRequirement,
            title: elem.title,
            priority: elem.priority,
            progressiveBar: elem.progressiveBar
          }
        })
      }
    }
  }
})