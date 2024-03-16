import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface RequisitoNaoFuncionalRegister extends models.RequisitoNaoFuncionalRegister { }
interface RequisitoNaoFuncionalResponse extends models.RequisitoNaoFuncionalResponse { }



interface State {
  requisitoNaoFuncional: RequisitoNaoFuncionalResponse
  requisitoNaoFuncionals: RequisitoNaoFuncionalResponse[]
}

export const useRequisitoNaoFuncionalStore = defineStore('RequisitoNaoFuncional', {
  state: (): State => ({
    requisitoNaoFuncional: {
      id: 0,
      titulo: "",
      valor: 0,
      projeto_id: 0
    },
    requisitoNaoFuncionals: []
  }),

  actions: {
    async deleteRequisitoNaoFuncional(id: number) {
      const response = await Api.request({
        method: 'delete',
        route: '/requisitoNaoFuncional/' + id.toString()
      })

      if (response?.status === 200) {
        return true
      } else {
        return false
      }
    },


    async fetchRequisitoNaoFuncional() {
      const response = await Api.request({
        method: 'get',
        route: '/requisitoNaoFuncional',
      })
      if (response?.status === 200) {
        this.requisitoNaoFuncional = response.data?.content.map((elem: any) => {
          return {
            id: elem.id,
            titulo: elem.titulo,
            valor: elem.valor,
            projeto_id: elem.projeto_id
          }
        })
      }
    },


    async registerRequisitoNaoFuncional(dadosRequisitoNaoFuncional: RequisitoNaoFuncionalRegister) {
      const response = await Api.request({
        method: 'post',
        route: '/requisitoNaoFuncional',
        body: dadosRequisitoNaoFuncional
      })
      return (response?.status) !== undefined ? response.status : 500
    },

    getRequisitoNaoFuncionalId(id: number) {
      const requisitoNaoFuncionalEncontrado = this.requisitoNaoFuncionals.find(element => element.id === id)
      if (requisitoNaoFuncionalEncontrado !== undefined) {
        this.requisitoNaoFuncional = requisitoNaoFuncionalEncontrado
      } else {
        // Lógica para lidar com a situação em que o requisitoNaoFuncional não foi encontrado
        this.requisitoNaoFuncional = {
          id: 0,
          titulo: "",
          valor: 0,
          projeto_id: 0
        }
      }
    }
  }
})