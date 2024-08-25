<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import { Form } from 'vee-validate';
import * as yup from 'yup';
import { useRoute, useRouter } from 'vue-router';
import yupErrorMessages from 'src/utils/yupErrorMessages';
import InputField from 'src/views/NewProject/components/InputField.vue';
import { InputFieldProps, models, TaskRequirementForm } from 'src/@types';
import MaskedInput from 'src/components/MaskedInput.vue';
import { useTaskTypeStore } from "src/stores/TaskTypeStore";
import { useFunctionalRequirementStore } from "src/stores/FunctionalRequirementStore";
import { useTeamMemberStore } from "src/stores/TeamMemberStore";
import { useTaskStore } from "src/stores/TaskStore";

interface Task extends models.Task { }
interface TeamMember extends models.TeamMember { }
interface Assignee extends models.Assignee { }
interface TaskForm extends models.TaskForm { }
interface TaskRequirement extends models.TaskRequirement { }
interface TaskImprovement extends models.TaskImprovement { }
interface TaskBug extends models.TaskBug { }

const $taskStore = useTaskStore();
const $taskTypeStore = useTaskTypeStore();
const $teamMemberStore = useTeamMemberStore()
const $functionalRequirementStore = useFunctionalRequirementStore();
const $route = useRoute()
const $router = useRouter()

const taskDefault: Task = {
  id: "",
  title: "",
  priority: "",
  details: "",
  complexity: "",
  effort: "",
  sizeTask: 0,
  startDate: "",
  endDate: "",
  taskType: "",
  taskrequirement_id: "",
  taskimprovement_id: "",
  taskbug_id: "",
  iteration_id: "",
  assignees: []
}

const task = ref<Task>({ ...taskDefault })
const taskForm = ref<any>(null)
const taskTypesOptions = ref<Array<any>>([]);
const backlogIterarion = ref<Array<any>>([]);

const teamMembers = ref<TeamMember[]>([])

const formValidations: any = {};
yup.setLocale(yupErrorMessages);

function getTeamMemberOptions() {
  return teamMembers.value.length > 0
    ? teamMembers.value.map((member: TeamMember) => {
      return {
        value: member.id ? member.id : '',
        name: `${member?.user?.name} (${member.role.function})`,
        selected: task.value.assignees.find((assignee: any) => assignee.member_id === member.id) ? true : false
      }
    })
    : []
}

function getSelectedTeamMemberOption(step: string) {
  return task.value.assignees.find((assignee: any) =>
    assignee.taskStep === step
  )?.member_id
}

function getStepDeadline(step: string) {
  return task.value.assignees.find((assignee: any) =>
    assignee.taskStep === step
  )?.deadline
}

function getStepChecksByTaskType(step: string, taskType: string) {
  let taskTypes: { [index: string]: string } = {
    'Requisito': 'Requirement',
    'Melhoria': 'Improvement',
    'Bug': 'Bug'
  }

  let steps: { [index: string]: string } = {
    'P': 'Project',
    'R': 'Requirement',
    'F': 'Front',
    'B': 'Back',
    'T': 'Test'
  }

  const taskTypeKey = `task${taskTypes[taskType]}`
  const checkStepKey = `check${steps[step]}`

  let type = task.value[taskTypeKey as keyof Task] || ''
  let check = type[checkStepKey as keyof typeof type]

  return check
}

const inputFields = ref<{ generalFields: InputFieldProps[], specificFields: InputFieldProps[] }>({ generalFields: [], specificFields: [] });
const inputFieldsRequirement = ref<{ generalFields: InputFieldProps[], specificFields: InputFieldProps[] }>({ generalFields: [], specificFields: [] });
const inputFieldsImprovement = ref<{ generalFields: InputFieldProps[], specificFields: InputFieldProps[] }>({ generalFields: [], specificFields: [] });
const inputFieldsBug = ref<{ generalFields: InputFieldProps[], specificFields: InputFieldProps[] }>({ generalFields: [], specificFields: [] });

