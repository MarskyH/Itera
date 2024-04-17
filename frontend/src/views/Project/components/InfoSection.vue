<script setup lang="ts">

defineProps<{
  title: string
  icon: string
}>()

</script>

<template>
  <div
    class="flex flex-col w-full gap-1"
  >
    <div class="flex gap-4 items-center font-semibold text-sm py-5 px-2">
      <FontAwesomeIcon
        :icon="`fa-solid fa-${icon}`"
      />
  
      {{ title }}
    </div>

    <slot />
  </div>
</template>