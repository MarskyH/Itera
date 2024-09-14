import userModel from "src/model/usuarioModel"
import { boolean } from "yup"

type id = number

export namespace API {
  export interface RequestParams {
    method: string
    route: string
    body?: Record<string, any>
    params?: Record<string, any>
    headers?: Record<string, any>
    responseType?: any
  }

  export interface Response {
    status: number
    data?: Record<string, any> | null | any
  }
  export type Request = (params: RequestParams) => Promise<API.Response | null>

}

export namespace models {
  export interface UserData {
    id: string
    iss: string
    sub: string
    username: string
    name: string
    role: string
    exp: number
  }

  export interface UserMemberModel {
    id: string
    name: string
    email: string
    login: string
    password?: string
    userRole: string
    enabled: boolean
    authorities: Array<{ [key: string]: string }>
    username: string
    credentialsNonExpired: boolean
    accountNonExpired: boolean
    accountNonLocked: boolean
  }

  export interface LoginModel {
    login: string
    password: string
  }

  export interface UserProfileRegisterModel {
    name: string
    login: string
    email: string
    password: string
    confirmationPassword?: string
    userRole?: string
  }

  export interface UserModel {
    id?: string
    name: string
    email: string
    login: string
    password: string
    userRole: string
  }

  export interface UserResponse {
    id: string
    nome: string
    email: string
    login: string
    password: string
    valorHora: number
    horasDedicada: number
    role: string
    enabled: boolean
    username: string
    authorities: [
      {
        authority: string
      }
    ]
    accountNonLocked: boolean
    credentialsNonExpired: boolean
    accountNonExpired: boolean
  }

  export interface BacklogRequirement {
    id?: string
    idRequirement: string
    title: string
    priority: string
    effort: number,
    sizeRequirement: number,
    progressiveBar: number
    orderRequirement: number
    checkCancelled: boolean
  }

  export interface Project {
    id?: string
    name: string
    clientName: string
    deadline: number
    workHours: number
    iterationTime: number
    createdBy?: string
  }

  export interface ProjectOnCreate {
    name: string
    clientName: string
    deadline: number
    workHours: number
    iterationTime: number
  }

  export interface ProjectOnView {
    name: string
    clientName: string
    deadline: number
    workHours: number
    iterationTime: number
    roles: Role[]
  }

  export interface Role {
    id?: string
    function: string
    skill: string
    competency: string
  }

  export interface RoleOnCreate {
    function: string
    skill: string
    competency: string
    project_id: string
  }

  export interface TeamMember {
    id?: string
    hourlyRate: number
    dedicatedHours: number
    user: UserModel
    role: Role
  }

  export interface TeamMemberOnIndex {
    id?: string
    hourlyRate: number
    dedicatedHours: number
    user: { id: string; name: string }
    roleName: { id: string; name: string }
    projectId: string
  }

  export interface TeamMemberOnCreate {
    hourlyRate: number
    dedicatedHours: number
    user_id: string
    role_id: string
    project_id: string
  }

  export interface TeamMemberPlanning{
    id: string
    hourlyRate: number
    dedicatedHours: number
    user?: UserMemberModel
    role?: Role
    project?: Project
  }

  export interface RiskOnCreate {
    title: string
    effect: string
    probability: string
    impact: string
    exposureDegree: string
    description: string
    type: string
    project_id: string
  }

  export interface Risk {
    id?: string
    title: string
    effect: string
    probability: string
    impact: string
    exposureDegree: string
    description: string
    type: string
  }

  export interface Degree {
    id: number
    name: string
  }

  export interface TaskType {
    id: number
    name: string
  }

  export interface RiskActionType {
    id: number
    name: string
  }

  export interface Priority {
    id: number
    name: string
  }

  export interface SizeRequirement {
    id: number
    name: string
  }

  export interface NonFunctionalRequirementWeights {
    value: number
    description: string
  }

  export interface FunctionalRequirement {
    id?: string
    title: string
    details: string
    complexity: string
    priority: string
    effort: number
    sizeRequirement: number
    progressiveBar?: number
  }

  export interface FunctionalRequirementOnCreate {
    title: string
    details: string
    complexity: string
    priority: string
    effort: number
    sizeRequirement: number
    project_id: string
  }

  export interface NonFunctionalRequirement {
    id?: string
    title: string
    description: description
    weights: NonFunctionalRequirementWeights
    multiple: boolean
  }
  export interface NonFunctionalRequirementProject {
    id?: string
    projectId: string
    nonfunctionalrequirementId: string
    weight: number
  }

  export interface NonFunctionalRequirementOnCreate {
    project_id: string
    nonfunctionalrequirement_id: string
    weight: number
  }

  export interface NonFunctionalRequirementProjectOnUpdate {
    id: string
    weight: number
  }

  export interface Iteration {
    id: string
    number: number
    startDate: string
    endDate: string
    active: Boolean
    project_id: string
    requirements: FunctionalRequirement[]
  }

  export interface Assignee {
    id?: string | null
    taskStep: string
    member_id: string
    deadline: number
    task_id?: string
  }

  export interface Pendency {
    id: string
    title: string
    description: string
    creationDate: string
    endDate: string
    status: boolean
    task_id: string
  }

  export interface PendencyOnCreate {
    id?: string | null
    title: string
    description: string
    status: boolean
    task_id: string
  }

  export interface PendencyOnUpdate {
    status: boolean
  }

  export interface PendencyForm {
    title: string
    description: string
  }

