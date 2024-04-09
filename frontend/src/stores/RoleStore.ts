import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface Role extends models.Role { }
interface RoleOnCreate extends models.RoleOnCreate { }

interface State {
  role: Role
  roles: Role[]
}

const roleDefault: Role = {
  id: "",
  function: "",
  skill: "",
  competency: "",
}

export const useRoleStore = defineStore('Role', {
  state: (): State => ({
    role: { ...roleDefault },
    roles: []
  }),

  actions: {
    async fetchRoles(projectId: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${projectId}/roles`,
      })

      if (response?.status === 200) {
        this.roles = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            function: elem.function,
            skill: elem.skill,
            competency: elem.competency,
          }
        })
      }
    },

    async createRole(projectId: string, roleData: Role) {
      const roleCreateData: RoleOnCreate = {
        function: roleData.function,
        skill: roleData.skill,
        competency: roleData.competency,
        project_id: projectId
      }

      const response = await Api.request({
        method: 'post',
        route: 'role',
        body: roleCreateData
      })

      return (response?.status) ? response.status : 500
    },

    
    async deleteRole(id: string) {
      const response = await Api.request({
        method: 'delete',
        route: `/role/${id}`
      })

      return response?.status === 200
    },
  
    /*

    getRoleById(id: number) {
      const fetchedRole = this.roles.find(element => element.id === id)

      if (fetchedRole) {
        this.role = fetchedRole
      } else {
        this.role = {...roleDefault}
      }
    }
    */
  }
})