<script setup lang="ts">

defineProps<{
  modelValue: boolean
  title: string
  icon: string
}>()

defineEmits(['update:modelValue', 'close', 'confirm'])

</script>

<template>
  <div
    v-show="modelValue"
  >
    <div
      class="inset-0 fixed top-0 left-0 bg-black/75 flex items-center justify-center"
    >
      <div class="flex flex-col md:w-2/4 sm:w-10/12 max-w-2xl bg-white dark:bg-eerieBlackLight-900 rounded-md m-4 p-4 gap-4">
        <div class="flex w-full gap-4 justify-between items-center">
          <div class="flex gap-4">
            <FontAwesomeIcon
              :icon="`fa-solid fa-${icon}`"
              class="text-lavenderIndigo-900"
            />
            
            <span class="text-sm font-semibold">
              {{ title }}
            </span>
          </div>

          <button @click="() => { $emit('update:modelValue', false); $emit('close') }">
            <FontAwesomeIcon
              icon="fa-solid fa-xmark"
            />
          </button>
        </div>
        <slot />

        <div class="flex w-full gap-4 justify-center items-center text-sm">
          <button
            class="flex text-white w-32 justify-evenly items-center bg-stone-400 dark:bg-stone-600 px-4 py-2 gap-4 rounded-md"
            @click="() => { $emit('update:modelValue', false); $emit('close')}"
          >
            Cancelar
          </button>

          <button 
            type="submit"
            class="flex text-white w-32 justify-evenly items-center bg-lavenderIndigo-900 px-4 py-2 gap-4 rounded-md"
            @click="() => $emit('confirm')"
          >
            Salvar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>