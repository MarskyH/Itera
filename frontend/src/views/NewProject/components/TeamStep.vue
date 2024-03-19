<script setup lang="ts">

import { onMounted, ref } from "vue";
import { Form } from 'vee-validate'
import * as yup from 'yup'

import ilustracao from "src/assets/ilustracaoEquipe.png"
import ActionModal from "src/components/ActionModal.vue";
import InputField from 'src/views/NewProject/components/InputField.vue'
import yupErrorMessages from 'src/utils/yupErrorMessages';
import ActionGridItem from "src/views/NewProject/components/ActionGridItem.vue";
import { InputFieldProps, TeamMemberForm, models } from "src/@types";
import { useTeamMemberStore } from "src/stores/TeamMemberStore";
import { useRoleStore } from "src/stores/RoleStore";
import { useProjectStore } from "src/stores/ProjectStore";
import { useUserStore } from "src/stores/UserStore";

interface TeamMember extends models.TeamMember {}
interface Role extends models.Role {}
interface UserModel extends models.UserModel{}

const $teamMemberStore = useTeamMemberStore()
const $userStore = useUserStore()
const $roleStore = useRoleStore()
const $projectStore = useProjectStore()

const isActionModalOpen = ref<boolean>(false)
const onEditRecord = ref<string | null>(null)
const actionModalTitle = ref<string>('Adicionar integrante')

const roleOptions = ref<Array<any>>([])
const userOptions = ref<Array<any>>([])

const teamMemberForm = ref<any>(null)

const users = ref<UserModel[]>([])

const roles = ref<Role[]>([/*
  {
    id: 1,
    name: 'Desenvolvedor Front-end',
    skills: 'Capacidade de trabalhar em squads multidisciplinares',
    abilities: 'Ter conhecimento de plataformas móveis, como iOS e Android'
  },
  {
    id: 2,
    name: 'Desenvolvedor Back-end',
    skills: 'Capacidade de trabalhar em squads multidisciplinares',
    abilities: 'Ter conhecimento de plataformas móveis, como iOS e Android'
  },
  {
    id: 3,
    name: 'Desenvolvedor Back-end',
    skills: 'Capacidade de trabalhar em squads multidisciplinares',
    abilities: 'Ter conhecimento de plataformas móveis, como iOS e Android'
  }*/
])

