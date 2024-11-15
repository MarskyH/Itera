import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface ActivityForm extends models.ActivityForm {}
interface ActivityOnCreate extends models.ActivityOnCreate { }
interface Activity extends models.Activity { }



interface State {
  activity: Activity
  activities: Activity[]
}

export const useActivityStore = defineStore('Activity', {
  state: (): State => ({
    activity: {
      id: "",
      title: "",
      description: "",
      type: "",
      priority: "",
    },
    activities: []
  }),

  actions: {
    async fetchActivities(projectId: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${projectId}/activities`,
      })
      if (response?.status === 200) {
        this.activities = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            title: elem.title,
            priority: elem.priority,
            description: elem.description,
            type: elem.type,
            project: elem.project
          }
        })
      }
    },

    async fetchActivity(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `activity/${id}`,
      })
      if (response?.status === 200) {
        this.activity = {
          id: response.data?.id,
          title: response.data?.title,
          priority: response.data?.priority,
          description: response.data?.description,
          type: response.data?.type,
        }
      }
    },

    async createActivity(activityFormData: ActivityForm, projectId: string) {
      const activityOnCreateData: ActivityOnCreate = {
        title: activityFormData.title,
        priority: activityFormData.priority,
        description: activityFormData.description,
        type: activityFormData.type,
        project_id: projectId
      }

      const response = await Api.request({
        method: 'post',
        route: 'activity',
        body: activityOnCreateData
      })

      return (response?.status) !== undefined ? response.status : 500
    },

    async updateActivity(id: string, activityData: Activity) {
      const response = await Api.request({
        method: 'put',
        route: `/activity/${id}`,
        body: activityData
      })

      return response?.status === 200
    },


    async deleteActivity(id: string) {
      const response = await Api.request({
        method: 'delete',
        route: `/activity/${id}`
      })

      if (response?.status === 200) {
        return true
      } else {
        return false
      }
    },
  }
})