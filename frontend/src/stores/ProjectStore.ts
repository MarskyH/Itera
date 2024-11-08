import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface Project extends models.Project { }
interface ProjectOnCreate extends models.ProjectOnCreate { }

interface State {
  project: Project
  projects: Project[]
  recentProjects: Project[]
}

const projectDefault = {
  id: "",
  name: "",
  deadline: 0,
  workHours: 0,
  iterationTime: 0,
  clientName: "",
  createdBy: ""
}

export const useProjectStore = defineStore('Project', {
  state: (): State => ({
    project: { ...projectDefault },
    projects: [],
    recentProjects: []
  }),

  actions: {
    setProject(projectData: Project) {
      this.project = projectData
    },

    async fetchProject(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/${id}`
      })

      if (response?.status === 200) {
        this.project = {
          id: response?.data.id,
          name: response?.data.name,
          deadline: response?.data.deadline,
          workHours: response?.data.workHours,
          iterationTime: response?.data.iterationTime,
          clientName: response?.data.clientName,
          createdBy: response?.data.createdBy
        }
      }
    },

    async fetchProjects() {
      const response = await Api.request({
        method: 'get',
        route: '/project',
      })

      if (response?.status === 200) {
        this.projects = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            name: elem.name,
            deadline: elem.deadline,
            workHours: elem.workHours,
            iterationTime: elem.iterationTime,
            clientName: elem.clientName,
            createdBy: elem.createdBy
          }
        })
      }
    },

    async fetchProjectsByUser(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/user/${id}`
      })

      if (response?.status === 200) {
        this.projects = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            name: elem.name,
            deadline: elem.deadline,
            workHours: elem.workHours,
            iterationTime: elem.iterationTime,
            clientName: elem.clientName,
            createdBy: elem.createdBy
          }
        })
      }
    },

    async fetchProjectsByMember(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/member/user/${id}`
      })

      if (response?.status === 200) {
        this.projects = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            name: elem.name,
            deadline: elem.deadline,
            workHours: elem.workHours,
            iterationTime: elem.iterationTime,
            clientName: elem.clientName,
            createdBy: elem.createdBy
          }
        })
      }
    },

    async fetchRecentProjectsByUser(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `/project/recent/user/${id}`
      })

      if (response?.status === 200) {
        this.recentProjects = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            name: elem.name,
            deadline: elem.deadline,
            workHours: elem.workHours,
            iterationTime: elem.iterationTime,
            clientName: elem.clientName,
            createdBy: elem.createdBy
          }
        })
      }
    },

    async fetchRecentProjectsByMember(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `/project/recent/member/user/${id}`
      })

      if (response?.status === 200) {
        this.recentProjects = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            name: elem.name,
            deadline: elem.deadline,
            workHours: elem.workHours,
            iterationTime: elem.iterationTime,
            clientName: elem.clientName,
            createdBy: elem.createdBy
          }
        })
      }
    },

    async createProject(projectData: Project) {
      const projectCreateData: ProjectOnCreate = {
        name: projectData.name,
        deadline: projectData.deadline,
        iterationTime: projectData.iterationTime,
        workHours: projectData.workHours,
        clientName: projectData.clientName
      }

      const response = await Api.request({
        method: 'post',
        route: '/project',
        body: projectCreateData
      })

      return {
        status: response?.status ? response?.status : 500,
        data: response?.data ? response?.data : undefined
        }
    },

    async updateProject(id: string, projectData: Project) {
      const response = await Api.request({
        method: 'put',
        route: `/project/${id}`,
        body: projectData
      })

      return response?.status || 500
    },

    
    async deleteProject(id: string) {
      const response = await Api.request({
        method: 'delete',
        route: `project/${id}`
      })

      return response?.status || 500
    },
  }
})