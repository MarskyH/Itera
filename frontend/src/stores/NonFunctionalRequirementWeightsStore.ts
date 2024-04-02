import { defineStore } from 'pinia'
import { models } from 'src/@types'
import Api from 'src/services/api'

interface NonFunctionalRequirementWeights extends models.NonFunctionalRequirementWeights { }

interface State {
  wheights: NonFunctionalRequirementWeights[]
}

export const usePriorityStore = defineStore('Priority', {
  state: (): State => ({
    wheights: []
  }),

  actions: {
    async fetchNonFunctionalRequirementWeights(nonFunctionalRequirementId: string) {
      try {
        const response = await Api.request({
          method: 'get',
          route: `nonFunctionalRequirement/${nonFunctionalRequirementId}/weights`,
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
    }
  }
})
