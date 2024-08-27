import { defineStore } from 'pinia'
import Api from 'src/services/api'

import { type models } from 'src/@types'
interface PendencyOnCreate extends models.PendencyOnCreate { }
interface Pendency extends models.Pendency { }
interface PendencyForm extends models.PendencyForm { }
interface PendencyOnUpdate extends models.PendencyOnUpdate { }

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

    async fetchPendencies(taskId: string) {
      const response = await Api.request({
        method: 'get',
        route: `/task/${taskId}/pendencies`
      })

      if (response?.status === 200) {
        this.pendencies = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            title: elem.title,
            description: elem.description,
            creationDate: elem.creationDate,
            endDate: elem.endDate,
            status: elem.status,
            task_id: elem.task_id
          }
        })
      }
    },

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

    async updatePendency(id: string, pendencyData: PendencyOnUpdate) {
      const response = await Api.request({
        method: 'put',
        route: `/pendency/${id}`,
        body: pendencyData
      })

      return (response?.status) !== undefined ? response.status : 500
    },
  }
})