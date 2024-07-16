<script setup lang="ts">
import IterationTaskList from './components/IterationTaskList.vue';
import { useRoute, useRouter } from "vue-router";
import { models } from "src/@types";
import { onMounted, ref } from "vue";
import { useTaskStore } from 'src/stores/TaskStore';
interface Task extends models.Task { }


const iterationToDoTasks = ref<Task[]>([])
const onLoad = ref<boolean>(false)

const $taskStore = useTaskStore()
const $route = useRoute()
const $router = useRouter()

onMounted(async () => {
  onLoad.value = true

  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'A fazer').then(async () => {
    iterationToDoTasks.value = $taskStore.tasks;
    onLoad.value = false
  })
})

const tasks2 = [
  { id: '1', title: 'Planejamento', icon: 'users', priority: 'Alta', responsible: 'Indefinido', progressiveBar: 35 },
  { id: '2', title: 'Revisão', icon: 'users', priority: 'Alta', responsible: 'Indefinido', progressiveBar: 68 },
  { id: '3', title: 'Retrospectiva', icon: 'users', priority: 'Alta', responsible: 'Indefinido', progressiveBar: 13 },
]

</script>

<template>
  <div
    v-if="!onLoad"
    class="flex gap-3 grow shrink-0 overflow-auto"
  >
    <IterationTaskList
      title="A fazer"
      list-name="A fazer"
      :tasks="iterationToDoTasks"
      @title-click="() => $router.push({ name: 'new-iteration-task', params: { projectId: $route.params.projectId, iterationId: $route.params.iterationId } })"
    />

    <IterationTaskList
      title="Fazendo"
      list-name="Fazendo"
      :tasks="[]"
    />

    <IterationTaskList
      title="Feito"
      list-name="Feito"
      :tasks="[]"
    />
    <IterationTaskList
      title="Pendente"
      list-name="Pendente"
      :tasks="[]"
    />
    <IterationTaskList
      title="Cancelado"
      list-name="Cancelado"
      :tasks="[]"
    />
  </div>
</template>