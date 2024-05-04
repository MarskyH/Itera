<script setup lang="ts">
import { ErrorMessage } from 'vee-validate'
import { onMounted, ref } from 'vue';

import { useField } from 'vee-validate';

defineEmits(['change'])

const props = defineProps({
  label: {
    type: String,
    default: 'label'
  },
  name: {
    type: String,
    default: 'name'
  },
  placeholder: {
    type: String,
    default: 'placeholder'
  },
  type: {
    type: String,
    default: 'input',
    required: false
  },
  required: {
    type: Boolean,
    default: false
  },
  disabled: {
    type: Boolean,
    default: false
  },
  value: {
    type: String,
    default: '',
    required: false
  },
  options: {
    type: Array<{ value: string; name: string; selected: boolean }>,
    default: [],
    required: false
  },
  hoverInfo: {
    type: String,
    default: undefined,
    required: false
  }
})

const { value } = useField(() => props.name, undefined, { initialValue: props.value });

const infoTooltipVisible = ref<boolean>(false)

</script>

<template>
  <div class="flex flex-col gap-1">
    <span
      class="flex text-semibold text-sm"
    >
      {{ label }}
      
      <span
        v-if="required"
        class="text-red-500 dark:text-red-400"
      >*</span>
    
      <button
        class="ml-1"
        v-show="hoverInfo"
        type="button"
        @click="() => infoTooltipVisible = true"
      >
        <FontAwesomeIcon
          icon="fa-solid fa-circle-question"
        />
      </button>
      
    </span>

    <input
      v-if="options.length === 0"
      v-model="value"
      :validate-on-input="true"
      :as="type"
      :disabled="disabled"
      :placeholder="placeholder"
      class="text-xs bg-transparent disabled:text-stone-500 disabled:dark:text-stone-400 disabled:bg-stone-200 disabled:dark:bg-stone-800 dark:bg-jet-900 border-stone-300 dark:border-stone-600 border-[1px] outline-0 rounded px-3 py-2 focus:ring-2 focus:ring-periwinkle-900 focus:dark:ring-charcoal-900"
      @change="() => $emit('change')"
    />

    <select
      v-else
      v-model="value"
      :validate-on-input="true"
      :as="type"
      :disabled="disabled"
      :placeholder="placeholder"
      class="text-xs bg-transparent disabled:text-stone-500 disabled:dark:text-stone-400 disabled:bg-stone-200 disabled:dark:bg-stone-800 dark:bg-jet-900 border-stone-300 dark:border-stone-600 border-[1px] outline-0 rounded px-3 py-2 focus:ring-2 focus:ring-periwinkle-900 focus:dark:ring-charcoal-900"
      @change="() => $emit('change')"
    >
      <option
        v-for="option in options"
        :key="option.value"
        :value="option.value"
        :selected="option.selected"
      >
        {{ option.name }}
      </option>
    </select>

    <ErrorMessage
      :name="name"
      class="text-xs text-red-500 dark:text-red-400"
    />
    
    <div
      v-show="infoTooltipVisible"
    >
      <div
        class="inset-0 fixed top-0 left-0 bg-black/75 flex items-center justify-center z-10"
      >
        <div 
          class="flex flex-col md:w-2/4 sm:w-10/12 max-w-2xl bg-white dark:bg-eerieBlackLight-900 rounded-md m-4 p-4 gap-4"
        >
          <div class="flex w-full gap-4 justify-between items-center">
            <div class="flex gap-4">
              <FontAwesomeIcon
                :icon="`fa-solid fa-gears`"
                class="text-lavenderIndigo-900"
              />
            
              <span class="text-sm font-semibold">
                {{ label }}
              </span>
            </div>

            <button
              @click="() => infoTooltipVisible = false"
              type="button"
            >
              <FontAwesomeIcon
                icon="fa-solid fa-xmark"
              />
            </button>
          </div>
        
          <span class="text-sm whitespace-break-spaces">
            {{ hoverInfo }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>