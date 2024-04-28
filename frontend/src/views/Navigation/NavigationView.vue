<script setup lang="ts">

import { RouterView } from 'vue-router'
import PageHeader from 'src/views/Navigation/components/PageHeader.vue';
import SideBarMenu from 'src/views/Navigation/components/SideBarMenu.vue';
import { shallowRef } from 'vue';

const sideViewContent = shallowRef<any | undefined>(undefined)

</script>

<template>
  <div
    class="flex flex-col lg:flex-row min-w-[320px] overflow-auto h-screen items-start gap-2 p-2 bg-antiFlashWhite-900 text-neutral-700 dark:bg-night-900 dark:text-white"
  >
    <SideBarMenu />
    
    <div class="flex flex-col gap-2 h-full shrink-0 lg:shrink min-w-[320px] w-full">
      <PageHeader
        :title="String($route.meta.title)"
        :icon="String($route.meta.icon)"
      />

      <div class="flex flex-col w-full h-full p-3 rounded-md bg-gradient-to-b from-white to-seaSalt-900 dark:bg-gradient-to-b dark:from-eerieBlackLight-900 dark:from-10% dark:to-eerieBlackDark-900 dark:to-90%">
        <RouterView @side-view-content-change="(value: any) => sideViewContent = value" />
      </div>
    </div>

    <div class="flex flex-col lg:shrink-0 h-full w-full lg:w-[320px] rounded-md p-3 bg-gradient-to-b from-white to-seaSalt-900 dark:bg-gradient-to-b dark:from-eerieBlackLight-900 dark:from-10% dark:to-eerieBlackDark-900 dark:to-90%">
      <component
        :is="$route.meta.sideViewComponent"
        :content="sideViewContent"
      />
    </div>
  </div>
</template>