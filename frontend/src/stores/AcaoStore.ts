import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface AcaoRegister extends models.AcaoRegister { }
interface AcaoResponse extends models.AcaoResponse { }



interface State {
  acao: AcaoResponse
  acaos: AcaoResponse[]
}

export const useAcaoStore = defineStore('Acao', {
  state: (): State => ({
    acao: {
      id: 0,
      titulo: "",
      descricao: "",
      tipo: "",
      risco_id: 0
    },
    acaos: []
  }),

  actions: {
    async deleteAcao(id: number) {
      const response = await Api.request({
        method: 'delete',
        route: '/acao/' + id.toString()
      })

      if (response?.status === 200) {
        return true
      } else {
        return false
      }
    },


    async fetchAcao() {
      const response = await Api.request({
        method: 'get',
        route: '/acao',
      })
      if (response?.status === 200) {
        this.acao = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            titulo: elem.titulo,
            descricao: elem.descricao,
            tipo: elem.tipo,
            risco_id: elem.risco_id
          }
        })
      }
    },


    async registerAcao(dadosAcao: AcaoRegister) {
      const response = await Api.request({
        method: 'post',
        route: '/acao',
        body: dadosAcao
      })
      return (response?.status) !== undefined ? response.status : 500
    },

    getAcaoId(id: number) {
      const acaoEncontrado = this.acaos.find(element => element.id === id)
      if (acaoEncontrado !== undefined) {
        this.acao = acaoEncontrado
      } else {
        // Lógica para lidar com a situação em que o acao não foi encontrado
        this.acao = {
          id: 0,
          titulo: "",
          descricao: "",
          tipo: "",
          risco_id: 0
        }
      }
    }
  }
})