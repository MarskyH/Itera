<script setup lang="ts">
import TaskList from './components/TaskList.vue';
import { useRoute } from "vue-router";
import { models } from "src/@types";
import { useBacklogStore } from "src/stores/BacklogStore";
import { onMounted, ref } from "vue";
interface BacklogRequirement extends models.BacklogRequirement {}


const backlogRequirements = ref<BacklogRequirement[]>([])
const onLoad = ref<boolean>(false)

const $backlogStore = useBacklogStore()
const $route = useRoute()

/*onMounted(async ()=>{
  onLoad.value = true

  await $backlogStore.fetchBacklog(String($route.params.projectId)).then(async () =>{
    backlogRequirements.value = $backlogStore.backlogRequirements;
    onLoad.value = false
  })
})*/

const tasks2 = [
  { id: '1', title: 'Planejamento', icon: 'users', priority: 'Alta', responsible: 'Indefinido' },
  { id: '2', title: 'Revisão', icon: 'users', priority: 'Alta', responsible: 'Indefinido' },
  { id: '3', title: 'Retrospectiva', icon: 'users', priority: 'Alta', responsible: 'Indefinido' },
]

</script>

<template>
  <div
    v-if="!onLoad"
    class="flex gap-3 grow shrink-0 overflow-auto"
  >
    <TaskList
      title="A fazer"
      :tasks="tasks2"
    />
    <TaskList
      title="Fazendo"
      :tasks="tasks2"
    />
    <TaskList
      title="Feito"
      :tasks="tasks2"
    />
    <TaskList
      title="Cancelado"
      :tasks="tasks2"
    />
  </div>
</template>