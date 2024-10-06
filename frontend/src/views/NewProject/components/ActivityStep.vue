<script setup lang="ts">

import { onMounted, ref } from "vue";
import { Form } from 'vee-validate'

import * as yup from 'yup'

import ilustracao from "src/assets/ilustracaoRiscos.png"
import ActionModal from "src/components/ActionModal.vue";
import InputField from 'src/views/NewProject/components/InputField.vue'
import yupErrorMessages from 'src/utils/yupErrorMessages';
import ActionGridItem from "src/views/NewProject/components/ActionGridItem.vue";
import ActivityDetails from "src/views/Project/components/ActivityDetails.vue";
import { InputFieldProps, ActivityForm, models } from "src/@types";
import { useActivityStore } from "src/stores/ActivityStore";
import { usePriorityStore } from "src/stores/PriorityStore";
import { useRiskActionTypeStore } from "src/stores/RiskActionTypeStore";
import { useRoute } from "vue-router";

interface Activity extends models.Activity { }
interface Priority extends models.Priority { }
interface RiskActionType extends models.RiskActionType { }

const $emit = defineEmits(['sideViewContentChange'])

const $route = useRoute()
const $priorityStore = usePriorityStore()
const $activityStore = useActivityStore()
const $riskActionTypeStore = useRiskActionTypeStore()

const isActionModalOpen = ref<boolean>(false)
const onEditRecord = ref<string | null>(null)
const actionModalTitle = ref<string>('Adicionar risco')

const formOnLoad = ref<boolean>(true)
const activityForm = ref<any>(null)

const activities = ref<Activity[]>([])
const priorityOptions = ref<Priority[]>([])
const riskActionTypeOptions = ref<RiskActionType[]>([])

const inputFields = ref<InputFieldProps[]>([])

yup.setLocale(yupErrorMessages);

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


async function setOptions() {
  await $riskActionTypeStore.fetchRiskActionTypes().then(() => {
    riskActionTypeOptions.value = $riskActionTypeStore.riskActionTypes
  })
  await $priorityStore.fetchPriorities().then(() => {
    priorityOptions.value = $priorityStore.priorities
  })
}

async function setActivities() {
  await $activityStore.fetchActivities(String($route.params.projectId)).then(() => {
    activities.value = $activityStore.activities
    console.log(activities.value)
  })
}

onMounted(async () => {
  await setActivities().then(async () => {
    await setOptions().then(async () => {
      inputFields.value = [
        {
          name: "title",
          label: "Título da ação",
          placeholder: "Digite o título da ação",
          required: true,
          validation: yup.string().required(),
          value: ''
        },
        {
          name: "priority",
          label: "Prioridade da ação",
          placeholder: "Selecione",
          required: true,
          options: setSelectOptions(priorityOptions.value),
          validation: yup.string().required(),
          value: '',
        },
        {
          name: "type",
          label: "Tipo da ação",
          placeholder: "Selecione",
          required: true,
          options: setSelectOptions(riskActionTypeOptions.value),
          validation: yup.string().required(),
          value: '',

        },
        {
          name: "description",
          label: "Descrição da ação",
          placeholder: "Digite a descrição da ação",
          required: true,
          validation: yup.string().required().min(20),
          value: '',
        },
      ]

      inputFields.value.forEach(inputField => formValidations[inputField.name] = inputField.validation)
      schema = yup.object(formValidations);

      formOnLoad.value = false
    })
  })
})


async function createActivity(activityFormValues: ActivityForm) {
  const projectId = String($route.params.projectId)
  await $activityStore.createActivity(activityFormValues, projectId).then((response: any) => {
    console.log(response)
    if (response === 200) {
      alert('Salvo com sucesso')
      isActionModalOpen.value = false
    } else {
      alert('Erro ao salvar')
      isActionModalOpen.value = false
    }
  })
}

function onSubmit(values: any) {
  let activityFormValues: ActivityForm = { ...values }

  if (!onEditRecord.value) {
    createActivity(activityFormValues)
  } else {
    updateActivity(activityFormValues)
  }

  isActionModalOpen.value = false
}

function setNewActivityForm() {
  actionModalTitle.value = 'Adicionar ação'
  activityForm.value.resetForm()
  isActionModalOpen.value = true
}

function removeActivity(activityId: string | undefined) {
  $activityStore.deleteActivity(activityId || '')
  activities.value = activities.value.filter((activity: Activity) => activity.id !== activityId)
}


function editActivity(activityId: string | undefined) {
  onEditRecord.value = activityId ? activityId : null
  actionModalTitle.value = 'Editar ação'

  let activty: Activity | undefined = activities.value.find(a => a.id === activityId)

  if (activty) {
    let editActivityFormValues: ActivityForm = { ...activty }
    activityForm.value?.setValues(editActivityFormValues)
  }


  isActionModalOpen.value = true

}