  export interface Task {
    id: string
    title: string
    priority: string
    details: string
    complexity: string
    effort: string
    sizeTask: number
    startDate: string
    endDate: string
    checkCancelled: boolean,
    detailsCancelled: string,
    progressiveBar?: number,
    taskType: string
    taskrequirement_id?: string
    taskimprovement_id?: string
    taskbug_id?: string
    taskplanning_id?: string
    taskRequirement?: TaskRequirement,
    taskImprovement?: TaskImprovement
    taskBug?: TaskBug
    taskPlanning?: TaskPlanning
    taskReview?: TaskReview
    iteration_id: string
    assignees: Assignee[]
    pendencies: Pendency[]
  }

  export interface TaskRequirement {
    id: string
    task_id: string
    checkProject: boolean
    checkRequirement: boolean
    checkFront: boolean
    checkBack: boolean
    checkTest: boolean
  }

  export interface TaskRequirementOnUpdate {
    id: string
    detailsCancelled: string
    checkProject: boolean
    checkRequirement: boolean
    checkFront: boolean
    checkBack: boolean
    checkTest: boolean
  }

  export interface TaskImprovement {
    id: string
    task_id: string
    checkProject: boolean
    checkRequirement: boolean
    checkFront: boolean
    checkBack: boolean
    checkTest: boolean
  }

  export interface TaskImprovementOnUpdate {
    id: string
    detailsCancelled: string
    checkProject: boolean
    checkRequirement: boolean
    checkFront: boolean
    checkBack: boolean
    checkTest: boolean
  }

  export interface TaskBug {
    id: string
    task_id: string
    checkFront: boolean
    checkBack: boolean
    checkTest: boolean
  }

  export interface TaskBugOnUpdate {
    id: string
    detailsCancelled: string
    checkFront: boolean
    checkBack: boolean
    checkTest: boolean
  }

  export interface TaskPlanning {
    id: string,
		totalSize: number,
    totalEffort: number,
    plannedSpeed: number,
    projectBacklog:BacklogRequirement[],
    projectMembers:TeamMemberPlanning[]
  }


  export interface TaskPlanningOnUpdate {
    id: string,
		totalSize: number,
    totalEffort: number,
    plannedSpeed: number,
    projectBacklog:BacklogRequirement[],
    projectMembers:TeamMemberPlanning[]
  }

  export interface TaskReview {
    id: string
		totalSize: number
    totalEffort: number
    completedSpeed: number
    iterationBacklog:BacklogRequirement[]
    completedScope:BacklogRequirement[]
    participatingMembers:TeamMemberPlanning[]
    checkHumanResources: boolean
    checkSpeed: boolean
    checkScope: boolean
    checkRisks: boolean
  }

  export interface TaskReviewOnUpdate {
    id: string
		totalSize: number
    totalEffort: number
    completedSpeed: number
    iterationBacklog:BacklogRequirement[]
    completedScope:BacklogRequirement[]
    participatingMembers:TeamMemberPlanning[]
    checkHumanResources: boolean
    checkSpeed: boolean
    checkScope: boolean
    checkRisks: boolean
  }

  export interface TaskOnCreate {
    title: string
    priority: string
    details: string
    complexity: string
    effort: string
    sizeTask: number
    startDate?: string
    endDate?: string
    taskType: string
    taskrequirement_id?: string
    taskimprovement_id?: string
    taskbug_id?: string
    iteration_id: string
  }

  export interface TaskRequirementOnCreate {
    task_id: string
    checkProject: boolean
    checkRequirement: boolean
    checkFront: boolean
    checkBack: boolean
    checkTest: boolean
  }

  export interface TaskImprovementOnCreate {
    task_id?: string
    checkProject: boolean
    checkRequirement: boolean
    checkFront: boolean
    checkBack: boolean
    checkTest: boolean
  }

  export interface TaskBugOnCreate {
    task_id?: string
    checkFront: boolean
    checkBack: boolean
    checkTest: boolean
  }

  export interface TaskForm {
    startDate?: string
    endDate?: string
    taskType: string
    assignees: Assignee[]
    taskRequirement: TaskRequirementOnUpdate
    taskImprovement: TaskImprovementOnUpdate
    taskBug: TaskBugOnUpdate
    taskPlanning: TaskPlanningOnUpdate
    taskReview:TaskReviewOnUpdate
  }
}

export interface InputFieldProps {
  name: string
  label: string
  placeholder: string
  type?: string
  required: boolean
  disabled?: boolean
  value?: string | boolean
  options?: { value: string; name: string; selected: boolean }[]
  hoverInfo?: string
  mask?: string
  validation: any
  onChange?(): any
}

export interface RoleForm {
  function: string
  skill: string
  competency: string
}

export interface TeamMemberForm {
  user: string
  hourlyRate: number
  dedicatedHours: number
  role: string
}

export interface RiskForm {
  title: string
  effect: string
  probability: string
  impact: string
  exposureDegree: string
  description: string
  type: string
}

export interface FunctionalRequirementForm {
  title: string
  details: string
  complexity: string
  priority: string
  effort: number
  sizeRequirement: number
}

export interface NonFunctionalRequirementForm {
  id: string
  weight: number
}



export interface TaskRequirementForm {
  task: Task
  taskRequirement: TaskRequirement
  assignees: Assignee[]
}

export interface TaskImprovementForm {
  title: string
  priority: string
  details: string
  complexity: string
  effort: string
  sizeTask: number
  taskType: string
  iteration_id: string
  taskImprovement: TaskImprovement
  assignees: Assignee[]
}

export interface TaskBugForm {
  title: string
  priority: string
  details: string
  complexity: string
  effort: string
  sizeTask: number
  taskType: string
  iteration_id: string
  taskBug: TaskBug
  assignees: Assignee[]
}