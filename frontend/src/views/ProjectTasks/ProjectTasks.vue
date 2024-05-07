<script setup lang="ts">
import TaskList from './components/TaskList.vue';

const tasks1 = [
  { id: '1', title: 'RF001 - Login', icon: 'bookmark', priority: 'Alta', responsible: 'Indefinido' },
  { id: '2', title: 'RF002 - Logout', icon: 'bookmark', priority: 'Alta', responsible: 'Indefinido' },
]

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