const teamMembers = ref<TeamMember[]>([
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

function getUserOptions () {
  return users.value.length > 0 
    ? users.value.map((user: UserModel) => {
      return { 
        value: user.id ? user.id : '', 
        name: user?.name, 
        selected: false
      }
    }) 
    : []
}

function getRolesOptions () {
  return roles.value.length > 0 
    ? roles.value.map((role: Role) => {
      return { 
        value: role.id ? role.id : '', 
        name: role?.function, 
        selected: false
      }
    }) 
    : []
}

let inputFields: InputFieldProps[] = []

let formValidations: any = {}
inputFields.forEach(inputField => formValidations[inputField.name] = inputField.validation)
const schema = yup.object(formValidations);

async function setUsers() {
  await $userStore.fetchUsers().then(() => {
    users.value = $userStore.users
    console.log(users.value)
  })
}

async function setRoles() {
  await $roleStore.fetchRoles($projectStore.project.id ? $projectStore.project.id : "").then(() => {
    roles.value = $roleStore.roles
    console.log(users.value)
  })
}

async function setTeamMembers() {

  const projectId = $projectStore.project.id || ''

  await $teamMemberStore.fetchTeamMembers(projectId).then(() => {
    teamMembers.value = $teamMemberStore.teamMembers
  })
}

onMounted(async () => {
  await setUsers().then(async ()=>{
    await setRoles().then(async ()=>{
      await setTeamMembers().then(async ()=>{
        roleOptions.value = getRolesOptions()
        userOptions.value = getUserOptions()
        inputFields =  [
          {
            name: "user",
            label: "Nome",
            placeholder: "Selecione o integrante",
            required: true,
            options: userOptions.value,
            validation: yup.string().required().min(3)
          },
          {
            name: "hourlyRate",
            label: "Valor hora-homem",
            placeholder: "R$ 0,00",
            required: true,
            validation: yup.number().required().min(1)
          },
          {
            name: "dedicatedHours",
            label: "Horas dedicadas",
            placeholder: "0",
            required: true,
            validation: yup.number().required().min(1)
          },
          {
            name: "role",
            label: "Papel",
            placeholder: "Selecione o papel",
            required: true,
            options: roleOptions.value,
            validation: yup.string().required()
          }
        ]
      });
    })
  })
  
  

  console.log($roleStore.roles, $userStore.users)
})

async function createTeamMember(teamMemberFormValues: TeamMemberForm) {
  const projectId = $projectStore.project.id || ''

  await $teamMemberStore.createTeamMember(teamMemberFormValues, projectId)
    .then((responseStatus: any) => {
      if(responseStatus === 200) {
        setTeamMembers()
      } else {
        alert('Falha ao criar integrante!')
      }
    }
  )
}

function onSubmit(values: any) {
  let teamMemberFormValues: TeamMemberForm = {...values}

  if(!onEditRecord.value) {
    createTeamMember(teamMemberFormValues)
  } else {
    updateTeamMember(teamMemberFormValues)
  }

  isActionModalOpen.value = false
}

function setNewTeamMemberForm() {
  actionModalTitle.value = 'Adicionar integrante'
  teamMemberForm.value.resetForm()
  isActionModalOpen.value = true
}

function removeTeamMember(memberId: string | undefined) {
  teamMembers.value = teamMembers.value.filter((member: TeamMember) => member.id !== memberId)
}


function editTeamMember(memberId: string | undefined) {
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

function updateTeamMember(values: TeamMemberForm) {
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
  <div
    v-if="teamMembers.length === 0"
    class="flex flex-col w-full h-full items-center justify-center gap-8"
  >
    <img
      :src="ilustracao"
      alt="Ilustração Novo Projeto"
      class="shrink-0 w-40 h-40"
    >

    <span class=" w-1/2 text-center text-stone-500 dark:text-stone-400">
      Adicione os integrantes da equipe do projeto que desempenharão os papéis
    </span>

    <button
      class="flex items-center bg-gradient-to-br from-40% from-lavenderIndigo-900 to-tropicalIndigo-900 p-4 gap-4 rounded-md"
      @click="() => isActionModalOpen = true"
    >
      <FontAwesomeIcon
        icon="fa-solid fa-user-plus"
        class="text-white"
      />

      <span class="font-semibold text-white">
        Adicionar integrante
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
          icon="fa-solid fa-user"
        />
        
        <span class="font-semibold px-2">Integrantes do projeto</span>
      </div>

      <button
        class="flex text-white justify-evenly items-center bg-lavenderIndigo-900 px-3 py-2 gap-4 rounded-md"
        @click="setNewTeamMemberForm()"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-plus"
        />
        
        <span class="font-semibold">Adicionar</span>
      </button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-4 gap-3">
      <ActionGridItem
        v-for="member in teamMembers"
        :key="member.id"
        icon="user"
        :title="member.user.name"
        @edit="editTeamMember(member.id)"
        @remove="removeTeamMember(member.id)"
      >
        <div class="flex flex-col gap-1">
          <span class="text-sm font-semibold">
            Papel
          </span>

          <span class="text-xs text-stone-500 dark:text-stone-400">
            {{ member.role.function }}
          </span>
        </div>

        <div class="flex flex-col gap-1">
          <span class="text-sm font-semibold">
            Valor hora-homem
          </span>

          <span class="text-xs text-stone-500 dark:text-stone-400">
            {{ member.hourlyRate }}
          </span>
        </div>

        <div class="flex flex-col gap-1">
          <span class="text-sm font-semibold">
            Horas dedicadas
          </span>

          <span class="text-xs text-stone-500 dark:text-stone-400">
            {{ member.dedicatedHours }}
          </span>
        </div>
      </ActionGridItem>
    </div>

    <div class="flex gap-5 justify-center">
      <button
        class="flex text-white w-32 justify-evenly items-center bg-stone-400 dark:bg-stone-600 px-4 py-2 gap-4 rounded-md"
        @click="$router.push({ name: 'roles' })"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-angle-left"
          class="text-neutral-500 dark:text-white text-xs"
        />

        <span class="font-semibold">Voltar</span>
      </button>
      
      <button
        class="flex text-white w-32 justify-evenly items-center bg-lavenderIndigo-900 px-4 py-2 gap-4 rounded-md"
        @click="$router.push({ name: 'risks' })"
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
    ref="teamMemberForm"
    :validation-schema="schema"
    @submit="onSubmit"
  >
    <ActionModal
      v-model="isActionModalOpen"
      :title="actionModalTitle"
      icon="user-plus"
    >
      <div
        class="flex flex-col w-full gap-5 px-8 py-4"
      >
        <InputField
          v-for="inputField in inputFields"
          :key="inputField.name"
          :label="inputField.label"
          :name="inputField.name"
          :placeholder="inputField.placeholder"
          :required="inputField.required"
          :options="inputField.options"
        />
      </div>
    </ActionModal>
  </Form>
</template>