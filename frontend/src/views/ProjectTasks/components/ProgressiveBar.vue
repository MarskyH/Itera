<script setup lang="ts">
const props = defineProps<{
    progress: number;
}>();
</script>

<template>
  <div class="flex items-center h-fit gap-1 bg-whiteSmoke-900/50 dark:bg-jet-900/50">
    <span class="text-xs text-stone-500 dark:text-stone-400">{{ props.progress !== undefined ? props.progress : 0 }}%</span>
    <div class="flex-1 h-[10px] bg-[#c0c3d2] rounded-[10px] overflow-hidden ml-[2px]">
      <div
        :style="{ width: props.progress !== undefined ? props.progress + '%' : '0%' }"
        class="h-full rounded l-3 bg-lavenderIndigo-900 dark:bg-tropicalIndigo-900 transition-all duration-300 ease"
      />
    </div>
  </div>
</template>
