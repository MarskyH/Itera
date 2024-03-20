<script setup lang="ts">

import { onMounted, ref } from "vue";
import { Form } from 'vee-validate'
import * as yup from 'yup'

import ilustracao from "src/assets/ilustracaoRiscos.png"
import ActionModal from "src/components/ActionModal.vue";
import InputField from 'src/views/NewProject/components/InputField.vue'
import yupErrorMessages from 'src/utils/yupErrorMessages';
import ActionGridItem from "src/views/NewProject/components/ActionGridItem.vue";
import { InputFieldProps, RiskForm, TeamMemberForm, models } from "src/@types";
import { useRiskStore } from "src/stores/RiskStore";
import { useProjectStore } from "src/stores/ProjectStore";

interface Risk extends models.Risk { }

const $riskStore = useRiskStore()
const $projectStore = useProjectStore()

const isActionModalOpen = ref<boolean>(false)
const onEditRecord = ref<string | null>(null)
const actionModalTitle = ref<string>('Adicionar risco')

const riskTypeOptions = ['Contigência', 'Mitigação']
const degreeOptions = ['Alto', 'Médio', 'Baixo']

const riskForm = ref<any>(null)

const risks = ref<Risk[]>([])

yup.setLocale(yupErrorMessages);

const setOptions = (options: string[]) => {
  return options.length > 0 ? options.map((option: string) => {
    return {
      value: option,
      name: option,
      selected: false
    }
  }) : []
}

let inputFields: InputFieldProps[] = []

let formValidations: any = {}
inputFields.forEach(inputField => formValidations[inputField.name] = inputField.validation)
const schema = yup.object(formValidations);

async function setRisks() {
  await $riskStore.fetchRisks($projectStore.project.id || "").then(() => {
    risks.value = $riskStore.risks
  })
}

onMounted(async () => {
  await setRisks().then(async () => {
    inputFields = [
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
        validation: yup.number().required().min(1)
      },
      {
        name: "probability",
        label: "Probabilidade",
        placeholder: "Selecione a probabilidade",
        required: true,
        options: setOptions(degreeOptions),
        validation: yup.string().required()
      },
      {
        name: "impact",
        label: "Impacto",
        placeholder: "Selecione a probabilidade",
        required: true,
        options: setOptions(degreeOptions),
        validation: yup.string().required()
      },
      {
        name: "exposureDegree",
        label: "Grau de exposição",
        placeholder: "Informe o grau de exposição do risco",
        required: true,
        options: setOptions(degreeOptions),
        validation: yup.string().required().min(3)
      },
      {
        name: "type",
        label: "Tipo de ação",
        placeholder: "Selecione o tipo de ação",
        required: true,
        options: setOptions(riskTypeOptions),
        validation: yup.string().required()
      },
      {
        name: "description",
        label: "Descrição da ação",
        placeholder: "Digite a ação a ser tomada com esse risco",
        required: true,
        validation: yup.string().required().min(3)
      },
    ]
  });
})

async function createRisk(riskFormValues: RiskForm) {
  const projectId = $projectStore.project.id || ''

  await $riskStore.createRisk(riskFormValues, projectId)
    .then((responseStatus: any) => {
      if (responseStatus === 200) {
        setRisks()
      } else {
        alert('Falha ao criar risco!')
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
  risks.value = risks.value.filter((risk: Risk) => risk.id !== riskId)
}


function editRisk(riskId: string | undefined) {
  /*
  onEditRecord.value = memberId ? memberId : null
  actionModalTitle.value = 'Editar integrante'

  let member: TeamMember | undefined = teamMembers.value.find((member: TeamMember) => member.id === memberId)
  
  if (member) {
    let editTeamMemberValues: TeamMemberForm = {
      ...member,
      roleId: member.role.id || '',
      userId: member.user.id || ''
    }

    teamMemberForm.value.setValues(editTeamMemberValues)

    let roleField = inputFields.find((field: any) => {
      field.name === 'role'
    })

    if(roleField) {
      let selectedOption = roleField?.options?.find(option => option.value === editTeamMemberValues.roleId)
      
      if (selectedOption) {
        selectedOption.selected = true
      }
    }

    let userField = inputFields.find((field: any) => {
      field.name === 'user'
    })

    if(userField) {
      let selectedOption = userField?.options?.find(option => option.value === editTeamMemberValues.userId)
      
      if (selectedOption) {
        selectedOption.selected = true
      }
    }
  }

  isActionModalOpen.value = true
  */
}

function updateRisk(values: RiskForm) {
  /*
  let teamMemberToEdit: TeamMember | undefined = undefined
  let teamMemberIndex = null

  teamMembers.value.forEach((member: TeamMember, index) => {
    if(member.id === onEditRecord.value) {
      teamMemberToEdit = member
      teamMemberIndex = index
    }
  }) 

  if (teamMemberToEdit && teamMemberIndex) {
    let teamMemberRole: Role | undefined = roles.value.find((role: Role) => role.id === values.roleId)
    let teamMemberUser: UserMemberModel | undefined = users.value.find((user: UserMemberModel) => user.id === values.userId)
    teamMemberToEdit = { 
      ...values,
      roleId: teamMemberRole?.id || "",
      userId: teamMemberUser?.id || ""
    }

    teamMembers.value[teamMemberIndex] = teamMemberToEdit
  }*/
}

</script>

<template>
  <div v-if="risks.length === 0" class="flex flex-col w-full h-full items-center justify-center gap-8">
    <img :src="ilustracao" alt="Ilustração Novo Projeto" class="shrink-0 w-60 h-60">

    <span class=" w-1/2 text-center text-stone-500 dark:text-stone-400">
      Insira os riscos associados ao projeto
    </span>

    <button
      class="flex items-center bg-gradient-to-br from-40% from-lavenderIndigo-900 to-tropicalIndigo-900 p-4 gap-4 rounded-md"
      @click="() => isActionModalOpen = true">
      <FontAwesomeIcon icon="fa-solid fa-warning" class="text-white" />

      <span class="font-semibold text-white">
        Adicionar risco
      </span>
    </button>
  </div>

  <div v-else class="flex flex-col gap-5">
    <div class="flex gap-5 rounded justify-between items-center text-sm">
      <div class="flex items-center gap-2 px-2 text-base">
        <FontAwesomeIcon icon="fa-solid fa-warning" />

        <span class="font-semibold px-2">Riscos do projeto</span>
      </div>

      <button class="flex text-white justify-evenly items-center bg-lavenderIndigo-900 px-3 py-2 gap-4 rounded-md"
        @click="setNewRiskForm()">
        <FontAwesomeIcon icon="fa-solid fa-plus" />

        <span class="font-semibold">Adicionar</span>
      </button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-4 gap-3">
      <ActionGridItem v-for="risk in risks" :key="risk.id" icon="warning" :title="risk.title" @edit="editRisk(risk.id)"
        @remove="removeRisk(risk.id)">
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

    <div class="flex gap-5 justify-center">
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

  <Form ref="riskForm" :validation-schema="schema" @submit="onSubmit">
    <ActionModal v-model="isActionModalOpen" :title="actionModalTitle" icon="warning">
      <div class="grid grid-cols-2 w-full gap-5 px-8 py-4">
        <InputField v-for="inputField in inputFields" :key="inputField.name" :label="inputField.label"
          :name="inputField.name" :placeholder="inputField.placeholder" :required="inputField.required"
          :options="inputField.options" />
      </div>

    </ActionModal>
  </Form>
</template>