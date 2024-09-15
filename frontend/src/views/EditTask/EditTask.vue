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
import { useBacklogStore } from "src/stores/BacklogStore";
import ProjectBacklog from "../ProjectTasks/ProjectBacklog.vue";

interface Task extends models.Task { }
interface TeamMember extends models.TeamMember { }
interface Assignee extends models.Assignee { }
interface TaskForm extends models.TaskForm { }
interface BacklogRequirement extends models.BacklogRequirement { }

const getCurrentDate = formatDate(new Date().toISOString());
const $taskStore = useTaskStore();
const $taskTypeStore = useTaskTypeStore();
const $teamMemberStore = useTeamMemberStore()
const $backlogStore = useBacklogStore()
const $functionalRequirementStore = useFunctionalRequirementStore();
const $route = useRoute()
const $router = useRouter()
const isTaskPlanning = ref<boolean>(false)
const isTaskReview = ref<boolean>(false)
const isTaskRetrospective = ref<boolean>(false)
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
  checkCancelled: false,
  detailsCancelled: "",
  taskType: "",
  taskrequirement_id: "",
  taskimprovement_id: "",
  taskbug_id: "",
  taskplanning_id: "",
  iteration_id: "",
  assignees: [],
  pendencies: [],
}
const task = ref<Task>({ ...taskDefault })
const taskForm = ref<any>(null)
const taskTypesOptions = ref<Array<any>>([]);
const backlogIterarion = ref<Array<any>>([]);
const backlogRequirements = ref<Array<any>>([])
const teamMembers = ref<TeamMember[]>([])
const formValidations: any = {};
yup.setLocale(yupErrorMessages);

function formatDate(dateString: string): string {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Mês é indexado em 0
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

function toISODate(dateString: string): string {
    const [day, month, year] = dateString.split('/').map(Number); 
    const date = new Date(year, month - 1, day); 
    const isoDate = new Date(
        date.getFullYear(),
        date.getMonth(),
        date.getDate(),
        new Date().getHours(),
        new Date().getMinutes(),
        new Date().getSeconds(),
        new Date().getMilliseconds()
    );
    return isoDate.toISOString();
}

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

function getCheckBacklogByTaskPlanning(id: string): boolean {
  let projectBacklogTaskPlanning = task.value.taskPlanning?.projectBacklog
  if(projectBacklogTaskPlanning !== undefined){
    return projectBacklogTaskPlanning.some(backlog => backlog.id === id);
  }
  return false
  
}

function getCheckMemberByTaskPlanning(id: string, projectMemberTaskPlanning: {id: string }[]): boolean {
  return projectMemberTaskPlanning.some(member => member.id === id);
}



const inputFields = ref<{ generalFields: InputFieldProps[], specificFields: InputFieldProps[] }>({ generalFields: [], specificFields: [] });
const inputFieldsRequirement = ref<{ generalFields: InputFieldProps[], specificFields: InputFieldProps[] }>({ generalFields: [], specificFields: [] });
const inputFieldsImprovement = ref<{ generalFields: InputFieldProps[], specificFields: InputFieldProps[] }>({ generalFields: [], specificFields: [] });
const inputFieldsBug = ref<{ generalFields: InputFieldProps[], specificFields: InputFieldProps[] }>({ generalFields: [], specificFields: [] });
const inputFieldsPlanning = ref<{ generalFields: InputFieldProps[], specificFields: InputFieldProps[], backlogFields : InputFieldProps[],  membersFields : InputFieldProps[]}>({ generalFields: [], specificFields: [], backlogFields: [], membersFields: []});
const inputFieldsReview = ref<{ generalFields: InputFieldProps[], specificFields: InputFieldProps[], backlogIterationFields : InputFieldProps[], backlogCompletedFields : InputFieldProps[], membersFields : InputFieldProps[]}>({ generalFields: [], specificFields: [], backlogIterationFields: [], backlogCompletedFields:[], membersFields: []});
const inputFieldsRetrospective = ref<{ generalFields: InputFieldProps[], specificFields: InputFieldProps[], membersFields : InputFieldProps[]}>({ generalFields: [], specificFields: [],  membersFields: []});

let typeTaskForm: { [key: string]: { generalFields: InputFieldProps[], specificFields: InputFieldProps[]} } = {
  '1': inputFieldsRequirement.value,
  '2': inputFieldsImprovement.value,
  '3': inputFieldsBug.value,
  '4': inputFieldsPlanning.value,
  '5': inputFieldsReview.value,
  '6': inputFieldsRetrospective.value
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
    console.log(task.value)
  });
}

