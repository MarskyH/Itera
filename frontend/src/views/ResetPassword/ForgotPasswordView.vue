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
      :style="{ background: `linear-gradient(to right, ${gradientColors.color1}, ${gradientColors.color2})` }"
    >
      <ModeToggleButton2 class="absolute top-8 right-8" type="button"/>

      <!-- Adicione justify-center para centralizar verticalmente e remova a altura fixa -->
      <div class="flex flex-col items-center justify-center w-[800px] shadow-md p-6 rounded-md bg-white dark:bg-onyx-900">
        <img
          :src="logoPath"
          alt="Itera Logo"
          class="w-64 h-32"
        />
        <div class="flex flex-col w-full items-center mb-4">
          <p class="font-bold text-25 text-jordyBlue-900 dark:text-lightSilver-900">
            Utilize o campo abaixo para inserir seu e-mail cadastrado. Iremos enviar um e-mail informando o link para que você possa redefinir sua senha. Verifique o lixo eletrônico ou spam.
          </p>
          <CustomInput
            name="email"
            type="email"
            label="E-mail"
            helper-text=""
            class-helper-text="text-lightSilver-900"
            :icon-path="mdiEmail"
            :required="true"
            :max-length="50"
          />
        
          <div class="flex-col items-center align-center">
            <CustomButton
              title="Confirmar"
              color="bg-lavenderIndigo-900"
              color-dark="bg-ube-900"
            />
            <div class="flex">
              <p class="font text-16 text-black dark:text-lightSilver-900">
                Já possui login?
              </p>
              <p class="px-1 font-bold text-16 text-maximumBluePurple-900 dark:text-maximumBluePurple-900">
                <a href="/login">Entre aqui</a>
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
import ModeToggleButton2 from '../Navigation/components/ModeToggleButton2.vue'
import FeedbackUserAction from '../../components/FeedbackUserAction.vue'
import { mdiAccount, mdiLock, mdiEmail } from '@mdi/js';
import { Form } from 'vee-validate'
import { object, string, ref as refYup } from 'yup'
import { usePasswordStore } from 'src/stores/PasswordStore'
import { models } from 'src/@types'
import { useRouter } from 'vue-router'
interface ForgotPassword extends models.ForgotPassword { }



const onError = ref<Boolean>(false)
const isVisible = ref<Boolean>(false)
const textResult = ref<String>("Login realizado com sucesso.")

const $router = useRouter()
const error = ref(false)
const errorText = ref('')
const $store = usePasswordStore()
const loading = ref(false)
const userEmail = ref('')
const showRegistrationModal = ref(false)


const isDark = useDark()
const gradientColors = computed(() => {
  return {
    color1: isDark.value ? '#B293DF' : '#813CE6',
    color2: isDark.value ? '#463C84' : '#A193FA'
  }
})

const schema = object().shape({
  email: string().required('Informe seu e-mail').matches(/^([A-Za-z\d]+([._][A-Za-z\d]+)*@[A-Za-z\d]+(.[A-Za-z\d]+)*(.[A-z]{2,}))?$/, 'Email inválido')
})


const handleSubmit = async (submitData: any) => {
  const data: ForgotPassword = submitData
  loading.value = true
  const response = await $store.ForgotPassword(
    data.email
  )

  if (response.status === 200 || response.status === 201) {
    userEmail.value = submitData.email
    loading.value = false
    showRegistrationModal.value = true
    isVisible.value = true
    onError.value = false
    textResult.value = 'E-mail enviado. Verifique o e-mail ' + data.email
  } else if (response.status !== 201) {
    errorText.value = response.data?.message ? response.data.message : 'Requisição não aceita.'
    error.value = true
    loading.value = false
    isVisible.value = true
    onError.value = true
    textResult.value = "Ocorreu um erro. Por favor tente novamente."
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