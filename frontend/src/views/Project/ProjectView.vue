<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { models } from "src/@types";
import { useProjectStore } from "src/stores/ProjectStore";
import { useRoleStore } from "src/stores/RoleStore";
import SectionsBreadcrumbs from "./components/SectionsBreadcrumbs.vue";

interface ProjectOnView extends models.ProjectOnView {}
interface Role extends models.Role {}

const $emits = defineEmits(['sideViewContentChange'])

const projectDefault = {
  id: "",
  name: "",
  deadline: 0,
  workHours: 0,
  iterationTime: 0,
  clientName: "",
  createdBy: "",
  roles: []
}

const $projectStore = useProjectStore()
const $roleStore = useRoleStore()

const $route = useRoute()

const project = ref<ProjectOnView>({...projectDefault})

onMounted(async () => {
  await $projectStore.fetchProject(String($route.params.projectId)).then(async () => {
    project.value = { 
      ...project.value,
      ...$projectStore.project,
    }

    $emits('sideViewContentChange', {
      name: project.value.name,
      deadline: project.value.deadline,
      workHours: project.value.workHours,
      iterationTime: project.value.iterationTime,
      clientName: project.value.clientName
    })
    
    await $roleStore.fetchRoles(String($route.params.projectId)).then(async () => {
      project.value.roles = $roleStore.roles
    })
  })
})

</script>

<template>
  <div class="flex flex-col gap-5">
    <div class="flex items-center p-2 gap-5">
      <FontAwesomeIcon
        icon="fa-solid fa-folder"
      />

      <span>{{ project.name }}</span>
    </div>

    <SectionsBreadcrumbs />

    <RouterView @side-view-content-change="(value: any) => $emit('sideViewContentChange', value)" />
  </div>
</template>