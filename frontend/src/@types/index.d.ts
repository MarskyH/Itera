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
        username: string
        nome: string
        horasDedicada: string
        role: string
        exp: number
    }

    export interface LoginModel {
        login: string
        password: string
    }

    export interface UserProfileRegisterModel {
        nome: string
        login: string
        email: string
        password: string
        confirmationPassword?: string
        valorHora?: number
        horasDedicada?: number
        role?: string
    }


    export interface UserModel {
        id?: string
        nome: string
        login: string
        email: string,
        password: string
        horasDedicada: number
        role: string
    }

    export interface UserResponse {
        id: string,
        nome: string,
        email: string,
        login: string,
        password: string,
        valorHora: number,
        horasDedicada: number,
        role: string,
        enabled: boolean,
        username: string,
        authorities: [
            {
                authority: string
            }
        ],
        accountNonLocked: boolean,
        credentialsNonExpired: boolean,
        accountNonExpired: boolean
    }

    export interface ProjetoRegister {
        nome: string,
        prazo: number,
        tempoIteracao: number,
        nomeCliente: string,
        createdBy: string,
    }

    export interface ProjetoResponse {
        id: number,
        nome: string,
        prazo: number,
        tempoIteracao: number,
        nomeCliente: string,
        createdBy: string
    }

    export interface EquipeRegister {
        projeto: ProjetoResponse
    }

    export interface EquipeResponse {
        /*Talvez não seja necessário*/
    }

    export interface EquipeUsers {
        id: string,
        nome: string,
        email: string,
        username: string,
        valorHora: number,
        horasDedicadas: number,
        userRole: string
    }

    export interface PapelRegister {
        funcao: string,
        habilidade: string,
        competencia: string,
        projeto: ProjetoResponse
    }

    export interface PapelResponse {
        id: number,
        funcao: string,
        habilidade: string,
        competencia: string
    }

    export interface RiscoRegister {
        titulo: string,
        efeito: string,
        probabilidade: string,
        impacto: string,
        grauExposicao: string,
        projeto: ProjetoResponse
    }

    export interface RiscoResponse {
        id: number,
        titulo: string,
        efeito: string,
        probabilidade: string,
        impacto: string,
        grauExposicao: string,
        projeto_id: number
    }

    export interface AcaoRegister {
        titulo: string,
        descricao: string,
        tipo: string,
        risco: {
            id: number,
            titulo: string,
            efeito: string,
            probabilidade: string,
            impacto: string,
            grauExposicao: string
        }
    }

    export interface AcaoResponse {
        id: number,
        titulo: string,
        descricao: string,
        tipo: string,
        risco_id: number
    }

    export interface RequisitoRegister {
        nome: string,
        titulo: string,
        detalhamento: string,
        complexidade: string,
        prioridade: string,
        esforco: number,
        tamanho: number,
        projeto: ProjetoResponse
    }
    export interface RequisitoResponse {
        id: number,
        nome: string,
        titulo: string,
        detalhamento: string,
        complexidade: string,
        prioridade: string,
        esforco: number,
        tamanho: number,
        projeto_id: number
    }

    export interface RequisitoNaoFuncionalRegister {
        titulo: string,
        valor: number,
        projeto: ProjetoResponse
    }

    export interface RequisitoNaoFuncionalResponse {
        id: number
        titulo: string,
        valor: number,
        projeto_id: number
    }


    export interface ProjetoCompletoResponse {
        id: number,
        nome: string,
        prazo: number,
        tempoIteracao: number,
        nomeCliente: string,
        createdBy: string,
        listaPapel: PapelResponse[],
        listaUserEquipe: UserResponse[],
        listaRiscos: RiscoResponse[],
        listaRequisitos: RequisitoResponse[],
        listaRequisitosNaoFuncionais: RequisitoNaoFuncionalResponse[]
    }






}