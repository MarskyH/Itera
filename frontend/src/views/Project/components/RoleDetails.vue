<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { models } from "src/@types";
import { useRoleStore } from "src/stores/RoleStore";

interface Role extends models.Role {}

const props = defineProps<{
  id: string
}>()

const roleDefault: Role = {
  id: "",
  function: "",
  skill: "",
  competency: "",
}

const $roleStore = useRoleStore()

const role = ref<Role>({...roleDefault})

onMounted(async () => {
  await $roleStore.fetchRole(props.id).then(async () => {
    role.value = $roleStore.role
  })
})
</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="flex gap-3 items-center">
      <FontAwesomeIcon
        icon="fa-solid fa-clipboard-user"
        class="text-stone-500 dark:text-stone-400"
      />

      <span class="font-bold">Detalhes do papel</span>
    </div>

    <div class="flex flex-col text-xs p-1 gap-4">
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-folder"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Função</span>

          <span> {{ role.function }} </span>
        </div>
      </div>

      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-handshake-simple"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold text-sm">Habilidades</span>

          <span>{{ role.skill }}</span>
        </div>
      </div>
          
      <div class="flex items-baseline gap-4">
        <FontAwesomeIcon
          icon="fa-solid fa-clock"
          class="text-neutral-400 w-4"
        />

        <div class="flex flex-col gap-1">
          <span class="font-semibold">Prazo</span>

          <span>{{ role.competency }}</span>
        </div>
      </div>
    </div>
    
    <div class="flex justify-end gap-5">
      <button
        class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-blueCrayola-900 dark:text-naplesYellow-900 hover:bg-blueCrayola-900/25 hover:dark:bg-naplesYellow-900/25 text-sm gap-2 text"
        @click="()=> $emit('edit')"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-pen"
        />

        <span class="font-semibold">Editar</span>
      </button>

      <button
        class="flex items-center rounded px-3 py-2 bg-platinum-900 dark:bg-davysGray-900 text-lightRed-900 hover:bg-lightRed-900/25 hover:dark:bg-lightRed-900/25 text-sm gap-2 text"
        @click="()=> $emit('remove')"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-trash"
        />

        <span class="font-semibold">Excluir projeto</span>
      </button>
    </div>
  </div>
</template>
