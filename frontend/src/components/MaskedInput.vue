<script setup lang="ts">

import CurrencyInput from './CurrencyInput.vue';
import TimeInput from './TimeInput.vue';

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
    type: [String, Boolean],
    default: ''
  },
  mask: {
    type: String,
    default: undefined,
    required: false
  }
})

const maskedInputs: any = {
  currency: CurrencyInput,
  time: TimeInput
}

</script>

<template>
  <component
    :is="maskedInputs[props.mask || 'currency']"
    v-bind="props"
  />
</template>