<script setup lang="ts">
import { useRouter } from 'vue-router'

const $router = useRouter()
defineProps({
    label: {
        type: String,
        default: 'label'
    },
    icon: {
        type: String,
        default: 'icon'
    },
    route: {
        type: String,
        default: 'route'
    }
})


</script>

<template>
    <button
      class="flex flex-col text-white w-full h-20 justify-center items-center bg-lavenderIndigo-900 px-1 py-1 gap-3 rounded-md"
      @click="$router.push(route)">
      <FontAwesomeIcon 
        :icon="icon" 
        class="text-white text-xs h-5 w-5" />
      <span class="font-semibold text-center text-sm">{{ label }}</span>
    </button>
  </template>
  