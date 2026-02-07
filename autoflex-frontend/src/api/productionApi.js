import api from "./axios";

export const productionApi = {
  /**
   * GET /api/production/suggestions
   * Calcula sugestões de produção
   */
  getSuggestions: async () => {
    const response = await api.get("/production/suggestions");
    return response.data;
  },
};
