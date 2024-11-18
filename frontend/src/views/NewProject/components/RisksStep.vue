<script setup lang="ts">

import { onMounted, ref } from "vue";
import { Form } from 'vee-validate'

import * as yup from 'yup'

import ilustracao from "src/assets/ilustracaoRiscos.png"
import ActionModal from "src/components/ActionModal.vue";
import InputField from 'src/views/NewProject/components/InputField.vue'
import yupErrorMessages from 'src/utils/yupErrorMessages';
import ActionGridItem from "src/views/NewProject/components/ActionGridItem.vue";
import RiskDetails from "src/views/Project/components/RiskDetails.vue";
import { InputFieldProps, RiskForm, models } from "src/@types";
import { useRiskStore } from "src/stores/RiskStore";
import { useDegreeStore } from "src/stores/DegreeStore";
import { useRiskActionTypeStore } from "src/stores/RiskActionTypeStore";
import { useRoute } from "vue-router";
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

interface Risk extends models.Risk { }
interface Degree extends models.Degree { }
interface RiskActionType extends models.RiskActionType { }

const $emit = defineEmits(['sideViewContentChange'])

const $route = useRoute()
const $riskStore = useRiskStore()
const $degreeStore = useDegreeStore()
const $riskActionTypeStore = useRiskActionTypeStore()

const isActionModalOpen = ref<boolean>(false)
const onEditRecord = ref<string | null>(null)
const actionModalTitle = ref<string>('Adicionar risco')

const formOnLoad = ref<boolean>(true)
const riskForm = ref<any>(null)

const risks = ref<Risk[]>([])
const degreeOptions = ref<Degree[]>([])
const riskActionTypeOptions = ref<RiskActionType[]>([])
const exposureDegree = ref<string | undefined>(undefined)

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

function setExposureDegreeValue() {
  let probability: string = riskForm.value.values.probability
  let impact: string = riskForm.value.values.impact
  
  probability !== "" && impact !== "" 
    && riskForm.value.setFieldValue('exposureDegree', computeExposureDegree(probability, impact))
}

const computeExposureDegree = (probability: string, impact: string) => {
  if (probability === '' || impact === '') return ''
  if (probability === 'Alto' || impact === 'Alto') return 'Alto'
  if (probability === 'Médio' || impact === 'Médio') return 'Médio'
  return 'Baixo'
}

async function setDegreeOptions() {
  await $degreeStore.fetchDegrees().then(() => {
    degreeOptions.value = $degreeStore.degrees
  })
}

async function setRiskActionTypeOptions() {
  await $riskActionTypeStore.fetchRiskActionTypes().then(() => {
    riskActionTypeOptions.value = $riskActionTypeStore.riskActionTypes
  })
}

async function setRisks() {
  await $riskStore.fetchRisks(String($route.params.projectId)).then(() => {
    risks.value = $riskStore.risks
  })
}

onMounted(async () => {
  await setRisks().then(async () => {
    await setDegreeOptions().then(async () => {
      await setRiskActionTypeOptions().then(async () => {
        inputFields.value = [
          {
            name: "title",
            label: "Risco",
            placeholder: "Digite o título do risco",
            required: true,
            validation: yup.string().required().min(3)
          },
          {
            name: "effect",
            label: "Efeito",
            placeholder: "Digite o efeito do risco",
            required: true,
            validation: yup.string().required().min(1)
          },
          {
            name: "probability",
            label: "Probabilidade",
            placeholder: "Selecione a probabilidade",
            type: "select",
            required: true,
            options: setSelectOptions(degreeOptions.value),
            validation: yup.string().required(),
            onChange: setExposureDegreeValue,
          },
          {
            name: "impact",
            label: "Impacto",
            placeholder: "Selecione a probabilidade",
            type: "select",
            required: true,
            options: setSelectOptions(degreeOptions.value),
            validation: yup.string().required(),
            onChange: setExposureDegreeValue
          },
          {
            name: "exposureDegree",
            label: "Grau de exposição",
            placeholder: "Preencha Probabilidade e Impacto",
            required: true,
            disabled: true,
            value: exposureDegree.value,
            validation: yup.string().required().min(3)
          },
          {
            name: "type",
            label: "Tipo de ação",
            placeholder: "Selecione o tipo de ação",
            required: true,
            type: "select",
            options: setSelectOptions(riskActionTypeOptions.value),
            validation: yup.string().required()
          },
          {
            name: "description",
            label: "Descrição da ação",
            placeholder: "Digite a ação a ser tomada com esse risco",
            type: "textarea",
            required: true,
            validation: yup.string().required().min(3)
          },
        ]
    
        inputFields.value.forEach(inputField => formValidations[inputField.name] = inputField.validation)
        schema = yup.object(formValidations);
        
        formOnLoad.value = false
      })
    })
  })
})


