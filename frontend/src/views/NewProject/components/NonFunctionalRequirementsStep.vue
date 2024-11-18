<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import { Form } from 'vee-validate'
import * as yup from 'yup'

import InputField from 'src/views/NewProject/components/InputField.vue'
import yupErrorMessages from 'src/utils/yupErrorMessages';
import { InputFieldProps, NonFunctionalRequirementForm, models } from "src/@types";
import { useNonFunctionalRequirementStore } from "src/stores/NonFunctionalRequirementStore";
import { useRoute, useRouter } from "vue-router";
import FeedbackUserAction from '../../../components/FeedbackUserAction.vue';
import LocalStorage from "src/services/localStorage";

const onError = ref<Boolean>(false)
const isVisible = ref<Boolean>(false)
const textResult = ref<String>("Login realizado com sucesso.")
const storage = new LocalStorage();

let userRole = storage.getLoggedUser()?.role || ''

function disableAction(){
  return (userRole !== 'Gerente')
}

interface NonFunctionalRequirement extends models.NonFunctionalRequirement { }
interface NonFunctionalRequirementProject extends models.NonFunctionalRequirementProject { }
interface NonFunctionalRequirementProjectOnUpdate extends models.NonFunctionalRequirementProjectOnUpdate { }
interface NonFunctionalRequirementsWeights extends models.NonFunctionalRequirementWeights { }

const $route = useRoute()
const $router = useRouter()
const $nonFunctionalRequirementStore = useNonFunctionalRequirementStore()
const nonFunctionalRequirements = ref<NonFunctionalRequirement[]>([])
const nonFunctionalRequirementsProject = ref<NonFunctionalRequirementProject[]>([])
const inputFields = ref<InputFieldProps[]>([])
const weights = ref<NonFunctionalRequirementsWeights[]>([])

const onEdit = ref<boolean>(false)

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

async function setNonFunctionalRequirementsProject() {
  const projectId = String($route.params.projectId)
  await $nonFunctionalRequirementStore.fetchNonFunctionalRequirementsProject(projectId);
  nonFunctionalRequirementsProject.value = $nonFunctionalRequirementStore.nonFunctionalRequirementsProejct;
}

async function setNonFunctionalRequirementsWeights(nonFunctionalRequirementId: string) {
  await $nonFunctionalRequirementStore.fetchNonFunctionalRequirementWeights(nonFunctionalRequirementId);
  weights.value = $nonFunctionalRequirementStore.weights;
  return setWeightOptions(weights.value);
}

