<script setup lang="ts">
import { onMounted, ref } from "vue";
import { models } from "src/@types";
import { useFunctionalRequirementStore } from "src/stores/FunctionalRequirementStore";

interface FunctionalRequirement extends models.FunctionalRequirement {}

const props = defineProps<{
  id: string
}>()

const functionalRequirementDefault: FunctionalRequirement = {
  id: '',
  title: '',
  details: '',
  complexity: '',
  priority: '',
  effort: 0,
  sizeRequirement: 0,
}

const $functionalRequirementStore = useFunctionalRequirementStore()

const functionalRequirement = ref<FunctionalRequirement>({...functionalRequirementDefault})

onMounted(async () => {
  if ($functionalRequirementStore.functionalRequirement.id !== "") {
    functionalRequirement.value = {...$functionalRequirementStore.functionalRequirement}
  } else {
    await $functionalRequirementStore.fetchFunctionalRequirement(props.id).then(async () => {
      functionalRequirement.value = $functionalRequirementStore.functionalRequirement
    })
  }
})

$functionalRequirementStore.$subscribe(() => {
  functionalRequirement.value = {...$functionalRequirementStore.functionalRequirement}
})

</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="flex gap-3 items-center">
      <FontAwesomeIcon
        icon="fa-solid fa-triangle-exclamation"
        class="text-stone-500 dark:text-stone-400"
      />

      <span class="font-bold">Detalhes do Requisito Funcional</span>
    </div>

    <div class="flex flex-col text-xs p-1 gap-4">
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-circle-info"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Título</span>

          <span> {{ functionalRequirement.title }} </span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-list"
          class="text-neutral-400 w-4"
        />
        
        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Detalhamento</span>

          <span>{{ functionalRequirement.details }}</span>
        </div>
      </div>
          
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-gear"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Complexidade</span>

          <span>{{ functionalRequirement.complexity }}</span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-arrow-down-wide-short"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Prioridade</span>

          <span>{{ functionalRequirement.priority }}</span>
        </div>
      </div>


      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-clock"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Esforço</span>

          <span>{{ functionalRequirement.effort }}</span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-expand"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Tamanho</span>

          <span>{{ functionalRequirement.sizeRequirement }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
