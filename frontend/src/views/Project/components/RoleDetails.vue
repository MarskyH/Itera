<script setup lang="ts">
import { onMounted, ref } from "vue";
import { models } from "src/@types";
import { useRoleStore } from "src/stores/RoleStore";

interface Role extends models.Role {}

const roleDefault: Role = {
  id: "",
  function: "",
  skill: "",
  competency: "",
}

const $roleStore = useRoleStore()

const role = ref<Role>({...roleDefault})

$roleStore.$subscribe(() => {
  role.value = {...$roleStore.role}
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
  </div>
</template>
