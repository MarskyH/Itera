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

const importOptions = [
  { id: 1, name: 'Pouco importante' },
  { id: 2, name: 'Importância baixa' },
  { id: 3, name: 'Importância média' },
  { id: 4, name: 'Importância alta' },
  { id: 5, name:'Extremamente importante' }
]

const nonFunctionalRequirementForm = ref<any>(null)

const nonFunctionalRequirements = ref<NonFunctionalRequirement[]>([])
const inputFields = ref<InputFieldProps[]>([])

yup.setLocale(yupErrorMessages);

const setOptions = (options: {id: number; name: string}[]) => {
  return options.length > 0 ? options.map((option: {id: number; name: string}) => {
    return {
      value: String(option.id),
      name: `${option.id} - ${option.name}`,
      selected: false
    }
  }) : []
}

let formValidations: any = {}
let schema: any

async function setNonFunctionalRequirements() {
  await $nonFunctionalRequirementStore.fetchNonFunctionalRequirements(String($route.params.projectId))
    .then(() => {
      nonFunctionalRequirements.value = $nonFunctionalRequirementStore.nonFunctionalRequirements
    }
  )
}

onMounted(async () => {
  await setNonFunctionalRequirements().then(() => {
    inputFields.value = [
      {
        name: "rnf1",
        label: "Comunicação de Dados",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf2",
        label: "Volume de Transações",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf3",
        label: "Processamento Complexo",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf4",
        label: "Facilidade Operacional",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf5",
        label: "Processamento Distribuído",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf6",
        label: "Entrada de Dados ON-LINE",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf7",
        label: "Reutilização de Código",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf8",
        label: "Múltiplos Locais",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf9",
        label: "Desempenho",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf10",
        label: "Eficiência do Usuário Final",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf11",
        label: "Facilidade de Implantação",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf12",
        label: "Facilidades de Mudanças",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf13",
        label: "Utilização do Equipamento",
        placeholder: "Selecione o grau de importância",
        type: "select",
        required: true,
        options: setOptions(importOptions),
        validation: yup.string().required(),
      },
      {
        name: "rnf14",
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
})

async function createNonFunctionalRequirement(nonFunctionalRequirementsFormValues: NonFunctionalRequirementForm[]) {
  const projectId = String($route.params.projectId)

  await $nonFunctionalRequirementStore.createNonFunctionalRequirements(nonFunctionalRequirementsFormValues, projectId)
    .then((responseStatus: any) => {
      if (responseStatus === 200) {
        setNonFunctionalRequirements()

        alert('Configuração do projeto concluída!')
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
  <div class="flex w-full justify-start items-center gap-2 px-2 text-base">
    <FontAwesomeIcon icon="fa-solid fa-gears" />

    <span class="font-semibold px-2">Requisitos Não Funcionais</span>
  </div>
  
  <Form
    ref="nonFunctionalRequirementForm"
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
        :type="inputField.type"
        :required="inputField.required"
        :options="inputField.options"
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