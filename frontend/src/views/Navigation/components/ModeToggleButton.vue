<script setup lang="ts">
import { useDark, useToggle } from '@vueuse/core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

const isDark = useDark()
const toggleDark = useToggle(isDark)
</script>

<template>
  <button
    class="flex w-16 bg-slate-300 dark:bg-neutral-600 rounded-full border-2 transition-all ease-in-out border-slate-200"
    @click="toggleDark()"
  >
    <div
      class="flex w-8 h-8 rounded-full bg-white justify-center items-center dark:translate-x-8 transition-all ease-in "
    >
      <FontAwesomeIcon
        :icon="`fa-solid fa-${isDark ? 'moon' : 'sun'}`"
        class="text-stone-700"
      />
    </div>
  </button>
</template>
