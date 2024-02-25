<script setup lang="ts">
import { useDark, useToggle } from '@vueuse/core'
import { computed } from 'vue'
import CustomInput from '@/components/CustomInput.vue'
import CustomButton from '@/components/CustomButton.vue'
import ModeToggleButton from '@/components/ModeToggleButton.vue'
import { Form } from 'vee-validate'








const isDark = useDark()
const gradientColors = computed(() => {
  return {
    color1: isDark.value ? '#B293DF' : '#813CE6',
    color2: isDark.value ? '#463C84' : '#A193FA'
  }
})

const logoPath = computed(() =>
  isDark.value ? 'src/assets/iteraLogoDark.svg' : 'src/assets/iteraLogoLight.svg'
)
</script>

<template>
  <main class="py-8 flex items-center justify-center min-h-screen" :style="{
    background: `linear-gradient(to right, ${gradientColors.color1}, ${gradientColors.color2})`
  }">
    <ModeToggleButton class="absolute top-8 right-8" />

    <div class="flex flex-col items-center w-[800px] h-[650px] shadow-md p-6 rounded-md bg-white dark:bg-onyx-900">
      <img :src="logoPath" alt="Itera Logo" class="w-64 h-32" />
      <div class="flex flex-col w-full items-center mb-4">
        <p class="font-bold text-25 text-jordyBlue-900 dark:text-lightSilver-900">
          Preencha com suas informações
        </p>
        <CustomInput title="Nome" placeholder="Usuário" icon="user" />
        <CustomInput title="E-mail" placeholder="E-mail" icon="envelope" />
        <CustomInput title="Senha" placeholder="Senha" icon="lock" />
        <CustomInput title="Repetir senha" placeholder="Repetir senha" icon="lock" />
        <div class="flex-col items-center align-center">
          <CustomButton title="Criar minha conta" color="bg-lavenderIndigo-900" color-dark="bg-ube-900" />
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
</template>
