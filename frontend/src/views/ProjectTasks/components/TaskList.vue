<script setup lang="ts">
import ActionGridItem from 'src/views/NewProject/components/ActionGridItem.vue';


defineProps<{
  title: string
  tasks: Object[]
}>()

</script>

<template>
  <div class="flex flex-col h-fit gap-2 bg-whiteSmoke-900/60 dark:bg-jet-900/60 rounded p-5">
    {{ title }}

    <ActionGridItem
      v-for="task in tasks"
      :key="task.id"
      icon="bookmark"
      :title="task.title"
      @edit="() => {}"
      @remove="() => {}"
      @side-view-content-change="() => () => {}"
    >
      <div class="flex flex-col gap-1">
        <span class="text-sm font-semibold">
          Prioridade
        </span>

        <span class="text-xs text-stone-500 dark:text-stone-400">
          {{ task.priority }}
        </span>
      </div>

      <div class="flex flex-col gap-1">
        <span class="text-sm font-semibold">
          Responsável
        </span>

        <span class="text-xs text-stone-500 dark:text-stone-400">
          {{ task.responsible }}
        </span>
      </div>
    </ActionGridItem>
  </div>
</template>