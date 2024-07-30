<script setup lang="ts">
import IterationTaskList from './components/IterationTaskList.vue';
import { useRoute, useRouter } from "vue-router";
import { models } from "src/@types";
import { onMounted, ref } from "vue";
import { useTaskStore } from 'src/stores/TaskStore';
interface Task extends models.Task { }

const iterationToDoTasks = ref<Task[]>([])
const iterationDoingTasks = ref<Task[]>([])
const iterationDoneTasks = ref<Task[]>([])
const iterationCanceledTasks = ref<Task[]>([])
const iterationPendingTasks = ref<Task[]>([])

const onLoad = ref<boolean>(false)

const $taskStore = useTaskStore()
const $route = useRoute()
const $router = useRouter()

onMounted(async () => {
  onLoad.value = true

  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'A fazer').then(async () => {
    console.log('A fazer tasks:', $taskStore.tasks);
    iterationToDoTasks.value.splice(0, iterationToDoTasks.value.length, ...$taskStore.tasks);
    onLoad.value = false
  })
  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'Fazendo').then(async () => {
    console.log('Fazendo tasks:', $taskStore.tasks);
    iterationDoingTasks.value.splice(0, iterationDoingTasks.value.length, ...$taskStore.tasks);
    onLoad.value = false
  })
  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'Feito').then(async () => {
    console.log('Feito tasks:', $taskStore.tasks);
    iterationDoneTasks.value.splice(0, iterationDoneTasks.value.length, ...$taskStore.tasks);
    onLoad.value = false
  })
  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'Cancelado').then(async () => {
    console.log('Cancelado tasks:', $taskStore.tasks);
    iterationCanceledTasks.value.splice(0, iterationCanceledTasks.value.length, ...$taskStore.tasks);
    onLoad.value = false
  })
  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'Pendente').then(async () => {
    console.log('Pendente tasks:', $taskStore.tasks);
    iterationPendingTasks.value.splice(0, iterationPendingTasks.value.length, ...$taskStore.tasks);
    onLoad.value = false
  })
})

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
      :add-button="true"
    />
    <IterationTaskList
      title="Fazendo"
      list-name="Fazendo"
      :tasks="iterationDoingTasks"
    />
    <IterationTaskList
      title="Feito"
      list-name="Feito"
      :tasks="iterationDoneTasks"
    />
    <IterationTaskList
      title="Pendente"
      list-name="Pendente"
      :tasks="iterationPendingTasks"
    />
    <IterationTaskList
      title="Cancelado"
      list-name="Cancelado"
      :tasks="iterationCanceledTasks"
    />
  </div>
</template>