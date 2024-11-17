<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { useProjectStore } from "src/stores/ProjectStore";
import { onMounted, ref } from "vue";
import LocalStorage from 'src/services/localStorage'

const $projectStore = useProjectStore()

const myProjects = ref<any[]>([])

const storage = new LocalStorage();


let userRole = storage.getLoggedUser()?.role || ''

let userId = storage.getLoggedUser()?.id || ''

const ilustracao = "src/assets/ilustracaoNovoProjeto.png"
onMounted(async () => {
if(userRole !== 'Gerente'){
  await $projectStore.fetchProjectsByMember(userId).then(() =>{
    myProjects.value = $projectStore.projects
  })
}else{
  await $projectStore.fetchProjectsByUser(userId).then(() => {
    myProjects.value = $projectStore.projects
  })
}})

</script>

<template>
  <div class="flex flex-col w-full h-full items-center justify-center gap-8">
    <template v-if="myProjects.length === 0">
      <img
        :src="ilustracao"
        alt="Ilustração Novo Projeto"
        class="shrink-0 w-40 h-40"
      >
      <span class="w-1/2 text-center text-stone-500 dark:text-stone-400">
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
    </template>

    <template v-else>
      <img
        :src="ilustracao"
        alt="Ilustração Novo Projeto"
        class="shrink-0 w-40 h-40"
      >
      <span class="w-1/2 text-center text-stone-500 dark:text-stone-400">
        Há projetos ativos. Use a função “Meus projetos” e acesse suas informações.
      </span>
      <button
        class="flex items-center bg-gradient-to-br from-40% from-lavenderIndigo-900 to-tropicalIndigo-900 p-4 gap-4 rounded-md"
        @click="$router.push({ name: 'my-projects' })"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-folder"
          class="text-white"
        />
        <span class="font-semibold text-white">
          Meus projetos
        </span>
      </button>
    </template>
  </div>
</template>


