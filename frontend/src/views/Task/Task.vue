<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">

import { onMounted, ref } from "vue";
import { Form } from 'vee-validate'
import * as yup from 'yup'
import { useRouter } from 'vue-router'
import yupErrorMessages from 'src/utils/yupErrorMessages';
import InputField from 'src/views/NewProject/components/InputField.vue'
import { InputFieldProps, models } from 'src/@types';
import { useProjectStore } from 'src/stores/ProjectStore';
import MaskedInput from 'src/components/MaskedInput.vue';
import { useTaskTypeStore } from "src/stores/TaskTypeStore";

interface TaskType extends models.TaskType { }

interface Project extends models.Project { }

const $projectStore = useProjectStore()
const $taskTypeStore = useTaskTypeStore()

const $router = useRouter()

const taskTypesOptions = ref<Array<any>>([])

yup.setLocale(yupErrorMessages);

let inputFields: InputFieldProps[] = []


function getTaskTypeOptions() {
  return taskTypesOptions.value.length > 0
    ? taskTypesOptions.value.map((type: TaskType) => {
      return {
        value: type.id ? type.id : '',
        name: type?.name,
        selected: false
      }
    })
    : []
}

async function setTaskTypes() {
  await $taskTypeStore.fetchTaskTypes().then(() => {
    taskTypesOptions.value = $taskTypeStore.types
  })
}


/*
async function createProject(projectOnCreateData: Project) {
    await $projectStore.createProject(projectOnCreateData)
        .then(async (response: any) => {
            console.log(response)
            if (response.status === 200) {

                const projectId: string = response.data.id

                await $projectStore.fetchProject(projectId).then(async () => {
                    $router.push({ name: 'roles', params: { projectId: $projectStore.project.id } })
                })
            } else {
                alert('Falha ao criar projeto!')
            }
        }
        )
}*/

function onSubmit(values: any) {
  let projectOnCreateData: Project = { ...values }

  //createProject(projectOnCreateData)
}
onMounted(async () => {
 // await setTaskTypes().then(async () => {
       // taskTypesOptions.value = getTaskTypeOptions()
        inputFields = [
          {
            name: "title",
            label: "Título da tarefa",
            placeholder: "Digite o título da tarefa",
            required: true,
            validation: yup.string().required().min(5)
          },
          {
            name: "type",
            label: "Tipo da tarefa",
            placeholder: "Selecione",
            type: "select",
            options: [{value: "1", name: "Teste", selected: false}],
            required: true,
            validation: yup.string().required()
          },
          {
            name: "backlog",
            label: "Backlog",
            placeholder: "Selecione",
            type: "select",
            options: [{value: "1", name: "Teste", selected: false}],
            required: true,
            validation: yup.string().required()
          },
          {
            name: "priority",
            label: "Prioridade",
            placeholder: "Selecione",
            type: "select",
            options: [{value: "1", name: "Teste", selected: false}],
            required: true,
            validation: yup.string().required()
          },
          {
            name: "complexity",
            label: "Prioridade",
            placeholder: "Selecione",
            type: "select",
            options: [{value: "1", name: "Teste", selected: false}],
            required: true,
            validation: yup.string().required()
          },
          {
            name: "effort",
            label: "Esforço",
            placeholder: "Digite o valor do esforço",
            required: true,
            validation: yup.string().required().min(5)
          },
          {
            name: "size",
            label: "Tamanho",
            placeholder: "Selecione",
            type: "select",
            options: [{value: "1", name: "Teste", selected: false}],
            required: true,
            validation: yup.string().required()
          },
          {
            name: "detail",
            label: "Detalhamento",
            placeholder: "Digite o detalhamento da tarefa",
            type: "textarea",
            required: true,
            validation: yup.string().required()
          },
        ]
        inputFields.forEach(inputField => formValidations[inputField.name] = inputField.validation)
      });
  //  })



let formValidations: any = {}
inputFields.forEach(inputField => formValidations[inputField.name] = inputField.validation)
const schema = yup.object(formValidations);

</script>

<template>
  <Form :validation-schema="schema" @submit="onSubmit" class="flex flex-col gap-10 p-5 items-center">
    <div class="grid grid-cols-1 lg:grid-cols-2 w-full gap-5">
      <div v-for="inputField in inputFields" :key="inputField.name">
        <MaskedInput v-if="inputField.mask" v-bind="inputField" />

        <InputField v-else v-bind="inputField" />
      </div>
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