onMounted(async () => {
  await setNonFunctionalRequirements();
  await setNonFunctionalRequirementsProject();

  for (const requirement of nonFunctionalRequirements.value) {
    const options = await setNonFunctionalRequirementsWeights(requirement.id || "");
    let value: string = ""

    if(nonFunctionalRequirementsProject.value){
      value = String(nonFunctionalRequirementsProject.value
        .find(req => req.nonfunctionalrequirementId === requirement.id)?.weight)
    }

    inputFields.value.push({
      name: requirement.id || 'name',
      label: requirement.title,
      placeholder: "Selecione o grau de importância",
      type: "select",
      value,
      required: true,
      options: options,
      hoverInfo: requirement.description,
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
    isVisible.value = true
    onError.value = false
    textResult.value = "Informações cadastradas com sucesso."
  } else {
    isVisible.value = true
    onError.value = true
    textResult.value = "Ocorreu um erro. Por favor tente novamente."
  }
}

async function updateNonFunctionalRequirement(nonFunctionalRequirementsFormValues: NonFunctionalRequirementForm[]) {
  var nonFunctionalRequirementProjectList: NonFunctionalRequirementProjectOnUpdate[] = []
  nonFunctionalRequirementsFormValues.forEach((rnfFormValue) => {
    nonFunctionalRequirementsProject.value.forEach((rnf) => {
      if (rnfFormValue.id == rnf.nonfunctionalrequirementId) {
        nonFunctionalRequirementProjectList.push({
          id: rnf?.id || '',
          weight: rnfFormValue?.weight || 0
        })
        
        console.log('achou', rnfFormValue.id,  rnf.nonfunctionalrequirementId, rnf.id, rnfFormValue.weight)
      } 
      //rnfFormValue.id == rnf.nonfunctionalrequirementId
    })

    console.log(nonFunctionalRequirementProjectList)
  })

  const responseStatus = await $nonFunctionalRequirementStore.updateProject(nonFunctionalRequirementProjectList)
  if (responseStatus === 200) {
    onEdit.value = false
  } else {
    //alert('Falha ao editar requisito não funcional!');
  }
}

function onSubmit(values: any) {
  let nonFunctionalRequirementFormValuesList: NonFunctionalRequirementForm[] = [];
  
  nonFunctionalRequirements.value.forEach((requirement: NonFunctionalRequirement) => {
    nonFunctionalRequirementFormValuesList.push({
      id: requirement.id || 'name',
      weight: values[requirement.id || 'rfn1']
    });
  })

  if(onEdit.value) {
    updateNonFunctionalRequirement(nonFunctionalRequirementFormValuesList)
  } else {
    createNonFunctionalRequirement(nonFunctionalRequirementFormValuesList)
  }
}

watch(isVisible, (newValue) => {
  if (!newValue && !onError.value) {
    $router.push({ name: 'my-projects' })
  }
});


</script>

<template>
   <FeedbackUserAction
    :text="textResult" 
    :onError="onError" 
    :isVisible="isVisible" 
    @update:isVisible="isVisible = $event" 
  />
  <Form
    ref="nonFunctionalRequirementForm"
    :validation-schema="schema"
    @submit="(values: any) => onSubmit(values)"
  >
    <div class="flex w-full justify-between items-center gap-2 px-2 text-base">
      <div class="flex gap-2 items-center">
        <FontAwesomeIcon icon="fa-solid fa-gears" />
  
        <span class="font-semibold px-2">Requisitos Não Funcionais</span>
      </div>

      <div
        v-if="$route.name !== 'non-functional-requirements' && !disableAction()" 
        class="flex gap-5"
      >
        <button
          v-show="!onEdit"
          class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-blueCrayola-900 dark:text-naplesYellow-900 hover:bg-blueCrayola-900/25 hover:dark:bg-naplesYellow-900/25 text-sm gap-2 text"
          type="button"
          @click="() => onEdit = true"
        >
          <FontAwesomeIcon
            icon="fa-solid fa-pen"
          />

          <span class="font-semibold">Editar</span>
        </button>

        <button
          v-show="onEdit"
          type="button"
          class="flex items-center rounded px-3 py-2 text-white bg-stone-400 dark:bg-stone-600 text-sm gap-2 text"
          @click="()=> onEdit = false"
        >
          <span class="font-semibold">Cancelar</span>
        </button>

        <button
          v-show="onEdit"
          class="flex items-center rounded px-3 py-2 text-white bg-lavenderIndigo-900 text-sm gap-2 text"
          type="submit"
        >
          <FontAwesomeIcon
            icon="fa-solid fa-check"
          />

          <span class="font-semibold">Salvar</span>
        </button>
      </div>
    </div>

    <div class="flex flex-col gap-10 p-5 items-center">
      <div class="grid grid-cols-1 lg:grid-cols-2 w-full gap-5">
        <InputField
          v-for="inputField in inputFields"
          :key="inputField.name"
          :label="inputField.label"
          :name="inputField.name"
          :placeholder="inputField.placeholder"
          :type="inputField.type"
          :required="inputField.required"
          :disabled="!onEdit && $route.name !== 'non-functional-requirements'"
          :options="inputField.options"
          :hover-info="inputField.hoverInfo"
          :value="inputField.value"
        />
      </div>

      <div
        v-if="$route.name === 'non-functional-requirements'"
        class="flex gap-5"
      >
        <button
          class="flex text-white w-32 justify-evenly items-center bg-stone-400 dark:bg-stone-600 px-4 py-2 gap-4 rounded-md"
          @click="$router.push({ name: 'home' })"
        >
          <FontAwesomeIcon
            icon="fa-solid fa-angle-left"
            class="text-neutral-500 dark:text-white text-xs"
          />
          <span class="font-semibold">Voltar</span>
        </button>

        <button
          class="flex text-white w-32 justify-evenly items-center bg-lavenderIndigo-900 px-4 py-2 gap-4 rounded-md"
          type="submit"
        >
          <span class="font-semibold">Avançar</span>
          <FontAwesomeIcon
            icon="fa-solid fa-angle-right"
            class="text-neutral-500 dark:text-white text-xs"
          />
        </button>
      </div>
    </div>
  </Form>
</template>
