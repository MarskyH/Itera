<script setup lang="ts">

import { RouterView } from 'vue-router'

import StepsBreadcrumbs from 'src/views/NewProject/components/StepsBreadcrumbs.vue';
import OptionCard from '../MyProjects/components/OptionCard.vue';

/*const optionsProject = [
  {
    label: "Tarefas",
    icon: "fa-solid fa-table-columns",
    route: "/tasks"
  },
  {
    label: "Cronograma",
    icon: "fa-solid fa-calendar",
    route: "/calendar"
  },
  {
    label: "Orçamento",
    icon: "fa-solid fa-file-invoice-dollar",
    route: "/budget"
  },
  {
    label: "Relatório",
    icon: "fa-solid fa-file-lines",
    route: "/record"
  },
]*/


</script>

<template>
  <div class="flex flex-col w-full h-full gap-5">
    <!--<div class="flex flex-row w-full h-min gap-5 justify-center items-center">
      <OptionCard
        v-for="(optionProject, index) in optionsProject"
        :key="index"
        :label="optionProject.label"
        :icon="optionProject.icon"
        :route="optionProject.route"
      />
    </div>-->
    <StepsBreadcrumbs />

    <RouterView />
  </div>
</template>