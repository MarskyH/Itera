<script setup lang="ts">
import TaskList from './components/TaskList.vue';
import { useRoute } from "vue-router";
import { models } from "src/@types";
import { useBacklogStore } from "src/stores/BacklogStore";
import { useIterationStore } from "src/stores/IterationStore";
import { onMounted, ref } from "vue";
interface BacklogRequirement extends models.BacklogRequirement { }
interface Iteration extends models.Iteration { }

const onLoad = ref<boolean>(false)

const backlogRequirements = ref<BacklogRequirement[]>([])
const iterations = ref<Iteration[]>([])


const $backlogStore = useBacklogStore()
const $iterationStore = useIterationStore()

const $route = useRoute()

onMounted(async () => {
  onLoad.value = true

  await $backlogStore.fetchBacklog(String($route.params.projectId)).then(async () => {
    await $iterationStore.fetchIterations(String($route.params.projectId)).then(async () => {
      backlogRequirements.value = $backlogStore.backlogRequirements;
      iterations.value = $iterationStore.iterations;
      console.log(iterations.value)
      onLoad.value = false
    })
  })
})


</script>

<template>
  <div v-if="!onLoad" class="flex gap-3 grow shrink-0 overflow-auto">
    <TaskList title="Backlog" :tasks="backlogRequirements" />

    <TaskList v-for="(iteration, index) in iterations" :key="iteration.id" :list-id="iteration.id"
      :title="`Iteração ${index + 1}`" title-link="project-iteration" :tasks="iteration.requirements" :order="index + 1"
      @title-click="() => $router.push({ name: 'project-iteration', params: { iterationId: iteration.id } })" />
  </div>
</template>