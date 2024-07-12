<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import ModeToggleButton2 from "./ModeToggleButton2.vue";
import {useLoginStore} from "src/stores/LoginStore"

const $store = useLoginStore()

defineProps({
  title: {
    type: String,
    required: true
  },
  icon: {
    type: String,
    required: true
  }
})

function logout(){
  $store.userLogout()
  window.location.reload();
}

</script>

<template>
  <div class="flex items-center justify-between gap-5 p-3 rounded-md bg-white dark:bg-eerieBlackLight-900">
    <div class="flex items-center gap-5">
      <button class="flex lg:hidden p-2">
        <FontAwesomeIcon
          icon="fa-solid fa-bars"
          class="text-tropicalIndigo-900"
        />
      </button>

      <div class="flex items-center w-fit h-fit rounded-md p-4 bg-periwinkle-900 dark:bg-charcoal-900">
        <FontAwesomeIcon
          :icon="`fa-solid fa-${icon}`"
          class="text-tropicalIndigo-900"
        />
      </div>

      <div class="text-2xl font-bold">
        {{ title }}
      </div>
    </div>

    <div class="flex gap-3 mr-1">
      <ModeToggleButton2 />

      <button
        class="flex w-10 h-10 p-3 justify-center items-center rounded-lg border-0 focus:border-2 border-slate-200 dark:border-slate-500 bg-stone-200/50 hover:bg-periwinkle-900/50 dark:bg-stone-700/50 dark:hover:bg-charcoal-900/50"
      >
        <FontAwesomeIcon
          :icon="`fa-solid fa-bell`"
          class="text-tropicalIndigo-900"
        />
      </button>

      <button
        class="flex justify-center items-center w-10 h-10 rounded-md p-3 bg-periwinkle-900 dark:bg-charcoal-900"
        @click="logout()"
      >
        <FontAwesomeIcon
          :icon="`fa-solid fa-user`"
          class="text-tropicalIndigo-900"
        />
      </button>
    </div>
  </div>
</template>