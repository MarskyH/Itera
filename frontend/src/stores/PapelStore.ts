import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface PapelRegister extends models.PapelRegister {}
interface PapelResponse extends models.PapelResponse {}



interface State {
  papel: PapelResponse
  papels: PapelResponse[]
}

export const usePapelStore = defineStore('Papel', {
  state: (): State => ({
    papel: {
      id: 0,
      funcao: "",
      habilidade: "",
      competencia: "",
    },
    papels: []
  }),

  actions: {
    async deletePapel (id: number) {
      const response = await Api.request({
        method: 'delete',
        route: '/papel/' + id.toString()
      })

      if (response?.status === 200) {
        return true
      } else {
        return false
      }
    },


    async fetchPapel () {
      const response = await Api.request({
        method: 'get',
        route: '/papel',
      })
      if (response?.status === 200) {
        this.papel = response.data?.content.map((elem: any) => {
          return {
            id: elem.id,
            funcao: elem.funcao,
            habilidade: elem.habilidade,
            competencia:elem.competencia,
          }
        })
      }
    },


    async registerPapel (dadosPapel: PapelRegister) {
      const response = await Api.request({
        method: 'post',
        route: '/papel',
        body: dadosPapel
      })
      return (response?.status) !== undefined ? response.status : 500
    },

    getPapelId (id: number) {
      const papelEncontrado = this.papels.find(element => element.id === id)
      if (papelEncontrado !== undefined) {
        this.papel = papelEncontrado
      } else {
        // Lógica para lidar com a situação em que o papel não foi encontrado
        this.papel = {
          id: 0,
          funcao: "",
          habilidade: "",
          competencia: "",
        }
      }
    }
  }
})