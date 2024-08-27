<script setup lang="ts">
import { models } from 'src/@types';
import { usePendencyStore } from 'src/stores/PendencyStore';

interface Pendency extends models.Pendency { }
interface PendencyOnUpdate extends models.PendencyOnUpdate { }

defineProps<Pendency>()

const $pendencyStore = usePendencyStore()

async function updateTaskPendency(pendencyId: string, taskId: string) {
  const pendencyData: PendencyOnUpdate = {
    status: false
  }

  await $pendencyStore.updatePendency(pendencyId, pendencyData).then(async (response: number) => {
    if(response === 200) {
      await $pendencyStore.fetchPendencies(taskId)
    } else {
      alert('Erro ao resolver pendência!')
    }
  })
}

</script>

<template>
  <div
    class="flex flex-col gap-3 my-3 p-4 bg-whiteSmoke-900 dark:bg-jet-900 rounded"
    :class="{'opacity-50': !status}"
  >
    <div
      class="flex gap-3 items-start justify-between"
    >
      <span class="text-sm font-semibold">
        {{ title }}
      </span>

      <button
        v-show="status"
        title="Nova tarefa"
        class="text-xs flex text-white justify-evenly items-center bg-emerald-500 p-2 rounded-md"
        @click="() => updateTaskPendency(id, task_id)"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-check"
        />
      </button>
    </div>

    <span class="text-xs text-justify">
      {{ description }}
    </span>
  </div>
</template>