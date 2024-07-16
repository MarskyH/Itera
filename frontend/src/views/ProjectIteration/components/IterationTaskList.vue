<script setup lang="ts">
import { ref } from 'vue';
import draggable from "vuedraggable";
import ActionGridItem from 'src/views/NewProject/components/ActionGridItem.vue';
import ProgressiveBar from './ProgressiveBar.vue';
import { useRoute, useRouter } from 'vue-router';
import { useTaskStore } from 'src/stores/TaskStore';

const props = defineProps<{
  title: string
  tasks: Object[]
  listName: string
}>()

const $taskStore = useTaskStore()

const $router = useRouter()
const $route = useRoute()

const tasksList = ref<Object[]>(props.tasks)

function openTaskForm(taskId: string) {
  $router.push({ 
    name: 'iteration-task-edit', 
    params: { 
      projectId: $route.params.projectId, 
      iterationId: $route.params.iterationId,
      taskId
    }
  })
}

async function updateTaskList(evt: any) {
  let movingTaskId = evt.draggedContext.element.id
  let taskListName = evt.relatedContext.component.componentData.listName

  await $taskStore.updateTaskListName(movingTaskId, taskListName)
}

function updateTaskOrder(list: any[], sliceIndex: number) {
  list.forEach(async (element: any, index: number) => {
    if (index >= sliceIndex) {
      await $taskStore.updateTaskOrder(element?.id, index)
    }
  });
}

async function moveTask(evt: any) {
  setTimeout(async () => {
    let oldIndex = evt.draggedContext.index
    let newIndex = evt.relatedContext.index

    if(!newIndex) {      
      updateTaskList(evt)
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
  <div class="flex flex-col h-fit gap-3 bg-whiteSmoke-900/50 dark:bg-jet-900/50 rounded p-3">
    <div class="flex gap-2 items-center text-lavenderIndigo-900 dark:text-tropicalIndigo-900 font-semibold">
      <FontAwesomeIcon
        icon="fa-solid fa-bars-staggered"
      />

      {{ title }}
    </div>

    <div class="flex max-h-[calc(100vh-200px)] flex-col gap-2 overflow-auto">
      <draggable
        :list="tasksList"
        :move="moveTask"
        :component-data="{ listName }"
        group="people"
        @start="(drag: any) => drag = true"
        @end="(drag: any) => drag = false"
        item-key="id"
        animation="200"
        class="flex flex-col gap-2 min-w-64"
      >
        <template #item="{element}">
          <ActionGridItem
            icon="bookmark"
            :title="element.title"
            @edit="() => {}"
            @remove="() => {}"
            @side-view-content-change="() => openTaskForm(element.id)"
          >
            <div class="flex flex-col gap-1">
              <span class="text-sm font-semibold">
                Prioridade
              </span>
        
              <span class="text-xs text-stone-500 dark:text-stone-400">
                {{ element.priority }}
              </span>
            </div>
      
            <div class="flex flex-col gap-1">
              <span class="text-sm font-semibold">
                Responsável
              </span>
        
              <span class="text-xs text-stone-500 dark:text-stone-400">
                {{ element.responsible }}
              </span>

              <ProgressiveBar :progress="0" />
            </div>
          </ActionGridItem>
        </template>
      </draggable>
    </div>
  </div>
</template>