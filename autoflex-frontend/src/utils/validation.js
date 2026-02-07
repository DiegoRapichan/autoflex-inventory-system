//Valida se campo é obrigatório
export const required = (value) => {
  if (!value || (typeof value === "string" && value.trim() === "")) {
    return "This field is required";
  }
  return null;
};

//Valida se é número positivo
export const positiveNumber = (value) => {
  const num = parseFloat(value);
  if (isNaN(num) || num <= 0) {
    return "Must be a positive number";
  }
  return null;
};

//Valida se é número >= 0
export const nonNegativeNumber = (value) => {
  const num = parseFloat(value);
  if (isNaN(num) || num < 0) {
    return "Must be zero or positive";
  }
  return null;
};

//Valida código único (formato alfanumérico)
export const validCode = (value) => {
  if (!value) return "Code is required";

  const codeRegex = /^[A-Z0-9-]+$/;
  if (!codeRegex.test(value)) {
    return "Code must contain only uppercase letters, numbers, and hyphens";
  }

  return null;
};

//Valida tamanho mínimo
export const minLength = (min) => (value) => {
  if (!value || value.length < min) {
    return `Must be at least ${min} characters`;
  }
  return null;
};

//Valida tamanho máximo
export const maxLength = (max) => (value) => {
  if (value && value.length > max) {
    return `Must be at most ${max} characters`;
  }
  return null;
};

//Combina múltiplos validadores
export const validate = (value, validators) => {
  for (const validator of validators) {
    const error = validator(value);
    if (error) return error;
  }
  return null;
};
