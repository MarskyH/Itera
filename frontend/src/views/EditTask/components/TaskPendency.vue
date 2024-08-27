<script setup lang="ts">

defineProps<{
  resolved: boolean
}>()

</script>

<template>
  <div
    class="flex flex-col gap-3 my-3 p-4 bg-whiteSmoke-900 dark:bg-jet-900 rounded"
    :class="{'opacity-50': resolved}"
  >
    <div
      class="flex gap-3 items-start justify-between"
    >
      <span class="text-sm font-semibold">
        Título
      </span>

      <button
        v-show="!resolved"
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
      Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum
    </span>
  </div>
</template>