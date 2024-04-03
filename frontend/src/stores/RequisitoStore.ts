import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface RequisitoRegister extends models.RequisitoRegister { }
interface RequisitoResponse extends models.RequisitoResponse { }



interface State {
  requisito: RequisitoResponse
  requisitos: RequisitoResponse[]
}

export const useRequisitoStore = defineStore('Requisito', {
  state: (): State => ({
    requisito: {
      id: 0,
      nome: "",
      titulo: "",
      detalhamento: "",
      complexidade: "",
      prioridade: "",
      esforco: 0,
      tamanho: 0,
      projeto_id: 0
    },
    requisitos: []
  }),

  actions: {
    async deleteRequisito(id: number) {
      const response = await Api.request({
        method: 'delete',
        route: '/requisito/' + id.toString()
      })

      if (response?.status === 200) {
        return true
      } else {
        return false
      }
    },


    async fetchRequisito() {
      const response = await Api.request({
        method: 'get',
        route: '/requisito',
      })
      if (response?.status === 200) {
        this.requisito = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            nome: elem.nome,
            titulo: elem.titulo,
            detalhamento: elem.detalhamento,
            complexidade: elem.complexidade,
            prioridade: elem.prioridade,
            esforco: elem.esforco,
            tamanho: elem.tamanho,
            projeto_id: elem.projeto_id
          }
        })
      }
    },


    async registerRequisito(dadosRequisito: RequisitoRegister) {
      const response = await Api.request({
        method: 'post',
        route: '/requisito',
        body: dadosRequisito
      })
      return (response?.status) !== undefined ? response.status : 500
    },

    getRequisitoId(id: number) {
      const requisitoEncontrado = this.requisitos.find(element => element.id === id)
      if (requisitoEncontrado !== undefined) {
        this.requisito = requisitoEncontrado
      } else {
        // Lógica para lidar com a situação em que o requisito não foi encontrado
        this.requisito = {
          id: 0,
          nome: "",
          titulo: "",
          detalhamento: "",
          complexidade: "",
          prioridade: "",
          esforco: 0,
          tamanho: 0,
          projeto_id: 0
        }
      }
    }
  }
})