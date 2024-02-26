<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

import PageHeader from 'src/components/PageHeader.vue'

const ilustracaoPath = "src/assets/ilustracaoNovoProjeto.png"

</script>

<template>
  <div
    class="flex flex-col h-full min-w-[320px] w-full rounded-md overflow-auto bg-gradient-to-b from-white to-seaSalt-900 dark:bg-gradient-to-b dark:from-eerieBlackLight-900 dark:from-10% dark:to-eerieBlackDark-900 dark:to-90%"
  >
    <PageHeader
      title="Home"
      icon="house"
    />

    <div class="flex flex-col w-full h-full items-center justify-center gap-8">
      <img
        :src="ilustracaoPath"
        alt="Ilustração Novo Projeto"
        class="shrink-0 w-40 h-40"
      >

      <span class=" w-1/2 text-center text-stone-500 dark:text-stone-400">
        Não há projetos ativos. Use a função “Novo projeto” e acesse nossas funcionalidades.
      </span>

      <button class="flex items-center bg-gradient-to-br from-40% from-lavenderIndigo-900 to-tropicalIndigo-900 p-4 gap-4 rounded-md">
        <FontAwesomeIcon
          icon="fa-solid fa-folder-plus"
          class="text-white"
        />

        <span class="font-semibold text-white">
          Comece criando um projeto
        </span>
      </button>
    </div>
  </div>
</template>
