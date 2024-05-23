<script setup lang="ts">
import TaskList from './components/TaskList.vue';
import { useRoute } from "vue-router";
import { models } from "src/@types";
import { useBacklogStore } from "src/stores/BacklogStore";
import { onMounted, ref } from "vue";
interface BacklogRequirement extends models.BacklogRequirement {}

const onLoad = ref<boolean>(false)

const backlogRequirements = ref<BacklogRequirement[]>([])

const $backlogStore = useBacklogStore()
const $route = useRoute()

onMounted(async ()=>{
  onLoad.value = true

  await $backlogStore.fetchBacklog(String($route.params.projectId)).then(async () =>{
    backlogRequirements.value = $backlogStore.backlogRequirements;
    onLoad.value = false
  })
})

const tasks2 = [
  { id: '1', title: 'Planejamento', icon: 'users', priority: 'Alta', responsible: 'Indefinido', progressiveBar: 20 },
  { id: '2', title: 'Revisão', icon: 'users', priority: 'Alta', responsible: 'Indefinido',},
  { id: '2', title: 'Retrospectiva', icon: 'users', priority: 'Alta', responsible: 'Indefinido',  progressiveBar: 50 },
]

</script>

<template>
  <div
    v-if="!onLoad"
    class="flex gap-3 grow shrink-0 overflow-auto"
  >
    <TaskList
      title="Backlog"
      :tasks="backlogRequirements"
    />
    <TaskList
      title="Iteração 1"
      title-link="project-iteration"
      :tasks="tasks2"
      :order="1"
      @title-click="() => $router.push({ name: 'project-iteration', params: { iterationId: 1 } })"
    />
    <TaskList
      title="Iteração 1"
      title-link="project-iteration"
      :tasks="tasks2"
      :order="2"
      @title-click="() => $router.push({ name: 'project-iteration', params: { iterationId: 2 } })"
    />
  </div>
</template>