<template>
  <Form
    @submit="handleSubmit"
    @invalid-submit="onInvalid"
    :validation-schema="schema"
  >
    <main
      class="py-8 flex items-center justify-center min-h-screen"
      :style="{ background: `linear-gradient(to right, ${gradientColors.color1}, ${gradientColors.color2})` }"
    >
      <ModeToggleButton class="absolute top-8 right-8" type="button"/>

      <!-- Adicione justify-center para centralizar verticalmente e remova a altura fixa -->
      <div class="flex flex-col items-center justify-center w-[800px] shadow-md p-6 rounded-md bg-white dark:bg-onyx-900">
        <img
          :src="logoPath"
          alt="Itera Logo"
          class="w-64 h-32"
        />
        <div class="flex flex-col w-full items-center mb-4">
          <p class="font-bold text-25 text-jordyBlue-900 dark:text-lightSilver-900">
            Insira sua nova senha e confirme ela. Não se preocupe, se esquecer novamente. Basta redefinir novamente. 
          </p>
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

          <CustomInput
            name="confirmationPassword"
            label="Confirme Senha"
            :type="showPassword ? 'text' : 'password'"
            :required="true"
            :icon-path="mdiLock"
            :max-length="80"
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
import ModeToggleButton from 'src/views/Navigation/components/ModeToggleButton.vue'
import { mdiAccount, mdiLock, mdiEmail } from '@mdi/js';
import { Form } from 'vee-validate'
import { object, string, ref as refYup } from 'yup'
import { usePasswordStore } from 'src/stores/PasswordStore'
import { models } from 'src/@types'
import { useRouter } from 'vue-router'
interface ResetPassword extends models.ResetPassword { }


const $router = useRouter()
const error = ref(false)
const errorText = ref('')
const showPassword = ref(false)
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
  password: string().required('Informe uma senha').matches(/^(?=.*[a-zA-Z])(?=.*\d).{8,}$/, 'Senha inválida'),
  confirmationPassword: string().required('Confirme a senha').oneOf([refYup('password')], 'As senhas informadas são diferentes').matches(/^(?=.*[a-zA-Z])(?=.*\d).{8,}$/, 'Senha inválida')
})


const handleSubmit = async (submitData: any) => {
  const urlParams = new URLSearchParams(window.location.search);
  const token = urlParams.get('token') || '';
  const data: ResetPassword = {
    token: token,
    newPassword: submitData.password
  }
  loading.value = true
  const response = await $store.ResetPassword(
    data.token,
    data.newPassword
  )

  if (response.status === 200 || response.status === 201) {
    userEmail.value = submitData.email
    loading.value = false
    showRegistrationModal.value = true
    alert('Senha redefinida com sucesso. Faça login')
    $router.push('/login')
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

const logoPath = computed(() =>
  isDark.value ? 'src/assets/iteraLogoDark.svg' : 'src/assets/iteraLogoLight.svg'
)
</script>