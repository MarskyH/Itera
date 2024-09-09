<script setup lang="ts">
import * as yup from 'yup'
import { ref } from 'vue';
import draggable from "vuedraggable";
import ActionGridItem from 'src/views/NewProject/components/ActionGridItem.vue';
import InputField from 'src/views/NewProject/components/InputField.vue'
import yupErrorMessages from 'src/utils/yupErrorMessages';
import ActionModal from 'src/components/ActionModal.vue';
import ProgressiveBar from './ProgressiveBar.vue';
import { useRoute, useRouter } from 'vue-router';
import { useTaskStore } from 'src/stores/TaskStore';
import { InputFieldProps, models} from "src/@types";
import { usePendencyStore } from 'src/stores/PendencyStore';

interface PendencyForm extends models.PendencyForm { }

const props = defineProps<{
  title: string
  tasks: Object[]
  listName: string
  addButton?: Boolean
}>()

const isActionModalOpen = ref<boolean>(false)
const actionModalTitle = ref<string>('Adicionar')
const inputFields = ref<InputFieldProps[]>([])
const formOnLoad = ref<boolean>(true)
const pendencyForm = ref<any>(null)
const idTaskInput = ref<string>('')
const listNameInput = ref<string>('')
const $pendencyStore = usePendencyStore()

yup.setLocale(yupErrorMessages);
let formValidations: any = {}
let schema: any

function createInputPendency(){
  inputFields.value = [
  {
    name: "title",
    label: "Título",
    placeholder: "Digite o título da pendência",
    required: true,
    validation: yup.string().required().min(5)
  },
  {
    name: "description",
    label: "Descrição",
    placeholder: "Digite a descrição da pendência",
    required: true,
    validation: yup.string().required().min(20)
  },
]
    
  inputFields.value.forEach(inputField => formValidations[inputField.name] = inputField.validation)
  schema = yup.object(formValidations);    
  formOnLoad.value = false
}

function createInputCancel(){
  inputFields.value = [
  {
    name: "detailsCancel",
    label: "Descrição",
    placeholder: "Digite o motivo do cancelamento da tarefa",
    required: true,
    validation: yup.string().required().min(5)
  }
]
    
  inputFields.value.forEach(inputField => formValidations[inputField.name] = inputField.validation)
  schema = yup.object(formValidations);    
  formOnLoad.value = false
}




const $taskStore = useTaskStore()

const $router = useRouter()
const $route = useRoute()

const tasksList = ref<Object[]>(props.tasks)

function openTaskForm(taskId: string) {
  $router.push({ 
    name: 'iteration-task-edit', 
    params: { 
      projectId: $route.params.projectId, 
      iterationId: $route.params.iterationId,
      taskId
    }
  })
}

async function updateTaskList(evt: any) {
  let movingTaskId = evt.draggedContext.element.id
  let taskListName = evt.relatedContext.component.componentData.listName
  listNameInput.value = taskListName
  if(taskListName === "Pendente"){
    createInputPendency()
    actionModalTitle.value = 'Adicionar pendência'
    idTaskInput.value = movingTaskId
    formOnLoad.value = false
    isActionModalOpen.value = true
  } else if(taskListName === "Cancelado") {
    createInputCancel()
    actionModalTitle.value = 'Cancelar tarefa'
    idTaskInput.value = movingTaskId
    formOnLoad.value = false
    isActionModalOpen.value = true
  }
  await $taskStore.updateTaskListName(movingTaskId, taskListName)
}

function updateTaskOrder(list: any[], sliceIndex: number) {
  list.forEach(async (element: any, index: number) => {
    if (index >= sliceIndex) {
      await $taskStore.updateTaskOrder(element?.id, index)
    }
  });
}

async function moveTask(evt: any) {
  console.log(props.tasks)
  setTimeout(async () => {
    let oldIndex = evt.draggedContext.index
    let newIndex = evt.relatedContext.index

    if(!newIndex) {      
      updateTaskList(evt)
      updateTaskOrder(tasksList.value, evt.draggedContext.futureIndex)
      updateTaskOrder(evt.relatedContext.list, evt.draggedContext.futureIndex)

      return
    }
    
    if (oldIndex < newIndex) {
      updateTaskOrder(tasksList.value, oldIndex)
    } else {
      updateTaskOrder(tasksList.value, newIndex)
    }
  }, 500)
}




