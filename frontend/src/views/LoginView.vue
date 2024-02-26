<script setup lang="ts">
import { useDark, useToggle } from "@vueuse/core";
import { computed } from 'vue';
import CustomInput from 'src/components/CustomInput.vue'
import CustomButton from "src/components/CustomButton.vue";
import ModeToggleButton from "src/components/ModeToggleButton.vue";


const isDark = useDark();
const gradientColors = computed(() => {
  return {
    color1: isDark.value ? '#B293DF' : '#813CE6',
    color2: isDark.value ? '#463C84' : '#A193FA'
  }
});

const logoPath = computed(() => isDark.value ? 'src/assets/iteraLogoDark.svg' : 'src/assets/iteraLogoLight.svg');

</script>

<template>
  <main class="pt-8 flex items-center justify-center min-h-screen"
    :style="{ background: `linear-gradient(to right, ${gradientColors.color1}, ${gradientColors.color2})` }">
    <ModeToggleButton class="absolute top-8 right-8" />

    <div class="flex justify-center w-[800px] h-[650px] shadow-md p-6 rounded-md bg-white dark:bg-onyx-900 ">
      <div class="flex-col ">
        <img :src="logoPath" alt="Itera Logo" class="w-64 h-32" />
        <div class="flex-col mb-4">
          <p class="font-bold text-25 text-jordyBlue-900 dark:text-lightSilver-900">
            Entre usando suas credenciais
          </p>
          <CustomInput title="Usuário" placeholder="Usuário" icon="user" />
          <CustomInput title="Senha" placeholder="Senha" icon="lock" />
          <div class="flex-col itmens-center align-center">
            <div class="flex">
              <p class="font text-16 text-black dark:text-lightSilver-900">
                Esqueceu sua senha?
              </p>
              <p class="px-1 font-bold text-16 text-maximumBluePurple-900 dark:text-maximumBluePurple-900">
                Recupere aqui
              </p>
            </div>
            <CustomButton title="Criar minha conta" color="bg-lavenderIndigo-900" color-dark="bg-ube-900" />
            <div class="flex">
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
    </div>
  </main>
</template>
