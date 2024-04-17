import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { TeamMemberForm, type models } from 'src/@types'

interface TeamMember extends models.TeamMember { }
interface TeamMemberOnCreate extends models.TeamMemberOnCreate { }

interface State {
  teamMember: TeamMember
  teamMembers: TeamMember[]
}

const teamMemberDefault: TeamMember = {
  id: '',
  hourlyRate: 0,
  dedicatedHours: 0,
  user: {
    id: '',
    name: '',
    email: '',
    login: '',
    password: '',
    userRole: '',
  },
  role: {
    id: '',
    function: '',
    skill: '',
    competency: '',
  }
}

export const useTeamMemberStore = defineStore('TeamMember', {
  state: (): State => ({
    teamMember: { ...teamMemberDefault },
    teamMembers: []
  }),

  actions: {
    async fetchTeamMembers(projectId: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${projectId}/teamMembers`
      })

      if (response?.status === 200) {
        this.teamMembers = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            hourlyRate: elem.hourlyRate,
            dedicatedHours: elem.dedicatedHours,
            user: elem.user,
            role: elem.role,
          }
        })
      }
    },

    async fetchTeamMember(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `teamMember/${id}`
      })

      if (response?.status === 200) {
        this.teamMember = {
          id: response.data?.id,
          hourlyRate: response.data?.hourlyRate,
          dedicatedHours: response.data?.dedicatedHours,
          user: response.data?.user,
          role: response.data?.role,
        }
      }
    },

    async createTeamMember(teamMemberFormData: TeamMemberForm, projectId: string) {
      const teamMemberCreateData: TeamMemberOnCreate = {
        hourlyRate: teamMemberFormData.hourlyRate,
        dedicatedHours: teamMemberFormData.dedicatedHours,
        user_id: teamMemberFormData.user,
        role_id: teamMemberFormData.role,
        project_id: projectId
      }

      const response = await Api.request({
        method: 'post',
        route: `teamMember`,
        body: teamMemberCreateData
      })

      return (response?.status) ? response.status : 500
    },

    async updateTeamMember(teamMemberId: string, teamMemberFormData: TeamMemberForm, projectId: string) {
      const teamMemberCreateData: TeamMemberOnCreate = {
        hourlyRate: teamMemberFormData.hourlyRate,
        dedicatedHours: teamMemberFormData.dedicatedHours,
        user_id: teamMemberFormData.user,
        role_id: teamMemberFormData.role,
        project_id: projectId
      }

      const response = await Api.request({
        method: 'put',
        route: `teamMember/${teamMemberId}`,
        body: teamMemberCreateData
      })

      return (response?.status) ? response.status : 500
    },

    async deleteTeamMember(id: string) {
      const response = await Api.request({
        method: 'delete',
        route: `/teamMember/${id}`
      })

      return response?.status === 200
    },
  }
})