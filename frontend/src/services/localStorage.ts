import CookieService from './cookieService'
import { type models } from 'src/@types'
interface UserData extends models.UserData { }

const cookieService = new CookieService()

export default class LocalStorage {
  has(key: string): boolean {
    const value = window.localStorage.getItem(key)

    return value !== null
  }

  get(key: string): any {
    if (!this.has(key)) {
      return undefined
    }

    return window.localStorage.getItem(key)
  }

  set(key: string, value: any): void {
    window.localStorage.setItem(key, value)
  }

  remove(key: string): any {
    if (this.has(key)) {
      window.localStorage.removeItem(key)
    }
  }

  getLoggedUser(): UserData | null {
    const loggedUser = this.get('loggedUser')
    if (loggedUser !== undefined && cookieService.get('Token') !== undefined) return JSON.parse(loggedUser)
    return null
  }

  setLoggedUser(token: string): void {
    this.set('loggedUser', JSON.stringify(this.parseToken(token)))
  }

  getToken(): string | undefined {
    return cookieService.get('Token'); // Utiliza CookieService para acesso seguro aos cookies
  }

  b64DecodeUnicode = (str: string): string => {
    // Going backwards: from bytestream, to percent-encoding, to original string.
    return decodeURIComponent(window.atob(str).split('').map(function (c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
    }).join(''))
  }

  capitalize = (nome: string): string => {
    const result = nome.toLocaleLowerCase().split(' ').map((str) => {
      return str !== 'de' && str !== 'da' && str !== '' ? str[0].toUpperCase() + str.substring(1) : str
    }).join(' ')

    return result.trim()
  }

  parseToken(token: string | undefined): UserData | null {
    if (token === undefined) {
      return null
    }

    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace('-', '+').replace('_', '/')

    const result: UserData = JSON.parse(this.b64DecodeUnicode(base64))

    result.name = this.capitalize(result.name)
    result.username = this.capitalize(result.username)
    result.role = this.capitalize(result.role)
  
    return result
  }

  logout() {
    try {
      // Clear local storage
      this.remove('loggedUser');
      cookieService.remove('Token');

      // Redirect to login page or appropriate landing page
      // window.location.href = '/login'; // Replace with your login URL
    } catch (error) {
      console.error('Error during logout:', error);
      // Handle errors gracefully, potentially notifying the user
    }
  }
}