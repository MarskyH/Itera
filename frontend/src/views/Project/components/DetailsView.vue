<script setup lang="ts">

defineProps<{
  content?: {
    component: any
    id: string
  }
}>()
 
</script>

<template>
  <component
    :is="content?.component"
    :id="content?.id"
  />
</template>
