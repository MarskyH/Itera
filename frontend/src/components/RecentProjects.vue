<script setup lang="ts">
import { onMounted, ref } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { useProjectStore } from "src/stores/ProjectStore";
import LocalStorage from "src/services/localStorage";

import { type models } from 'src/@types'
interface Project extends models.Project { }

defineProps<{
  content?: any
}>()

const storage = new LocalStorage();

const $projectStore = useProjectStore()
const recentProjects = ref<Project[]>([])

onMounted(async () => {
  let userId = storage.getLoggedUser()?.id || ''

  await $projectStore.fetchRecentProjectsByUser(userId)
    .then(() => {
      recentProjects.value = $projectStore.recentProjects
    })
})

</script>

<template>
  <div class="flex flex-col gap-3">
    <div class="flex gap-3 items-center">
      <FontAwesomeIcon
        icon="fa-solid fa-clock-rotate-left"
        class="text-stone-500 dark:text-stone-400"
      />

      <span class="font-bold">Projetos recentes</span>
    </div>

    <button
      v-for="project in recentProjects"
      :key="project.id"
      class="flex items-center text-sm p-2 gap-2 rounded dark:hover:bg-stone-700/50 hover:bg-stone-200/50"
      @click="() => $router.push({ name: 'project-info', params: { projectId: project.id } })"
    >
      <FontAwesomeIcon
        icon="fa-solid fa-folder"
        class="text-neutral-400"
      />

      {{ project.name }}
    </button>
  
    <div
      v-if="recentProjects.length === 0"
      class="flex flex-col w-full h-full justify-center items-center p-3"
    >
      <span class=" w-1/2 text-center text-stone-500 dark:text-stone-400 text-sm">
        Não há projetos recentes para serem abertos
      </span>
    </div>
  </div>
</template>
