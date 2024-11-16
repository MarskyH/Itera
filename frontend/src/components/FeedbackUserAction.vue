<script setup lang="ts">
const props = defineProps<{
  text: String
  onError: Boolean
  isVisible: Boolean 
}>()

defineEmits(['update:modelValue', 'close', 'confirm', 'update:onError', 'update:isVisible'])
</script>

<template>
  <div
    v-show="props.isVisible"
    :class="[
      'absolute flex gap-3 items-center bottom-0 right-0 rounded p-3 border-[1px] m-4 text-white',
      props.onError ? 'bg-red-500/80 border-red-400' : 'bg-green-500/80 border-green-400'
    ]">
    <FontAwesomeIcon :icon="props.onError ? 'fa-solid fa-triangle-exclamation' : 'fa-solid fa-circle-check'" />

    <span class="text-sm">
      {{ props.text }}
    </span>

    <button 
      class="hover:opacity-75 px-[6px] rounded-full" 
      @click="$emit('update:isVisible', false)"> 
      <FontAwesomeIcon icon="fa-solid fa-xmark" />
    </button>
  </div>
</template>
