<script setup lang="ts">
import { onMounted, ref } from "vue";
import { models } from "src/@types";
import { useTeamMemberStore } from "src/stores/TeamMemberStore";

interface TeamMember extends models.TeamMember {}

const props = defineProps<{
  id: string
}>()

const teamMemberDefault: TeamMember = {
  id: '',
  hourlyRate: 0,
  dedicatedHours: 0,
  user: {
    id: '',
    name: '',
    email: '',
    login: '',
    password: '',
    userRole: '',
  },
  role: {
    id: '',
    function: '',
    skill: '',
    competency: '',
  }
}

const $teamMemberStore = useTeamMemberStore()

const teamMember = ref<TeamMember>({...teamMemberDefault})

onMounted(async () => {
  if ($teamMemberStore.teamMember.id !== "") {
    teamMember.value = {...$teamMemberStore.teamMember}
  } else {
    await $teamMemberStore.fetchTeamMember(props.id).then(async () => {
      teamMember.value = $teamMemberStore.teamMember
    })
  }
})

$teamMemberStore.$subscribe(() => {
  teamMember.value = {...$teamMemberStore.teamMember}
})

</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="flex gap-3 items-center">
      <FontAwesomeIcon
        icon="fa-solid fa-clipboard-user"
        class="text-stone-500 dark:text-stone-400"
      />

      <span class="font-bold">Detalhes do integrante</span>
    </div>

    <div class="flex flex-col text-xs p-1 gap-4">
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-user"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Nome</span>

          <span> {{ teamMember.user.name }} </span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-dollar-sign"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Valor hora-homem</span>

          <span>{{ teamMember.hourlyRate }}</span>
        </div>
      </div>
          
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-user-clock"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Horas dedicadas</span>

          <span>{{ teamMember.dedicatedHours }}</span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-clipboard-user"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Papel</span>

          <span>{{ teamMember.role.function }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
