import { defineStore } from 'pinia'
import Api from 'src/services/api'

import { type models } from 'src/@types'
interface PendencyOnCreate extends models.PendencyOnCreate { }
interface Pendency extends models.Pendency { }
interface PendencyForm extends models.PendencyForm { }


interface State {
  pendency: Pendency
  pendencies: Pendency[]
}

const pendencyDefault: Pendency = {
  id: '',
  title: '',
  description: '',
  creationDate: '',
  endDate: '',
  status: true,
  task_id: '',
}

export const usePendencyStore = defineStore('Pendency', {
  state: (): State => ({
    pendency: { ...pendencyDefault },
    pendencies: []
  }),


  actions: {

    async createPendency(pendencyFormData: PendencyForm, taskId: string) {
      const pendencyOnCreateData: PendencyOnCreate = {
        title: pendencyFormData.title,
        description: pendencyFormData.description,
        status: true,
        task_id: taskId,
      }

      const response = await Api.request({
        method: 'post',
        route: 'pendency',
        body: pendencyOnCreateData
      })

      return (response?.status) !== undefined ? response.status : 500
    },

    async updatePendency(id: string, pendencyData: Pendency) {
      const response = await Api.request({
        method: 'put',
        route: `/pendency/${id}`,
        body: pendencyData
      })

      return response?.status === 200
    },
  }
})