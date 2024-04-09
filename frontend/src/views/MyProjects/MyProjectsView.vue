<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { useProjectStore } from "src/stores/ProjectStore";
import { onMounted, ref } from "vue";

const $projectStore = useProjectStore()

const myProjects = ref<any[]>([])


onMounted(async () => {
  await $projectStore.fetchProjects().then(() => {
    myProjects.value = $projectStore.projects
  })
})

</script>

<template>
  <div class="flex flex-col w-full h-full items-center gap-4">
    <div
      v-for="project in myProjects"
      :key="project.id"
      class="flex flex-col w-full gap-4 bg-whiteSmoke-900 dark:bg-jet-900 rounded p-5"
    >
      <div class="flex gap-2 items-center">
        <FontAwesomeIcon
          icon="fa-solid fa-folder"
        />
  
        {{ project.name }}
      </div>
      
      <div class="flex gap-4">
        <div class="flex gap-2 items-center text-neutral-400 text-xs">
          <FontAwesomeIcon
            icon="fa-solid fa-handshake-simple"
          />
          
          {{ project.clientName }}
        </div>
  
        <div class="flex gap-2 items-center text-neutral-400 text-xs">
          <FontAwesomeIcon
            icon="fa-solid fa-clock"
          />
    
          {{ `${project.deadline} ${project.deadline === 1 ? 'dia' : 'dias'}` }}
        </div>
      </div>

      <div class="flex justify-end gap-5 text-xs">
        <button
          class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-tropicalIndigo-900 hover:bg-tropicalIndigo-900/25 hover:dark:bg-tropicalIndigo-900/25 gap-2"
          @click="()=> $router.push({ name: 'project-info', params: { projectId: project.id } })"
        >
          <FontAwesomeIcon
            icon="fa-solid fa-folder-open"
          />

          <span class="font-semibold">Abrir</span>
        </button>

        <button
          class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-blueCrayola-900 dark:text-naplesYellow-900 hover:bg-blueCrayola-900/25 hover:dark:bg-naplesYellow-900/25 gap-2 text"
          @click="()=> $emit('edit')"
        >
          <FontAwesomeIcon
            icon="fa-solid fa-pen"
          />

          <span class="font-semibold">Editar</span>
        </button>

        <button
          class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-lightRed-900 hover:bg-lightRed-900/25 hover:dark:bg-lightRed-900/25 gap-2 text"
          @click="()=> $emit('remove')"
        >
          <FontAwesomeIcon
            icon="fa-solid fa-trash"
          />

          <span class="font-semibold">Remover</span>
        </button>
      </div>
    </div>
  </div>
</template>

