export default interface userModel {
    id?: number
    username: string
    password: string
    email: string
    nome: string
    matricula: string
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
        nomeGrupo: string
      }
    ]
  }