<script setup lang="ts">

import { onMounted, ref } from "vue";
import { Form } from 'vee-validate'
import * as yup from 'yup'

import ActionModal from "src/components/ActionModal.vue";
import InputField from 'src/views/NewProject/components/InputField.vue'
import yupErrorMessages from 'src/utils/yupErrorMessages';
import ActionGridItem from "src/views/NewProject/components/ActionGridItem.vue";
import FunctionalRequirementDetails from "src/views/Project/components/FunctionalRequirementDetails.vue";
import { InputFieldProps, FunctionalRequirementForm, models } from "src/@types";
import { useRoute } from "vue-router";
import { useFunctionalRequirementStore } from "src/stores/FunctionalRequirementStore";
import { useDegreeStore } from "src/stores/DegreeStore";
import { usePriorityStore } from "src/stores/PriorityStore";
 

interface FunctionalRequirement extends models.FunctionalRequirement {}
interface Degree extends models.Degree {}
interface Priority extends models.Priority {}

const $emit = defineEmits(['sideViewContentChange'])

const $route = useRoute()
const $functionalRequirementStore = useFunctionalRequirementStore()
const $degreeStore = useDegreeStore()
const $priorityStore = usePriorityStore()

const isActionModalOpen = ref<boolean>(false)
const onEditRecord = ref<string | null>(null)
const actionModalTitle = ref<string>('Adicionar requisito')

const formOnLoad = ref<boolean>(true)
const functionalRequirementForm = ref<any>(null)

const degreeOptions = ref<Degree[]>([])
const priorityOptions = ref<Priority[]>([])

const functionalRequirements = ref<FunctionalRequirement[]>([
  /*
  {
    id: 1,
    name: 'José Mendes',
    hourlyRate: 3.0,
    dedicatedHours: 6,
    role: roles.find(role => role.id === 1)
  },
  {
    id: 2,
    name: 'Maria Silva',
    hourlyRate: 3.0,
    dedicatedHours: 6,
    role: roles.find(role => role.id === 2)
  },
  {
    id: 3,
    name: 'Ana Santos',
    hourlyRate: 3.0,
    dedicatedHours: 6,
    role: roles.find(role => role.id === 3)
  }*/
])

yup.setLocale(yupErrorMessages);

let inputFields: InputFieldProps[] = []

let formValidations: any = {}
let schema: any

const setSelectOptions = (options: { id: number; name: string }[]) => {
  return options.length > 0 ? options.map((option: { id: number; name: string }) => {
    return {
      value: option.name,
      name: option.name,
      selected: false
    }
  }) : []
}

async function setFunctionalRequirements() {
  await $functionalRequirementStore.fetchFunctionalRequirements(String($route.params.projectId)).then(() => {
    functionalRequirements.value = $functionalRequirementStore.functionalRequirements
  })
}

async function setDegreeOptions() {
  await $degreeStore.fetchDegrees().then(() => {
    degreeOptions.value = $degreeStore.degrees
  })
}

async function setPriorityOption(){
  await $priorityStore.fetchPriorities().then(()=>{
    priorityOptions.value = $priorityStore.priorities
  })
}

onMounted(async () => {
  await setFunctionalRequirements().then(async ()=>{
    await setDegreeOptions().then(async () => {
      await setPriorityOption().then(async () => {
        inputFields =  [
        {
          name: "title",
          label: "Título",
          placeholder: "Digite o título",
          required: true,
          validation: yup.string().required().min(3)
        },
        {
          name: "priority",
          label: "Prioridade",
          placeholder: "Selecione a prioridade",
          type: "select",
          required: true,
          options: setSelectOptions(priorityOptions.value),
          validation: yup.string().required()
        },
        {
          name: "details",
          label: "Detalhamento",
          placeholder: "Digite os detalhes",
          required: true,
          validation: yup.string().required().min(3)
        },
        {
          name: "complexity",
          label: "Complexidade",
          placeholder: "Selecione a complexidade",
          type: "select",
          required: true,
          options: setSelectOptions(degreeOptions.value),
          validation: yup.string().required()
        },
        {
          name: "effort",
          label: "Esforço",
          placeholder: "Digite o esforço",
          required: true,
          validation: yup.number().required().min(1)
        },
        {
          name: "sizeRequirement",
          label: "Tamanho",
          placeholder: "Digite o tamanho",
          required: true,
          validation: yup.number().required().min(1)
        },
      ]

      inputFields.forEach(inputField => formValidations[inputField.name] = inputField.validation)
      schema = yup.object(formValidations);

      formOnLoad.value = false
      })
    });
  });
})

async function createFunctionalRequirement(functionalRequirementFormValues: FunctionalRequirementForm) {
  const projectId: string = String($route.params.projectId)

  await $functionalRequirementStore.createFunctionalRequirement(functionalRequirementFormValues, projectId)
    .then((responseStatus: any) => {
      if(responseStatus === 200) {
        setFunctionalRequirements()
      } else {
        alert('Falha ao criar requisito! Tente novamente.')
      }
    }
  )
}

function onSubmit(values: any) {
  let functionalRequirementFormValues: FunctionalRequirementForm = {...values}

  if(!onEditRecord.value) {
    createFunctionalRequirement(functionalRequirementFormValues)
  } else {
    updateFunctionalRequirement(functionalRequirementFormValues)
  }

  isActionModalOpen.value = false
}

function setNewFunctionalRequirementForm() {
  actionModalTitle.value = 'Adicionar requisito'
  functionalRequirementForm.value.resetForm()
  isActionModalOpen.value = true
}

function removeFunctionalRequirement(requirementId: string | undefined) {
  $functionalRequirementStore.deleteFunctionalRequiriment(requirementId || '')
  functionalRequirements.value = functionalRequirements.value
    .filter((requirement: FunctionalRequirement) => requirement.id !== requirementId)
}


