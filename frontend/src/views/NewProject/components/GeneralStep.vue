<script setup lang="ts">
import { Form } from 'vee-validate'
import * as yup from 'yup'
import { useRouter } from 'vue-router'
import yupErrorMessages from 'src/utils/yupErrorMessages';
import InputField from 'src/views/NewProject/components/InputField.vue'
import { InputFieldProps, models } from 'src/@types';
import { useProjectStore } from 'src/stores/ProjectStore';
import router from 'src/router';

interface Project extends models.Project {}

const $projectStore = useProjectStore()

const $router = useRouter()

yup.setLocale(yupErrorMessages);

async function createProject(projectOnCreateData: Project) {
  await $projectStore.createProject(projectOnCreateData)
    .then(async (response: any) => {
      console.log(response)
      if(response.status === 200) {
        
        const projectId : string = response.data.id

        console.log("PROJECT ID: ", projectId)
      
        await $projectStore.fetchProject(projectId).then(async ()=>{
          $router.push({name:'roles'})
        })
      } else {
        alert('Falha ao criar projeto!')
      }
    }
  )
}

function onSubmit(values: any) {
  let projectOnCreateData: Project = {...values}

  createProject(projectOnCreateData)
}

const inputFields: InputFieldProps[] = [
  {
    name: "name",
    label: "Nome do Projeto",
    placeholder: "Digite o nome do projeto",
    required: true,
    validation: yup.string().required().min(5)
  },
  {
    name: "clientName",
    label: "Nome do Cliente",
    placeholder: "Digite o nome do cliente",
    required: true,
    validation: yup.string().required().min(5)
  },
  {
    name: "deadline",
    label: "Prazo",
    placeholder: "Digite o prazo em dias úteis",
    required: true,
    validation: yup.number().required().min(1)
  },
  {
    name: "workload",
    label: "Carga Horária Diária",
    placeholder: "Digite a carga horária diária do projeto",
    required: true,
    validation: yup.number().required().min(1)
  },
  {
    name: "iterationTime",
    label: "Tempo de iteração",
    placeholder: "Digite o tempo de iteração em dias úteis",
    required: true,
    validation: yup.number().required().min(1)
  }
]

let formValidations: any = {}
inputFields.forEach(inputField => formValidations[inputField.name] = inputField.validation)
const schema = yup.object(formValidations);

</script>

<template>
  <Form
    :validation-schema="schema"
    @submit="onSubmit"
    class="flex flex-col gap-10 p-5 items-center"
  >
    <div class="grid grid-cols-1 lg:grid-cols-2 w-full gap-5">
      <InputField
        v-for="inputField in inputFields"
        :key="inputField.name"
        :label="inputField.label"
        :name="inputField.name"
        :placeholder="inputField.placeholder"
        :required="inputField.required"
      />
    </div>

    <div class="flex gap-5">
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
  </Form>
</template>