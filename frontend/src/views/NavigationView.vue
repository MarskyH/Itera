<script setup lang="ts">

import { RouterView, useRoute } from 'vue-router'
import SideBarMenu from "../components/SideBarMenu.vue";
import RecentProjects from "src/components/RecentProjects.vue";
import UserOptions from "src/components/UserOptions.vue";

</script>

<template>
  <div
    class="flex h-screen items-start gap-2 p-2 bg-antiFlashWhite-900 text-neutral-700 dark:bg-night-900 dark:text-white"
  >
    <SideBarMenu />

    <RouterView />

    <div class="flex shrink-0 flex-col h-full w-[320px] gap-2">
      <UserOptions />
      <component :is="RecentProjects" />
    </div>
  </div>
</template>