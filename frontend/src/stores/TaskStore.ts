import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { type models } from 'src/@types'
interface Task extends models.Task { }
interface TaskOnCreate extends models.TaskOnCreate { }

interface State {
  task: Task
  tasks: Task[]
}

const taskDefault: Task = {
  id: "",
  title: "",
  priority: "",
  startDate: "",
  endDate: "",
  taskType: "",
  taskrequirement_id: "",
  taskimprovement_id: "",
  taskbug_id: "",
  iteration_id: "",
}

export const useTaskStore = defineStore('Task', {
  state: (): State => ({
    task: { ...taskDefault },
    tasks: []
  }),

  actions: {
    async fetchTasks(iterationId: string) {
      const response = await Api.request({
        method: 'get',
        route: `project/iteration/${iterationId}/tasks`,
      })

      if (response?.status === 200) {
        this.tasks = response.data?.map((elem: any) => {
          return {
            id: elem.id,
            title: elem.title,
            priority: elem.priority,
            startDate: elem.startDate,
            endDate: elem.endDate,
            taskType: elem.taskType,
            taskrequirement_id: elem.taskrequirement_id,
            taskimprovement_id: elem.taskimprovement_id,
            taskbug_id: elem.taskbug_id,
            iteration_id: elem.iteration_id,
          }
        })
      }
    },

    async fetchTask(id: string) {
      const response = await Api.request({
        method: 'get',
        route: `task/${id}`,
      })

      if (response?.status === 200) {
        this.task = {
          id: response.data.id,
          title: response.data.title,
          priority: response.data.priority,
          startDate: response.data.startDate,
          endDate: response.data.endDate,
          taskType: response.data.taskType,
          taskrequirement_id: response.data.taskrequirement_id,
          taskimprovement_id: response.data.taskimprovement_id,
          taskbug_id: response.data.taskbug_id,
          iteration_id: response.data.iteration_id,
        }
      }
    },

    async createTask(projectId: string, taskData: Task) {
      const taskCreateData: TaskOnCreate = {
        title: taskData.title,
        priority: taskData.priority,
        startDate: taskData.startDate,
        endDate: taskData.endDate,
        taskType: taskData.taskType,
        taskrequirement_id: taskData.taskrequirement_id,
        taskimprovement_id: taskData.taskimprovement_id,
        taskbug_id: taskData.taskbug_id,
        iteration_id: taskData.iteration_id,
      }

      const response = await Api.request({
        method: 'post',
        route: 'role',
        body: taskCreateData
      })

      return (response?.status) ? response.status : 500
    },

    
    async deleteTask(id: string) {
      const response = await Api.request({
        method: 'delete',
        route: `/task/${id}`
      })

      return response?.status === 200
    },

    /*
    async updateTask(id: string, taskData: Task) {
      const response = await Api.request({
        method: 'put',
        route: `/task/${id}`,
        body: taskData
      })

      return response?.status === 200
    },*/
  }
})