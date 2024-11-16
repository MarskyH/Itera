<script setup lang="ts">
import IterationTaskList from './components/IterationTaskList.vue';
import FeedbackUserAction from '../../components/FeedbackUserAction.vue';
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
const isVisible = ref<Boolean>(false)
const textResult = ref<String>("Tarefa movida com sucesso")

const listNameKey: {[key: string]: string} = {
  'A fazer': 'toDoTasks',
  'Fazendo': 'doingTasks',
  'Feito': 'doneTasks',
  'Pendente': 'pendingTasks',
  'Canceled': 'canceledTasks'
}

function onCancelDrag(cancelEvent: { fromList: string, toList: string, task: Task }) {
    onError.value = true
    isVisible.value = true
    textResult.value = "Não é possível mover para essa lista"

    var taskIndex: number = iterationTasks.value[listNameKey[cancelEvent.toList]].indexOf(cancelEvent.task)

    while(taskIndex < 0) {
      taskIndex++
    }

    iterationTasks.value[listNameKey[cancelEvent.toList]].splice(taskIndex, 1)
    iterationTasks.value[listNameKey[cancelEvent.fromList]].push(cancelEvent.task)
}

function onConfirmDrag(confirmEvent: { fromList: string, toList: string, task: Task }) {
    onError.value = false
    isVisible.value = true
    textResult.value = "Tarefa movida com sucesso da lista '" + confirmEvent.fromList + "' para a lista '"  + confirmEvent.toList + "'" 
}


onMounted(async () => {
  onLoad.value = true

  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'A fazer').then(async () => {
    iterationTasks.value.toDoTasks.splice(0, iterationTasks.value.toDoTasks.length, ...$taskStore.tasks);
    onLoad.value = false
  })
  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'Fazendo').then(async () => {
    iterationTasks.value.doingTasks.splice(0, iterationTasks.value.doingTasks.length, ...$taskStore.tasks);
    onLoad.value = false
  })
  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'Feito').then(async () => {
    iterationTasks.value.doneTasks.splice(0, iterationTasks.value.doneTasks.length, ...$taskStore.tasks);
    onLoad.value = false
  })
  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'Cancelado').then(async () => {
    iterationTasks.value.canceledTasks.splice(0, iterationTasks.value.canceledTasks.length, ...$taskStore.tasks);
    onLoad.value = false
  })
  await $taskStore.fetchIterationTasks(String($route.params.iterationId), 'Pendente').then(async () => {
    iterationTasks.value.pendingTasks.splice(0, iterationTasks.value.pendingTasks.length, ...$taskStore.tasks);
    onLoad.value = false
  })
})

</script>

<template>
  <FeedbackUserAction
    :text="textResult" 
    :onError="onError" 
    :isVisible="isVisible" 
    @update:isVisible="isVisible = $event" 
  />
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
      @on-confirm-drag="onConfirmDrag"
    />
    <IterationTaskList
      title="Fazendo"
      list-name="Fazendo"
      :tasks="iterationTasks.doingTasks"
      on-error="false"
      @on-cancel-drag="onCancelDrag"
      @on-confirm-drag="onConfirmDrag"
    />
    <IterationTaskList
      title="Feito"
      list-name="Feito"
      :tasks="iterationTasks.doneTasks"
      on-error="false"
      @on-cancel-drag="onCancelDrag"
      @on-confirm-drag="onConfirmDrag"
    />
    <IterationTaskList
      title="Pendente"
      list-name="Pendente"
      :tasks="iterationTasks.pendingTasks"
      on-error="false"
      @on-cancel-drag="onCancelDrag"
      @on-confirm-drag="onConfirmDrag"
    />
    <IterationTaskList
      title="Cancelado"
      list-name="Cancelado"
      :tasks="iterationTasks.canceledTasks"
      on-error="false"
      @on-cancel-drag="onCancelDrag"
      @on-confirm-drag="onConfirmDrag"
    />
  </div>
</template>