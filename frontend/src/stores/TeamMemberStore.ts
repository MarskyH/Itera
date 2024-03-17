import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'

interface TeamMember extends models.TeamMember { }
interface TeamMemberOnCreate extends models.TeamMemberOnCreate { }

interface State {
  teamMember: TeamMember
  teamMembers: TeamMember[]
}

const teamMemberDefault: TeamMember = {
  id: '',
  name: '',
  username: '',
  hourlyRate: 0,
  dedicatedHours: 0,
  role: ''
}

export const useTeamMemberStore = defineStore('TeamMember', {
  state: (): State => ({
    teamMember: { ...teamMemberDefault },
    teamMembers: []
  }),

  actions: {
    async fetchTeamMembers(teamId: string) {
      const response = await Api.request({
        method: 'get',
        route: `/users/${teamId}`
      })

      if (response?.status === 200) {
        this.teamMembers = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            name: elem.nome,
            username: elem.username,
            hourlyRate: elem.valorHora,
            dedicatedHours: elem.horasDedicadas,
            role: elem.userRole
          }
        })
      }
    },

    async createTeamMember(teamId: string, teamMemberData: TeamMember) {
      const teamMemberCreateData: TeamMemberOnCreate = {
        name: teamMemberData.name,
        username: teamMemberData.username,
        hourlyRate: teamMemberData.hourlyRate,
        dedicatedHours: teamMemberData.dedicatedHours,
        role: teamMemberData.role
      }

      const response = await Api.request({
        method: 'post',
        route: `/users/${teamId}`,
        body: teamMemberCreateData
      })

      return (response?.status) ? response.status : 500
    },
  }
})