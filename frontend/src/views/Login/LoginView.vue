<template>
  <FeedbackUserAction
    :text="textResult" 
    :onError="onError" 
    :isVisible="isVisible" 
    @update:isVisible="isVisible = $event" 
  />
  <Form
    @submit="handleSubmit"
    @invalid-submit="onInvalid"
    :validation-schema="schema"
  >
    <main
      class="py-8 flex items-center justify-center min-h-screen"
      :style="{background: `linear-gradient(to right, ${gradientColors.color1}, ${gradientColors.color2})`}"
    >
      <ModeToggleButton2 class="absolute top-8 right-8" type="button"/>

      <div class="flex flex-col items-center w-[800px] h-[500px] shadow-md p-6 rounded-md bg-white dark:bg-onyx-900">
        <img
          :src="logoPath"
          alt="Itera Logo"
          class="w-64 h-32"
        />
        <div class="flex flex-col w-full items-center mb-4">
          <p class="font-bold text-25 text-jordyBlue-900 dark:text-lightSilver-900">
            Entre com suas credenciais
          </p>
          <CustomInput
            name="login"
            label="Usuário"
            class-helper-text="text-lightSilver-900"
            :required="true"
            :max-length="16"
            :icon-path="mdiAccount"
          />
         
          <CustomInput
            name="password"
            label="Senha"
            helper-text=""
            class-helper-text="text-lightSilver-900"
            :type="showPassword ? 'text' : 'password'"
            :required="true"
            :icon-path="mdiLock"
            :max-length="80"
          />
          <div class="flex-col itmens-center align-center">
            <div class="flex">
              <input
                type="checkbox"
                id="showPasswordCheckbox"
                v-model="showPassword"
                class="mr-2 cursor-pointer"
              />
              <label
                for="showPasswordCheckbox"
                class="text-jordyBlue-900 dark:text-lightSilver-900 cursor-pointer"
              >
                Mostrar Senha
              </label>
              <p class="ml-8 px-1 font-bold text-16 text-maximumBluePurple-900 dark:text-maximumBluePurple-900">
                <a href="/esqueceu-sua-senha"> Esqueceu sua senha?</a>
              </p>
            </div>
            
            <CustomButton
              title="Entrar"
              color="bg-lavenderIndigo-900"
              color-dark="bg-ube-900"
            />
            <div class="flex ml-9">
              <p class="font text-16 text-black dark:text-lightSilver-900">
                Não possui login?
              </p>
              <p class="px-1 font-bold text-16 text-maximumBluePurple-900 dark:text-maximumBluePurple-900">
                <a href="/cadastro"> Crie sua conta</a>
              </p>
            </div>
          </div>
        </div>
      </div>
    </main>
  </Form>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useDark, useToggle } from '@vueuse/core'
import { computed } from 'vue'
import CustomButton from 'src/components/CustomButton.vue'
import CustomInput from 'src/components/CustomInput.vue'
import { mdiAccount, mdiLock, mdiEmail } from '@mdi/js';
import { Form } from 'vee-validate'
import { object, string, ref as refYup } from 'yup'
import { useLoginStore } from 'src/stores/LoginStore'
import { models } from 'src/@types'
import { useRouter } from 'vue-router'
import FeedbackUserAction from '../../components/FeedbackUserAction.vue';
import ModeToggleButton2 from '../Navigation/components/ModeToggleButton2.vue'


interface UserLogin extends models.LoginModel { }




const $router = useRouter()
const error = ref(false)
const errorText = ref('')
const showPassword = ref(false)
const $store = useLoginStore()
const loading = ref(false)
const userEmail = ref('')
const showRegistrationModal = ref(false)
const onError = ref<Boolean>(false)
const isVisible = ref<Boolean>(false)
const textResult = ref<String>("Login realizado com sucesso.")

const isDark = useDark()
const gradientColors = computed(() => {
  return {
    color1: isDark.value ? '#B293DF' : '#813CE6',
    color2: isDark.value ? '#463C84' : '#A193FA'
  }
})

const schema = object().shape({
  login: string().required('Informe um nome de usuário').min(4, 'Nome de usuário pequeno').max(15, 'Nome de usuário muito grande').trim().matches(/^[A-Za-z0-9_.-]+$/, 'Use apenas letras, números e os seguintes caracteres . _ -'),
  password: string().required('Informe uma senha').matches(/^(?=.*[a-zA-Z])(?=.*\d).{8,}$/, 'Senha inválida'),
})

const toggleShowPassword = () => {
  showPassword.value = !showPassword.value
}

const handleSubmit = async (submitData: any) => {
  const profileData: UserLogin = submitData
  loading.value = true
  const response = await $store.userLogin(
    profileData.login,
    profileData.password,
  )
  
  if (response.status === 201 || response.status === 200) {
    error.value = false
    loading.value = false
    showRegistrationModal.value = true
    $router.push('/home')
  } else if (response.status !== 201) {
    errorText.value = response.data?.message ? response.data.message : 'Requisição rejeitada pelo servidor.'
    isVisible.value = true
    onError.value = true
    textResult.value = "Falha ao realizar o login. Motivo: " + errorText.value
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

const logoPath = computed(() =>
  isDark.value ? 'src/assets/iteraLogoDark.svg' : 'src/assets/iteraLogoLight.svg'
)
</script>