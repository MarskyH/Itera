<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { useProjectStore } from "src/stores/ProjectStore";
import { onMounted, ref } from "vue";

const $projectStore = useProjectStore()

const myProjects = ref<any[]>([])


onMounted(async () => {
  await $projectStore.fetchProjects().then(() => {
    myProjects.value = $projectStore.projects

    console.log(myProjects.value)
  })
})

</script>

<template>
  <div class="flex flex-col w-full h-full items-center gap-4">
    <div
      v-for="project in myProjects"
      :key="project.id"
      class="flex w-full gap-4 items-center bg-whiteSmoke-900 dark:bg-jet-900 rounded p-5"
    >
      <FontAwesomeIcon
        :icon="`fa-solid fa-folder`"
      />

      {{ project.name }}
    </div>
  </div>
</template>