function setTaskFormByType() {
  let removeFields = Object.keys(formValidations).filter((key: string) => key !== 'title' && key !== 'type')
  removeFields.forEach((field: string) => {
    delete formValidations[field]
  })

  let type: string = taskForm.value.values.type
  let typeInputFields: InputFieldProps[] = typeTaskForm[type].generalFields.concat(typeTaskForm[type].specificFields)

  if(type === 'Planejamento' || type === '4'){
    isTaskPlanning.value = true
  }
  
  if(type === 'Revisão' || type === '5'){
    isTaskReview.value = true
  }

  if(type === 'Retrospectiva' || type === '6'){
    isTaskRetrospective.value = true
  }

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
    let index = 0
    console.log(backlogIterarion.value)
    backlogIterarion.value.forEach((element)=>{
      console.log(element.title)
      inputFieldsReview.value.backlogIterationFields[index] = {
        name: ""+element.id,
        label: element.title,
        placeholder: "",
        type: "span",
        required: false,
        validation: yup.string(),
        disabled: true
      }
      index++
    })
    index = 0
    if (task.value.taskReview !== undefined) {
        task.value.taskReview?.completedScope.forEach((element) => {
          inputFieldsReview.value.backlogCompletedFields[index] = {
            name: "" + (element.id !== undefined ? element.id : ''),
            label: element.title || '',
            placeholder: "",
            type: "span",
            required: false,
            validation: yup.string(),
            disabled: true
          };
          index++;
        });
      }
  });
}

async function setBacklogProject() {
    await $functionalRequirementStore.fetchFunctionalRequirements(String($route.params.projectId)).then(async () => {
    backlogRequirements.value =  $functionalRequirementStore.functionalRequirements
    let index = 0
    backlogRequirements.value.forEach((element)=>{
      inputFieldsPlanning.value.backlogFields[index] = {
        name: ""+element.id,
        label: element.title,
        placeholder: "",
        type: "checkbox",
        required: false,
        validation: yup.string(),
        value: getCheckBacklogByTaskPlanning(element.id !== undefined ? element.id : '')
      }
      index++
    })
  })
}

async function setTeamMembers() {
  await $teamMemberStore.fetchTeamMembers(String($route.params.projectId)).then(() => {
    teamMembers.value = $teamMemberStore.teamMembers
    let index = 0
    teamMembers.value.forEach((element)=>{
      inputFieldsPlanning.value.membersFields[index] = {
        name: ""+element.id,
        label: element.user.name,
        placeholder: "",
        type: "checkbox",
        required: false,
        validation: yup.string(),
        value: getCheckMemberByTaskPlanning(element.id !== undefined ? element.id : '', task.value.taskPlanning?.projectMembers !== undefined ? task.value.taskPlanning?.projectMembers : [])
      }
      index++
    })
    index = 0
    teamMembers.value.forEach((element)=>{
      inputFieldsReview.value.membersFields[index] = {
        name: ""+element.id,
        label: element.user.name,
        placeholder: "",
        type: "checkbox",
        required: false,
        validation: yup.string(),
        value: getCheckMemberByTaskPlanning(element.id !== undefined ? element.id : '', task.value.taskReview?.participatingMembers !== undefined ? task.value.taskReview?.participatingMembers : [])
      }
      index++
    })
    index = 0
    teamMembers.value.forEach((element)=>{
      inputFieldsRetrospective.value.membersFields[index] = {
        name: ""+element.id,
        label: element.user.name,
        placeholder: "",
        type: "checkbox",
        required: false,
        validation: yup.string(),
        value: getCheckMemberByTaskPlanning(element.id !== undefined ? element.id : '', task.value.taskRetrospective?.participants !== undefined ? task.value.taskRetrospective?.participants : [])
      }
      index++
    })
  })
  
}


