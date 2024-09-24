<script setup lang="ts">
import { ref } from "vue";
import { models } from "src/@types";
import { useActivityStore } from "src/stores/ActivityStore";

interface Activity extends models.Activity {}

const activityDefault: Activity = {
  id: '',
  title: '',
  description: '',
  priority: '',
  type: '',
}

const $activityStore = useActivityStore()

const activity = ref<Activity>({...activityDefault})

$activityStore.$subscribe(() => {
  activity.value = {...$activityStore.activity}
})

</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="flex gap-3 items-center">
      <FontAwesomeIcon
        icon="fa-solid fa-triangle-exclamation"
        class="text-stone-500 dark:text-stone-400"
      />

      <span class="font-bold">Detalhes da ação</span>
    </div>

    <div class="flex flex-col text-xs p-1 gap-4">
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-circle-info"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Título</span>

          <span> {{ activity.title }} </span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-fire"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Prioridade</span>

          <span>{{ activity.priority }}</span>
        </div>
      </div>
      
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-shield"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Tipo da ação</span>

          <span>{{ activity.type }}</span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-fire-extinguisher"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Descrição da ação</span>

          <span>{{ activity.description }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
