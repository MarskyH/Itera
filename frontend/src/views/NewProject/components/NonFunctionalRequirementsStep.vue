<script setup lang="ts">

import { onMounted, ref } from "vue";
import { Form } from 'vee-validate'
import * as yup from 'yup'


import InputField from 'src/views/NewProject/components/InputField.vue'
import yupErrorMessages from 'src/utils/yupErrorMessages';
import { InputFieldProps, NonFunctionalRequirementForm, models } from "src/@types";
import { useNonFunctionalRequirementStore } from "src/stores/NonFunctionalRequirementStore";
import { useRoute } from "vue-router";

interface NonFunctionalRequirement extends models.NonFunctionalRequirement { }

const $route = useRoute()
const $nonFunctionalRequirementStore = useNonFunctionalRequirementStore()



const importOptions = ['1 - Pouco importante', '2 - Importância baixa', '3 - Importância média:', '4 - Importância alta', '5 - Extremamente importante']

const nonFunctionalRequirementForm = ref<any>(null)

const nonFunctionalRequirements = ref<NonFunctionalRequirement[]>([])
const inputFields = ref<InputFieldProps[]>([])

yup.setLocale(yupErrorMessages);

const setOptions = (options: string[]) => {
  return options.length > 0 ? options.map((option: string) => {
    const value = option.split(' ')[0]; // Pega o primeiro número antes do espaço em branco
    return {
      value: value,
      name: option,
      selected: false
    }
  }) : []
}

let formValidations: any = {}
let schema: any

async function setNonFunctionalRequirements() {
  await $nonFunctionalRequirementStore.fetchNonFunctionalRequirements(String($route.params.projectId)).then(() => {
    nonFunctionalRequirements.value = $nonFunctionalRequirementStore.nonFunctionalRequirements
  })
}

onMounted(async () => {
  inputFields.value = [
    {
      name: "Comunicação de Dados",
      label: "Comunicação de Dados",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Volume de Transações",
      label: "Volume de Transações",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Processamento Complexo",
      label: "Processamento Complexo",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Facilidade Operacional",
      label: "Facilidade Operacional",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Processamento Distribuído",
      label: "Processamento Distribuído",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Entrada de Dados ON-LINE",
      label: "Entrada de Dados ON-LINE",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Reutilização de Código",
      label: "Reutilização de Código",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Múltiplos Locais",
      label: "Múltiplos Locais",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Desempenho",
      label: "Desempenho",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Eficiência do Usuário Final",
      label: "Eficiência do Usuário Final",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Facilidade de Implantação",
      label: "Facilidade de Implantação",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Facilidades de Mudanças",
      label: "Facilidades de Mudanças",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Utilização do Equipamento",
      label: "Utilização do Equipamento",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
    {
      name: "Atualização ON-LINE",
      label: "Atualização ON-LINE",
      placeholder: "Selecione o grau de importância",
      type: "select",
      required: true,
      options: setOptions(importOptions),
      validation: yup.string().required(),
    },
  ];

  inputFields.value.forEach(inputField => formValidations[inputField.name] = inputField.validation)
  schema = yup.object(formValidations);
})

async function createNonFunctionalRequirement(nonFunctionalRequirementsFormValues: NonFunctionalRequirementForm[]) {
  const projectId = String($route.params.projectId)

  await $nonFunctionalRequirementStore.createNonFunctionalRequirements(nonFunctionalRequirementsFormValues, projectId)
    .then((responseStatus: any) => {
      if (responseStatus === 200) {
        setNonFunctionalRequirements()
      } else {
        alert('Falha ao cadastrar requisito não funcional!')
      }
    }
    )
}

function onSubmit(values: any) {
  const nonFunctionalRequirementFormValuesList: NonFunctionalRequirementForm[] = [];

  // Itera sobre os campos do formulário e adiciona os valores a nonFunctionalRequirementFormValuesList
  for (const fieldName in values) {
    console.log(fieldName, values[fieldName])
    nonFunctionalRequirementFormValuesList.push({
      title: fieldName,
      valueRequirement: values[fieldName]
    });
  }

  console.log(nonFunctionalRequirementFormValuesList); // Verifica se o campo rnf15 está presente aqui

  // Chama a função createNonFunctionalRequirement com a lista de valores do formulário
  createNonFunctionalRequirement(nonFunctionalRequirementFormValuesList);
}

</script>

<template>
  <Form :validation-schema="schema" @submit="onSubmit" class="flex flex-col gap-10 p-5 items-center">
    <span>
      Para cada item escolha um valor entre 1 e 5. Em que 1 é menos importante e 5 é mais importante.
    </span>
    <div class="grid grid-cols-1 lg:grid-cols-2 w-full gap-5">
      <InputField v-for="(inputField, index) in inputFields" v-show="index < inputFields.length - 1"
        :key="inputField.name" :label="inputField.label" :name="inputField.name" :placeholder="inputField.placeholder"
        :type="inputField.type" :required="inputField.required" :options="inputField.options" />
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