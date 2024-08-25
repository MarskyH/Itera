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
interface TaskForm extends models.TaskForm { }

interface State {
  task: Task
  tasks: Task[]
}

const taskDefault: Task = {
  id: "",
  title: "",
  details: '',
  complexity: '',
  effort: '',
  sizeTask: 0,
  priority: "",
  startDate: "",
  endDate: "",
  taskType: "",
  taskRequirement: {
    id: '',
    task_id: '',
    checkProject: false,
    checkRequirement: false,
    checkFront: false,
    checkBack: false,
    checkTest: false
  },
  taskImprovement: {
    id: '',
    task_id: '',
    checkProject: false,
    checkRequirement: false,
    checkFront: false,
    checkBack: false,
    checkTest: false
  },
  taskBug: {
    id: '',
    task_id: '',
    checkFront: false,
    checkBack: false,
    checkTest: false
  },
  iteration_id: "",
  assignees: []
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
            details: elem.taskData.details,
            complexity: elem.taskData.complexity,
            effort: elem.taskData.effort,
            sizeTask: elem.taskData.sizeTask,
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

    async fetchIterationTasks(iterationId: string, listName: string) {
      const response = await Api.request({
        method: 'get',
        route: `task/iteration/${iterationId}`,
        params: { listName }
      })

      if (response?.status === 200) {
        this.tasks = response.data?.map((elem: any) => {
          return {
            id: elem.taskData.id,
            title: elem.taskData.title,
            priority: elem.taskData.priority,
            details: elem.taskData.details,
            complexity: elem.taskData.complexity,
            effort: elem.taskData.effort,
            sizeTask: elem.taskData.sizeTask,
            startDate: elem.taskData.startDate,
            endDate: elem.taskData.endDate,
            taskType: elem.taskData.taskType,
            taskrequirement_id: elem.taskData.taskrequirement_id,
            taskimprovement_id: elem.taskData.taskimprovement_id,
            taskbug_id: elem.taskData.taskbug_id,
            iteration_id: elem.taskData.iteration_id,
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
          details: response.data.details,
          complexity: response.data.complexity,
          effort: response.data.effort,
          sizeTask: response.data.sizeTask,
          startDate: response.data.startDate,
          endDate: response.data.endDate,
          taskType: response.data.taskType,
          taskRequirement: response.data.taskRequirement,
          taskImprovement: response.data.taskImprovement,
          taskBug: response.data.taskBug,
          iteration_id: response.data.iteration_id,
          assignees: response.data.assignees
        }
      }
    },

    async createTask(iterationId: string, taskData: Task) {
      const taskCreateData: TaskOnCreate = {
        title: taskData.title,
        priority: taskData.priority,
        details: taskData.details,
        complexity: taskData.complexity,
        effort: taskData.effort,
        sizeTask: taskData.sizeTask,
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
        details: taskData.details,
        complexity: taskData.complexity,
        effort: taskData.effort,
        sizeTask: taskData.sizeTask,
        startDate: taskData.startDate,
        endDate: taskData.endDate,
        taskType: taskData.taskType,
        taskrequirement_id: taskData.taskrequirement_id,
        taskimprovement_id: taskData.taskimprovement_id,
        taskbug_id: taskData.taskbug_id,
        iteration_id: iterationId,
      }

      const taskRequirementCreateData: TaskRequirementOnCreate = {
        task_id: taskRequirementData.task_id,
        checkProject: taskRequirementData.checkProject,
        checkRequirement: taskRequirementData.checkRequirement,
        checkFront: taskRequirementData.checkFront,
        checkBack: taskRequirementData.checkBack,
        checkTest: taskRequirementData.checkTest,
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

    async createTaskImprovement(
      iterationId: string,
      taskData: TaskOnCreate,
      taskImprovementData: TaskImprovementOnCreate,
      listAssignees: Assignee[]
  ) {
      const taskImprovementCreateData: TaskImprovementOnCreate = {
          checkProject: taskImprovementData.checkProject,
          checkRequirement: taskImprovementData.checkRequirement,
          checkFront: taskImprovementData.checkFront,
          checkBack: taskImprovementData.checkBack,
          checkTest: taskImprovementData.checkTest,
      };
  
      const taskImprovementForm: TaskImprovementForm = {
          title: taskData.title,
          priority: taskData.priority,
          details: taskData.details,
          complexity: taskData.complexity,
          effort: taskData.effort,
          sizeTask: taskData.sizeTask,
          taskType: taskData.taskType,
          iteration_id: iterationId,
          taskImprovement: taskImprovementCreateData,
          assignees: listAssignees,
      };
  
      try {
          const response = await Api.request({
              method: 'post',
              route: 'task',
              body: taskImprovementForm,
          });
  
          return response;
      } catch (error) {
          console.error('Error creating task improvement:', error);
          return { status: 500, message: 'Internal Server Error' };
      }
  },
  

    async createTaskBug(iterationId: string, taskData: TaskOnCreate, taskBugData: TaskBugOnCreate, listAssignees: Assignee[]) {
      const taskBugCreateData: TaskBugOnCreate = {
        checkFront: taskBugData.checkFront,
        checkBack: taskBugData.checkBack,
        checkTest: taskBugData.checkTest,
      }

      const taskBugForm: TaskBugForm = {
        title: taskData.title,
        priority: taskData.priority,
        details: taskData.details,
        complexity: taskData.complexity,
        effort: taskData.effort,
        sizeTask: taskData.sizeTask,
        taskType: taskData.taskType,
        iteration_id: iterationId,
        taskBug: taskBugCreateData,
        assignees: listAssignees
      }

      try {
        const response = await Api.request({
            method: 'post',
            route: 'task',
            body: taskBugForm,
        });

        return response;
    } catch (error) {
        console.error('Error creating task bug:', error);
        return { status: 500, message: 'Internal Server Error' };
    }
    },

    async updateTask(id: string, taskData: TaskForm) {
      if(taskData.taskBug != null){
        taskData.assignees = taskData.assignees.filter(assignee => assignee.taskStep !== 'P' && assignee.taskStep !== 'R');
      }
      const response = await Api.request({
        method: 'put',
        route: `/task/${id}`,
        body: taskData
      })

      return response
    },

    async updateTaskListName(id: string, listName: String) {
      const response = await Api.request({
        method: 'put',
        route: `/task/${id}`,
        body: { listName: listName }
      })

      return response
    },

    async updateTaskOrder(id: string, futureIndex: number) {
      const response = await Api.request({
        method: 'put',
        route: `/task/${id}`,
        body: { orderTask: futureIndex + 1 }
      })

      return response
    },

    async deleteTask(id: string) {
      const response = await Api.request({
        method: 'delete',
        route: `/task/${id}`
      })

      return response?.status === 200
    },
  }
})