function onSubmit(values: any) {
  if(listNameInput.value === "Pendente"){
    let pendencyFormValues: PendencyForm = { ...values }
    alert(`${pendencyFormValues.title} + ${pendencyFormValues.description} + ${values.title} + ${values.description}` )
    $pendencyStore.createPendency(pendencyFormValues, idTaskInput.value)
    isActionModalOpen.value = false
  }else if(listNameInput.value === "Cancelado"){
    $taskStore.updateTaskCancel(idTaskInput.value, values.detailsCancel)
    isActionModalOpen.value = false
  }
}

</script>

<template>
  <div class="flex flex-col h-fit gap-3 bg-whiteSmoke-900/50 dark:bg-jet-900/50 rounded p-3">
    <div class="flex gap-2 items-center justify-between text-lavenderIndigo-900 dark:text-tropicalIndigo-900 font-semibold">
      <div class="flex gap-2 items-center">
        <FontAwesomeIcon
          icon="fa-solid fa-bars-staggered"
        />

        {{ title }}
      </div>

      <button
        v-show="addButton"
        title="Nova tarefa"
        class="text-xs flex text-white justify-evenly items-center bg-lavenderIndigo-900 p-2 rounded-md"
        @click="$router.push({ name: 'iteration-task-new'})"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-plus"
        />
      </button>
    </div>

    <div class="flex max-h-[calc(100vh-200px)] flex-col gap-2 overflow-auto">
      <draggable
        :list="tasksList"
        :move="moveTask"
        :component-data="{ listName }"
        group="people"
        @start="(drag: any) => drag = true"
        @end="(drag: any) => drag = false"
        item-key="id"
        animation="200"
        class="flex flex-col gap-2 min-w-64"
      >
        <template #item="{element}">
          <ActionGridItem
            icon="bookmark"
            :title="element.title"
            @edit="() => {}"
            @remove="() => {}"
            @side-view-content-change="() => openTaskForm(element.id)"
          >
            <div class="flex flex-col gap-1">
              <span class="text-sm font-semibold">
                Prioridade
              </span>
        
              <span class="text-xs text-stone-500 dark:text-stone-400">
                {{ element.priority }}
              </span>
            </div>
      
            <div class="flex flex-col gap-1">
              <span class="text-sm font-semibold">
                Responsável
              </span>
        
              <span class="text-xs text-stone-500 dark:text-stone-400">
                {{ element.responsible }}
              </span>

              <ProgressiveBar :progress="element.progressiveBar" />
            </div>
          </ActionGridItem>
        </template>
      </draggable>
    </div>
  </div>
  <Form
    ref="pendencyForm"
    :validation-schema="schema"
    @submit="onSubmit"
    v-if="!formOnLoad"
  >
    <ActionModal
      v-model="isActionModalOpen"
      :title="actionModalTitle"
      icon="clock"
    >
      <div class="flex flex-col px-8 py-4 gap-5">
        <div class="grid grid-cols-2 w-full gap-5">
          <InputField
            v-for="(inputField, index) in inputFields"
            v-show="index < inputFields.length - 1"
            :key="inputField.name"
            v-slot="{ field }"
            :label="inputField.label"
            :name="inputField.name"
            :placeholder="inputField.placeholder"
            :type="inputField.type"
            :disabled="inputField.disabled"
            :value="inputField.value"
            :required="inputField.required"
            :options="inputField.options"
            @change="inputField.onChange"
          >
            {{ console.log(field.onInput) }}
          </InputField>
        </div>
        
        <div class="grid grid-cols-1 w-full gap-5">
          <InputField
            :key="inputFields[inputFields.length - 1]?.name"
            :label="inputFields[inputFields.length - 1]?.label"
            :name="inputFields[inputFields.length - 1]?.name"
            :placeholder="inputFields[inputFields.length - 1]?.placeholder"
            :type="inputFields[inputFields.length - 1]?.type"
            :required="inputFields[inputFields.length - 1]?.required"
            :options="inputFields[inputFields.length - 1]?.options"
          />
        </div>
      </div>
    </ActionModal>
  </Form>
</template>