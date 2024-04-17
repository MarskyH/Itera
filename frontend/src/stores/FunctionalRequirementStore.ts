import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { FunctionalRequirementForm, type models } from 'src/@types'
interface FunctionalRequirement extends models.FunctionalRequirement { }
interface FunctionalRequirementOnCreate extends models.FunctionalRequirementOnCreate { }

interface State {
  functionalRequirement: FunctionalRequirement
  functionalRequirements: FunctionalRequirement[]
}

const functionalRequirementDefault: FunctionalRequirement = {
  id: '',
  title: '',
  details: '',
  complexity: '',
  priority: '',
  effort: 0,
  sizeRequirement: 0
}

export const useFunctionalRequirementStore = defineStore('FunctionalRequirement', {
  state: (): State => ({
    functionalRequirement: {
      ...functionalRequirementDefault
    },
    functionalRequirements: []
  }),

  actions: {
    async fetchFunctionalRequirements(projectId: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${projectId}/requirements`
      })
      if (response?.status === 200) {
        this.functionalRequirements = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            title: elem.title,
            details: elem.details,
            complexity: elem.complexity,
            priority: elem.priority,
            effort: elem.effort,
            sizeRequirement: elem.sizeRequirement
          }

        })
      }
    },

    async fetchFunctionalRequirement(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `requirement/${id}`,
      })
      if (response?.status === 200) {
        this.functionalRequirement = {
          id: response.data?.id,
          title: response.data?.title,
          details: response.data?.details,
          complexity: response.data?.complexity,
          priority: response.data?.priority,
          effort: response.data?.effort,
          sizeRequirement: response.data?.sizeRequirement
        }
      }
    },

    async createFunctionalRequirement(functionalRequirementFormData: FunctionalRequirementForm, projectId: string) {
      const functionalRequirementCreateData: FunctionalRequirementOnCreate = {
        title: functionalRequirementFormData.title,
        details: functionalRequirementFormData.details,
        complexity: functionalRequirementFormData.complexity,
        priority: functionalRequirementFormData.priority,
        effort: functionalRequirementFormData.effort,
        sizeRequirement: functionalRequirementFormData.sizeRequirement,
        project_id: projectId
      }

      const response = await Api.request({
        method: 'post',
        route: 'requirement',
        body: functionalRequirementCreateData
      })

      return response?.status || 500
    },

    
    async updateFunctionalRequirement(id: string, functionalRequirementData: FunctionalRequirement) {
      const response = await Api.request({
        method: 'put',
        route: `/requirement/${id}`,
        body: functionalRequirementData
      })

      return response?.status === 200
    },

    async deleteFunctionalRequiriment(id: string) {
      const response = await Api.request({
        method: 'delete',
        route: `/requirement/${id}`
      })

      return response?.status === 200
    },
  }
})