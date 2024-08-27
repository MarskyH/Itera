<script setup lang="ts">
import { ref } from 'vue';
import * as yup from 'yup';

import { Form } from 'vee-validate';
import yupErrorMessages from 'src/utils/yupErrorMessages';
import InputField from 'src/views/NewProject/components/InputField.vue';
import { InputFieldProps, models } from 'src/@types';
import TaskPendency from 'src/views/EditTask/components/TaskPendency.vue'

const taskPendenciesForm = ref<any>(null)
const onCreateTaskPendency = ref<boolean>(false)

const inputFields = ref<InputFieldProps[]>([
  {
    name: "title",
    label: "Título",
    placeholder: "Digite o título da pendência",
    required: true,
    validation: yup.string().required().min(3),
  },
  {
    name: "description",
    label: "Descrição",
    placeholder: "Digite a descrição da pendência",
    required: false,
    validation: yup.string(),
    type: "textarea"
  },
])

const formValidations: any = {};

inputFields.value.forEach((inputField: InputFieldProps) => {
    formValidations[inputField.name] = inputField.validation;
  });

const schema = yup.object(formValidations)

yup.setLocale(yupErrorMessages);

async function onSubmit(values: any) {
  console.log(values)
}

</script>

<template>
  <div class="flex flex-col gap-3">
    <div class="flex gap-3 items-center justify-between">
      <div class="flex gap-3 items-center">
        <FontAwesomeIcon
          icon="fa-solid fa-hourglass"
          class="text-stone-500 dark:text-stone-400"
        />

        <span class="font-bold">Pendências</span>
      </div>

      <button
        v-show="!onCreateTaskPendency"
        title="Nova tarefa"
        class="text-xs flex text-white justify-evenly items-center bg-lavenderIndigo-900 p-2 rounded-md"
        @click="() => onCreateTaskPendency = true"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-plus"
        />
      </button>
    </div>

    <Form
      v-show="onCreateTaskPendency"
      ref="taskPendenciesForm"
      :validation-schema="schema"
      @submit="onSubmit"
      class="flex flex-col p-1 gap-5 overflow-auto"
    >
      <InputField
        v-for="inputField in inputFields"
        v-bind="inputField"
        :key="inputField.name"
      />

      <button
        type="submit"
        class="flex text-white w-full text-sm font-semibold justify-evenly items-center bg-lavenderIndigo-900 px-4 py-2 gap-4 rounded-md"
      >
        Salvar
      </button>
    </Form>

    <div>
      <TaskPendency :resolved="false" />
      <TaskPendency :resolved="true" />
    </div>
  </div>
</template>