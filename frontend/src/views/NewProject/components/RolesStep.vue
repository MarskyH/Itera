<script setup lang="ts">

import { onMounted, ref } from "vue";
import { Form } from 'vee-validate'
import * as yup from 'yup'

import ilustracao from "src/assets/ilustracaoPapeis.png"
import ActionModal from "src/components/ActionModal.vue";
import InputField from 'src/views/NewProject/components/InputField.vue'
import yupErrorMessages from 'src/utils/yupErrorMessages';
import ActionGridItem from "src/views/NewProject/components/ActionGridItem.vue";
import RoleDetails from "src/views/Project/components/RoleDetails.vue";
import { InputFieldProps, RoleForm, models } from "src/@types";
import { useRoleStore } from "src/stores/RoleStore";
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

interface Role extends models.Role { }

const $emit = defineEmits(['sideViewContentChange'])

const $route = useRoute()
const $roleStore = useRoleStore()

const isActionModalOpen = ref<boolean>(false)

const roleForm = ref<any>(null)
const onEditRecord = ref<string | null>(null)
const actionModalTitle = ref<string>('Adicionar papel')

const roles = ref<Role[]>([])

yup.setLocale(yupErrorMessages);

const inputFields: InputFieldProps[] = [
  {
    name: "function",
    label: "Papel",
    placeholder: "Digite o papel no projeto",
    required: true,
    validation: yup.string().required().min(5)
  },
  {
    name: "skill",
    label: "Habilidade(s)",
    placeholder: "Digite as habilidades do papel",
    required: true,
    validation: yup.string().required().min(5)
  },
  {
    name: "competency",
    label: "Competência(s)",
    placeholder: "Digite as competências do papel",
    required: true,
    validation: yup.string().required().min(5)
  }
]

let formValidations: any = {}
inputFields.forEach(inputField => formValidations[inputField.name] = inputField.validation)
const schema = yup.object(formValidations);

async function setRoles() {

  await $roleStore.fetchRoles(String($route.params.projectId)).then(() => {
    roles.value = $roleStore.roles
  })
}

onMounted(() => setRoles())

async function createRole(roleFormValues: RoleForm) {
  const projectId: string = String($route.params.projectId)

  await $roleStore.createRole(projectId, roleFormValues)
    .then((responseStatus: any) => {
      if (responseStatus === 200) {
        isVisible.value = true
        onError.value = false
        textResult.value = "Informações cadastradas com sucesso."
        setRoles()
      } else {
        isVisible.value = true
        onError.value = true
        textResult.value = "Ocorreu um erro. Por favor tente novamente."
      }
    }
    )
}

function onSubmit(values: any) {
  let roleFormValues: RoleForm = { ...values }

  if (!onEditRecord.value) {
    createRole(roleFormValues)
  } else {
    updateRole(roleFormValues)
  }

  isActionModalOpen.value = false
}

function setNewRoleForm() {
  actionModalTitle.value = 'Adicionar papel'
  roleForm.value.resetForm()
  isActionModalOpen.value = true
}

function removeRole(roleId: string | undefined) {
  $roleStore.deleteRole(roleId || '')
  roles.value = roles.value.filter(role => role.id !== roleId)
}

function editRole(roleId: string | undefined) {
  onEditRecord.value = roleId ? roleId : null
  actionModalTitle.value = 'Editar papel'

  let role: Role | undefined = roles.value.find(r => r.id === roleId)

  if (role) {
    let editRoleFormValues: RoleForm = { ...role }
    roleForm.value?.setValues(editRoleFormValues)
  }

  isActionModalOpen.value = true
}

function updateRole(values: RoleForm) {
  let roleToEdit: Role | undefined = undefined
  let roleIndex = 0

  roles.value.forEach((role: Role, index) => {
    if (role.id === onEditRecord.value) {
      roleToEdit = role
      roleIndex = index
    }
  })

  if (roleToEdit && String(roleIndex)) {
    $roleStore.updateRole(onEditRecord.value || '', values)
    $roleStore.$state.role = {...values, id: onEditRecord.value || ''}
    roleToEdit = { ...values }

    roles.value[roleIndex] = roleToEdit
  }
}

