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
    user: UserMemberModel
    role: Role
    project: Project
  }

  export interface TeamMemberOnIndex {
    id?: string
    hourlyRate: number
    dedicatedHours: number
    user: { id: string; name: string}
    roleName: { id: string; name: string}
    projectId: string
  }

  export interface TeamMemberOnCreate {
    hourlyRate: number
    dedicatedHours: number
    user_id: string
    role_id: string
    project_id: string
  }



  /*

  export interface RiscoRegister {
    titulo: string
    efeito: string
    probabilidade: string
    impacto: string
    grauExposicao: string
    projeto: ProjetoResponse
  }

  export interface RiscoResponse {
    id: number
    titulo: string
    efeito: string
    probabilidade: string
    impacto: string
    grauExposicao: string
    projeto_id: number
  }

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
}

export interface InputFieldProps {
  name: string
  label: string
  placeholder: string
  required: boolean
  options?: { value: string; name: string; selected: boolean }[]
  validation: any
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