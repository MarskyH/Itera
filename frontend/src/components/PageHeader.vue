<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

defineProps({
  title: {
    type: String,
    required: true
  },
  icon: {
    type: String,
    required: true
  }
})

</script>

<template>
  <div class="flex items-center gap-5 p-3">
    <div
      class="flex items-center w-fit h-fit rounded-md p-4 bg-periwinkle-900 dark:bg-charcoal-900"
    >
      <FontAwesomeIcon
        :icon="`fa-solid fa-${icon}`"
        class="text-tropicalIndigo-900"
      />
    </div>

    <div class="text-2xl font-bold">
      {{ title }}
    </div>
  </div>
</template>