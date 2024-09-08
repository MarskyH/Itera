<script setup lang="ts">
import { ref, watch } from 'vue';
import draggable from "vuedraggable";
import ActionGridItem from 'src/views/NewProject/components/ActionGridItem.vue';
import ProgressiveBar from './ProgressiveBar.vue';
import { useBacklogStore } from 'src/stores/BacklogStore';



const props = defineProps<{
  title: string
  tasks: Object[]
  order?: number
  listId?: string
  disable?: boolean
}>()

defineEmits(['titleClick'])

const tasksList = ref<Object[]>(props.tasks)

const $backlogStore = useBacklogStore()

function updateRequirementOrder(requirementList: any[], sliceIndex: number) {
  requirementList.forEach(async (element: any, index: number) => {
    if (index >= sliceIndex) {
      await $backlogStore.updateBacklogRequirement(element?.id, index)
    }
  });
}

async function updateRequirementIteration(evt: any) {
  let movingRequirementId = evt.draggedContext.element.id
  let iterationId = evt.relatedContext.component.componentData.listId

  await $backlogStore.updateBacklogRequirementIteration(movingRequirementId, iterationId)
}

async function moveRequirement(evt: any) {
  setTimeout(async () => {
    let oldIndex = evt.draggedContext.index
    let newIndex = evt.relatedContext.index

    if(!newIndex) {      
      updateRequirementIteration(evt)
      updateRequirementOrder(tasksList.value, evt.draggedContext.futureIndex)
      updateRequirementOrder(evt.relatedContext.list, evt.draggedContext.futureIndex)

      return
    }
    
    if (oldIndex < newIndex) {
      updateRequirementOrder(tasksList.value, oldIndex)
    } else {
      updateRequirementOrder(tasksList.value, newIndex)
    }
  }, 500)
}


</script>

<template>
  <div class="flex flex-col h-fit gap-3 bg-whiteSmoke-900/50 dark:bg-jet-900/50 rounded p-3">
    <button
      class="flex gap-2 items-center text-lavenderIndigo-900 dark:text-tropicalIndigo-900 font-semibold"
      @click="() => order ? $emit('titleClick', order) : {}"
    >
      <FontAwesomeIcon icon="fa-solid fa-bars-staggered" />

      {{ title }}
    </button>

    <div class="flex max-h-[calc(100vh-200px)] flex-col gap-2 overflow-auto">
      <draggable
        :list="tasksList"
        :move="moveRequirement"
        group="people"
        :component-data="{ listId }"
        @start="(drag: any) => drag = true"
        @end="(drag: any) => drag = false"
        item-key="id"
        animation="200"
        class="flex flex-col gap-2 min-w-64"
      >
        <template #item="{ element }">
          <ActionGridItem
            icon="bookmark"
            :title="element.title"
            @edit="() => { }"
            @remove="() => { }"
            @side-view-content-change="() => () => { }"
            :class="{'opacity-50': element.checkCancelled}"
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
                Progresso
              </span>
              <ProgressiveBar :progress="element.progressiveBar" />
            </div>
          </ActionGridItem>
        </template>
      </draggable>
    </div>
  </div>
</template>