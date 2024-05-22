import { defineStore } from 'pinia'
import Api from 'src/services/api'
import { TaskRequirementForm, TaskImprovementForm, TaskBugForm, type models } from 'src/@types'
interface Assignee extends models.Assignee { }
interface Task extends models.Task { }
interface TaskOnCreate extends models.TaskOnCreate { }
interface TaskRequirement extends models.TaskRequirement { }
interface TaskRequirementOnCreate extends models.TaskRequirementOnCreate { }
interface TaskImprovement extends models.TaskImprovement { }
interface TaskImprovementOnCreate extends models.TaskImprovementOnCreate { }
interface TaskBug extends models.TaskBug { }
interface TaskBugOnCreate extends models.TaskBugOnCreate { }



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

    async createTask(iterationId: string, taskData: Task) {
      const taskCreateData: TaskOnCreate = {
        title: taskData.title,
        priority: taskData.priority,
        startDate: taskData.startDate,
        endDate: taskData.endDate,
        taskType: taskData.taskType,
        taskrequirement_id: taskData.taskrequirement_id,
        taskimprovement_id: taskData.taskimprovement_id,
        taskbug_id: taskData.taskbug_id,
        iteration_id: iterationId,
      }

      const response = await Api.request({
        method: 'post',
        route: 'task',
        body: taskCreateData
      })

      return (response?.status) ? response.status : 500
    },

    async createTaskRequirement(iterationId: string, taskData: Task, taskRequirementData: TaskRequirement, listAssignees: Assignee[]) {
      const taskCreateData: TaskOnCreate = {
        title: taskData.title,
        priority: taskData.priority,
        startDate: taskData.startDate,
        endDate: taskData.endDate,
        taskType: taskData.taskType,
        taskrequirement_id: taskData.taskrequirement_id,
        taskimprovement_id: taskData.taskimprovement_id,
        taskbug_id: taskData.taskbug_id,
        iteration_id: iterationId,
      }

      const taskRequirementCreateData: TaskRequirementOnCreate = {
        details:taskRequirementData.details, 
        complexity:taskRequirementData.complexity,  
        sizeTask:taskRequirementData.sizeTask,  
        task_id:taskRequirementData.task_id, 
        checkProject:taskRequirementData.checkProject,  
        checkRequirement:taskRequirementData.checkRequirement,  
        checkFront:taskRequirementData.checkFront,  
        checkBack:taskRequirementData.checkBack, 
        checkTest:taskRequirementData.checkTest, 
      }

      const taskRequirementForm: TaskRequirementForm = {
        task: taskCreateData,
        taskRequirement: taskRequirementCreateData,
        assignees: listAssignees
      }

      const response = await Api.request({
        method: 'post',
        route: 'task/type/requirement',
        body: taskRequirementForm
      })

      return (response?.status) ? response.status : 500
    },

    async createTaskImprovement(iterationId: string, taskData: Task, taskImprovementData: TaskImprovement, listAssignees: Assignee[]) {
      const taskCreateData: TaskOnCreate = {
        title: taskData.title,
        priority: taskData.priority,
        startDate: taskData.startDate,
        endDate: taskData.endDate,
        taskType: taskData.taskType,
        taskrequirement_id: taskData.taskrequirement_id,
        taskimprovement_id: taskData.taskimprovement_id,
        taskbug_id: taskData.taskbug_id,
        iteration_id: iterationId,
      }

      const taskImprovementCreateData: TaskImprovementOnCreate = {
        details:taskImprovementData.details, 
        complexity:taskImprovementData.complexity,  
        sizeTask:taskImprovementData.sizeTask,  
        task_id:taskImprovementData.task_id, 
        checkProject:taskImprovementData.checkProject,  
        checkRequirement:taskImprovementData.checkRequirement,  
        checkFront:taskImprovementData.checkFront,  
        checkBack:taskImprovementData.checkBack, 
        checkTest:taskImprovementData.checkTest, 
      }

      const taskImprovementForm: TaskImprovementForm = {
        task: taskCreateData,
        taskImprovement: taskImprovementCreateData,
        assignees: listAssignees
      }

      const response = await Api.request({
        method: 'post',
        route: 'task/type/improvement',
        body: taskImprovementForm
      })

      return (response?.status) ? response.status : 500
    },

    async createTaskBug(iterationId: string, taskData: Task, taskBugData: TaskBug, listAssignees: Assignee[]) {
      const taskCreateData: TaskOnCreate = {
        title: taskData.title,
        priority: taskData.priority,
        startDate: taskData.startDate,
        endDate: taskData.endDate,
        taskType: taskData.taskType,
        taskrequirement_id: taskData.taskrequirement_id,
        taskimprovement_id: taskData.taskimprovement_id,
        taskbug_id: taskData.taskbug_id,
        iteration_id: iterationId,
      }

      const taskBugCreateData: TaskBugOnCreate = {
        details:taskBugData.details, 
        complexity:taskBugData.complexity,  
        sizeTask:taskBugData.sizeTask,  
        task_id:taskBugData.task_id, 
        checkFront:taskBugData.checkFront,  
        checkBack:taskBugData.checkBack, 
        checkTest:taskBugData.checkTest, 
      }

      const taskBugForm: TaskBugForm = {
        task: taskCreateData,
        taskBug: taskBugCreateData,
        assignees: listAssignees
      }

      const response = await Api.request({
        method: 'post',
        route: 'task/type/bug',
        body: taskBugForm
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