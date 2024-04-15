<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

defineProps<{
  content?: any
}>()

</script>

<template>
  <div class="flex gap-3 items-center">
    <FontAwesomeIcon
      icon="fa-solid fa-clock-rotate-left"
      class="text-stone-500 dark:text-stone-400"
    />
    <span class="font-bold">Projetos recentes</span>
  </div>

  <div class="flex flex-col w-full h-full justify-center items-center p-3">
    <span class=" w-1/2 text-center text-stone-500 dark:text-stone-400 text-sm">
      Não há projetos recentes para serem abertos
    </span>
  </div>
</template>
