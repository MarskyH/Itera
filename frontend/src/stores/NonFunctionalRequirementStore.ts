import { defineStore } from 'pinia'
import Api from 'src/services/api'

import { NonFunctionalRequirementForm, type models } from 'src/@types'
interface NonFunctionalRequirementOnCreate extends models.NonFunctionalRequirementOnCreate { }
interface NonFunctionalRequirement extends models.NonFunctionalRequirement { }

interface State {
  nonFunctionalRequirement: NonFunctionalRequirement
  nonFunctionalRequirements: NonFunctionalRequirement[]
}

const nonFunctionalRequirementDefault: NonFunctionalRequirement = {
  id: '',
  title: '',
  description: '',
  weights: {
    value: 0,
    description: ''
  },
  multiple: false
}

export const useNonFunctionalRequirementStore = defineStore('NonFunctionalRequirement', {
  state: (): State => ({
    nonFunctionalRequirement: { ...nonFunctionalRequirementDefault },
    nonFunctionalRequirements: []
  }),

  actions: {
    async fetchNonFunctionalRequirement(id: string) {
      const response = await Api.request({
        method: 'get',
        route: '/nonFunctionalRequirement/' + id,
      })
      if (response?.status === 200) {
        this.nonFunctionalRequirement = {
          id: response.data?.id,
          title: response.data?.title,
          description: response.data.description,
          weights: response.data?.weights,
          multiple: response.data?.multiple
        }
      }
    },

    /*
    async fetchNonFunctionalRequirements(projectId: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${projectId}/nonFunctionalRequirements`,
      })
      if (response?.status === 200) {
        this.nonFunctionalRequirements = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            title: elem.title,
            valueRequirement: elem.valueRequirement,
            project: elem.project
          }
        })
      }
    },*/


    async createNonFunctionalRequirements(nonFunctionalRequirementFormDataList: NonFunctionalRequirementForm[], projectId: string) {
      try {
        const nonFunctionalRequirementOnCreateDataList: NonFunctionalRequirementOnCreate[] = nonFunctionalRequirementFormDataList.map(form => ({
          project_id: projectId,
          nonfunctionalrequirement_id: form.id,
          weight: form.wheight
        }));

        const response = await Api.request({
          method: 'post',
          route: 'nonFunctionalRequirementProject',
          body: nonFunctionalRequirementOnCreateDataList
        });

        return (response?.status) !== undefined ? response.status : 500;
      } catch (error) {
        console.error("Error while creating non-functional requirements:", error);
        return 500;
      }
    },

    /*
    async deleteNonFunctionalRequirement(id: string) {
      const response = await Api.request({
        method: 'delete',
        route: '/nonFunctionalRequirement/' + id
      })
    
      if (response?.status === 200) {
        return true
      } else {
        return false
      }
    },*/
  }
})