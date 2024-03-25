import { defineStore } from 'pinia'
import { models } from 'src/@types'
import Api from 'src/services/api'

interface RiskActionType extends models.RiskActionType { }

interface State {
  riskActionTypes: RiskActionType[]
}

export const useRiskActionTypeStore = defineStore('RiskActionType', {
  state: (): State => ({
    riskActionTypes: []
  }),

  actions: {
    async fetchRiskActionTypes() {
      const response = await Api.request({
        method: 'get',
        route: 'riskActionType',
      })
      if (response?.status === 200) {
        this.riskActionTypes = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            name: elem.name
          }
        })
      }
    }
  }
})