async function createRisk(riskFormValues: RiskForm) {
  const projectId = String($route.params.projectId)

  await $riskStore.createRisk(riskFormValues, projectId)
  .then((responseStatus: any) => {
      if (responseStatus === 200) {
        isVisible.value = true
        onError.value = false
        textResult.value = "Informações cadastradas com sucesso."
        setRisks()
      } else {
        isVisible.value = true
        onError.value = true
        textResult.value = "Ocorreu um erro. Por favor tente novamente."
      }
    }
  )
}

function onSubmit(values: any) {
  let riskFormValues: RiskForm = { ...values }

  if (!onEditRecord.value) {
    createRisk(riskFormValues)
  } else {
    updateRisk(riskFormValues)
  }

  isActionModalOpen.value = false
}

function setNewRiskForm() {
  actionModalTitle.value = 'Adicionar risco'
  riskForm.value.resetForm()
  isActionModalOpen.value = true
}

function removeRisk(riskId: string | undefined) {
  $riskStore.deleteRisk(riskId || '')
  risks.value = risks.value.filter((risk: Risk) => risk.id !== riskId)
}


function editRisk(riskId: string | undefined) {
  onEditRecord.value = riskId  ? riskId : null
  actionModalTitle.value = 'Editar risco'

  let risk: Risk | undefined = risks.value.find(r => r.id === riskId)

  if(risk) {
    let editRiskFormValues: RiskForm = {...risk}
    riskForm.value?.setValues(editRiskFormValues)
  }
  

  isActionModalOpen.value = true

}

function updateRisk(values: RiskForm) {
  let riskToEdit: Risk | undefined = undefined
  let riskIndex = 0

  risks.value.forEach((risk: Risk, index) => {
    if(risk.id === onEditRecord.value){
      riskToEdit = risk
      riskIndex = index
    }
  })

  if(riskToEdit && String(riskIndex)){
    $riskStore.updateRisk(onEditRecord.value || '', values)
    $riskStore.$state.risk = {...values, id: onEditRecord.value || ''}
    riskToEdit = { ...values }

    risks.value[riskIndex] = riskToEdit
  }
}

async function viewRiskOnSide(riskId: string) {
  $emit('sideViewContentChange', { component: RiskDetails })
  await $riskStore.fetchRisk(riskId)
}

</script>

