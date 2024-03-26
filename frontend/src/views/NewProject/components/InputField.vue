<script setup lang="ts">
  import { Field, ErrorMessage } from 'vee-validate'

  defineEmits(['change'])

  defineProps({
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
      default: ''
    },
    options: {
      type: Array<{ value: string; name: string; selected: boolean }>,
      default: [],
      required: false
    }
  })
</script>

<template>
  <div class="flex flex-col gap-1">
    <span
      class="text-semibold text-sm"
    >
      {{ label }}

      <span
        v-if="required"
        class="text-red-500 dark:text-red-400"
      >*</span>
    </span>

    <Field
      :name="name"
      :validate-on-input="true"
      :placeholder="placeholder"
      :as="type"
      :value="value"
      :disabled="disabled"
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
    </Field>

    <ErrorMessage
      :name="name"
      class="text-xs text-red-500 dark:text-red-400"
    />
  </div>
</template>