<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

import ModeToggleButton2 from "@/components/ModeToggleButton2.vue";

</script>

<template>
  <div
    class="flex items-center justify-between w-full rounded-md p-3 gap-3 bg-white dark:bg-eerieBlackLight-900"
  >
    <div class="flex items-center gap-3">
      <button
        class="flex w-10 h-10 p-3 justify-center items-center rounded-lg border-0 focus:border-2 border-slate-200 dark:border-slate-500 bg-stone-200/50 hover:bg-periwinkle-900/50 dark:bg-stone-700/50 dark:hover:bg-charcoal-900/50"
      >
        <FontAwesomeIcon
          :icon="`fa-solid fa-bell`"
          class="text-tropicalIndigo-900"
        />
      </button>

      <ModeToggleButton2 />
    </div>
    
    <div class="flex items-center gap-3">
      <span class="text-xs font-semibold">
        Marcus Loureiro
      </span>

      <div
        class="flex items-center w-10 h-10 rounded-md p-3 bg-periwinkle-900 dark:bg-charcoal-900"
      >
        <FontAwesomeIcon
          :icon="`fa-solid fa-user`"
          class="text-tropicalIndigo-900"
        />
      </div>
    </div>
  </div>
</template>
