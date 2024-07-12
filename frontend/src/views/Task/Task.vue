<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { Form } from 'vee-validate';
import * as yup from 'yup';
import { useRoute } from 'vue-router';
import yupErrorMessages from 'src/utils/yupErrorMessages';
import InputField from 'src/views/NewProject/components/InputField.vue';
import { InputFieldProps, models } from 'src/@types';
import { useProjectStore } from 'src/stores/ProjectStore';
import MaskedInput from 'src/components/MaskedInput.vue';
import { useTaskTypeStore } from "src/stores/TaskTypeStore";
import { useFunctionalRequirementStore } from "src/stores/FunctionalRequirementStore";
import { useTeamMemberStore } from "src/stores/TeamMemberStore";
import { useTaskStore } from "src/stores/TaskStore";

interface TaskType extends models.TaskType { }
interface Task extends models.Task { }
interface Project extends models.Project { }
interface TeamMember extends models.TeamMember { }
interface Assignee extends models.Assignee { }
interface TaskForm extends models.TaskForm { }


const $projectStore = useProjectStore();
const $taskStore = useTaskStore();
const $taskTypeStore = useTaskTypeStore();
const $teamMemberStore = useTeamMemberStore()
const $functionalRequirementStore = useFunctionalRequirementStore();
const $route = useRoute()

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
}

const task = ref<Task>({ ...taskDefault })
const taskForm = ref<any>(null)
const taskTypesOptions = ref<Array<any>>([]);
const backlogIterarion = ref<Array<any>>([]);
const selectedTaskType = ref<string>("");
const showAdditionalFields = ref<boolean>(false);

const teamMembers = ref<TeamMember[]>([])

const formValidations: any = {};

backlogIterarion.value = [
  {
    id: "1",
    title: "Title 1",
    details: "Details 1",
    complexity: "High",
    priority: "Low",
    effort: 10,
    sizeRequirement: "Large",
    progressiveBar: 50
  },
  {
    id: "2",
    title: "Title 2",
    details: "Details 2",
    complexity: "Medium",
    priority: "High",
    effort: 20,
    sizeRequirement: "Medium",
    progressiveBar: 75
  },
  {
    id: "3",
    title: "Title 3",
    details: "Details 3",
    complexity: "Low",
    priority: "Medium",
    effort: 15,
    sizeRequirement: "Small",
    progressiveBar: 60
  },
  {
    id: "4",
    title: "Title 4",
    details: "Details 4",
    complexity: "High",
    priority: "High",
    effort: 30,
    sizeRequirement: "Large",
    progressiveBar: 40
  },
  {
    id: "5",
    title: "Title 5",
    details: "Details 5",
    complexity: "Medium",
    priority: "Low",
    effort: 25,
    sizeRequirement: "Medium",
    progressiveBar: 90
  },
  {
    id: "6",
    title: "Title 6",
    details: "Details 6",
    complexity: "Low",
    priority: "Medium",
    effort: 5,
    sizeRequirement: "Small",
    progressiveBar: 20
  },
  {
    id: "7",
    title: "Title 7",
    details: "Details 7",
    complexity: "Medium",
    priority: "Medium",
    effort: 18,
    sizeRequirement: "Large",
    progressiveBar: 70
  },
  {
    id: "8",
    title: "Title 8",
    details: "Details 8",
    complexity: "High",
    priority: "High",
    effort: 22,
    sizeRequirement: "Medium",
    progressiveBar: 80
  },
  {
    id: "9",
    title: "Title 9",
    details: "Details 9",
    complexity: "Low",
    priority: "Low",
    effort: 12,
    sizeRequirement: "Small",
    progressiveBar: 30
  },
  {
    id: "10",
    title: "Title 10",
    details: "Details 10",
    complexity: "Medium",
    priority: "High",
    effort: 27,
    sizeRequirement: "Large",
    progressiveBar: 60
  }
];


yup.setLocale(yupErrorMessages);

const isTaskFormEmpty = computed(() => {
  console.log(!taskForm.value || !taskForm.value.values || !Object.keys(taskForm.value.values).length)
  return !taskForm.value || !taskForm.value.values || !Object.keys(taskForm.value.values).length;
});

const setSelectOptions = (options: { id: string; title: string }[]) => {
  return options.length > 0 ? options.map((option: { id: string; title: string }) => {
    return {
      value: option.id,
      name: option.title,
      selected: false
    }
  }) : []
}

function getTeamMemberOptions() {
  return teamMembers.value.length > 0
    ? teamMembers.value.map((member: TeamMember) => {
      return {
        value: member.id ? member.id : '',
        name: `${member?.user?.name} (${member.role.function})`,
        selected: false
      }
    })
    : []
}

const inputFields = ref<InputFieldProps[]>([]);
const inputFieldsRequirement = ref<InputFieldProps[]>([]);
const inputFieldsImprovement = ref<InputFieldProps[]>([]);
const inputFieldsBug = ref<InputFieldProps[]>([]);

