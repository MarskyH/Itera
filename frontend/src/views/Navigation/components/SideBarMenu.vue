<script setup lang="ts">
import { computed, ref } from "vue";
import { useDark } from "@vueuse/core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

const isDark = useDark();

const menuExpanded = ref<boolean>(true);

import iteraIconDark from 'src/assets/iteraIconDark.svg'
import iteraIconLight from 'src/assets/iteraIconDark.svg'
import iteraLogoHorizontalDark from 'src/assets/iteraLogoHorizontalDark.svg'
import iteraLogoHorizontalLight from 'src/assets/iteraLogoHorizontalLight.svg'

const logoIconPath = computed(() =>
  isDark.value ? iteraIconDark : iteraIconLight
)

const logoPath = computed(() =>
  isDark.value ? iteraLogoHorizontalDark : iteraLogoHorizontalLight
)

const menuButtons = [
  {
    icon: "house",
    title: "Início",
    link: "home" 
  },
  {
    icon: "folder-open",
    title: "Meus projetos",
    link: "projects"
  },
  {
    icon: "folder-plus",
    title: "Novo projeto",
    link: "new-project"
  },
  {
    icon: "file-export",
    title: "Exportar relatório",
    link: "export-report"
  },
  {
    icon: "bell",
    title: "Notificações",
    link: "notifications"
  },
  {
    icon: "gear",
    title: "Configurações",
    link: "settings"
  },
];
</script>

<template>
  <Transition>
    <div
      v-show="true"
      class="hidden lg:flex flex-col shrink-0 h-full rounded-md p-3 bg-gradient-to-b from-white to-seaSalt-900 dark:bg-gradient-to-b dark:from-eerieBlackLight-900 dark:from-10% dark:to-eerieBlackDark-900 dark:to-90% menu-transition"
      :style="`width: ${menuExpanded ? '240px' : '72px'}`"
    >
      <div
        class="flex items-center gap-5 w-full h-fit rounded-md px-4 bg-periwinkle-900 dark:bg-charcoal-900"
        :class="[
          menuExpanded ? 'px-4' : 'px-4 py-3',
        ]"
      >
        <button :onClick="() => (menuExpanded = !menuExpanded)">
          <FontAwesomeIcon
            icon="fa-solid fa-bars"
            class="text-tropicalIndigo-900"
          />
        </button>

        <img
          v-if="menuExpanded"
          :src="logoPath"
          alt="Itera Logo"
          class="w-32 h-16"
        />
      </div>

      <div class="flex flex-col h-full w-full my-3 gap-2">
        <button
          v-for="menuButton in menuButtons"
          :key="menuButton.link"
          class="flex items-center rounded-md gap-4 p-3 hover:bg-gray-800/5 dark:hover:bg-gray-100/5 font-semibold"
          :class="[menuExpanded ? 'justify-start' : 'justify-center']"
          @click="$router.push({ name: menuButton.link })"
        >
          <img
            v-if="menuButton.link === 'home' && !menuExpanded"
            :src="logoIconPath"
            alt="Itera Home"
            class="w-5 h-5"
          />

          <FontAwesomeIcon
            v-else
            :icon="`fa-solid fa-${menuButton.icon}`"
            class="text-tropicalIndigo-900 w-5"
          />

          <Transition name="fade">
            <span
              v-show="menuExpanded"
              class="text-xs"
            >
              {{ menuButton.title }}
            </span>
          </Transition>
        </button>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.menu-transition {
  @apply transition-all ease-in-out duration-[0.25s];
}

.fade-enter-active {
  @apply transition-opacity duration-[1s] ease-in-out;
}
.fade-leave-active {
  @apply transition-opacity duration-[0.001s] ease-in-out;
}

.fade-enter-from,
.fade-leave-to {
  @apply opacity-0;
}
</style>
