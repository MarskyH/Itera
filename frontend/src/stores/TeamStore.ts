import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'

interface Project extends models.Project { }
interface Team extends models.Team { }
interface TeamOnCreate extends models.TeamOnCreate { }

interface State {
  team: Team
  teams: Team[]
}

const teamDefault: Team = {
  id: ""
}

export const useTeamStore = defineStore('Team', {
  state: (): State => ({
    team: { ...teamDefault },
    teams: []
  }),

  actions: {
    async fetchTeam(projectId: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${projectId}/team`
      })

      if (response?.status === 200) {
        this.team = {
          id: response.data.id
        }
      }
    },

    async createTeam(projectId: string) {
      const teamOnCreateData: TeamOnCreate = {
        project_id: projectId
      }

      const response = await Api.request({
        method: 'post',
        route: '/team',
        body: teamOnCreateData
      })
      return (response?.status) ? response.status : 500
    },

    /*
    async deleteTeam(id: string) {
      const response = await Api.request({
        method: 'delete',
        route: '/equipe/' + id.toString()
      })

      return response?.status === 200
    }*/
  }
})