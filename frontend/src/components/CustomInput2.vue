<template>
  <div
    class="input"
    :id="name.replaceAll('.', '-')"
  >
    <OField
      message-class="text-xs mt-1 max-w-[250px]"
      :label-class="classNames({
        ['m-2 text-sm ml-1 font-normal dark:text-oldSilver-900']: true,
        ['opacity-80']: disabled
      })"
    >
      <template #label>
        <div v-if="label">
          {{ label }}
          <sup
            class="text-red-500"
            v-if="required"
          >*</sup>
        </div>
        <div v-if="customLabel">
          <div class="mb-1.5">
            <slot name="label" />
            <sup
              class="text-red-500"
              v-if="required"
            >*</sup>
          </div>
        </div>
      </template>
      <template #default>
        <div
          :class="classNames({
            ['outline-2 outline outline-lavenderIndigo-900 bg-white dark:bg-blackOlive-900 text-quickSilver-900']: focused && !disabled,
            ['bg-gray-100 cursor-not-allowed']: disabled,
            ['outline-2 outline outline-red-500']: !meta.valid && meta.validated && meta.touched,
            ['outline-2 outline outline-emerald-500']: meta.valid && meta.validated && meta.dirty,
            ['']: withoutValidation,
            [`${inputClass} rounded-lg w-80 h-10 py-1 px-3 border grid grid-cols-8`]: true
          })"
          v-if="type !== 'textarea'"
        >
          <div
            :class="{ ['cursor-not-allowed']: disabled }"
            class="flex flex-row text-gray-400 items-center"
          >
            <img
              class="w-[20px]"
              :src="iconPath"
              v-if="imgIcon"
              alt="Icon"
            >

            <SvgIcon
              type="mdi"
              class="col-span-1"
              size="20"
              :path="iconPath"
              v-else-if="iconPath"
            />
          </div>
          <money3
            v-if="money"
            class="col-span-6 focus:outline-none bg-transparent"
            :class="iconPath ? 'col-span-7' : 'col-span-8'"
            :model-value="inputValue"
            v-bind="config"
            :placeholder="placeholder"
            :disabled="disabled"
            :type="type"
            :name="name"
            :required="required"
            :step="step"
            :maxlength="maxLength"
            @update:model-value="handleInput"
            @focus="() => {
              focused = true
              config.allowBlank = false
            }"
            @blur="focused = false; handleBlur()"
          />
          <OInput
            v-else
            override
            :root-class="classNames({
              ['col-span-7']: iconPath,
              ['col-span-8']: !iconPath
            })"
            :input-class="classNames({
              ['focus:outline-none bg-transparent w-full']: true,
              ['cursor-not-allowed']: disabled
            })"
            :status-icon="false"
            :placeholder="placeholder"
            :disabled="disabled"
            :type="type"
            :name="name"
            :required="required"
            :data-maska="mask"
            :step="step"
            :maxlength="maxLength"
            v-model="inputValue"
            @update:model-value="handleInput"
            @focus="focused = true"
            @blur="focused = false; handleBlur()"
            v-maska
          />
        </div>

        <div v-else>
          <OInput
            type="textarea"
            :root-class="classNames({
              ['col-span-7']: iconPath,
              ['col-span-8']: !iconPath,
              ['w-full md:w-1/2']: true
            })"
            :input-class="classNames({
              ['bg-gray-100 cursor-not-allowed']: disabled,
              ['outline outline-red-500']: !meta.valid && meta.validated && meta.touched,
              ['outline outline-emerald-500']: meta.valid && meta.validated && meta.touched,
              ['px-2 py-0.5 border border-gray-400 h-full rounded-md block focus:outline-sky-400 focus:outline-2']: true
            })"
            :placeholder="placeholder"
            :disabled="disabled"
            :name="name"
            :required="required"
            :data-maska="mask"
            :step="step"
            :maxlength="maxLength"
            :style="{ height: height || '128px' }"
            v-model="inputValue"
            @update:model-value="handleInput"
            @focus="focused = true"
            @blur="focused = false; handleBlur()"
            v-maska
          />
        </div>
      </template>
      <template #message>
        <div
          v-if="meta.validated"
          class="text-red-500"
        >
          {{ meta.valid ? null : customErrorMessage ? props.errorMessage : errorMessage }}
        </div>
        <div :class="classHelperText">
          {{ helperText }}
        </div>
      </template>
    </OField>
  </div>
</template>
  
<script lang="ts" setup>
import { ref, toRef } from 'vue'
import { useField } from 'vee-validate'
import classNames from 'classnames'
import SvgIcon from '@jamescoyle/vue-icon'
import { OField, OInput } from '@oruga-ui/oruga-next'

type inputs = 'date' | 'text' | 'email' | 'number' | 'password' | 'textarea' | 'datetime-local'

const $emit = defineEmits(['update:value'])

interface Props {
  name: string
  label?: string
  helperText?: string
  placeholder?: string
  type?: inputs
  iconPath?: string
  inputClass?: string
  required?: boolean
  mask?: string
  maxLength?: number | string
  imgIcon?: boolean
  step?: number | string
  disabled?: boolean,
  classHelperText?: string,
  errorMessage?: string,
  customErrorMessage?: boolean,
  withoutValidation?: boolean
  money?: boolean
  height?: string | number,
  customLabel?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  placeholder: '',
  type: 'text',
  label: '',
  iconPath: '',
  inputClass: '',
  helperText: '',
  mask: '',
  maxLength: 300,
  minLength: 1,
  min: 0,
  step: 1,
  classHelperText: '',
  errorMessage: '',
  customErrorMessage: false,
  withoutValidation: false,
  money: false,
  height: '128px',
  customLabel: false

})

const focused = ref(false)
const name = toRef(props, 'name')
const config = ref({
  prefix: 'R$ ',
  suffix: '',
  masked: false,
  thousands: '.',
  decimal: ',',
  precision: 2,
  allowBlank: true,
  disableNegative: true,
  disabled: false,
  min: null,
  max: null,
  minimumNumberOfCharacters: 0,
  shouldRound: true,
  focusOnRight: true,
  currentValue: 'null'
})


const inputValue = ref<string | number | undefined>(undefined);
// eslint-disable-next-line vue/no-dupe-keys
const {
  handleBlur,
  handleChange,
  errorMessage,
  meta
} = useField(name, undefined)

function handleInput(value: string | number | unknown) {
  if (typeof value === 'string' || typeof value === 'number') {
    inputValue.value = value as string | number;  // ou pode usar inputValue.value = String(value) para garantir que seja uma string
    if (props.money && config.value.currentValue === 'null') {
      return;
    }
    $emit('update:value', value);
    handleChange(value);
  }
}
</script>