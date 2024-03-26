<script setup lang="ts">

const steps = [
  {
    title: 'Geral',
    link: 'new-project'
  },
  {
    title: 'Habilidades e competências',
    link: 'roles'
  },
  {
    title: 'Equipe',
    link: 'team'
  },
  {
    title: 'Riscos',
    link: 'risks'
  },
  {
    title: 'Requisitos funcionais',
    link: 'functional-requirements'
  },
  {
    title: 'Requisitos não funcionais',
    link: 'non-functional-requirements'
  }
]

</script>

<template>
  <div class="flex gap-2 p-1 bg-periwinkle-900 dark:bg-charcoal-900 rounded">
    <div
      v-for="(step, index) in steps"
      :key="step.link"
      class="flex w-full items-center gap-2"
    >
      <button
        class="flex w-full whitespace-wrap items-center justify-center h-full text-xs p-2 hover:bg-stone-800/10 rounded"
        :class="[$route.name === step.link ? 'text-lavenderIndigo-900 dark:text-tropicalIndigo-900 font-bold' : 'text-neutral-500 dark:text-white']"
        @click="$router.push({ name: step.link })"
      >
        {{ step.title }}
      </button>

      <FontAwesomeIcon
        v-if="index < steps.length - 1"
        icon="fa-solid fa-angle-right"
        class="text-neutral-500 dark:text-white text-xs"
      />
    </div>
  </div>
</template>