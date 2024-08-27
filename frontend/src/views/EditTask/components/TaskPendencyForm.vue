<script setup lang="ts">
import { ref } from 'vue';
import * as yup from 'yup';

import { Form } from 'vee-validate';
import yupErrorMessages from 'src/utils/yupErrorMessages';
import InputField from 'src/views/NewProject/components/InputField.vue';
import { InputFieldProps, models } from 'src/@types';
import { usePendencyStore } from 'src/stores/PendencyStore';
import { useRoute } from 'vue-router';

interface PendencyForm extends models.PendencyForm { }

defineProps<{
  onCreateTaskPendency: boolean
}>()

const $emits = defineEmits(['update:onCreateTaskPendency'])

const $route = useRoute()
const $pendencyStore = usePendencyStore()

const taskPendenciesForm = ref<any>(null)

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
  const pendencyFormData: PendencyForm = {
    title: values.title,
    description: values.description,
  }

  const taskId = String($route.params.taskId)

  await $pendencyStore.createPendency(pendencyFormData, taskId).then(async (response: number) => {
    if(response === 200) {
      await $pendencyStore.fetchPendencies(String($route.params.taskId))
      $emits('update:onCreateTaskPendency', false)
    } else {
      alert('Erro ao salvar pendência')
    }
  })

}
</script>

<template>
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

    <div class="flex gap-3">
      <button
        type="button"
        class="text-xs flex text-white justify-evenly items-center bg-stone-500 py-2 px-3 rounded-md"
        @click="() => $emits('update:onCreateTaskPendency', false)"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-xmark"
        />
      </button>
  
      <button
        type="submit"
        class="flex text-white w-full text-sm font-semibold justify-evenly items-center bg-lavenderIndigo-900 px-4 py-2 gap-4 rounded-md"
      >
        Salvar
      </button>
    </div>
  </Form>
</template>
