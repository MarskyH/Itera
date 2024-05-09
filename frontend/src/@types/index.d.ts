import userModel from "src/model/usuarioModel"

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

  export interface RiskActionType {
    id: number
    name: string
  }

  export interface Priority {
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
}

export interface InputFieldProps {
  name: string
  label: string
  placeholder: string
  type?: string
  required: boolean
  disabled?: boolean
  value?: string
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



/*

export interface AcaoRegister {
  titulo: string
  descricao: string
  tipo: string
  risco: {
    id: number
    titulo: string
    efeito: string
    probabilidade: string
    impacto: string
    grauExposicao: string
  }
}

export interface AcaoResponse {
  id: number
  titulo: string
  descricao: string
  tipo: string
  risco_id: number
}

export interface RequisitoRegister {
  nome: string
  titulo: string
  detalhamento: string
  complexidade: string
  prioridade: string
  esforco: number
  tamanho: number
  projeto: ProjetoResponse
}
export interface RequisitoResponse {
  id: number
  nome: string
  titulo: string
  detalhamento: string
  complexidade: string
  prioridade: string
  esforco: number
  tamanho: number
  projeto_id: number
}

export interface RequisitoNaoFuncionalRegister {
  titulo: string
  valor: number
  projeto: ProjetoResponse
}

export interface RequisitoNaoFuncionalResponse {
  id: number
  titulo: string
  valor: number
  projeto_id: number
}


export interface ProjetoCompletoResponse {
  id: number
  nome: string
  prazo: number
  tempoIteracao: number
  nomeCliente: string
  createdBy: string
  listaPapel: PapelResponse[]
  listaUserEquipe: UserResponse[]
  listaRiscos: RiscoResponse[]
  listaRequisitos: RequisitoResponse[]
  listaRequisitosNaoFuncionais: RequisitoNaoFuncionalResponse[]
}

*/
