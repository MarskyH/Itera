import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'

interface UserProfileRegisterModel extends models.UserProfileRegisterModel { }


export const useCadastroPerfilStore = defineStore('CadastroPerfilStore', {
    state: () => ({
    }),

    actions: {
        async userProfileRegister(
            nome: string,
            login: string,
            password: string,
            role: string,

        ) {
            const data: UserProfileRegisterModel = {
                nome,
                login,
                password,
                role,
            }

            const response = await Api.request({
                method: 'post',
                route: '/auth/register',
                body: {
                    ...data,
                    redirect: `${window.location.origin}/home`
                }
            })

            return {
                status: (response?.status) !== undefined ? response.status : 500,
                data: (response?.data !== undefined) ? response?.data : null
            }
        },
    }
})