<template>
  <FeedbackUserAction
    :text="textResult" 
    :onError="onError" 
    :isVisible="isVisible" 
    @update:isVisible="isVisible = $event" 
  />
  <div
    v-if="risks.length === 0"
    class="flex flex-col w-full h-full items-center justify-center gap-8"
  >
    <img
      :src="ilustracao"
      alt="Ilustração Novo Projeto"
      class="shrink-0 w-60 h-60"
    >

    <span class=" w-1/2 text-center text-stone-500 dark:text-stone-400">
      Insira os riscos associados ao projeto
    </span>

    <button
      class="flex items-center bg-gradient-to-br from-40% from-lavenderIndigo-900 to-tropicalIndigo-900 p-4 gap-4 rounded-md"
      @click="() => isActionModalOpen = true"
    >
      <FontAwesomeIcon
        icon="fa-solid fa-warning"
        class="text-white"
      />

      <span class="font-semibold text-white">
        Adicionar risco
      </span>
    </button>
  </div>

  <div
    v-else
    class="flex flex-col gap-5"
  >
    <div class="flex gap-5 rounded justify-between items-center text-sm">
      <div class="flex items-center gap-2 px-2 text-base">
        <FontAwesomeIcon icon="fa-solid fa-warning" />

        <span class="font-semibold px-2">Riscos do projeto</span>
      </div>

      <button
        v-if="!disableAction()"
        class="flex text-white justify-evenly items-center bg-lavenderIndigo-900 px-3 py-2 gap-4 rounded-md"
        @click="setNewRiskForm()"
      >
        <FontAwesomeIcon icon="fa-solid fa-plus" />

        <span class="font-semibold">Adicionar</span>
      </button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-4 gap-3">
      <ActionGridItem
        v-for="risk in risks"
        :key="risk.id"
        icon="warning"
        :title="risk.title"
        @edit="editRisk(risk.id)"
        @remove="removeRisk(risk.id)"
        @side-view-content-change="() => viewRiskOnSide(risk.id || '')"
      >
        <div class="flex flex-col gap-1">
          <span class="text-sm font-semibold">
            Grau de exposição
          </span>

          <span class="text-xs text-stone-500 dark:text-stone-400">
            {{ risk.exposureDegree }}
          </span>
        </div>

        <div class="flex flex-col gap-1">
          <span class="text-sm font-semibold">
            Tipo de ação
          </span>

          <span class="text-xs text-stone-500 dark:text-stone-400">
            {{ risk.type }}
          </span>
        </div>
      </ActionGridItem>
    </div>

    <div 
      v-show="$route.name === 'risks'"
      class="flex gap-5 justify-center"
    >
      <button
        class="flex text-white w-32 justify-evenly items-center bg-stone-400 dark:bg-stone-600 px-4 py-2 gap-4 rounded-md"
        @click="$router.push({ name: 'team' })"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-angle-left"
          class="text-neutral-500 dark:text-white text-xs"
        />

        <span class="font-semibold">Voltar</span>
      </button>

      <button
        class="flex text-white w-32 justify-evenly items-center bg-lavenderIndigo-900 px-4 py-2 gap-4 rounded-md"
        @click="$router.push({ name: 'functional-requirements' })"
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
    ref="riskForm"
    :validation-schema="schema"
    @submit="onSubmit"
    v-if="!formOnLoad"
  >
    <ActionModal
      v-model="isActionModalOpen"
      :title="actionModalTitle"
      icon="warning"
    >
      <div class="flex flex-col px-8 py-4 gap-5">
        <div class="grid grid-cols-2 w-full gap-5">
          <InputField
            v-for="(inputField, index) in inputFields"
            v-show="index < inputFields.length - 1"
            :key="inputField.name"
            v-slot="{ field }"
            :label="inputField.label"
            :name="inputField.name"
            :placeholder="inputField.placeholder"
            :type="inputField.type"
            :disabled="inputField.disabled"
            :value="inputField.value"
            :required="inputField.required"
            :options="inputField.options"
            @change="inputField.onChange"
          >
            {{ console.log(field.onInput) }}
          </InputField>
        </div>
        
        <div class="grid grid-cols-1 w-full gap-5">
          <InputField
            :key="inputFields[inputFields.length - 1]?.name"
            :label="inputFields[inputFields.length - 1]?.label"
            :name="inputFields[inputFields.length - 1]?.name"
            :placeholder="inputFields[inputFields.length - 1]?.placeholder"
            :type="inputFields[inputFields.length - 1]?.type"
            :required="inputFields[inputFields.length - 1]?.required"
            :options="inputFields[inputFields.length - 1]?.options"
          />
        </div>
      </div>
    </ActionModal>
  </Form>
</template>