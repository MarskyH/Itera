import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'

interface UserProfileRegisterModel extends models.UserProfileRegisterModel {}
interface AdminProfileRegisterModel extends models.AdminProfileRegisterModel {}

interface ValidateEgress extends models.ValidateEgress {}

export const useCadastroPerfilStore = defineStore('CadastroPerfilStore', {
  state: () => ({
  }),

  actions: {
    async egressValidation (
      nome: string,
      matricula: string,
      email: string
    ) {
      const data: ValidateEgress = {
        nome,
        matricula,
        email
      }

      const response = await Api.request({
        method: 'post',
        route: '/egressoValido',
        body: data
      })

      return (response?.status) !== undefined ? response.status : 500
    },

    async validateEmail (tokenAuth: string) {
      const response = await Api.request({
        method: 'post',
        route: `/auth/validarEmail/${tokenAuth}`
      })

      return {
        status: (response?.status) !== undefined ? response.status : 500,
        data: (response?.data !== undefined) ? response?.data : null
      }
    },

    async requestNewValidation (email: string) {
      const response = await Api.request({
        method: 'post',
        route: '/auth/solicitarNovaValidacaoEmail',
        body: {
          email,
          redirect: `${window.location.origin}/validar-email`
        }
      })

      return {
        status: (response?.status) !== undefined ? response.status : 500,
        data: (response?.data !== undefined) ? response?.data : null
      }
    },

    async userProfileRegister (
      username: string,
      password: string,
      email: string,
      nome: string,
      registration?: string
    ) {
      const data: UserProfileRegisterModel = {
        username,
        password,
        email,
        nome,
        registration
      }

      const response = await Api.request({
        method: 'post',
        route: '/auth/register',
        body: {
          ...data,
          redirect: `${window.location.origin}/validar-email`
        }
      })

      return {
        status: (response?.status) !== undefined ? response.status : 500,
        data: (response?.data !== undefined) ? response?.data : null
      }
    },

    async registrationByAdmin (
      username: string,
      email: string,
      nome: string,
      grupos: string[]
    ) {
      const data: AdminProfileRegisterModel = {
        username,
        email,
        nome,
        grupos
      }

      const response = await Api.request({
        method: 'post',
        route: '/administrador/usuario/register',
        body: data
      })

      return {
        status: (response?.status) !== undefined ? response.status : 500,
        data: (response?.data !== undefined) ? response?.data : null
      }
    }
  }
})