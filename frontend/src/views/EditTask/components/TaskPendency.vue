<script setup lang="ts">
import { models } from 'src/@types';
import { ref } from 'vue';
import { usePendencyStore } from 'src/stores/PendencyStore';
import ActionModal from 'src/components/ActionModal.vue';
import InformationModal from 'src/components/InformationModal.vue';
//import { title } from 'process';

interface Pendency extends models.Pendency { }
interface PendencyOnUpdate extends models.PendencyOnUpdate { }

defineProps<Pendency>()

const $pendencyStore = usePendencyStore()
const isActionModalOpen = ref<boolean>(false)
const actionModalTitle = ref<string>('Confirmar')
const isInformationModalOpen = ref<boolean>(false)
const informationModalTitle = ref<string>('Informação')
const informartionDescription = ref<string>('Mensagem')
const informationIcon = ref<string>('sucesso')
const responseStatus = ref<number>(500)
const confirmModal = ref<string>('Confirmar')
const checkConfirm = ref<boolean>(false)
const pendencyData: PendencyOnUpdate = {
    status: false
}

function createModal(title: string){
    actionModalTitle.value = `Confirme a resolução da pendência "${title}".`
    isActionModalOpen.value = true
  }


async function updatePendencyAfterModal(pendencyId: string, taskId: string){
    isActionModalOpen.value = false
    if(checkConfirm.value){
      await $pendencyStore.updatePendency(pendencyId, pendencyData).then(async (response) => {
        if(response?.status === 200) {
          isInformationModalOpen.value = true
          responseStatus.value = 200
          informartionDescription.value = "Funcionalidade executada com sucesso"
          informationIcon.value = "check"
        } else {
          isInformationModalOpen.value = true
          responseStatus.value = 500
          informartionDescription.value = "Funcionalidade executada com erros"
          informationIcon.value = "warning"
        }
        await $pendencyStore.fetchPendencies(taskId)
      })
    }
  }

</script>

<template>
  <div
    class="flex flex-col gap-3 my-3 p-4 bg-whiteSmoke-900 dark:bg-jet-900 rounded"
    :class="{'opacity-50': !status}"
  >
    <div
      class="flex gap-3 items-start justify-between"
    >
      <span class="text-sm font-semibold">
        {{ title }}
      </span>

      <button
        v-show="status"
        title="Nova tarefa"
        class="text-xs flex text-white justify-evenly items-center bg-emerald-500 p-2 rounded-md"
        @click="() => createModal(title)"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-check"
        />
      </button>
    </div>

    <span class="text-xs text-justify">
      {{ description }}
    </span>
  </div>
  <ActionModal
    v-model="isActionModalOpen"
    :title="actionModalTitle"
    :confirm="confirmModal"
    icon="circle-check"
    @confirm="()=>{checkConfirm = true
                   updatePendencyAfterModal(id, task_id)
    }"
  />
  <InformationModal
    v-model="isInformationModalOpen"
    :title="informationModalTitle"
    :icon="'information'"
    :icon-message="informationIcon"
    :message="informartionDescription"
    @confirm="()=> isInformationModalOpen = false"
  />
</template>