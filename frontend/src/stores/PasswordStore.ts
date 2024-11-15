import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'

interface ForgotPassword extends models.ForgotPassword { }
interface ResetPassword extends models.ResetPassword { }


export const usePasswordStore = defineStore('PasswordStore', {
    state: () => ({
    }),

    actions: {
        async ForgotPassword(
            email: string
        ) {
            const data: ForgotPassword = {
                email
            }

            const response = await Api.request({
                method: 'post',
                route: '/auth/forgot-password',
                body: {
                    ...data
                }
            })

            return {
                status: (response?.status) !== undefined ? response.status : 500,
                data: (response?.data !== undefined) ? response?.data : null
            }
        },
        async ResetPassword(
            token: string,
            newPassword: string,
        ) {
            const data: ResetPassword = {
                token, 
                newPassword
            }

            const response = await Api.request({
                method: 'post',
                route: '/auth/reset-password',
                body: {
                    ...data
                }
            })

            return {
                status: (response?.status) !== undefined ? response.status : 500,
                data: (response?.data !== undefined) ? response?.data : null
            }
        },
    }
})
