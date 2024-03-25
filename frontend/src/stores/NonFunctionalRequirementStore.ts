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
  valueRequirement: 0,
  project: {
    id: '',
    name: '',
    clientName: '',
    deadline: 0,
    workHours: 0,
    iterationTime: 0,
    createdBy: '',
  }
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
          valueRequirement: response.data?.valueRequirement,
          project: response.data?.project
        }
      }
    },

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
    },


    async createNonFunctionalRequirements(nonFunctionalRequirementFormDataList: NonFunctionalRequirementForm[], projectId: string) {
      try {
        const nonFunctionalRequirementOnCreateDataList: NonFunctionalRequirementOnCreate[] = nonFunctionalRequirementFormDataList.map(form => ({
          title: form.title,
          valueRequirement: form.valueRequirement,
          project_id: projectId
        }));

        const response = await Api.request({
          method: 'post',
          route: 'nonFunctionalRequirement',
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