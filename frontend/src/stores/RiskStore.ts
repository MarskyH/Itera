import { defineStore } from 'pinia'
import Api from 'src/services/api'

import { RiskForm, type models } from 'src/@types'
interface RiskOnCreate extends models.RiskOnCreate { }
interface Risk extends models.Risk { }

interface State {
  risk: Risk
  risks: Risk[]
}

const riskDefault: Risk = {
  id: '',
  title: '',
  effect: '',
  probability: '',
  impact: '',
  exposureDegree: '',
  description: '',
  type: '',
}

export const useRiskStore = defineStore('Risk', {
  state: (): State => ({
    risk: { ...riskDefault },
    risks: []
  }),


  actions: {

    async fetchRisks(projectId: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${projectId}/risks`,
      })
      if (response?.status === 200) {
        this.risks = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            title: elem.title,
            effect: elem.effect,
            probability: elem.probability,
            impact: elem.impact,
            exposureDegree: elem.exposureDegree,
            description: elem.description,
            type: elem.type,
            project: elem.project
          }
        })
      }
    },

    async fetchRisk(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `risk/${id}`,
      })
      if (response?.status === 200) {
        this.risk = {
          id: response.data?.id,
          title: response.data?.title,
          effect: response.data?.effect,
          probability: response.data?.probability,
          impact: response.data?.impact,
          exposureDegree: response.data?.exposureDegree,
          description: response.data?.description,
          type: response.data?.type,
        }
      }
    },

    async createRisk(riskFormData: RiskForm, projectId: string) {
      const riskOnCreateData: RiskOnCreate = {
        title: riskFormData.title,
        effect: riskFormData.effect,
        probability: riskFormData.probability,
        impact: riskFormData.impact,
        exposureDegree: riskFormData.exposureDegree,
        description: riskFormData.description,
        type: riskFormData.type,
        project_id: projectId
      }

      const response = await Api.request({
        method: 'post',
        route: 'risk',
        body: riskOnCreateData
      })

      return (response?.status) !== undefined ? response.status : 500
    },

    async updateRisk(id: string, riskData: Risk) {
      const response = await Api.request({
        method: 'put',
        route: `/risk/${id}`,
        body: riskData
      })

      return response?.status === 200
    },


    async deleteRisk(id: string) {
      const response = await Api.request({
        method: 'delete',
        route: `/risk/${id}`
      })

      if (response?.status === 200) {
        return true
      } else {
        return false
      }
    },
  }
})