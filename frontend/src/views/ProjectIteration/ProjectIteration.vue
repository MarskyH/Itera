<script setup lang="ts">
import IterationTaskList from './components/IterationTaskList.vue';
import { useRoute } from "vue-router";
import { models } from "src/@types";
import { onMounted, ref } from "vue";
import { useTaskStore } from 'src/stores/TaskStore';
interface Task extends models.Task { }

const iterationTasks = ref<{
  [key: string]: Task[],
}>({
  toDoTasks: [],
  doingTasks: [],
  doneTasks: [],
  canceledTasks: [],
  pendingTasks: []
})

defineEmits(['sideViewContentChange'])

const onLoad = ref<boolean>(false)

const $taskStore = useTaskStore()
const $route = useRoute()

const onError = ref<Boolean>(false)

const listNameKey: {[key: string]: string} = {
  'A fazer': 'toDoTasks',
  'Fazendo': 'doingTasks',
  'Feito': 'doneTasks',
  'Pendente': 'pendingTasks',
  'Canceled': 'canceledTasks'
}

function onCancelDrag(cancelEvent: { fromList: string, toList: string, task: Task }) {
    onError.value = true

    var taskIndex: number = iterationTasks.value[listNameKey[cancelEvent.toList]].indexOf(cancelEvent.task)

    while(taskIndex < 0) {
      taskIndex++
    }

    iterationTasks.value[listNameKey[cancelEvent.toList]].splice(taskIndex, 1)
    iterationTasks.value[listNameKey[cancelEvent.fromList]].push(cancelEvent.task)
}

onMounted(async () => {
  onLoad.value = true

  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'A fazer').then(async () => {
    console.log('A fazer tasks:', $taskStore.tasks);
    iterationTasks.value.toDoTasks.splice(0, iterationTasks.value.toDoTasks.length, ...$taskStore.tasks);
    onLoad.value = false
  })
  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'Fazendo').then(async () => {
    console.log('Fazendo tasks:', $taskStore.tasks);
    iterationTasks.value.doingTasks.splice(0, iterationTasks.value.doingTasks.length, ...$taskStore.tasks);
    onLoad.value = false
  })
  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'Feito').then(async () => {
    console.log('Feito tasks:', $taskStore.tasks);
    iterationTasks.value.doneTasks.splice(0, iterationTasks.value.doneTasks.length, ...$taskStore.tasks);
    onLoad.value = false
  })
  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'Cancelado').then(async () => {
    console.log('Cancelado tasks:', $taskStore.tasks);
    iterationTasks.value.canceledTasks.splice(0, iterationTasks.value.canceledTasks.length, ...$taskStore.tasks);
    onLoad.value = false
  })
  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'Pendente').then(async () => {
    console.log('Pendente tasks:', $taskStore.tasks);
    iterationTasks.value.pendingTasks.splice(0, iterationTasks.value.pendingTasks.length, ...$taskStore.tasks);
    onLoad.value = false
  })
})

</script>

<template>
  <div
    class="absolute flex gap-3 items-center bottom-0 right-0 bg-red-500/80 rounded p-3 border-[1px] border-red-400 m-4 text-white"
    v-show="onError"
  >
    <FontAwesomeIcon
      icon="fa-solid fa-triangle-exclamation"
    />

    <span class="text-sm">
      Não é possível mover para essa lista
    </span>

    <button
      class="hover:bg-red-500/50 px-[6px] rounded-full" 
      @click="() => onError = false"
    >
      <FontAwesomeIcon
        icon="fa-solid fa-xmark"
      />
    </button>
  </div>

  <div
    v-if="!onLoad"
    class="flex gap-3 grow shrink-0 overflow-auto"
  >
    <IterationTaskList
      title="A fazer"
      list-name="A fazer"
      :tasks="iterationTasks.toDoTasks"
      :add-button="true"
      on-error="false"
      @on-cancel-drag="onCancelDrag"
    />
    <IterationTaskList
      title="Fazendo"
      list-name="Fazendo"
      :tasks="iterationTasks.doingTasks"
      on-error="false"
      @on-cancel-drag="onCancelDrag"
    />
    <IterationTaskList
      title="Feito"
      list-name="Feito"
      :tasks="iterationTasks.doneTasks"
      on-error="false"
      @on-cancel-drag="onCancelDrag"
    />
    <IterationTaskList
      title="Pendente"
      list-name="Pendente"
      :tasks="iterationTasks.pendingTasks"
      on-error="false"
      @on-cancel-drag="onCancelDrag"
    />
    <IterationTaskList
      title="Cancelado"
      list-name="Cancelado"
      :tasks="iterationTasks.canceledTasks"
      on-error="false"
      @on-cancel-drag="onCancelDrag"
    />
  </div>
</template>