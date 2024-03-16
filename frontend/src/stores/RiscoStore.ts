import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface RiscoRegister extends models.RiscoRegister { }
interface RiscoResponse extends models.RiscoResponse { }



interface State {
  risco: RiscoResponse
  riscos: RiscoResponse[]
}

export const useRiscoStore = defineStore('Risco', {
  state: (): State => ({
    risco: {
      id: 0,
      titulo: "",
      efeito: "",
      probabilidade: "",
      impacto: "",
      grauExposicao: "",
      projeto_id: 0
    },
    riscos: []
  }),

  actions: {
    async deleteRisco(id: number) {
      const response = await Api.request({
        method: 'delete',
        route: '/risco/' + id.toString()
      })

      if (response?.status === 200) {
        return true
      } else {
        return false
      }
    },


    async fetchRisco() {
      const response = await Api.request({
        method: 'get',
        route: '/risco',
      })
      if (response?.status === 200) {
        this.risco = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            titulo: elem.titulo,
            efeito: elem.efeito,
            probabilidade: elem.probabilidade,
            impacto: elem.impacto,
            grauExposicao: elem.grauExposicao,
            projeto_id: elem.projeto_id
          }
        })
      }
    },


    async registerRisco(dadosRisco: RiscoRegister) {
      const response = await Api.request({
        method: 'post',
        route: '/risco',
        body: dadosRisco
      })
      return (response?.status) !== undefined ? response.status : 500
    },

    getRiscoId(id: number) {
      const riscoEncontrado = this.riscos.find(element => element.id === id)
      if (riscoEncontrado !== undefined) {
        this.risco = riscoEncontrado
      } else {
        // Lógica para lidar com a situação em que o risco não foi encontrado
        this.risco = {
          id: 0,
          titulo: "",
          efeito: "",
          probabilidade: "",
          impacto: "",
          grauExposicao: "",
          projeto_id: 0
        }
      }
    }
  }
})