let typeTaskForm: { [key: string]: { generalFields: InputFieldProps[], specificFields: InputFieldProps[] } } = {
  '1': inputFieldsRequirement.value,
  '2': inputFieldsImprovement.value,
  '3': inputFieldsBug.value
}

const schema = ref<any>(() => {
  let allInputFields: InputFieldProps[] = inputFields.value.generalFields.concat(inputFields.value.specificFields)
  allInputFields.forEach((inputField: InputFieldProps) => {
    formValidations[inputField.name] = inputField.validation;
  });

  return yup.object(formValidations);
});

async function setTask() {
  await $taskStore.fetchTask(String($route.params.taskId)).then(() => {
    task.value = $taskStore.task;
  });
}

function setTaskFormByType() {
  let removeFields = Object.keys(formValidations).filter((key: string) => key !== 'title' && key !== 'type')
  removeFields.forEach((field: string) => {
    delete formValidations[field]
  })

  let type: string = taskForm.value.values.type
  let typeInputFields: InputFieldProps[] = typeTaskForm[type].generalFields.concat(typeTaskForm[type].specificFields)

  typeInputFields.forEach((inputField) => {
    formValidations[inputField.name] = inputField.validation;
  });

  schema.value = yup.object(formValidations)

  inputFields.value.generalFields = inputFields.value.generalFields.filter((field: InputFieldProps) => field.name === 'title' || field.name === 'type')
  inputFields.value.specificFields = typeInputFields
}

async function setTaskTypes() {
  await $taskTypeStore.fetchTaskTypes().then(() => {
    taskTypesOptions.value = $taskTypeStore.types;
  });

}

async function setBacklogIteration() {
  await $functionalRequirementStore.fetchFunctionalRequirementsByIteration(String($route.params.iterationId)).then(() => {
    backlogIterarion.value = $functionalRequirementStore.functionalRequirements;
  });
}

async function setTeamMembers() {
  await $teamMemberStore.fetchTeamMembers(String($route.params.projectId)).then(() => {
    teamMembers.value = $teamMemberStore.teamMembers
  })
}

let taskType: { [key: string]: string } = {
  '1': 'Requisito',
  '2': 'Melhoria',
  '3': 'Bug'
}

let taskTypeValues: { [key: string]: string } = {
  'Requisito': '1',
  'Melhoria': '2',
  'Bug': '3'
}

async function onSubmit(values: any) {
  console.log(values)

  let taskRequirement: any = null

  if (task.value.taskType === 'Requisito') {
    taskRequirement = {
      id: task.value.taskRequirement?.id,
      checkProject: values.checkProject,
      checkRequirement: values.checkRequirement,
      checkFront: values.checkFront,
      checkBack: values.checkBack,
      checkTest: values.checkTest
    }
  }

  let taskImprovement: any = null

  if (task.value.taskType === 'Melhoria') {
    taskImprovement = {
      id: task.value.taskImprovement?.id,
      checkProject: values.checkProject,
      checkRequirement: values.checkRequirement,
      checkFront: values.checkFront,
      checkBack: values.checkBack,
      checkTest: values.checkTest
    }
  }

  let taskBug: any = null

  if (task.value.taskType === 'Bug') {
    taskBug = {
      id: task.value.taskBug?.id,
      checkFront: values.checkFront,
      checkBack: values.checkBack,
      checkTest: values.checkTest
    }
  }

  let assignees: Assignee[] = [
    {
      taskStep: 'P',
      deadline: values.deadlineProject,
      member_id: values.projectAssignee,
      task_id: task.value.id
    },
    {
      taskStep: 'R',
      deadline: values.deadlineRequirement,
      member_id: values.requirementAssignee,
      task_id: task.value.id
    },
    {
      taskStep: 'F',
      deadline: values.deadlineFront,
      member_id: values.frontAssignee,
      task_id: task.value.id
    },
    {
      taskStep: 'B',
      deadline: values.deadlineBack,
      member_id: values.backAssignee,
      task_id: task.value.id
    },
    {
      taskStep: 'T',
      deadline: values.deadlineTest,
      member_id: values.testAssignee,
      task_id: task.value.id
    },
  ]

  const taskData: TaskForm = {
    taskType: taskType[values.type],
    assignees,
    taskRequirement,
    taskImprovement,
    taskBug
  }

  await $taskStore.updateTask(task.value.id, taskData).then((response: any) => {
    if (response.status === 200) {
      alert('Salvo com sucesso')
      $router.push({ name: 'project-iteration', params: { projectId: $route.params.projectId, iterationId: $route.params.iterationId } })
    } else {
      alert('Erro ao salvar')
    }
  })
}

