<script setup lang="ts">
import { onMounted, ref } from "vue";
import { models } from "src/@types";
import { useRiskStore } from "src/stores/RiskStore";

interface Risk extends models.Risk {}

const props = defineProps<{
  id: string
}>()

const riskDefault: Risk = {
  id: '',
  title: '',
  effect: '',
  probability: '',
  impact: '',
  exposureDegree: '',
  description: '',
  type: '',
}

const $riskStore = useRiskStore()

const risk = ref<Risk>({...riskDefault})

onMounted(async () => {
  if ($riskStore.risk.id !== "") {
    risk.value = {...$riskStore.risk}
  } else {
    await $riskStore.fetchRisk(props.id).then(async () => {
      risk.value = $riskStore.risk
    })
  }
})

$riskStore.$subscribe(() => {
  risk.value = {...$riskStore.risk}
})

</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="flex gap-3 items-center">
      <FontAwesomeIcon
        icon="fa-solid fa-triangle-exclamation"
        class="text-stone-500 dark:text-stone-400"
      />

      <span class="font-bold">Detalhes do risco</span>
    </div>

    <div class="flex flex-col text-xs p-1 gap-4">
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-circle-info"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Título</span>

          <span> {{ risk.title }} </span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-fire"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Efeito</span>

          <span>{{ risk.effect }}</span>
        </div>
      </div>
          
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-percent"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Probabilidade</span>

          <span>{{ risk.probability }}</span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-burst"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Impacto</span>

          <span>{{ risk.impact }}</span>
        </div>
      </div>


      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-bomb"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Grau de exposição</span>

          <span>{{ risk.exposureDegree }}</span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-shield"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Tipo da ação</span>

          <span>{{ risk.type }}</span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-fire-extinguisher"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Descrição da ação</span>

          <span>{{ risk.description }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
