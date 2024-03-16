import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface ProjetoRegister extends models.ProjetoRegister {}
interface ProjetoResponse extends models.ProjetoResponse {}



interface State {
  projeto: ProjetoResponse
  projetos: ProjetoResponse[]
}

export const useProjetoStore = defineStore('Projeto', {
  state: (): State => ({
    projeto: {
      id: 0,
      nome: "",
      prazo: 0,
      tempoIteracao: 0,
      nomeCliente: "",
      createdBy: ""
    },
    projetos: []
  }),

  actions: {
    async deleteProjeto (id: number) {
      const response = await Api.request({
        method: 'delete',
        route: '/projeto/' + id.toString()
      })

      if (response?.status === 200) {
        return true
      } else {
        return false
      }
    },


    async fetchProjetos () {
      const response = await Api.request({
        method: 'get',
        route: '/projeto',
      })
      if (response?.status === 200) {
        this.projetos = response.data?.content.map((elem: any) => {
          return {
            id: elem.id,
            nome: elem.nome,
            prazo: elem.prazo,
            tempoIteracao:elem.tempoIteracao,
            nomeCliente:elem.nomeCliente,
            createdBy: elem.createdBy
          }
        })
      }
    },

    async fetchProjetoNome (nome: string) {
      const response = await Api.request({
        method: 'get',
        route: '/projeto',
        params: {nome}
      })
      if (response?.status === 200) {
        this.projeto = {
          id: response.data?.id,
          nome: response.data?.nome,
          prazo: response.data?.prazo,
          tempoIteracao: response.data?.tempoIteracao,
          nomeCliente: response.data?.nomeCliente,
          createdBy: response.data?.createdBy
        };
      }
    },

    async registerProjeto (dadosProjeto: ProjetoRegister) {
      const response = await Api.request({
        method: 'post',
        route: '/projeto',
        body: dadosProjeto
      })
      return (response?.status) !== undefined ? response.status : 500
    },

    getProjetoId (id: number) {
      const projetoEncontrado = this.projetos.find(element => element.id === id)
      if (projetoEncontrado !== undefined) {
        this.projeto = projetoEncontrado
      } else {
        // Lógica para lidar com a situação em que o projeto não foi encontrado
        this.projeto = {
          id: 0,
          nome: "",
          prazo: 0,
          tempoIteracao: 0,
          nomeCliente: "",
          createdBy: ""
        }
      }
    }
  }
})