watch(() => taskForm.value?.values.backlog, (newBacklogId) => {
  const selectedBacklog = backlogIterarion.value.find(backlog => backlog.id === newBacklogId);
  if (selectedBacklog) {
    taskForm.value.setFieldValue('title', selectedBacklog.title);
    taskForm.value.setFieldValue('detail', selectedBacklog.details);
    taskForm.value.setFieldValue('complexity', selectedBacklog.complexity);
    taskForm.value.setFieldValue('priority', selectedBacklog.priority);
    taskForm.value.setFieldValue('effort', selectedBacklog.effort);
    taskForm.value.setFieldValue('size', selectedBacklog.sizeRequirement);
  }
}, { immediate: true });

onMounted(async () => {
  await setTaskTypes().then(async () => {
    await setBacklogIteration().then(async () => {
      await setTask().then(async () => {
        await setTeamMembers().then(async () => {
          inputFields.value = {
            generalFields: [
              {
                name: "title",
                label: "Título da tarefa",
                placeholder: "Digite o título da tarefa",
                required: true,
                validation: yup.string().required().min(5),
                value: task.value.title,
                disabled: true
              },
              {
                name: "type",
                label: "Tipo da tarefa",
                placeholder: "Selecione",
                type: "select",
                options: [
                  { value: "1", name: "Requisito", selected: task.value.taskType === "Requisito" },
                  { value: "2", name: "Melhoria", selected: task.value.taskType === "Melhoria" },
                  { value: "3", name: "Bug", selected: task.value.taskType === "Bug" },
                ],
                required: true,
                value: task.value.taskType ? taskTypeValues[task.value.taskType] : '',
                validation: yup.string().required(),
                onChange: setTaskFormByType
              },
            ],
            specificFields: []
          };

          inputFieldsRequirement.value = {
            generalFields: [
              {
                name: "priority",
                label: "Prioridade",
                placeholder: "Selecione",
                required: true,
                disabled: true,
                validation: yup.string().required(),
                value: task.value.priority
              },
              {
                name: "complexity",
                label: "Complexidade",
                placeholder: "Selecione",
                required: true,
                disabled: true,
                validation: yup.string().required(),
                value: task.value.complexity
              },
              {
                name: "effort",
                label: "Esforço",
                placeholder: "Digite o valor do esforço",
                required: true,
                disabled: true,
                validation: yup.number().required().min(1),
                value: task.value.effort
              },
              {
                name: "size",
                label: "Tamanho",
                placeholder: "Selecione",
                required: true,
                disabled: true,
                validation: yup.string().required(),
                value: task.value.sizeTask.toString()
              },
              {
                name: "detail",
                label: "Detalhamento",
                placeholder: "Digite o detalhamento da tarefa",
                type: "textarea",
                required: true,
                disabled: true,
                validation: yup.string().required(),
                value: task.value.details
              }
            ],
            specificFields: [
              {
                name: "checkRequirement",
                label: "Requisito",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('R', task.value.taskType)
              },
              {
                name: "requirementAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('R') || ''
              },
              {
                name: "deadlineRequirement",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('R')) !== 'undefined' ? String(getStepDeadline('R')) : ''
              },
              {
                name: "checkProject",
                label: "Projeto",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('P', task.value.taskType)
              },
              {
                name: "projectAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('P') || ''
              },
              {
                name: "deadlineProject",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('P')) !== 'undefined' ? String(getStepDeadline('P')) : ''
              },
              {
                name: "checkFront",
                label: "Front-end",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('F', task.value.taskType)
              },
              {
                name: "frontAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('F') || ''
              },
              {
                name: "deadlineFront",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('F')) !== 'undefined' ? String(getStepDeadline('F')) : ''
              },
              {
                name: "checkBack",
                label: "Back-end",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('B', task.value.taskType)
              },
              {
                name: "backAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('B') || ''
              },
              {
                name: "deadlineBack",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('B')) !== 'undefined' ? String(getStepDeadline('B')) : ''
              },
              {
                name: "checkTest",
                label: "Testes",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('T', task.value.taskType)
              },
              {
                name: "testAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('T') || ''
              },
              {
                name: "deadlineTest",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('T')) !== 'undefined' ? String(getStepDeadline('T')) : ''
              }
            ]
          }

          inputFieldsImprovement.value = {
            generalFields: [
              {
                name: "priority",
                label: "Prioridade",
                placeholder: "Selecione",
                required: true,
                disabled: true,
                validation: yup.string().required(),
                value: task.value.priority
              },
              {
                name: "complexity",
                label: "Complexidade",
                placeholder: "Selecione",
                required: true,
                disabled: true,
                validation: yup.string().required(),
                value: task.value.complexity
              },
              {
                name: "effort",
                label: "Esforço",
                placeholder: "Digite o valor do esforço",
                required: true,
                disabled: true,
                validation: yup.number().required().min(1),
                value: task.value.effort
              },
              {
                name: "size",
                label: "Tamanho",
                placeholder: "Selecione",
                required: true,
                disabled: true,
                validation: yup.string().required(),
                value: task.value.sizeTask.toString()
              },
              {
                name: "detail",
                label: "Detalhamento",
                placeholder: "Digite o detalhamento da tarefa",
                type: "textarea",
                required: true,
                disabled: true,
                validation: yup.string().required(),
                value: task.value.details
              }
            ],
            specificFields: [
            {
                name: "checkRequirement",
                label: "Requisito",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('R', task.value.taskType)
              },
              {
                name: "requirementAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('R') || ''
              },
              {
                name: "deadlineRequirement",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('R')) !== 'undefined' ? String(getStepDeadline('R')) : ''
              },
              {
                name: "checkProject",
                label: "Projeto",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('P', task.value.taskType)
              },
              {
                name: "projectAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('P') || ''
              },
              {
                name: "deadlineProject",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('P')) !== 'undefined' ? String(getStepDeadline('P')) : ''
              },
              {
                name: "checkFront",
                label: "Front-end",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('F', task.value.taskType)
              },
              {
                name: "frontAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('F') || ''
              },
              {
                name: "deadlineFront",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('F')) !== 'undefined' ? String(getStepDeadline('F')) : ''
              },
              {
                name: "checkBack",
                label: "Back-end",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('B', task.value.taskType)
              },
              {
                name: "backAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('B') || ''
              },
              {
                name: "deadlineBack",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('B')) !== 'undefined' ? String(getStepDeadline('B')) : ''
              },
              {
                name: "checkTest",
                label: "Testes",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('T', task.value.taskType)
              },
              {
                name: "testAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('T') || ''
              },
              {
                name: "deadlineTest",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('T')) !== 'undefined' ? String(getStepDeadline('T')) : ''
              }
            ]
          }

          inputFieldsBug.value = {
            generalFields: [
              {
                name: "priority",
                label: "Prioridade",
                placeholder: "Selecione",
                required: true,
                disabled: true,
                validation: yup.string().required(),
                value: task.value.priority
              },
              {
                name: "complexity",
                label: "Complexidade",
                placeholder: "Selecione",
                required: true,
                disabled: true,
                validation: yup.string().required(),
                value: task.value.complexity
              },
              {
                name: "effort",
                label: "Esforço",
                placeholder: "Digite o valor do esforço",
                required: true,
                disabled: true,
                validation: yup.number().required().min(1),
                value: task.value.effort
              },
              {
                name: "size",
                label: "Tamanho",
                placeholder: "Selecione",
                required: true,
                disabled: true,
                validation: yup.string().required(),
                value: task.value.sizeTask.toString()
              },
              {
                name: "detail",
                label: "Detalhamento",
                placeholder: "Digite o detalhamento da tarefa",
                type: "textarea",
                required: true,
                disabled: true,
                validation: yup.string().required(),
                value: task.value.details
              }
            ],
            specificFields: [
              {
                name: "checkFront",
                label: "Front-end",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('F', task.value.taskType)
              },
              {
                name: "frontAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('F') || ''
              },
              {
                name: "deadlineFront",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('F')) !== 'undefined' ? String(getStepDeadline('F')) : ''
              },
              {
                name: "checkBack",
                label: "Back-end",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('B', task.value.taskType)
              },
              {
                name: "backAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('B') || ''
              },
              {
                name: "deadlineBack",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('B')) !== 'undefined' ? String(getStepDeadline('B')) : ''
              },
              {
                name: "checkTest",
                label: "Testes",
                placeholder: "",
                type: "checkbox",
                required: false,
                validation: yup.string(),
                value: getStepChecksByTaskType('T', task.value.taskType)
              },
              {
                name: "testAssignee",
                label: "Responsável",
                placeholder: "Selecione",
                type: "select",
                options: getTeamMemberOptions(),
                required: false,
                validation: yup.string(),
                value: getSelectedTeamMemberOption('T') || ''
              },
              {
                name: "deadlineTest",
                label: "Prazo",
                placeholder: "Digite o prazo para esta etapa",
                type: "text",
                required: false,
                validation: yup.number(),
                value: String(getStepDeadline('T')) !== 'undefined' ? String(getStepDeadline('T')) : ''
              }
            ]
          }

          typeTaskForm['1'] = inputFieldsRequirement.value
          typeTaskForm['2'] = inputFieldsImprovement.value
          typeTaskForm['3'] = inputFieldsBug.value
        })
      });
    });
  })

  if (task.value.taskType) {
    setTaskFormByType()
  }
});
</script>

<template>
  <Form
    ref="taskForm"
    :validation-schema="schema"
    @submit="onSubmit"
    class="flex flex-col gap-5 p-5 items-center overflow-auto"
  >
    <div class="grid grid-cols-1 lg:grid-cols-2 w-full gap-5">
      <div
        v-for="inputField in inputFields.generalFields"
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
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 w-full gap-5">
      <div
        v-for="inputField in inputFields.specificFields"
        :key="inputField.name"
        :class="{ 'col-span-2': inputField.name === 'detail' || inputField.type === 'checkbox' }"
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
    </div>

    <div class="flex gap-5">
      <button
        class="flex text-white w-32 justify-evenly items-center bg-stone-400 dark:bg-stone-600 px-4 py-2 gap-4 rounded-md"
        @click="() => $router.push({ name: 'project-iteration', params: { projectId: $route.params.projectId, iterationId: $route.params.iterationId } })"
      >
        <span>Cancelar</span>
      </button>
      <button
        type="submit"
        class="flex text-white w-32 justify-evenly items-center bg-lavenderIndigo-900 px-4 py-2 gap-4 rounded-md"
      >
        <span>Salvar</span>
      </button>
    </div>
  </Form>
</template>
