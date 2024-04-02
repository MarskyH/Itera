import { defineStore } from 'pinia'
import Api from 'src/services/api'

import { NonFunctionalRequirementForm, type models } from 'src/@types'
interface NonFunctionalRequirementOnCreate extends models.NonFunctionalRequirementOnCreate { }
interface NonFunctionalRequirement extends models.NonFunctionalRequirement { }
interface NonFunctionalRequirementWeights extends models.NonFunctionalRequirementWeights { }

interface State {
  nonFunctionalRequirement: NonFunctionalRequirement
  nonFunctionalRequirements: NonFunctionalRequirement[]
  wheights: NonFunctionalRequirementWeights[]
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
    nonFunctionalRequirements: [],
    wheights: []
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
            description:elem.description,
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
          this.wheights = [];

          // Itera sobre as chaves numeradas de "0" a "5"
          for (let i = 0; i <= 5; i++) {
            const key = i.toString();
            // Verifica se a chave existe na resposta
            if (key in response.data) {
              // Extrai os valores de "value" e "description" e adiciona ao array wheights
              const { value, description } = response.data[key];
              this.wheights.push({ value, description });
            }
          }
        }
      } catch (error) {
        console.error('Erro ao buscar pesos de requisitos não funcionais:', error);
      }
    },


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
  }
})