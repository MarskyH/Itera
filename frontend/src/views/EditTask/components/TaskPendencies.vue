<script setup lang="ts">
import { onMounted, ref } from 'vue';
import TaskPendencyForm from 'src/views/EditTask/components/TaskPendencyForm.vue'
import TaskPendency from 'src/views/EditTask/components/TaskPendency.vue'
import { models } from 'src/@types';
import { useRoute } from 'vue-router';
import { usePendencyStore } from 'src/stores/PendencyStore';

interface Pendency extends models.Pendency { }

const $route = useRoute()
const $pendencyStore = usePendencyStore()

const onCreateTaskPendency = ref<boolean>(false)
const taskPendencies = ref<Pendency[]>([])

onMounted(async () => {
  await $pendencyStore.fetchPendencies(String($route.params.taskId))
})

$pendencyStore.$subscribe(() => {
  taskPendencies.value = $pendencyStore.pendencies
})


</script>

<template>
  <div class="flex flex-col gap-3 overflow-y-auto">
    <div class="flex gap-3 items-center justify-between">
      <div class="flex gap-3 items-center">
        <FontAwesomeIcon
          icon="fa-solid fa-hourglass"
          class="text-stone-500 dark:text-stone-400"
        />

        <span class="font-bold">Pendências</span>
      </div>

      <button
        v-show="!onCreateTaskPendency"
        title="Nova tarefa"
        class="text-xs flex text-white justify-evenly items-center bg-lavenderIndigo-900 p-2 rounded-md"
        @click="() => onCreateTaskPendency = true"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-plus"
        />
      </button>
    </div>

    <TaskPendencyForm 
      v-model:on-create-task-pendency="onCreateTaskPendency"
    />

    <div>
      <TaskPendency
        v-for="pendency in taskPendencies"
        :key="pendency.id"
        v-bind="pendency"
      />
    </div>
  </div>
</template>