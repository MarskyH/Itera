<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { models } from "src/@types";
import { useProjectStore } from "src/stores/ProjectStore";
import { useRoleStore } from "src/stores/RoleStore";

interface ProjectOnView extends models.ProjectOnView {}
interface Role extends models.Role {}

defineProps<{
  content: {[key: string]: string}
}>()

const projectDefault = {
  id: "",
  name: "",
  deadline: 0,
  workHours: 0,
  iterationTime: 0,
  clientName: "",
  createdBy: "",
  roles: []
}

const $projectStore = useProjectStore()
const $roleStore = useRoleStore()

const $route = useRoute()

const project = ref<ProjectOnView>({...projectDefault})

onMounted(async () => {
  await $projectStore.fetchProject(String($route.params.projectId)).then(async () => {
    project.value = { 
      ...project.value,
      ...$projectStore.project,
    }
    
    await $roleStore.fetchRoles(String($route.params.projectId)).then(async () => {
      project.value.roles = $roleStore.roles
    })
  })
})
</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="flex gap-3 items-center">
      <FontAwesomeIcon
        icon="fa-solid fa-circle-info"
        class="text-stone-500 dark:text-stone-400"
      />

      <span class="font-bold">Detalhes do projeto</span>
    </div>

    {{ content }}

    <div class="flex flex-col text-xs p-1 gap-4">
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-folder"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Nome</span>

          <span>{{ project.name }}</span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-handshake-simple"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Cliente</span>

          <span>{{ project.clientName }}</span>
        </div>
      </div>
          
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-clock"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Prazo</span>

          <span>{{ project.deadline }}</span>
        </div>
      </div>

      <div class="flex gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-hourglass-half"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Tempo de iteração</span>

          <span>{{ project.iterationTime }}</span>
        </div>
      </div>
          
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-business-time"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Carga horária</span>

          <span>{{ project.workHours }}</span>
        </div>
      </div>
    </div>
    
    <div class="flex justify-end gap-5">
      <button
        class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-blueCrayola-900 dark:text-naplesYellow-900 hover:bg-blueCrayola-900/25 hover:dark:bg-naplesYellow-900/25 text-sm gap-2 text"
        @click="()=> $emit('edit')"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-pen"
        />

        <span class="font-semibold">Editar</span>
      </button>

      <button
        class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-lightRed-900 hover:bg-lightRed-900/25 hover:dark:bg-lightRed-900/25 text-sm gap-2 text"
        @click="()=> $emit('remove')"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-trash"
        />

        <span class="font-semibold">Excluir projeto</span>
      </button>
    </div>
  </div>
</template>
