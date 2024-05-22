<script setup lang="ts">
import ActionGridItem from 'src/views/NewProject/components/ActionGridItem.vue';


defineProps<{
  title: string
  tasks: Object[]
}>()

</script>

<template>
  <div class="flex flex-col h-fit gap-3 bg-whiteSmoke-900/50 dark:bg-jet-900/50 rounded p-3">
    <div class="flex gap-2 items-center text-lavenderIndigo-900 dark:text-tropicalIndigo-900 font-semibold">
      <FontAwesomeIcon
        icon="fa-solid fa-bars-staggered"
      />

      {{ title }}
    </div>

    <div class="flex max-h-[calc(100vh-200px)] flex-col gap-2 overflow-auto">
      <ActionGridItem
        v-for="task in tasks"
        :key="task.id"
        :icon="task.icon"
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
  </div>
</template>