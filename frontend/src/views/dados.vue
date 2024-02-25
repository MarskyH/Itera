<template>
    <OLoading
      :full-page="true"
      v-model:active="loading"
      full-page-class="bg-white/[.25] backdrop-blur-[1px] z-50"
    >
      <SvgIcon
        type="mdi"
        size="80"
        class="text-blue-400 animate-spin"
        :path="mdiLoading"
      />
    </OLoading>
    <Form
      @submit="handleSubmit"
      @invalid-submit="onInvalid"
      :validation-schema="schema"
    >
      <div class="w-full flex items-center justify-center bg-neutral-100 my-8">
        <div
          class="flex flex-col items-center bg-white w-[960px] py-6 mx-6 rounded-2xl shadow-md"
        >
          <InvalidInsert
            :text="errorText"
            :show-alert="error"
          />
          <h1 class="text-blue-900 text-4xl font-bold mb-6">
            Cadastro
          </h1>
          <div class="mb-8">
            <p class="text-blue-400 text-base text-center font-bold mb-5">
              Preencha os campos abaixo
            </p>
            <div class="flex flex-col gap-y-2 mb-4">
              <div class="flex flex-col gap-x-6 gap-y-2 md:gap-x-16 lg:gap-x-20 xl:gap-x-24 2xl:gap-x-32 sm:flex-row">
                <CustomInput
                  name="nome"
                  label="Nome Completo"
                  class-helper-text="text-gray-600"
                  :required="true"
                  :icon-path="mdiAccount"
                  :max-length="100"
                />
                <CustomInput
                  name="username"
                  label="Usuário"
                  helper-text="Use no mínimo quatro caracteres sem espaços"
                  class-helper-text="text-gray-600"
                  :required="true"
                  :icon-path="mdiAccount"
                  :max-length="50"
                />
              </div>
              <div class="flex flex-col gap-x-6 gap-y-2 md:gap-x-16 lg:gap-x-20 xl:gap-x-24 2xl:gap-x-32 sm:flex-row">
                <CustomInput
                  name="registration"
                  type="text"
                  label="Matrícula"
                  mask="############"
                  helper-text="Informe a sua matrícula da faculdade"
                  class-helper-text="text-gray-600"
                  :error-message="`Matrícula inválida, faltam ${missingDigits} dígitos`"
                  custom-error-message
                  :icon-path="mdiSchool"
                  @update:value="checkRegistrationLength"
                />
                <CustomInput
                  name="email"
                  type="email"
                  label="E-mail"
                  helper-text="Informe o mesmo e-mail que está cadastrado no SIGAA"
                  class-helper-text="text-gray-600"
                  :icon-path="mdiEmail"
                  :required="true"
                  :max-length="50"
                />
              </div>
              <div class="flex flex-col gap-x-6 gap-y-2 md:gap-x-16 lg:gap-x-20 xl:gap-x-24 2xl:gap-x-32 sm:flex-row">
                <CustomInput
                  name="password"
                  label="Senha"
                  helper-text="Use oito ou mais caracteres com uma combinação de letras, números e símbolos"
                  class-helper-text="text-gray-600"
                  :type="showPassword? 'text' : 'password'"
                  :required="true"
                  :icon-path="mdiLock"
                  :max-length="80"
                />
                <CustomInput
                  name="confirmationPassword"
                  label="Confirme Senha"
                  :type="showPassword? 'text' : 'password'"
                  :required="true"
                  :icon-path="mdiLock"
                  :max-length="80"
                />
              </div>
              <CustomCheckbox
                label="Visualizar senhas"
                name="showPassword"
                @update:value="toggleShowPassword"
              />
            </div>
          </div>
          <CustomButton type="submit">
            Cadastrar-se
          </CustomButton>
          <p class="mt-9">
            Já possui conta?
            <RouterLink
              to="/entrar"
              class="text-sky-600"
            >
              Entre
            </RouterLink>
          </p>
          <p class="mt-2 w-72 text-sm text-center">
            Ao clicar em Cadastrar-se, você concorda com nossa
            <RouterLink
              to="/privacidade"
              class="text-sky-600"
            >
              Política de Privacidade e Cookies.
            </RouterLink>
          </p>
        </div>
      </div>
    </Form>
    <CustomDialog
      v-model="showRegistrationModal"
    >
      <div class="flex flex-col h-full justify-center items-center text-center gap-y-6 sm:gap-y-6">
        <div class="flex flex-col justify-center items-center gap-y-4">
          <SvgIcon
            type="mdi"
            size="75"
            :path="mdiAlertCircle"
            class="text-amber-400"
          />
          <p class="text-amber-400 text-2xl sm:text-3xl font-bold">
            Açao necessária
          </p>
        </div>
        <p class="font-medium text-lg max-w-lg">
          Para validar o seu cadastro siga as instruções que foram enviadas para o e-mail:
        </p>
        <p class="font-bold text-lg">
          {{ userEmail }}
        </p>
        <CustomButton
          type="button"
          tag="router"
          color="sky"
          text-class="text-white font-bold text-lg p-20 w-64 py-6"
          link="/"
        >
          Fechar
        </CustomButton>
      </div>
    </CustomDialog>
  </template>
  
  <script setup lang="ts">
  import { onMounted, ref } from 'vue'
  import CustomInput from 'src/components/CustomInput.vue'
  import SvgIcon from '@jamescoyle/vue-icon'
  import { mdiAccount, mdiSchool, mdiEmail, mdiLock, mdiLoading, mdiAlertCircle } from '@mdi/js'
  import { Form } from 'vee-validate'
  import { object, string, ref as refYup } from 'yup'
  import CustomButton from 'src/components/CustomButton.vue'
  import InvalidInsert from 'src/components/InvalidInsert.vue'
  import CustomCheckbox from 'src/components/CustomCheckbox.vue'
  import CustomDialog from 'src/components/CustomDialog.vue'
  import { useCadastroPerfilStore } from 'src/store/CadastroPerfilStore'
  import { OLoading } from '@oruga-ui/oruga-next'
  import { models } from 'src/@types'
  interface UserProfileRegisterModel extends models.UserProfileRegisterModel { }
  
  const error = ref(false)
  const errorText = ref('')
  const missingDigits = ref(0)
  const showPassword = ref(false)
  const $store = useCadastroPerfilStore()
  const loading = ref(false)
  const userEmail = ref('')
  const showRegistrationModal = ref(false)
  
  const schema = object().shape({
    nome: string().required('Informe nome e sobrenome').trim().matches(/^[A-Za-zÀ-ÿ]+(?:\s[A-Za-zÀ-ÿ]+)+$/, 'Informe nome e sobrenome'),
    username: string().required('Informe um nome de usuário').min(4, 'Nome de usuário pequeno').max(15, 'Nome de usuário muito grande').trim().matches(/^[A-Za-z0-9_.-]+$/, 'Use apenas letras, números e os seguintes caracteres . _ -'),
    registration: string().max(12).matches(/^(\d{12})?$/),
    email: string().required('Informe um e-mail').matches(/^([A-Za-z\d]+([._][A-Za-z\d]+)*@[A-Za-z\d]+(.[A-Za-z\d]+)*(.[A-z]{2,}))?$/, 'Email inválido'),
    password: string().required('Informe uma senha').matches(/^(?=.*[a-zA-Z])(?=.*\d).{8,}$/, 'Senha inválida'),
    confirmationPassword: string().required('Confirme a senha').oneOf([refYup('password')], 'As senhas informadas são diferentes').matches(/^(?=.*[a-zA-Z])(?=.*\d).{8,}$/, 'Senha inválida')
  })
  
  const checkRegistrationLength = ($event: Event) => {
    missingDigits.value = 12 - String($event).length
  }
  
  const toggleShowPassword = () => {
    showPassword.value = !showPassword.value
  }
  
  const handleSubmit = async (submitData: any) => {
    const profileData: UserProfileRegisterModel = submitData
    loading.value = true
    const response = await $store.userProfileRegister(
      profileData.username,
      profileData.password,
      profileData.email,
      profileData.nome,
      profileData.registration
    )
  
    if (response.status === 201) {
      userEmail.value = submitData.email
      loading.value = false
      showRegistrationModal.value = true
    } else if (response.status !== 201) {
      errorText.value = response.data?.message ? response.data.message : 'Requisição não aceita.'
      error.value = true
      loading.value = false
    }
  }
  
  const onInvalid = (e: any) => {
    console.log(e)
  }
  
  onMounted(() => {
    window.scrollTo(0, 0)
  })
  </script>
  
  <style>
  input::-webkit-outer-spin-button,
  input::-webkit-inner-spin-button {
    /* display: none; <- Crashes Chrome on hover */
    -webkit-appearance: none;
    margin: 0;
    /* <-- Apparently some margin are still there even though it's hidden */
  }
  
  input::-ms-reveal,
  input::-ms-clear {
    display: none;
  }
  </style>