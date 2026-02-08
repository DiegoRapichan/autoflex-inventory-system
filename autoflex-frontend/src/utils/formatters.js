/**
 * Formata número como moeda BRL
 */
export const formatCurrency = (value) => {
  if (value === null || value === undefined) return "R$ 0,00";

  return new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL",
  }).format(value);
};

/**
 * Formata número decimal
 */
export const formatNumber = (value, decimals = 2) => {
  if (value === null || value === undefined) return "0";

  return new Intl.NumberFormat("pt-BR", {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals,
  }).format(value);
};

/**
 * Formata data
 */
export const formatDate = (dateString) => {
  if (!dateString) return "-";

  const date = new Date(dateString);
  return new Intl.DateTimeFormat("pt-BR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(date);
};

/**
 * Formata apenas a data (sem hora)
 */
export const formatDateOnly = (dateString) => {
  if (!dateString) return "-";

  const date = new Date(dateString);
  return new Intl.DateTimeFormat("pt-BR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  }).format(date);
};

/**
 * Trunca texto longo
 */
export const truncate = (text, maxLength = 50) => {
  if (!text) return "";
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + "...";
};

/**
 * Verifica se estoque está baixo
 */
export const isLowStock = (quantity, threshold = 10) => {
  return quantity < threshold;
};

/**
 * Retorna classe CSS baseada no nível de estoque
 */
export const getStockBadgeClass = (quantity, threshold = 10) => {
  if (quantity === 0) return "badge-danger";
  if (quantity < threshold) return "badge-warning";
  return "badge-success";
};

/**
 * Formata quantidade com unidade
 */
export const formatQuantityWithUnit = (quantity, unit) => {
  return `${formatNumber(quantity, 3)} ${unit || ""}`;
};
