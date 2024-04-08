<script setup lang="ts">

import { RouterView } from 'vue-router'

import StepsBreadcrumbs from 'src/views/SelectedProject/components/StepsBreadcrumbs.vue';

</script>

<template>
  <div class="flex flex-col w-full h-full gap-5">
    <StepsBreadcrumbs />

    <RouterView />
  </div>
</template>