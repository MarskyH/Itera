<script setup lang="ts">

const toolButtons = [
  { icon: 'clipboard-list', title: 'Tarefas', link: 'project-iterations' },
  { icon: 'hourglass-half', title: 'Cronograma', link: '' },
  { icon: 'piggy-bank', title: 'Orçamento', link: '' },
  { icon: 'square-poll-vertical', title: 'Relatório', link: '' }
]

</script>

<template>
  <div class="flex gap-3">
    <button
      v-for="toolButton in toolButtons"
      :key="toolButton.icon"
      class="flex flex-col w-full rounded items-center gap-2 p-4 bg-platinum-900 dark:bg-davysGray-900 text-sm font-semibold"
      @click="$router.push({ name: 'project-iterations' })"
    >
      <FontAwesomeIcon
        :icon="`fa-solid fa-${toolButton.icon}`"
      />

      {{ toolButton.title }}
    </button>
  </div>
</template>