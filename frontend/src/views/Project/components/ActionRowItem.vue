<script setup lang="ts">

defineProps<{
  title: string
}>()

defineEmits(['edit', 'remove'])

</script>

<template>
  <div class="flex gap-4 justify-between bg-whiteSmoke-900 dark:bg-jet-900 px-5 py-3 rounded">
    <div class="flex gap-3 items-center text-xs text-lavenderIndigo-900 dark:text-tropicalIndigo-900">
      <span>
        {{ title }}
      </span>
    </div>

    <div class="flex justify-end gap-3">
      <button
        class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-blueCrayola-900 dark:text-naplesYellow-900 hover:bg-blueCrayola-900/25 hover:dark:bg-naplesYellow-900/25 text-sm gap-2 text"
        @click="()=> $emit('edit')"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-pen"
        />
      </button>

      <button
        class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-lightRed-900 hover:bg-lightRed-900/25 hover:dark:bg-lightRed-900/25 text-sm gap-2 text"
        @click="()=> $emit('remove')"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-trash"
        />
      </button>
    </div>
  </div>
</template>