<script setup lang="ts">
import { models } from 'src/@types';

interface Pendency extends models.Pendency { }

defineProps<Pendency>()

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
        @click="() => {}"
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