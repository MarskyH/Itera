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
        exp: number
        iat: number
        idUsuario: number
        isEgresso: boolean
        iss: string
        nomeCompleto: string
        nome: string
        scope: string
        sobrenome: string
        sub: string
    }

    export interface LoginModel {
        username: string
        password: string
    }

    export interface UserProfileRegisterModel {
        nome: string
        username: string
        registration?: string
        email: string
        confirmationEmail?: string
        password: string
        confirmationPassword?: string
        accessLevel?: string
        grupos?: string[]
    }


    export interface UserModel {
        id?: number
        username: string
        password: string
        email: string
        nome: string
        registration?: string
        egresso?: {
            id: number
            matricula: number
            etnia: {
                id: number
                nome: string
            }
            genero: {
                id: number
                nome: string
            }
            cotista: true
            pcd: true
            interesseEmPos: true
            lattes: string
            linkedin: string
            endereco: {
                id: number
                cidade: string
                estado: string
                pais: string
            }
        }
        grupos?: [
            {
                id: number
                nomeGrupo?: string
            }
        ]
    }

}