<script setup lang="ts">

defineProps<{
  content?: {
    component: any
  }
}>()
 
</script>

<template>
  <component
    :is="content?.component"
  />
</template>
