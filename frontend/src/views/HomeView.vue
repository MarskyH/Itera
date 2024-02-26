<script setup lang="ts">
import SideBarMenu from "../components/SideBarMenu.vue";
import MainContent from "src/components/MainContent.vue";
import SideContent from "src/components/SideContent.vue";
import UserOptions from "src/components/UserOptions.vue";

</script>

<template>
  <div
    class="flex h-screen items-start gap-2 p-2 bg-antiFlashWhite-900 text-neutral-700 dark:bg-night-900 dark:text-white"
  >
    <SideBarMenu />

    <MainContent />

    <div class="flex shrink-0 flex-col h-full w-[320px] gap-2">
      <UserOptions />

      <SideContent />
    </div>
  </div>
</template>
