<script setup lang="ts">

defineProps<{
  icon: string
  title: string
}>()

defineEmits(['edit', 'remove'])

</script>

<template>
  <div class="flex flex-col gap-4 justify-between bg-whiteSmoke-900 dark:bg-jet-900 rounded p-5">
    <div class="flex flex-col gap-4">
      <div class="flex gap-3 items-baseline text-lavenderIndigo-900 dark:text-tropicalIndigo-900">
        <FontAwesomeIcon
          :icon="`fa-solid fa-${icon}`"
        />
      
        <span class="font-semibold">
          {{ title }}
        </span>
      </div>

      <slot />
    </div>

    <div class="flex justify-end gap-5">
      <button
        class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-blueCrayola-900 dark:text-naplesYellow-900 hover:bg-blueCrayola-900/25 hover:dark:bg-naplesYellow-900/25 text-sm gap-2 text"
        @click="()=> $emit('edit')"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-pen"
        />

        <span class="font-semibold">Editar</span>
      </button>

      <button
        class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-lightRed-900 hover:bg-lightRed-900/25 hover:dark:bg-lightRed-900/25 text-sm gap-2 text"
        @click="()=> $emit('remove')"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-trash"
        />

        <span class="font-semibold">Remover</span>
      </button>
    </div>
  </div>
</template>