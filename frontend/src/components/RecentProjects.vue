<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

</script>

<template>
  <div
    class="flex flex-col h-full w-full rounded-md p-3 bg-gradient-to-b from-white to-seaSalt-900 dark:bg-gradient-to-b dark:from-eerieBlackLight-900 dark:from-10% dark:to-eerieBlackDark-900 dark:to-90%"
  >
    <div class="flex gap-3 items-center">
      <FontAwesomeIcon
        icon="fa-solid fa-clock-rotate-left"
        class="text-stone-500 dark:text-stone-400"
      />
      <span class="font-bold">Projetos recentes</span>
    </div>

    <div class="flex flex-col w-full h-full justify-center items-center">
      <span class=" w-1/2 text-center text-stone-500 dark:text-stone-400 text-sm">
        Não há projetos recentes para serem abertos
      </span>
    </div>
  </div>
</template>
