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
import { Form } from 'vee-validate';

interface PendencyForm extends models.PendencyForm { }

const props = defineProps<{
  title: string
  tasks: Object[]
  listName: string
  addButton?: Boolean,
  onError: string
}>()

const $emits = defineEmits(['onCancelDrag', 'onConfirmDrag'])

const isActionModalOpen = ref<boolean>(false)
const actionModalTitle = ref<string>('Adicionar')
const inputFields = ref<InputFieldProps[]>([])
const formOnLoad = ref<boolean>(true)
const pendencyForm = ref<any>(null)
const idTaskInput = ref<string>('')
const listNameInput = ref<string>('')
const $pendencyStore = usePendencyStore()

yup.setLocale(yupErrorMessages);
const schema = ref<any>(null)

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
      type: "textarea",
      required: true,
      validation: yup.string().required().min(20)
    },
  ]
  

  var formValidations: any = {};
  inputFields.value.forEach(inputField => formValidations[inputField.name] = inputField.validation)
  schema.value = yup.object(formValidations);    
  formOnLoad.value = false

  console.log(formValidations, schema.value)
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
  
  var formValidations: any = {};
  inputFields.value.forEach(inputField => formValidations[inputField.name] = inputField.validation)
  schema.value = yup.object(formValidations);    
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

  return await $taskStore.updateTaskListName(movingTaskId, taskListName)
    .then((response) => {
      console.log(response?.status)
      return response?.status
    })
}

function updateTaskOrder(list: any[], sliceIndex: number) {
  list.forEach(async (element: any, index: number) => {
    if (index >= sliceIndex) {
      await $taskStore.updateTaskOrder(element?.id, index)
    }
  });
}

async function moveTask (evt: any) {
  let oldIndex = evt.draggedContext.index
  let newIndex = evt.relatedContext.index
  
  setTimeout(async () => {

    if(!newIndex) {  
      await updateTaskList(evt).then((response) => {
        console.log(response)
        updateTaskOrder(tasksList.value, evt.draggedContext.futureIndex)
        updateTaskOrder(evt.relatedContext.list, evt.draggedContext.futureIndex)
        
        if(response !== 200) {
          $emits('onCancelDrag', { 
            fromList: props.listName, 
            toList: evt.relatedContext.component.componentData.listName, 
            task: evt.draggedContext.element 
          })

          return false
        }else{
          $emits('onConfirmDrag', { 
            fromList: props.listName, 
            toList: evt.relatedContext.component.componentData.listName, 
            task: evt.draggedContext.element 
          })

          return true
        }
      })
    }
  }, 500)
  
  if (oldIndex < newIndex) {
    updateTaskOrder(tasksList.value, oldIndex)
  } else {
    updateTaskOrder(tasksList.value, newIndex)
  }
}

async function onSubmit(values: any) {
  if(listNameInput.value === "Pendente"){
    let pendencyFormValues: PendencyForm = { ...values }
    
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
        :move="(evt: any)=> moveTask(evt)"
        :component-data="{ listName }"
        group="people"
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
        <InputField
          v-for="inputField in inputFields"
          :key="inputField.name"
          v-bind="inputField"
        />
      </div>
    </ActionModal>
  </Form>
</template>