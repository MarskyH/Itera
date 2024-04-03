<script setup lang="ts">
import { useDark, useToggle } from '@vueuse/core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

const isDark = useDark()
const toggleDark = useToggle(isDark)
</script>

<template>
  <button
    class="flex w-10 h-10 p-3 justify-center items-center rounded-lg border-0 focus:border-2 border-slate-200 dark:border-slate-500 bg-stone-200/50 hover:bg-periwinkle-900/50 dark:bg-stone-700/50 dark:hover:bg-charcoal-900/50"
    @click="toggleDark()"
  >
    <FontAwesomeIcon
      :icon="`fa-solid fa-${isDark ? 'moon' : 'sun'}`"
      class="text-tropicalIndigo-900"
    />
  </button>
</template>
