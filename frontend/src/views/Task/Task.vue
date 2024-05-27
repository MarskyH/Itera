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

const taskTypesOptions = ref<Array<any>>([]);
const selectedTaskType = ref<string>("");
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
];

const formValidations: any = {};

const schema = computed(() => {
  inputFields.forEach((inputField) => {
    formValidations[inputField.name] = inputField.validation;
  });

  if (showAdditionalFields.value) {
    const selectedType = selectedTaskType.value;
    if (selectedType === "1") {
      inputFieldsRequirement.forEach((inputField) => {
        formValidations[inputField.name] = inputField.validation;
      });
    } else if (selectedType === "2") {
      inputFieldsImprovement.forEach((inputField) => {
        formValidations[inputField.name] = inputField.validation;
      });
    } else if (selectedType === "3") {
      inputFieldsBug.forEach((inputField) => {
        formValidations[inputField.name] = inputField.validation;
      });
    }
  }

  return yup.object().shape(formValidations);
});

async function setTaskTypes() {
  await $taskTypeStore.fetchTaskTypes().then(() => {
    taskTypesOptions.value = $taskTypeStore.types;
  });
}

function onSubmit(values: any) {
  let projectOnCreateData: Project = { ...values };
  console.log(projectOnCreateData);
  // Implement project creation logic here
}

function handleContinue() {
  showAdditionalFields.value = true;
  const selectedType = selectedTaskType.value;
    if (selectedType === "1") {
      console.log("select 1")
      inputFieldsRequirement.forEach((inputField) => {
        formValidations[inputField.name] = inputField.validation;
      });
    } else if (selectedType === "2") {
      console.log("select 2")
      inputFieldsImprovement.forEach((inputField) => {
        formValidations[inputField.name] = inputField.validation;
      });
    } else if (selectedType === "3") {
      console.log("select 3")
      inputFieldsBug.forEach((inputField) => {
        formValidations[inputField.name] = inputField.validation;
      });
    }
    alert(showAdditionalFields.value + "-" + selectedTaskType.value)
}

onMounted(async () => {
  await setTaskTypes();
});
</script>

<template>
  <Form
    :validation-schema="schema"
    @submit="onSubmit"
    class="flex flex-col gap-10 p-5 items-center"
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
