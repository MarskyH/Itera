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
        login: string
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
        password: string
        confirmationPassword?: string
        horasDedicada?: number
        role?: string
    }


    export interface UserModel {
        id?: string
        nome: string
        login: string
        password: string
        horasDedicada: number
        role: string
    }

}