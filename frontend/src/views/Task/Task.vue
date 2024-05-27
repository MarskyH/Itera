<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { Form } from 'vee-validate';
import * as yup from 'yup';
import { useRouter } from 'vue-router';
import yupErrorMessages from 'src/utils/yupErrorMessages';
import InputField from 'src/views/NewProject/components/InputField.vue';
import { InputFieldProps, models } from 'src/@types';
import { useProjectStore } from 'src/stores/ProjectStore';
import MaskedInput from 'src/components/MaskedInput.vue';
import { useTaskTypeStore } from "src/stores/TaskTypeStore";

interface TaskType extends models.TaskType { }
interface Project extends models.Project { }

const $projectStore = useProjectStore();
const $taskTypeStore = useTaskTypeStore();
const $router = useRouter();

const taskForm = ref<any>(null)
const formValidations: any = {};

const taskTypesOptions = ref<Array<any>>([]);
const selectedTaskType = ref<string>("");

selectedTaskType.value = "1"
const showAdditionalFields = ref<boolean>(false);

yup.setLocale(yupErrorMessages);

let inputFields: InputFieldProps[] = [
  {
    name: "title",
    label: "Título da tarefa",
    placeholder: "Digite o título da tarefa",
    required: true,
    validation: yup.string().required().min(5),
  },
  {
    name: "type",
    label: "Tipo da tarefa",
    placeholder: "Selecione",
    type: "select",
    options: [
      { value: "1", name: "Requisito", selected: false },
      { value: "2", name: "Melhoria", selected: false },
      { value: "3", name: "Bug", selected: false },
    ],
    required: true,
    validation: yup.string().required(),
    onChange: setTaskFormByType
  },
];