let taskType: { [key: string]: string } = {
  '1': 'Requisito',
  '2': 'Melhoria',
  '3': 'Bug',
  '4': 'Planejamento',
  '5': 'Revisão',
  '6': 'Retrospectiva'
}

let taskTypeValues: { [key: string]: string } = {
  'Requisito': '1',
  'Melhoria': '2',
  'Bug': '3',
  'Planejamento': '4',
  'Revisão': '5',
  'Retrospectiva': '6' 
}

async function onSubmit(values: any) {


  let taskRequirement: any = null
  if (task.value.taskType === 'Requisito') {
    taskRequirement = {
      id: task.value.taskRequirement?.id,
      detailsCancelled: values.detailsCancelled,
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
      detailsCancelled: values.detailsCancelled,
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
      detailsCancelled: values.detailsCancelled,
      checkFront: values.checkFront,
      checkBack: values.checkBack,
      checkTest: values.checkTest
    }
  }



  var assignees: Assignee[] = [
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

  let taskPlanning: any = null
  if (task.value.taskType === 'Planejamento') {
    let projectBacklogSelected:BacklogRequirement[] = []
    let membersSelected: TeamMember [] = []
    let indexBacklog = 0
    let indexMember = 0
    backlogRequirements.value.forEach((element)=>{
      if(element.id !== undefined){
        if(values[element.id] === true){
          projectBacklogSelected[indexBacklog] = element
          indexBacklog++
        }
      }
    })
    teamMembers.value.forEach((element)=>{
      if(element.id !== undefined){
        if(values[element.id] === true){
          membersSelected[indexMember] = element
          indexMember++
        }
      }
    })
    taskPlanning = {
      id: task.value.taskPlanning?.id,
      totalSize: values.totalSize,
      totalEffort: values.totalEffort,
      plannedSpeed: values.plannedSpeed,
      projectBacklog: projectBacklogSelected,
      projectMembers: membersSelected
    }

    assignees = []
}

  let taskReview: any = null
  if (task.value.taskType === 'Revisão'){
    let membersSelected: TeamMember [] = []
    let indexMember = 0
    teamMembers.value.forEach((element)=>{
      if(element.id !== undefined){
        if(values[element.id] === true){
          membersSelected[indexMember] = element
          indexMember++
        }
      }
    })
    console.log(values)
    taskReview = {
      id: task.value.taskReview?.id,
      participatingMembers: membersSelected,
      checkHumanResources: values.checkHumanResources,
      checkSpeed: values.checkSpeed,
      checkScope: values.checkScope,
      checkRisks: values.checkRisks,
    }
    console.log(taskReview)
    assignees = []
  }

  let taskRetrospective: any = null
  if (task.value.taskType === 'Retrospectiva'){
    let membersSelected: TeamMember [] = []
    let indexMember = 0
    teamMembers.value.forEach((element)=>{
      if(element.id !== undefined){
        if(values[element.id] === true){
          membersSelected[indexMember] = element
          indexMember++
        }
      }
    })
    taskRetrospective = {
      id: task.value.taskReview?.id,
      participants: membersSelected,
      strengths: values.strengths,
      weaknesses: values.weaknesses,
      improvements: values.improvements
    }
    assignees = []
  }

  const taskData: TaskForm = {
    startDate: values.startDate !== undefined ? toISODate(values.startDate) : undefined,
    endDate: values.endDate !== undefined ? toISODate(values.endDate) : undefined,
    taskType: taskType[values.type],
    assignees,
    taskRequirement,
    taskImprovement,
    taskBug,
    taskPlanning,
    taskReview,
    taskRetrospective
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
    await setTask().then(async () => {
      await setBacklogIteration().then(async () => {
        await setBacklogProject().then(async ()=>{
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
                    { value: "4", name: "Planejamento", selected: task.value.taskType === "Planejamento"},
                    { value: "5", name: "Revisão", selected: task.value.taskType === "Revisão"},
                    { value: "6", name: "Retrospectiva", selected: task.value.taskType === "Retrospectiva"}
                  ],
                  required: true,
                  value: task.value.taskType ? taskTypeValues[task.value.taskType] : '',
                  validation: yup.string().required(),
                  onChange: setTaskFormByType,
                  disabled: true
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
                },
                {
                  name: "detailsCancelled",
                  label: "Cancelamento de Tarefa",
                  placeholder: "Digite o motivo do cancelamento da tarefa",
                  type: "textarea",
                  required: false,
                  disabled: task.value.checkCancelled,
                  validation: yup.string(),
                  value: task.value.detailsCancelled
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
                  value: String(getStepDeadline('R')) !== 'undefined' ? String(getStepDeadline('R')) : '0'
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
                  value: String(getStepDeadline('P')) !== 'undefined' ? String(getStepDeadline('P')) : '0'
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
                  value: String(getStepDeadline('F')) !== 'undefined' ? String(getStepDeadline('F')) : '0'
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
                  value: String(getStepDeadline('B')) !== 'undefined' ? String(getStepDeadline('B')) : '0'
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
                  value: String(getStepDeadline('T')) !== 'undefined' ? String(getStepDeadline('T')) : '0'
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
                },     
                {
                  name: "detailsCancelled",
                  label: "Cancelamento de Tarefa",
                  placeholder: "Digite o motivo do cancelamento da tarefa",
                  type: "textarea",
                  required: false,
                  disabled: task.value.checkCancelled,
                  validation: yup.string().required(),
                  value: task.value.detailsCancelled
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
                  value: String(getStepDeadline('R')) !== 'undefined' ? String(getStepDeadline('R')) : '0'
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
                  value: String(getStepDeadline('P')) !== 'undefined' ? String(getStepDeadline('P')) : '0'
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
                  value: String(getStepDeadline('F')) !== 'undefined' ? String(getStepDeadline('F')) : '0'
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
                  value: String(getStepDeadline('B')) !== 'undefined' ? String(getStepDeadline('B')) : '0'
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
                  value: String(getStepDeadline('T')) !== 'undefined' ? String(getStepDeadline('T')) : '0'
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
                },
                {
                  name: "detailsCancelled",
                  label: "Cancelamento de Tarefa",
                  placeholder: "Digite o motivo do cancelamento da tarefa",
                  type: "textarea",
                  required: false,
                  disabled: task.value.checkCancelled,
                  validation: yup.string().required(),
                  value: task.value.detailsCancelled
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
                  value: String(getStepDeadline('F')) !== 'undefined' ? String(getStepDeadline('F')) : '0'
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
                  value: String(getStepDeadline('B')) !== 'undefined' ? String(getStepDeadline('B')) : '0'
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
                  value: String(getStepDeadline('T')) !== 'undefined' ? String(getStepDeadline('T')) : '0'
                }
              ]
            }

            inputFieldsPlanning.value = {
              generalFields: [],
              specificFields: [
              {
                  name: "startDate",
                  label: "Data inicial",
                  placeholder: "Data inicial",
                  type: "text",
                  required: false,
                  validation: yup.string(),
                  value: task.value.startDate === undefined ? `${getCurrentDate}` : String(formatDate(task.value.startDate))
                },
                {
                  name: "endDate",
                  label: "Data final",
                  placeholder: "Data final",
                  type: "text",
                  required: false,
                  validation: yup.string(),
                  value: task.value.endDate === undefined ? `${getCurrentDate}` : String(formatDate(task.value.endDate))
                },
              {
                  name: "totalSize",
                  label: "Tamanho total",
                  placeholder: "Tamanho total selecionado",
                  type: "text",
                  required: false,
                  validation: yup.number(),
                  value: task.value.taskPlanning?.totalSize === undefined ? '0' : String(task.value.taskPlanning?.totalSize),
                  disabled: true
                },
                {
                  name: "totalEffort",
                  label: "Esforço total",
                  placeholder: "Esforço total selecionado",
                  type: "text",
                  required: false,
                  validation: yup.number(),
                  value: task.value.taskPlanning?.totalEffort === undefined ? '0' : String(task.value.taskPlanning?.totalEffort),
                  disabled: true
                },      {
                  name: "plannedSpeed",
                  label: "Tamanho total",
                  placeholder: "Tamanho total selecionado",
                  type: "text",
                  required: false,
                  validation: yup.number(),
                  value: task.value.taskPlanning?.plannedSpeed === undefined ? '0' : String(task.value.taskPlanning?.plannedSpeed),
                  disabled: true
                },
              ],
              backlogFields: inputFieldsPlanning.value.backlogFields,
              membersFields: inputFieldsPlanning.value.membersFields
            }

            inputFieldsReview.value = {
              generalFields: [],
              specificFields: [
              {
                  name: "startDate",
                  label: "Data inicial",
                  placeholder: "Data inicial",
                  type: "text",
                  required: false,
                  validation: yup.string(),
                  value: task.value.startDate === undefined ? `${getCurrentDate}` : String(formatDate(task.value.startDate))
                },
                {
                  name: "endDate",
                  label: "Data final",
                  placeholder: "Data final",
                  type: "text",
                  required: false,
                  validation: yup.string(),
                  value: task.value.endDate === undefined ? `${getCurrentDate}` : String(formatDate(task.value.endDate))
                },
              {
                  name: "totalSize",
                  label: "Tamanho total concluído",
                  placeholder: "Tamanho total concluído",
                  type: "text",
                  required: false,
                  validation: yup.number(),
                  value: task.value.taskReview?.totalSize === undefined ? '0' : String(task.value.taskReview?.totalSize),
                  disabled: true
                },
                {
                  name: "totalEffort",
                  label: "Esforço total concluído",
                  placeholder: "Esforço total concluído",
                  type: "text",
                  required: false,
                  validation: yup.number(),
                  value: task.value.taskReview?.totalEffort === undefined ? '0' : String(task.value.taskReview?.totalEffort),
                  disabled: true
                },      
                {
                  name: "completedSpeed",
                  label: "Velocidade concluída",
                  placeholder: "Velocidade total concluída",
                  type: "text",
                  required: false,
                  validation: yup.number(),
                  value: task.value.taskReview?.completedSpeed === undefined ? '0' : String(task.value.taskReview?.completedSpeed),
                  disabled: true
                },
                {
                  name: "checkHumanResources",
                  label: "Recursos humanos de acordo?",
                  placeholder: "",
                  type: "checkbox",
                  required: false,
                  validation: yup.string(),
                  value: task.value.taskReview?.checkHumanResources === undefined ? true : Boolean(task.value.taskReview?.checkHumanResources),
                },
                {
                  name: "checkSpeed",
                  label: "Velocidade concluída de acordo?",
                  placeholder: "",
                  type: "checkbox",
                  required: false,
                  validation: yup.string(),
                  value: task.value.taskReview?.checkSpeed === undefined ? true : Boolean(task.value.taskReview?.checkSpeed),
                },
                {
                  name: "checkScope",
                  label: "Escopo concluído de acordo?",
                  placeholder: "",
                  type: "checkbox",
                  required: false,
                  validation: yup.string(),
                  value: task.value.taskReview?.checkScope === undefined ? true : Boolean(task.value.taskReview?.checkScope),
                },
                {
                  name: "checkRisks",
                  label: "Riscos de acordo?",
                  placeholder: "",
                  type: "checkbox",
                  required: false,
                  validation: yup.string(),
                  value: task.value.taskReview?.checkRisks === undefined ? true : Boolean(task.value.taskReview?.checkRisks),
                },
              ],
              backlogIterationFields: inputFieldsReview.value.backlogIterationFields,
              backlogCompletedFields: inputFieldsReview.value.backlogCompletedFields,
              membersFields: inputFieldsReview.value.membersFields
            }

            inputFieldsRetrospective.value = {
              generalFields: [],
              specificFields: [
              {
                  name: "startDate",
                  label: "Data inicial",
                  placeholder: "Data inicial",
                  type: "text",
                  required: false,
                  validation: yup.string(),
                  value: task.value.startDate === undefined ? `${getCurrentDate}` : String(formatDate(task.value.startDate))
                },
                {
                  name: "endDate",
                  label: "Data final",
                  placeholder: "Data final",
                  type: "text",
                  required: false,
                  validation: yup.string(),
                  value: task.value.endDate === undefined ? `${getCurrentDate}` : String(formatDate(task.value.endDate))
                },
                {
                  name: "strengths",
                  label: "Pontos fortes",
                  placeholder: "Digite aqui os pontos fortes desta iteração",
                  type: "textarea",
                  required: false,
                  disabled: task.value.checkCancelled,
                  validation: yup.string().required(),
                  value: task.value.taskRetrospective?.strengths === undefined ? '' : String(task.value.taskRetrospective?.strengths)
                },
                {
                  name: "weaknesses",
                  label: "Pontos fracos",
                  placeholder: "Digite aqui os pontos fracos desta iteração",
                  type: "textarea",
                  required: false,
                  validation: yup.string().required(),
                  value: task.value.taskRetrospective?.weaknesses === undefined ? '' : String(task.value.taskRetrospective?.weaknesses)
                },
                {
                  name: "improvements",
                  label: "Pontos de melhoria",
                  placeholder: "Digite aqui os pontos a serem melhorados desta iteração",
                  type: "textarea",
                  required: false,
                  validation: yup.string().required(),
                  value: task.value.taskRetrospective?.improvements === undefined ? '' : String(task.value.taskRetrospective?.improvements)
                },
          
              ],
              membersFields: inputFieldsRetrospective.value.membersFields
            }

            typeTaskForm['1'] = inputFieldsRequirement.value
            typeTaskForm['2'] = inputFieldsImprovement.value
            typeTaskForm['3'] = inputFieldsBug.value
            typeTaskForm['4'] = inputFieldsPlanning.value
            typeTaskForm['5'] = inputFieldsReview.value
            typeTaskForm['6'] = inputFieldsRetrospective.value
          })
        });
      })
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
        v-for="inputField in inputFields.specificFields.filter((inputField)=> inputField.name !== 'detailsCancelled')"
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
    <div class="w-full flex flex-col items-start">
      <p v-if="isTaskReview" class="text-left">
        Marque ou desmarque as checkbox se os itens estão ou não de acordo. Por padrão todos estarão marcados.
      </p>
    </div>
    <div 
      v-if="isTaskPlanning"
      class="grid grid-cols-2 lg:grid-cols-2 w-full gap-5">
      <div class="flex flex-col space-y-5">
        <span class="font-bold">Backlog do projeto</span>
        <div
          v-for="inputField in inputFieldsPlanning.backlogFields"
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
      <div class="flex flex-col space-y-5">
        <span class="font-bold">Recursos Humanos</span>
        <div
          v-for="inputField in inputFieldsPlanning.membersFields"
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
    </div>
    <div 
      v-if="isTaskReview"
      class="grid grid-cols-2 lg:grid-cols-2 w-full gap-5">
      <div class="flex flex-col space-y-5">
        <span class="font-bold">Backlog da iteração</span>
        <div
          v-for="inputField in inputFieldsReview.backlogIterationFields"
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
      <div class="flex flex-col space-y-5">
        <span class="font-bold">Escopo concluído</span>

        <!-- Verifica se inputFieldsReview.backlogCompletedFields está vazio -->
        <p v-if="inputFieldsReview.backlogCompletedFields.length === 0">
          Não foram encontrados requisitos com status de concluído
        </p>

        <!-- Renderiza os campos se a lista não estiver vazia -->
        <div
          v-else
          v-for="inputField in inputFieldsReview.backlogCompletedFields"
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
      <div class="flex flex-col space-y-5">
        <span class="font-bold">Recursos Humanos</span>
        <div
          v-for="inputField in inputFieldsReview.membersFields"
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
    </div>
    <div 
      v-if="isTaskRetrospective"
      class="grid grid-cols-2 lg:grid-cols-2 w-full gap-5">
      <div class="flex flex-col space-y-5">
        <span class="font-bold">Recursos Humanos</span>
        <div
          v-for="inputField in inputFieldsRetrospective.membersFields"
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
    </div>
    <div class="grid grid-cols-1 lg:grid-cols-1 w-full gap-5">
      <div
        v-for="inputField in inputFields.specificFields.filter((inputField)=> inputField.name === 'detailsCancelled')"
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
        <span>Voltar</span>
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