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

function updateTaskOrder(taskList: any[], sliceIndex: number) {
  taskList.forEach(async (element: any, index: number) => {
    if (index >= sliceIndex) {
      await $taskStore.updateTaskOrder(element?.id, index)
    }
  });
}

async function updateListName(evt: any) {
  let movingTaskId = evt.draggedContext.element.id
  let listName = evt.relatedContext.component.componentData.listId

  await $taskStore.updateTaskListName(movingTaskId, listName)
}

async function moveTask(evt: any) {
  setTimeout(async () => {
    let oldIndex = evt.draggedContext.index
    let newIndex = evt.relatedContext.index

    if(!newIndex) {      
      updateListName(evt)
      updateTaskOrder(tasksList.value, evt.draggedContext.futureIndex)
      updateTaskOrder(evt.relatedContext.list, evt.draggedContext.futureIndex)

      return
    }
    
    if (oldIndex < newIndex) {
      updateTaskOrder(tasksList.value, oldIndex)
    } else {
      updateTaskOrder(tasksList.value, newIndex)
    }
  }, 500)
}


</script>

<template>
  <div
    v-if="!onLoad"
    class="flex gap-3 grow shrink-0 overflow-auto"
  >
    <IterationTaskList
      title="A fazer"
      :tasks="iterationToDoTasks"
      @title-click="() => $router.push({ name: 'new-iteration-task', params: { projectId: $route.params.projectId, iterationId: $route.params.iterationId } })"
    />

    <IterationTaskList
      title="Fazendo"
      :tasks="[]"
    />

    <IterationTaskList
      title="Feito"
      :tasks="[]"
    />
    <IterationTaskList
      title="Pendente"
      :tasks="[]"
    />
    <IterationTaskList
      title="Cancelado"
      :tasks="[]"
    />
  </div>
</template>