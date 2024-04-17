import { defineStore } from 'pinia'
import Api from 'src/services/api'

import { NonFunctionalRequirementForm, type models } from 'src/@types'
interface NonFunctionalRequirementOnCreate extends models.NonFunctionalRequirementOnCreate { }
interface NonFunctionalRequirement extends models.NonFunctionalRequirement { }
interface NonFunctionalRequirementWeights extends models.NonFunctionalRequirementWeights { }
interface NonFunctionalRequirementProject extends models.NonFunctionalRequirementProject { }
interface NonFunctionalRequirementProjectOnUpdate extends models.NonFunctionalRequirementProjectOnUpdate { }

interface State {
  nonFunctionalRequirement: NonFunctionalRequirement
  nonFunctionalRequirementsProejct: NonFunctionalRequirementProject[]
  nonFunctionalRequirements: NonFunctionalRequirement[]
  weights: NonFunctionalRequirementWeights[]
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
    nonFunctionalRequirementsProejct : [],
    nonFunctionalRequirements: [],
    weights: []
  }),

  actions: {
    async fetchNonFunctionalRequirement(id: string) {
      const response = await Api.request({
        method: 'get',
        route: '/nonFunctionalRequirements/' + id,
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

    async fetchNonFunctionalRequirementsProject(projectId: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${projectId}/nonFunctionalRequirementsProject`,
      })
      if (response?.status === 200) {
        this.nonFunctionalRequirementsProejct = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            projectId: elem.project_id,
            nonfunctionalrequirementId: elem.nonfunctionalrequirement_id,
            weight: elem.weight,
          }
        })
      }
    },


    async fetchNonFunctionalRequirements() {
      const response = await Api.request({
        method: 'get',
        route: `/nonFunctionalRequirements`,
      })
      if (response?.status === 200) {
        this.nonFunctionalRequirements = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            title: elem.title,
            description: elem.description,
            weights: elem.weights,
            multiple: elem.multiple
          }
        })
      }
    },

    async fetchNonFunctionalRequirementWeights(nonFunctionalRequirementId: string) {
      try {
        const response = await Api.request({
          method: 'get',
          route: `nonFunctionalRequirements/${nonFunctionalRequirementId}/weights`,
        })
        if (response?.status === 200) {
          // Limpe o array antes de adicionar novos valores
          this.weights = [];

          // Itera sobre as chaves numeradas de "0" a "5"
          for (let i = 0; i <= 5; i++) {
            const key = i.toString();
            // Verifica se a chave existe na resposta
            if (key in response.data) {
              // Extrai os valores de "value" e "description" e adiciona ao array weights
              const { value, description } = response.data[key];
              this.weights.push({ value, description });
            }
          }
        }
      } catch (error) {
        console.error('Erro ao buscar pesos de requisitos não funcionais:', error);
      }
    },

    
    async updateProject(nonFunctionalRequirementProjectList: NonFunctionalRequirementProjectOnUpdate[]) {
      const response = await Api.request({
        method: 'put',
        route: `/nonFunctionalRequirementProject`,
        body: nonFunctionalRequirementProjectList
      })

      return response?.status || 500
    },

    async createNonFunctionalRequirements(nonFunctionalRequirementFormDataList: NonFunctionalRequirementForm[], projectId: string) {
      const nonFunctionalRequirementOnCreateDataList: NonFunctionalRequirementOnCreate[] = nonFunctionalRequirementFormDataList.map(form => ({
        project_id: projectId,
        nonfunctionalrequirement_id: form.id,
        weight: form.weight
      }));

      const response = await Api.request({
        method: 'post',
        route: 'nonFunctionalRequirementProject',
        body: nonFunctionalRequirementOnCreateDataList
      });

      return response?.status || 500;
    },
  }
})