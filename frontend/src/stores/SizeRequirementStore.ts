import { defineStore } from 'pinia'
import { models } from 'src/@types'
import Api from 'src/services/api'

interface SizeRequirement extends models.SizeRequirement { }

interface State {
  sizeRequirements: SizeRequirement[]
}

export const useSizeRequirementStore = defineStore('SizeRequirement', {
  state: (): State => ({
    sizeRequirements: []
  }),

  actions: {
    async fetchSizeRequirements() {
      const response = await Api.request({
        method: 'get',
        route: 'sizerequirement',
      })
      if (response?.status === 200) {
        this.sizeRequirements = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            name: elem.name
          }
        })
      }
    }
  }
})