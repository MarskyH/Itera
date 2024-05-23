<script setup lang="ts">
import { ref } from 'vue';
import draggable from "vuedraggable";
import ActionGridItem from 'src/views/NewProject/components/ActionGridItem.vue';
import ProgressiveBar from './ProgressiveBar.vue';
import { useBacklogStore } from 'src/stores/BacklogStore';

const props = defineProps<{
  title: string
  tasks: Object[]
  order?: number
}>()

defineEmits(['titleClick'])

const tasksList = ref<Object[]>(props.tasks)

const $backlogStore = useBacklogStore()


async function updateRequirementOrder(evt: any) {
  await $backlogStore.updateBacklogRequirement(evt.draggedContext.element?.idRequirement, evt.draggedContext.futureIndex).then(()=>{
    console.log(evt.relatedContext.list)
  })
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
        v-model="tasksList"
        :move="updateRequirementOrder"
        group="people"
        @start="(drag: any) => drag = true"
        @end="(drag: any) => drag = false"
        item-key="id"
        animation="200"
        class="flex flex-col gap-2 min-w-64"
      >
        <template #item="{ element }">
          <ActionGridItem
            :icon="element.icon"
            :title="element.title"
            @edit="() => { }"
            @remove="() => { }"
            @side-view-content-change="() => () => { }"
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

              <ProgressiveBar :progress="element.progressiveBar" />
            </div>
          </ActionGridItem>
        </template>
      </draggable>
    </div>
  </div>
</template>