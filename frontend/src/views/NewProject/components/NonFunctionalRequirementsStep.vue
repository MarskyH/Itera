<script setup lang="ts">
import { onMounted, ref } from "vue";
import { Form } from 'vee-validate'
import * as yup from 'yup'

import InputField from 'src/views/NewProject/components/InputField.vue'
import yupErrorMessages from 'src/utils/yupErrorMessages';
import { InputFieldProps, NonFunctionalRequirementForm, models } from "src/@types";
import { useNonFunctionalRequirementStore } from "src/stores/NonFunctionalRequirementStore";
import { useRoute } from "vue-router";

interface NonFunctionalRequirement extends models.NonFunctionalRequirement { }
interface NonFunctionalRequirementsWeights extends models.NonFunctionalRequirementWeights { }

const $route = useRoute()
const $nonFunctionalRequirementStore = useNonFunctionalRequirementStore()
const nonFunctionalRequirements = ref<NonFunctionalRequirement[]>([])
const inputFields = ref<InputFieldProps[]>([])
const weights = ref<NonFunctionalRequirementsWeights[]>([])

yup.setLocale(yupErrorMessages);

const setWeightOptions = (weights: { value: number; description: string }[]) => {
  return weights.map(weight => ({
    value: String(weight.value),
    name: `${weight.value} - ${weight.description}`,
    selected: false
  }));
}

let schema: any;

async function setNonFunctionalRequirements() {
  await $nonFunctionalRequirementStore.fetchNonFunctionalRequirements();
  nonFunctionalRequirements.value = $nonFunctionalRequirementStore.nonFunctionalRequirements;
}

async function setNonFunctionalRequirementsWeights(nonFunctionalRequirementId: string) {
  await $nonFunctionalRequirementStore.fetchNonFunctionalRequirementWeights(nonFunctionalRequirementId);
  weights.value = $nonFunctionalRequirementStore.wheights;
  return setWeightOptions(weights.value);
}

onMounted(async () => {
  await setNonFunctionalRequirements();
  const firstRequirementId = nonFunctionalRequirements.value[0]?.id || "";

  for (const requirement of nonFunctionalRequirements.value) {
    const options = await setNonFunctionalRequirementsWeights(requirement.id || "");
    inputFields.value.push({
      name: requirement.id || 'name',
      label: requirement.title,
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: options,
      validation: yup.string().required(),
    });
  }

  const validations: Record<string, any> = {};
  inputFields.value.forEach(inputField => {
    validations[inputField.name] = inputField.validation;
  });
  schema = yup.object(validations);
});

async function createNonFunctionalRequirement(nonFunctionalRequirementsFormValues: NonFunctionalRequirementForm[]) {
  const projectId = String($route.params.projectId);

  const responseStatus = await $nonFunctionalRequirementStore.createNonFunctionalRequirements(nonFunctionalRequirementsFormValues, projectId);
  if (responseStatus === 200) {
    await setNonFunctionalRequirements();
    alert('Configuração do projeto concluída!');
  } else {
    alert('Falha ao cadastrar requisito não funcional!');
  }
}

function onSubmit(values: any) {
  /*
  const nonFunctionalRequirementFormValuesList: NonFunctionalRequirementForm[] = [];
  for (const fieldName in values) {
    nonFunctionalRequirementFormValuesList.push({
      title: fieldName,
      valueRequirement: values[fieldName]
    });
  }

  createNonFunctionalRequirement(nonFunctionalRequirementFormValuesList);*/
}
</script>

<template>
  <div class="flex w-full justify-start items-center gap-2 px-2 text-base">
    <FontAwesomeIcon icon="fa-solid fa-gears" />
    <span class="font-semibold px-2">Requisitos Não Funcionais</span>
  </div>

  <Form ref="nonFunctionalRequirementForm" :validation-schema="schema" @submit="onSubmit"
    class="flex flex-col gap-10 p-5 items-center">
    <div class="grid grid-cols-1 lg:grid-cols-2 w-full gap-5">
      <InputField v-for="inputField in inputFields" :key="inputField.name" :label="inputField.label"
        :name="inputField.name" :placeholder="inputField.placeholder" :type="inputField.type"
        :required="inputField.required" :options="inputField.options" />
    </div>

    <div class="flex gap-5">
      <button
        class="flex text-white w-32 justify-evenly items-center bg-stone-400 dark:bg-stone-600 px-4 py-2 gap-4 rounded-md"
        @click="$router.push({ name: 'home' })">
        <FontAwesomeIcon icon="fa-solid fa-angle-left" class="text-neutral-500 dark:text-white text-xs" />
        <span class="font-semibold">Voltar</span>
      </button>

      <button class="flex text-white w-32 justify-evenly items-center bg-lavenderIndigo-900 px-4 py-2 gap-4 rounded-md"
        type="submit">
        <span class="font-semibold">Avançar</span>
        <FontAwesomeIcon icon="fa-solid fa-angle-right" class="text-neutral-500 dark:text-white text-xs" />
      </button>
    </div>
  </Form>
</template>