function updateActivity(values: ActivityForm) {
  console.log(values)
  let activityToEdit: Activity | undefined = undefined
  let activityIndex = 0

  activities.value.forEach((activity: Activity, index) => {
    if (activity.id === onEditRecord.value) {
      activityToEdit = activity
      activityIndex = index
    }
  })

  if (activityToEdit && String(activityIndex)) {
    $activityStore.updateActivity(onEditRecord.value || '', values)
    $activityStore.$state.activity = { ...values, id: onEditRecord.value || '' }
    activityToEdit = { ...values }

    activities.value[activityIndex] = activityToEdit
  }
}

async function viewActivityOnSide(activityId: string) {
  $emit('sideViewContentChange', { component: ActivityDetails })
  await $activityStore.fetchActivity(activityId)
}

</script>

<template>
  <div v-if="activities.length === 0" class="flex flex-col w-full h-full items-center justify-center gap-8">
    <img :src="ilustracao" alt="Ilustração Novo Projeto" class="shrink-0 w-60 h-60">

    <span class=" w-1/2 text-center text-stone-500 dark:text-stone-400">
      Insira as ações associados ao projeto
    </span>

    <button
      class="flex items-center bg-gradient-to-br from-40% from-lavenderIndigo-900 to-tropicalIndigo-900 p-4 gap-4 rounded-md"
      @click="() => isActionModalOpen = true">
      <FontAwesomeIcon icon="fa-solid fa-warning" class="text-white" />

      <span class="font-semibold text-white">
        Adicionar ação
      </span>
    </button>
  </div>

  <div v-else class="flex flex-col gap-5">
    <div class="flex gap-5 rounded justify-between items-center text-sm">
      <div class="flex items-center gap-2 px-2 text-base">
        <FontAwesomeIcon icon="fa-solid fa-shield" />

        <span class="font-semibold px-2">Ações do projeto</span>
      </div>

      <button class="flex text-white justify-evenly items-center bg-lavenderIndigo-900 px-3 py-2 gap-4 rounded-md"
        @click="setNewActivityForm()">
        <FontAwesomeIcon icon="fa-solid fa-plus" />

        <span class="font-semibold">Adicionar</span>
      </button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-4 gap-3">
      <ActionGridItem v-for="activity in activities" :key="activity.id" icon="shield" :title="activity.title"
        @edit="editActivity(activity.id)" @remove="removeActivity(activity.id)"
        @side-view-content-change="() => viewActivityOnSide(activity.id || '')">
        <div class="flex flex-col gap-1">
          <span class="text-sm font-semibold">
            Prioridade da ação
          </span>

          <span class="text-xs text-stone-500 dark:text-stone-400">
            {{ activity.priority }}
          </span>
        </div>

        <div class="flex flex-col gap-1">
          <span class="text-sm font-semibold">
            Tipo de ação
          </span>

          <span class="text-xs text-stone-500 dark:text-stone-400">
            {{ activity.type }}
          </span>
        </div>
      </ActionGridItem>
    </div>

    <div v-show="$route.name === 'activities'" class="flex gap-5 justify-center">
      <button
        class="flex text-white w-32 justify-evenly items-center bg-stone-400 dark:bg-stone-600 px-4 py-2 gap-4 rounded-md"
        @click="$router.push({ name: 'team' })">
        <FontAwesomeIcon icon="fa-solid fa-angle-left" class="text-neutral-500 dark:text-white text-xs" />

        <span class="font-semibold">Voltar</span>
      </button>

      <button class="flex text-white w-32 justify-evenly items-center bg-lavenderIndigo-900 px-4 py-2 gap-4 rounded-md"
        @click="$router.push({ name: 'functional-requirements' })">
        <span class="font-semibold">Avançar</span>

        <FontAwesomeIcon icon="fa-solid fa-angle-right" class="text-neutral-500 dark:text-white text-xs" />
      </button>
    </div>
  </div>

  <Form ref="activityForm" :validation-schema="schema" @submit="onSubmit" v-if="!formOnLoad">
    <ActionModal v-model="isActionModalOpen" :title="actionModalTitle" icon="warning">
      <div class="flex flex-col px-8 py-4 gap-5">
        <div class="grid grid-cols-2 w-full gap-5">
          <InputField v-for="(inputField, index) in inputFields" v-show="index < inputFields.length - 1"
            :key="inputField.name" v-slot="{ field }" :label="inputField.label" :name="inputField.name"
            :placeholder="inputField.placeholder" :type="inputField.type" :disabled="inputField.disabled"
            :value="inputField.value" :required="inputField.required" :options="inputField.options"
            @change="inputField.onChange">
            {{ console.log(field.onInput) }}
          </InputField>
        </div>

        <div class="grid grid-cols-1 w-full gap-5">
          <InputField :key="inputFields[inputFields.length - 1]?.name"
            :label="inputFields[inputFields.length - 1]?.label" :name="inputFields[inputFields.length - 1]?.name"
            :placeholder="inputFields[inputFields.length - 1]?.placeholder"
            :type="inputFields[inputFields.length - 1]?.type" :required="inputFields[inputFields.length - 1]?.required"
            :options="inputFields[inputFields.length - 1]?.options" />
        </div>
      </div>
    </ActionModal>
  </Form>
</template>