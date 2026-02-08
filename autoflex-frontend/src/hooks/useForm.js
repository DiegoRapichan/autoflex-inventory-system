import { useState } from "react";

/**
 * Hook para gerenciar estado de formulários
 */
export function useForm(initialState = {}) {
  const [values, setValues] = useState(initialState);
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setValues({
      ...values,
      [name]: type === "checkbox" ? checked : value,
    });

    // Limpa erro do campo ao digitar
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: null,
      });
    }
  };

  const setFieldValue = (name, value) => {
    setValues({
      ...values,
      [name]: value,
    });
  };

  const setFieldError = (name, error) => {
    setErrors({
      ...errors,
      [name]: error,
    });
  };

  const reset = () => {
    setValues(initialState);
    setErrors({});
    setIsSubmitting(false);
  };

  return {
    values,
    errors,
    isSubmitting,
    setIsSubmitting,
    handleChange,
    setFieldValue,
    setFieldError,
    setErrors,
    reset,
  };
}
