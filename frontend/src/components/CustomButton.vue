<script setup lang="ts">
import { useDark } from '@vueuse/core'

const isDark = useDark()

const props = defineProps({
    title: {
        type: String,
        required: true
    },
    color: {
        type: String,
        required: true
    },
    colorDark: {
        type: String,
        required: true
    }
})

</script>

<template>
  <button :class="`flex w-full items-center justify-center rounded-md my-4 px-4 py-2 text-white font-medium ${isDark ? props.colorDark : props.color}`">
    {{ props.title }}
  </button>
</template>