let inputFieldsRequirement: InputFieldProps[] = [
  {
    name: "backlog",
    label: "Backlog",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "priority",
    label: "Prioridade",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "complexity",
    label: "Complexidade",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "effort",
    label: "Esforço",
    placeholder: "Digite o valor do esforço",
    required: true,
    validation: yup.string().required().min(5),
  },
  {
    name: "size",
    label: "Tamanho",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "detail",
    label: "Detalhamento",
    placeholder: "Digite o detalhamento da tarefa",
    type: "textarea",
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "requirementeAssignee",
    label: "Responsável requisito",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "requirementProject",
    label: "Prazo requisito",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
  {
    name: "projectAssignee",
    label: "Responsável projeto",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "deadlineProject",
    label: "Prazo projeto",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
  {
    name: "frontAssignee",
    label: "Responsável front-end",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "deadlineFront",
    label: "Prazo front-end",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
  {
    name: "backAssignee",
    label: "Responsável back-end",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "deadlineBack",
    label: "Prazo back-end",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
  {
    name: "testAssignee",
    label: "Responsável testes",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "deadlineTest",
    label: "Prazo testes",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
];

let inputFieldsImprovement: InputFieldProps[] = [
  {
    name: "priority",
    label: "Prioridade",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "complexity",
    label: "Complexidade",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "effort",
    label: "Esforço",
    placeholder: "Digite o valor do esforço",
    required: true,
    validation: yup.string().required().min(5),
  },
  {
    name: "size",
    label: "Tamanho",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "detail",
    label: "Detalhamento",
    placeholder: "Digite o detalhamento da tarefa",
    type: "textarea",
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "requirementeAssignee",
    label: "Responsável requisito",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "requirementProject",
    label: "Prazo requisito",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
  {
    name: "projectAssignee",
    label: "Responsável projeto",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "deadlineProject",
    label: "Prazo projeto",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
  {
    name: "frontAssignee",
    label: "Responsável front-end",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "deadlineFront",
    label: "Prazo front-end",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
  {
    name: "backAssignee",
    label: "Responsável back-end",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "deadlineBack",
    label: "Prazo back-end",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
  {
    name: "testAssignee",
    label: "Responsável testes",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "deadlineTest",
    label: "Prazo testes",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
];

let inputFieldsBug: InputFieldProps[] = [
  {
    name: "priority",
    label: "Prioridade",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "complexity",
    label: "Complexidade",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "effort",
    label: "Esforço",
    placeholder: "Digite o valor do esforço",
    required: true,
    validation: yup.string().required().min(5),
  },
  {
    name: "size",
    label: "Tamanho",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "detail",
    label: "Detalhamento",
    placeholder: "Digite o detalhamento da tarefa",
    type: "textarea",
    required: true,
    validation: yup.string().required(),
  },
  {
    name: "frontAssignee",
    label: "Responsável front-end",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "deadlineFront",
    label: "Prazo front-end",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
  {
    name: "backAssignee",
    label: "Responsável back-end",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "deadlineBack",
    label: "Prazo back-end",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
  {
    name: "testAssignee",
    label: "Responsável testes",
    placeholder: "Selecione",
    type: "select",
    options: [{ value: "1", name: "Teste", selected: false }],
    required: false,
    validation: yup.string(),
  },
  {
    name: "deadlineTest",
    label: "Prazo testes",
    placeholder: "Digite o prazo para esta etapa",
    type: "text",
    required: false,
    validation: yup.string(),
  },
];

const typeTaskForm: {[key: string]: InputFieldProps[]} = {
  '1': inputFieldsRequirement,
  '2': inputFieldsImprovement,
  '3': inputFieldsBug
}

const schema = ref<any>(() => {
  inputFields.forEach((inputField) => {
    formValidations[inputField.name] = inputField.validation;
  });

  return yup.object(formValidations);
});

function setTaskFormByType() {
  let removeFields = Object.keys(formValidations).filter((key: string) => key !== 'title' && key !== 'type')
  removeFields.forEach((field: string) => {
    delete formValidations[field]
  })

  let type: string = taskForm.value.values.type
  let typeInputFields: InputFieldProps[] =  typeTaskForm[type]

  typeInputFields.forEach((inputField) => {
    formValidations[inputField.name] = inputField.validation;
  });

  schema.value = yup.object(formValidations)

  inputFields = inputFields.filter((field: InputFieldProps) => field.name === 'title' || field.name === 'type')
  inputFields = inputFields.concat(typeInputFields)
}

async function setTaskTypes() {
  await $taskTypeStore.fetchTaskTypes().then(() => {
    taskTypesOptions.value = $taskTypeStore.types;
  });
}

function onSubmit(values: any) {
  let projectOnCreateData: Project = { ...values };
  console.log(projectOnCreateData);
}

function handleContinue() {

}

onMounted(async () => {
  await setTaskTypes();
});
</script>

<template>
  <Form
    ref="taskForm"
    :validation-schema="schema"
    @submit="onSubmit"
    class="flex flex-col gap-10 p-5 items-center overflow-auto"
  >
    <div class="grid grid-cols-1 lg:grid-cols-2 w-full gap-5">
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

      <template v-if="showAdditionalFields">
        <template v-if="selectedTaskType === '1'">
          <div
            v-for="inputField in inputFieldsRequirement"
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
        </template>

        <template v-if="selectedTaskType === '2'">
          <div
            v-for="inputField in inputFieldsImprovement"
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
        </template>

        <template v-if="selectedTaskType === '3'">
          <div
            v-for="inputField in inputFieldsBug"
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
        </template>
      </template>
    </div>

    <div class="flex gap-5">
      <button
        class="flex text-white w-32 justify-evenly items-center bg-stone-400 dark:bg-stone-600 px-4 py-2 gap-4 rounded-md"
        @click="$router.push({ name: 'home' })"
      >
        <span>Cancelar</span>
      </button>
      <button
        v-if="!showAdditionalFields"
        @click.prevent="handleContinue"
        class="flex text-white w-32 justify-evenly items-center bg-stone-600 dark:bg-lavander-800 px-4 py-2 gap-4 rounded-md"
      >
        <span>Continuar</span>
      </button>
      <button
        v-if="showAdditionalFields"
        type="submit"
        class="flex text-white w-32 justify-evenly items-center bg-stone-600 dark:bg-lavander-800 px-4 py-2 gap-4 rounded-md"
      >
        <span>Salvar</span>
      </button>
    </div>
  </Form>
</template>
