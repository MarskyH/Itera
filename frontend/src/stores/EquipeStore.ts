import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface EquipeRegister extends models.EquipeRegister { }
interface EquipeResponse extends models.EquipeResponse { }



interface State {
  equipe: EquipeResponse
  equipes: EquipeResponse[]
}

export const useEquipeStore = defineStore('Equipe', {
  state: (): State => ({
    equipe: {
      projeto:{
        id: 0,
        nome: "",
        prazo: 0,
        tempoIteracao: 0,
        nomeCliente: "",
        createdBy: ""
     }
    },
    equipes: []
  }),

  actions: {
    async deleteEquipe(id: number) {
      const response = await Api.request({
        method: 'delete',
        route: '/equipe/' + id.toString()
      })

      if (response?.status === 200) {
        return true
      } else {
        return false
      }
    },



    async registerEquipe(dadosEquipe: EquipeRegister) {
      const response = await Api.request({
        method: 'post',
        route: '/equipe',
        body: dadosEquipe
      })
      return (response?.status) !== undefined ? response.status : 500
    },
  }
})