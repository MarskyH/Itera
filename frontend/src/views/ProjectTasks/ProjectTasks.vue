<script setup lang="ts">
import TaskList from './components/TaskList.vue';
import { useRoute } from "vue-router";
import { models } from "src/@types";
import { useBacklogStore } from "src/stores/BacklogStore";
import { onMounted, ref } from "vue";
interface Backlog extends models.Backlog {}


const tasks1 = ref<Backlog[]>([])




const $backlogStore = useBacklogStore()
const $route = useRoute()

onMounted(async ()=>{
  await $backlogStore.fetchBacklog(String($route.params.projectId)).then(async () =>{
      tasks1.value = $backlogStore.items;
  })
})


const tasks2 = [
  { id: '1', title: 'Planejamento', icon: 'users', priority: 'Alta', responsible: 'Indefinido' },
  { id: '2', title: 'Revisão', icon: 'users', priority: 'Alta', responsible: 'Indefinido' },
  { id: '2', title: 'Retrospectiva', icon: 'users', priority: 'Alta', responsible: 'Indefinido' },
]

</script>

<template>
  <div class="flex gap-3">
    <TaskList
      title="Backlog"
      :tasks="tasks1"
    />
    <TaskList
      title="Iteração 1"
      :tasks="tasks2"
    />
  </div>
</template>