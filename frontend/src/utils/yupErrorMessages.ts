const yupErrorMessages = {
  mixed: {
    required: 'Campo obrigatório',
    notType: function notType(_ref: any) {
      switch (_ref.type) {
        case 'number':
          return 'Deve ser número';
        case 'string':
          return 'Deve ser texto';
        default:
          return 'Valor inválido';
      }
    }
  },
  string: {
    min: 'Deve ter pelo menos ${min} caracteres',
  },
  number: {
    min: 'Deve ser no mínimo ${min}',
    max: 'Deve ser no máximo ${max}',
    integer: 'Deve ser um número inteiro',
  },
}

export default yupErrorMessages