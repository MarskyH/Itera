<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

const ilustracao = "src/assets/ilustracaoNovoProjeto.png"

</script>

<template>
  <div class="flex flex-col w-full h-full items-center justify-center gap-8">
    <img
      :src="ilustracao"
      alt="Ilustração Novo Projeto"
      class="shrink-0 w-40 h-40"
    >

    <span class=" w-1/2 text-center text-stone-500 dark:text-stone-400">
      Não há projetos ativos. Use a função “Novo projeto” e acesse nossas funcionalidades.
    </span>

    <button
      class="flex items-center bg-gradient-to-br from-40% from-lavenderIndigo-900 to-tropicalIndigo-900 p-4 gap-4 rounded-md"
      @click="$router.push({ name: 'new-project' })"
    >
      <FontAwesomeIcon
        icon="fa-solid fa-folder-plus"
        class="text-white"
      />

      <span class="font-semibold text-white">
        Comece criando um projeto
      </span>
    </button>
  </div>
</template>

