import { defineStore } from 'pinia'
import Api from 'src/services/api'

import { type models } from 'src/@types'
interface PendencyOnCreate extends models.PendencyOnCreate { }
interface Pendency extends models.Pendency { }

interface State {
  pendency: Pendency
  pendencies: Pendency[]
}

const pendencyDefault: Pendency = {
  id: '',
  title: '',
  description: '',
  creationDate: '',
  endDate: '',
  status: true,
  task_id: '',
}

export const usePendencyStore = defineStore('Pendency', {
  state: (): State => ({
    pendency: { ...pendencyDefault },
    pendencies: []
  }),


  actions: {
/*
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
    },*/
  }
})