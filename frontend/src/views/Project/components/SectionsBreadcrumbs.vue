<script setup lang="ts">

const sections = [
  {
    title: 'Habilidades e competências',
    link: 'view-roles'
  },
  {
    title: 'Equipe',
    link: 'view-team'
  },
  {
    title: 'Riscos',
    link: 'view-risks'
  },
  {
    title: 'Requisitos funcionais',
    link: 'view-functional-requirements'
  },
  {
    title: 'Requisitos não funcionais',
    link: 'view-non-functional-requirements'
  }
]

</script>

<template>
  <div class="flex gap-2 p-1 bg-periwinkle-900 dark:bg-charcoal-900 rounded">
    <div
      v-for="section in sections"
      :key="section.link"
      class="flex w-full items-center gap-2"
    >
      <button
        class="flex w-full whitespace-wrap items-center justify-center h-full text-xs p-2 hover:bg-stone-800/10 rounded"
        :class="[$route.name === section.link ? 'text-lavenderIndigo-900 dark:text-tropicalIndigo-900 font-bold' : 'text-neutral-500 dark:text-white']"
        @click="$router.push({ name: section.link })"
      >
        {{ section.title }}
      </button>
    </div>
  </div>
</template>