function editFunctionalRequirement(requirementId: string | undefined) {
  onEditRecord.value = requirementId  ? requirementId : null
  actionModalTitle.value = 'Editar Requisito Funcional'

  let requirement: FunctionalRequirement | undefined = functionalRequirements.value.find(r => r.id === requirementId)

  if(requirement) {
    let editRequirementFormValues: FunctionalRequirementForm = {...requirement}
    functionalRequirementForm.value?.setValues(editRequirementFormValues)
  }
  

  isActionModalOpen.value = true
}

function updateFunctionalRequirement(values: FunctionalRequirementForm) {
  let requirementToEdit: FunctionalRequirement | undefined = undefined
  let requirementIndex = 0

  functionalRequirements.value.forEach((requirement: FunctionalRequirement, index) => {
    if(requirement.id === onEditRecord.value){
      requirementToEdit = requirement
      requirementIndex = index
    }
  })

  if(requirementToEdit && String(requirementIndex)){
    $functionalRequirementStore.updateFunctionalRequirement(onEditRecord.value || '', values)
    $functionalRequirementStore.$state.functionalRequirement = {...values, id: onEditRecord.value || ''}

    requirementToEdit = { ...values }

    functionalRequirements.value[requirementIndex] = requirementToEdit
  }
}

async function viewFunctionalRequirementOnSide(requirementId: string) {
  $emit('sideViewContentChange', { component: FunctionalRequirementDetails })
  await $functionalRequirementStore.fetchFunctionalRequirement(requirementId)
}

</script>

<template>
  <div
    v-if="functionalRequirements.length === 0"
    class="flex flex-col w-full h-full items-center justify-center gap-8"
  >
    <span class=" w-1/2 text-center text-stone-500 dark:text-stone-400">
      Adicione os requisitos funcionais para comporem as funcionalidades do projeto
    </span>

    <button
      class="flex items-center bg-gradient-to-br from-40% from-lavenderIndigo-900 to-tropicalIndigo-900 p-4 gap-4 rounded-md"
      @click="() => isActionModalOpen = true"
    >
      <FontAwesomeIcon
        icon="fa-solid fa-bookmark"
        class="text-white"
      />

      <span class="font-semibold text-white">
        Adicionar requisito
      </span>
    </button>
  </div>

  <div
    v-else
    class="flex flex-col gap-5"
  >
    <div class="flex gap-5 rounded justify-between items-center text-sm">
      <div class="flex items-center gap-2 px-2 text-base">
        <FontAwesomeIcon
          icon="fa-solid fa-bookmark"
        />
        
        <span class="font-semibold px-2">Requisitos funcionais</span>
      </div>

      <button
        class="flex text-white justify-evenly items-center bg-lavenderIndigo-900 px-3 py-2 gap-4 rounded-md"
        @click="setNewFunctionalRequirementForm()"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-plus"
        />
        
        <span class="font-semibold">Adicionar</span>
      </button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-4 gap-3">
      <ActionGridItem
        v-for="requirement in functionalRequirements"
        :key="requirement.id"
        icon="bookmark"
        :title="requirement.title"
        @edit="editFunctionalRequirement(requirement.id)"
        @remove="removeFunctionalRequirement(requirement.id)"
        @side-view-content-change="() => viewFunctionalRequirementOnSide(requirement.id || '')"
      >
        <div class="flex flex-col gap-1">
          <span class="text-sm font-semibold">
            Prioridade
          </span>

          <span class="text-xs text-stone-500 dark:text-stone-400">
            {{ requirement.priority }}
          </span>
        </div>

        <div class="flex flex-col gap-1">
          <span class="text-sm font-semibold">
            Esforço
          </span>

          <span class="text-xs text-stone-500 dark:text-stone-400">
            {{ requirement.effort }}
          </span>
        </div>

        <div class="flex flex-col gap-1">
          <span class="text-sm font-semibold">
            Complexidade
          </span>

          <span class="text-xs text-stone-500 dark:text-stone-400">
            {{ requirement.complexity }}
          </span>
        </div>
      </ActionGridItem>
    </div>

    <div class="flex gap-5 justify-center">
      <button
        class="flex text-white w-32 justify-evenly items-center bg-stone-400 dark:bg-stone-600 px-4 py-2 gap-4 rounded-md"
        @click="$router.push({ name: 'requirements' })"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-angle-left"
          class="text-neutral-500 dark:text-white text-xs"
        />

        <span class="font-semibold">Voltar</span>
      </button>
      
      <button
        class="flex text-white w-32 justify-evenly items-center bg-lavenderIndigo-900 px-4 py-2 gap-4 rounded-md"
        @click="$router.push({ name: 'non-functional-requirements' })"
      >
        <span class="font-semibold">Avançar</span>

        <FontAwesomeIcon
          icon="fa-solid fa-angle-right"
          class="text-neutral-500 dark:text-white text-xs"
        />
      </button>
    </div>
  </div>

  <Form
    ref="functionalRequirementForm"
    :validation-schema="schema"
    @submit="onSubmit"
    v-if="!formOnLoad"
  >
    <ActionModal
      v-model="isActionModalOpen"
      :title="actionModalTitle"
      icon="bookmark"
    >
      <div class="grid grid-cols-2 w-full gap-5 px-8 py-4">
        <InputField
          v-for="inputField in inputFields"
          :key="inputField.name"
          :label="inputField.label"
          :name="inputField.name"
          :placeholder="inputField.placeholder"
          :type="inputField.type"
          :required="inputField.required"
          :options="inputField.options"
        />
      </div>
    </ActionModal>
  </Form>
</template>