async function viewRoleOnSide(roleId: string) {
  $emit('sideViewContentChange', { component: RoleDetails })
  await $roleStore.fetchRole(roleId)
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
    v-if="roles.length === 0"
    class="flex flex-col w-full h-full items-center justify-center gap-8"
  >
    <img
      :src="ilustracao"
      alt="Ilustração Novo Projeto"
      class="shrink-0 w-40 h-40"
    >

    <span class=" w-1/2 text-center text-stone-500 dark:text-stone-400">
      Adicione os papéis da equipe do projeto para dividir as habilidades e competências
    </span>

    <button
      class="flex items-center bg-gradient-to-br from-40% from-lavenderIndigo-900 to-tropicalIndigo-900 p-4 gap-4 rounded-md"
      @click="() => isActionModalOpen = true"
    >
      <FontAwesomeIcon
        icon="fa-solid fa-clipboard-user"
        class="text-white"
      />

      <span class="font-semibold text-white">
        Adicionar papel
      </span>
    </button>
  </div>

  <div
    v-else
    class="flex flex-col gap-5"
  >
    <div class="flex gap-5 rounded justify-between items-center text-sm">
      <div class="flex items-center gap-2 px-2 text-base">
        <FontAwesomeIcon icon="fa-solid fa-clipboard-user" />

        <span class="font-semibold px-2">Papéis do projeto</span>
      </div>

      <button
        v-if="!disableAction()"
        class="flex text-white justify-evenly items-center bg-lavenderIndigo-900 px-3 py-2 gap-4 rounded-md"
        @click="setNewRoleForm()"
      >
        <FontAwesomeIcon icon="fa-solid fa-plus" />

        <span class="font-semibold">Adicionar</span>
      </button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-4 gap-3">
      <ActionGridItem
        v-for="role in roles"
        :key="role.id"
        icon="clipboard-user"
        :title="role.function"
        @edit="editRole(role.id)"
        @remove="removeRole(role.id)"
        @side-view-content-change="() => viewRoleOnSide(role.id || '')"
      >
        <div class="flex flex-col gap-1">
          <span class="text-sm font-semibold">
            Habilidades
          </span>

          <span class="text-xs text-stone-500 dark:text-stone-400">
            {{ role.skill }}
          </span>
        </div>

        <div class="flex flex-col gap-1">
          <span class="text-sm font-semibold">
            Competências
          </span>

          <span class="text-xs text-stone-500 dark:text-stone-400">
            {{ role.competency }}
          </span>
        </div>
      </ActionGridItem>
    </div>

    <div
      v-show="$route.name === 'roles'"
      class="flex gap-5 justify-center" 
    >
      <button
        class="flex text-white w-32 justify-evenly items-center bg-stone-400 dark:bg-stone-600 px-4 py-2 gap-4 rounded-md"
        @click="$router.push({ name: 'new-project' })"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-angle-left"
          class="text-neutral-500 dark:text-white text-xs"
        />

        <span class="font-semibold">Voltar</span>
      </button>

      <button
        class="flex text-white w-32 justify-evenly items-center bg-lavenderIndigo-900 px-4 py-2 gap-4 rounded-md"
        @click="$router.push({ name: 'team' })"
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
    ref="roleForm"
    :validation-schema="schema"
    @submit="onSubmit"
  >
    <ActionModal
      v-model="isActionModalOpen"
      :title="actionModalTitle"
      icon="clipboard-user"
    >
      <div class="flex flex-col w-full gap-5 px-8 py-4">
        <InputField
          v-for="inputField in inputFields"
          :key="inputField.name"
          :label="inputField.label"
          :name="inputField.name"
          :placeholder="inputField.placeholder"
          :required="inputField.required"
        />
      </div>
    </ActionModal>
  </Form>
</template>