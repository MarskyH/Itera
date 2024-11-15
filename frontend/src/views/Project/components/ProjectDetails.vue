<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { Form } from 'vee-validate'
import * as yup from 'yup'
import yupErrorMessages from 'src/utils/yupErrorMessages';

import { InputFieldProps, models } from 'src/@types';
import { useProjectStore } from "src/stores/ProjectStore";

import ActionModal from "src/components/ActionModal.vue";
import InputField from 'src/views/NewProject/components/InputField.vue'
import MaskedInput from "src/components/MaskedInput.vue";
import LocalStorage from "src/services/localStorage";

const storage = new LocalStorage();

let userRole = storage.getLoggedUser()?.role || ''

function disableAction(){
  return (userRole !== 'Gerente')
}

interface ProjectOnView extends models.ProjectOnView {}
interface Project extends models.Project {}

const projectDefault = {
  id: "",
  name: "",
  deadline: 0,
  workHours: 0,
  iterationTime: 0,
  clientName: "",
  createdBy: "",
  roles: []
}

const $projectStore = useProjectStore()

const $route = useRoute()
const $router = useRouter()

const project = ref<ProjectOnView>({...projectDefault})

const onEdit = ref<boolean>(false)
const onDelete = ref<boolean>(false)
const formOnLoad = ref<boolean>(true)

yup.setLocale(yupErrorMessages);

let inputFields: InputFieldProps[] = []

let formValidations: any = {}
let schema: any

async function updateProject(projectOnCreateData: Project) {
  const projectId: string = String($route.params.projectId) || ''

  await $projectStore.updateProject(projectId ,projectOnCreateData)
    .then((response: any) => {
      if(response === 200) {
        $projectStore.$state.project = {...projectOnCreateData, id: projectId}
        onEdit.value = false
      }
    }
  )
}

async function deleteProject() {
  if (onDelete.value) {
    const projectId: string = String($route.params.projectId) || ''
    
    await $projectStore.deleteProject(projectId)
      .then((response) => {
         console.log(response)

        if(response === 204 || response === 200) {
          $router.push({ name: 'my-projects' })
        }
      })

  } else {
    onDelete.value = true
  }

}

onMounted(async () => {
  await $projectStore.fetchProject(String($route.params.projectId)).then(async () => {
    project.value = { 
      ...project.value,
      ...$projectStore.project,
    }
    
    inputFields = [  
      {
        name: "name",
        label: "Nome do Projeto",
        placeholder: "Digite o nome do projeto",
        required: true,
        value: project.value.name,
        validation: yup.string().required().min(5)
      },
      {
        name: "clientName",
        label: "Nome do Cliente",
        placeholder: "Digite o nome do cliente",
        required: true,
        value: project.value.clientName,
        validation: yup.string().required().min(5)
      },
      {
        name: "deadline",
        label: "Prazo",
        placeholder: "Digite o prazo em dias úteis",
        required: true,
        value: String(project.value.deadline),
        validation: yup.number().required().min(1)
      },
      {
        name: "workHours",
        label: "Carga Horária Diária",
        placeholder: "00:00",
        required: true,
        mask: "time",
        value: String(project.value.workHours),
        validation: yup.string().required()
      },
      {
        name: "iterationTime",
        label: "Tempo de iteração",
        placeholder: "Digite o tempo de iteração em dias úteis",
        required: true,
        value: String(project.value.iterationTime),
        validation: yup.number().required().min(1)
      }
    ]

    inputFields.forEach(inputField => formValidations[inputField.name] = inputField.validation)
    schema =  yup.object(formValidations);
    
    formOnLoad.value = false

  })
})

$projectStore.$subscribe(() => {
  project.value = {...$projectStore.project, roles: []}
})

</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="flex gap-3 items-center">
      <FontAwesomeIcon
        icon="fa-solid fa-circle-info"
        class="text-stone-500 dark:text-stone-400"
      />

      <span class="font-bold">Detalhes do projeto</span>
    </div>

    <div class="flex flex-col text-xs p-1 gap-4">
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-folder"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Nome</span>

          <span>{{ project.name }}</span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-handshake-simple"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Cliente</span>

          <span>{{ project.clientName }}</span>
        </div>
      </div>
          
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-clock"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Prazo</span>

          <span> {{ project.deadline + ' dias úteis' }} </span>
        </div>
      </div>

      <div class="flex gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-hourglass-half"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Tempo de iteração</span>

          <span>{{ project.iterationTime + ' dias úteis' }}</span>
        </div>
      </div>
          
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-business-time"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Carga horária</span>

          <span>{{ project.workHours + 'h' }}</span>
        </div>
      </div>
    </div>
    
    <div 
      class="flex justify-end gap-5" 
      v-if="!disableAction()">
      <button
        class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-blueCrayola-900 dark:text-naplesYellow-900 hover:bg-blueCrayola-900/25 hover:dark:bg-naplesYellow-900/25 text-sm gap-2 text"
        @click="()=> onEdit = true"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-pen"
        />

        <span class="font-semibold">Editar</span>
      </button>

      <button
        class="flex items-center rounded px-3 py-2 text-sm gap-2 text"
        :class="[onDelete ? 'bg-lightRed-900 text-white' : 'bg-platinum-900 dark:bg-davysGray-900 text-lightRed-900 hover:bg-lightRed-900/25 hover:dark:bg-lightRed-900/25']"
        @click="()=> deleteProject()"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-trash"
        />

        <span class="font-semibold">{{ onDelete ? 'Confirmar' : 'Excluir projeto' }}</span>
      </button>
    </div>
  </div>

  <Form
    v-if="!formOnLoad"    
    :validation-schema="schema"
    @submit="(values: any) => updateProject(values)"
  >
    <ActionModal
      v-model="onEdit"
      title="Editar Projeto"
      icon="folder"
    >
      <div class="flex flex-col w-full gap-5 px-8 py-4">
        <div 
          v-for="inputField in inputFields"
          :key="inputField.name"
        >
          <MaskedInput
            v-if="inputField.mask"
            v-bind="inputField"
          />

          <InputField
            v-else
            v-bind="inputField"
          />
        </div>
      </div>
    </ActionModal>
  </Form>
</template>
