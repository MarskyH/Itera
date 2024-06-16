import axios, { type AxiosError } from 'axios'
import type { API } from 'src/@types'
import LocalStorage from 'src/services/localStorage'

const baseURL = import.meta.env.VITE_API_URL_LOCAL

const Axios = axios.create({
  baseURL,
  withCredentials: true
})

Axios.interceptors.request.use((config) => {
  const Token = new LocalStorage().getToken()
  if (Token !== undefined) config.headers.Authorization = `Bearer ${Token}`
  return config
})

const request: API.Request = async ({
  method,
  route,
  body,
  params,
  headers,
  responseType
}) => {
  let statusError: any
  let message: any
  const response = await Axios({
    method,
    url: route,
    data: body,
    params: (params != null) ? new URLSearchParams(params) : undefined,
    headers,
    responseType
  }).catch((error: AxiosError) => {
    statusError = (error.response != null) ? error.response.status : null
    message = (error.response != null) ? error.response.data : null
  })
  return {
    status: (response != null) ? response.status : statusError,
    data: (response != null) ? response.data : message
  }
}

export default {
  request
}