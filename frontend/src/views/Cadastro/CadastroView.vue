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
      <ModeToggleButton2
        class="absolute top-8 right-8"
        type="button"
      />

      <div class="flex flex-col items-center w-[800px] h-max shadow-md p-6 rounded-md bg-white dark:bg-onyx-900">
        <img
          :src="logoPath"
          alt="Itera Logo"
          class="w-64 h-32"
        />
        <div class="flex flex-col w-full items-center mb-4">
          <p class="font-bold text-25 text-jordyBlue-900 dark:text-lightSilver-900">
            Preencha com suas informações
          </p>
          <CustomInput
            name="name"
            label="Nome Completo"
            class-helper-text="text-lightSilver-900"
            :required="true"
            :max-length="100"
            :icon-path="mdiAccount"
          />
          <CustomInput
            name="login"
            label="Usuário"
            class-helper-text="text-lightSilver-900"
            :required="true"
            :max-length="16"
            :icon-path="mdiAccount"
          />
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
          <div style="width: 40%">
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
          </div>
          <div class="flex space-x-4 mb-4 mt-4 justify-center w-full">
            <div
              @click="selectRole('GERENTE')"
              :class="selectedRole === 'GERENTE' ? 'border-lavenderIndigo-900' : 'border-gray-300'"
              class="p-4 border-2 cursor-pointer w-[150px] text-center rounded-lg"
            >
              <FontAwesomeIcon
                icon="fa-solid fa-user-tie"
                class="text-neutral-500 dark:text-white text-3xl mb-2"
              />
              <p class="font-bold text-25 text-jordyBlue-900 dark:text-lightSilver-900">
                Gerente
              </p>
            </div>

            <div
              @click="selectRole('USER')"
              :class="selectedRole === 'USER' ? 'border-lavenderIndigo-900' : 'border-gray-300'"
              class="p-4 border-2 cursor-pointer w-[150px] text-center rounded-lg"
            >
              <FontAwesomeIcon
                icon="fa-solid fa-user"
                class="text-neutral-500 dark:text-white text-3xl mb-2"
              />
              <p class="font-bold text-25 text-jordyBlue-900 dark:text-lightSilver-900">
                Membro
              </p>
            </div>
          </div>


          <div class="flex-col items-center align-center">
            <CustomButton
              title="Criar minha conta"
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
import { onMounted, ref, computed, watch } from 'vue'
import { useDark } from '@vueuse/core'
import CustomButton from 'src/components/CustomButton.vue'
import CustomInput from 'src/components/CustomInput.vue'
import ModeToggleButton2 from '../Navigation/components/ModeToggleButton2.vue'
import FeedbackUserAction from '../../components/FeedbackUserAction.vue';
import { mdiAccount, mdiLock, mdiEmail } from '@mdi/js';
import { Form } from 'vee-validate'
import { object, string, ref as refYup } from 'yup'
import { useCadastroPerfilStore } from 'src/stores/CadastroStore'
import { models } from 'src/@types'
import { useRouter } from 'vue-router'

interface UserProfileRegisterModel extends models.UserModel { }

const $router = useRouter()
const error = ref(false)
const errorText = ref('')
const loading = ref(false)
const userEmail = ref('')
const showRegistrationModal = ref(false)
const selectedRole = ref('USER')
const showPassword = ref(false)
const onError = ref<Boolean>(false)
const isVisible = ref<Boolean>(false)
const textResult = ref<String>("Cadastro realizado com sucesso.")

const $store = useCadastroPerfilStore()

const isDark = useDark()
const gradientColors = computed(() => {
  return {
    color1: isDark.value ? '#B293DF' : '#813CE6',
    color2: isDark.value ? '#463C84' : '#A193FA'
  }
})

const schema = object().shape({
  name: string().required('Informe nome e sobrenome').trim().matches(/^[A-Za-zÀ-ÿ]+(?:\s[A-Za-zÀ-ÿ]+)+$/, 'Informe nome e sobrenome'),
  login: string().required('Informe um nome de usuário').min(4, 'Nome de usuário pequeno').max(15, 'Nome de usuário muito grande').trim().matches(/^[A-Za-z0-9_.-]+$/, 'Use apenas letras, números e os seguintes caracteres . _ -'),
  email: string().required('Informe um e-mail').matches(/^([A-Za-z\d]+([._][A-Za-z\d]+)*@[A-Za-z\d]+(.[A-Za-z\d]+)*(.[A-z]{2,}))?$/, 'Email inválido'),
  password: string().required('Informe uma senha').matches(/^(?=.*[a-zA-Z])(?=.*\d).{8,}$/, 'Senha inválida'),
  confirmationPassword: string().required('Confirme a senha').oneOf([refYup('password')], 'As senhas informadas são diferentes').matches(/^(?=.*[a-zA-Z])(?=.*\d).{8,}$/, 'Senha inválida')
})

const toggleShowPassword = () => {
  showPassword.value = !showPassword.value
}

const selectRole = (role: string) => {
  selectedRole.value = role
}

const handleSubmit = async (submitData: any) => {
  const profileData: UserProfileRegisterModel = submitData
  loading.value = true

  const response = await $store.userProfileRegister(
    profileData.name,
    profileData.email,
    profileData.login,
    profileData.password,
    selectedRole.value,
  )

  if (response.status === 200 || response.status === 201) {
    userEmail.value = submitData.email
    loading.value = false
    showRegistrationModal.value = true
    isVisible.value = true
  } else {
    onError.value = false
    isVisible.value = true
    errorText.value = response.data?.message ? response.data.message : 'Requisição rejeitada pelo servidor.'
    textResult.value = "Falha ao realizar o cadastro. Motivo: " + errorText.value
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


watch(isVisible, (newValue) => {
  if (!newValue && !error.value) {
    $router.push('/home');
  }
});

const logoPath = computed(() =>
  isDark.value ? 'src/assets/iteraLogoDark.svg' : 'src/assets/iteraLogoLight.svg'
)
</script>