let typeTaskForm: { [key: string]: InputFieldProps[] } = {
  '1': inputFieldsRequirement.value,
  '2': inputFieldsImprovement.value,
  '3': inputFieldsBug.value
}

const schema = ref<any>(() => {
  inputFields.value.forEach((inputField) => {
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
  let typeInputFields: InputFieldProps[] = typeTaskForm[type]

  typeInputFields.forEach((inputField) => {
    formValidations[inputField.name] = inputField.validation;
  });

  schema.value = yup.object(formValidations)

  inputFields.value = inputFields.value.filter((field: InputFieldProps) => field.name === 'title' || field.name === 'type')
  inputFields.value = inputFields.value.concat(typeInputFields)
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

async function onSubmit(values: any) {
  console.log(values)

  let assigneies: Assignee[] = [
    {
      taskStep: 'Projeto',
      deadline: values.deadlineProject,
      user_id: values.projectAssignee,
      task_id: task.value.id
    },
    {
      taskStep: 'Requisito',
      deadline: values.deadlineRequirement,
      user_id: values.requirementAssignee,
      task_id: task.value.id
    },
    {
      taskStep: 'Front',
      deadline: values.deadlineFront,
      user_id: values.frontAssignee,
      task_id: task.value.id
    },
    {
      taskStep: 'Back',
      deadline: values.deadlineBack,
      user_id: values.backAssignee,
      task_id: task.value.id
    },
    {
      taskStep: 'Teste',
      deadline: values.deadlineTest,
      user_id: values.testAssignee,
      task_id: task.value.id
    },
  ]

  const taskData: TaskForm = {
    taskType: taskType[values.type],
    assigneies
  }

  await $taskStore.updateTask(task.value.id ,taskData).then((response: any) => {
    if (response.status === 200) {
      alert('Salvo com sucesso')
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
          inputFields.value = [
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
                { value: "1", name: "Requisito", selected: false },
                { value: "2", name: "Melhoria", selected: false },
                { value: "3", name: "Bug", selected: false },
              ],
              required: true,
              validation: yup.string().required(),
              onChange: setTaskFormByType
            },
          ];

          inputFieldsRequirement.value = [
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
              validation: yup.string().required().min(1),
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
            },
            {
              name: "requirementAssignee",
              label: "Responsável requisito",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "deadlineRequirement",
              label: "Prazo requisito",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            },
            {
              name: "projectAssignee",
              label: "Responsável projeto",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "deadlineProject",
              label: "Prazo projeto",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            },
            {
              name: "frontAssignee",
              label: "Responsável front-end",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "deadlineFront",
              label: "Prazo front-end",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            },
            {
              name: "backAssignee",
              label: "Responsável back-end",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "deadlineBack",
              label: "Prazo back-end",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            },
            {
              name: "testAssignee",
              label: "Responsável testes",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "deadlineTest",
              label: "Prazo testes",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            }
          ]

          inputFieldsImprovement.value = [
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
              validation: yup.string().required().min(5),
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
            },
            {
              name: "requirementAssignee",
              label: "Responsável requisito",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "requirementProject",
              label: "Prazo requisito",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            },
            {
              name: "projectAssignee",
              label: "Responsável projeto",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "deadlineProject",
              label: "Prazo projeto",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            },
            {
              name: "frontAssignee",
              label: "Responsável front-end",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "deadlineFront",
              label: "Prazo front-end",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            },
            {
              name: "backAssignee",
              label: "Responsável back-end",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "deadlineBack",
              label: "Prazo back-end",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            },
            {
              name: "testAssignee",
              label: "Responsável testes",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "deadlineTest",
              label: "Prazo testes",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            }
          ]

          inputFieldsBug.value = [
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
              validation: yup.string().required().min(5),
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
            },
            {
              name: "frontAssignee",
              label: "Responsável front-end",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "deadlineFront",
              label: "Prazo front-end",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            },
            {
              name: "backAssignee",
              label: "Responsável back-end",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "deadlineBack",
              label: "Prazo back-end",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            },
            {
              name: "testAssignee",
              label: "Responsável testes",
              placeholder: "Selecione",
              type: "select",
              options: getTeamMemberOptions(),
              required: false,
              validation: yup.string(),
            },
            {
              name: "deadlineTest",
              label: "Prazo testes",
              placeholder: "Digite o prazo para esta etapa",
              type: "text",
              required: false,
              validation: yup.number(),
            }
          ]

          typeTaskForm['1'] = inputFieldsRequirement.value
          typeTaskForm['2'] = inputFieldsImprovement.value
          typeTaskForm['3'] = inputFieldsBug.value
        })
      });
    });
  })
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
        type="submit"
        class="flex text-white w-32 justify-evenly items-center bg-lavenderIndigo-900 px-4 py-2 gap-4 rounded-md"
      >
        <span>Salvar</span>
      </button>
    </div>
  </Form>
</template>
