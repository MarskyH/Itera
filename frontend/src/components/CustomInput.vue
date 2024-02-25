<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
const props = defineProps({
  title: String,
  placeholder: String,
  icon: String
})
</script>

<template>
  <div class="flex w-1/2 flex-col items-start my-2">
    <label class="text-sm font-medium text-black dark:text-oldSilver-900" for="input-id">{{
      props.title
    }}</label>
    <div class="relative mt-2 flex items-center w-full">
      <input
        id="input-id"
        class="border-2 border-gray-300 rounded-xl w-full pl-8 pr-4 py-2 bg-white dark:bg-blackOlive-900 text-quickSilver-900 focus:border-blue-500"
        type="text"
        :placeholder="props.placeholder"
      />
      <span class="absolute left-3 text-silverChalice-900">
        <font-awesome-icon :icon="`fa-solid fa-${props.icon}`" />
      </span>
    </div>
  </div>
</template>
