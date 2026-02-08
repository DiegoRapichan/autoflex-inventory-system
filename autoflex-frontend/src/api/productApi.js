import api from "./axios";
export default api;
/**
 * API de Produtos
 * Todas as chamadas relacionadas a produtos
 */
export const productApi = {
  /**
   * GET /api/products
   * Lista todos os produtos
   */
  getAll: async () => {
    const response = await api.get("/products");
    return response.data;
  },

  /**
   * GET /api/products/:id
   * Busca produto por ID
   */
  getById: async (id) => {
    const response = await api.get(`/products/${id}`);
    return response.data;
  },

  /**
   * GET /api/products/:id/with-materials
   * Busca produto com matérias-primas
   */
  getWithMaterials: async (id) => {
    const response = await api.get(`/products/${id}/with-materials`);
    return response.data;
  },

  /**
   * POST /api/products
   * Cria novo produto
   */
  create: async (productData) => {
    const response = await api.post("/products", productData);
    return response.data;
  },

  /**
   * PUT /api/products/:id
   * Atualiza produto
   */
  update: async (id, productData) => {
    const response = await api.put(`/products/${id}`, productData);
    return response.data;
  },

  /**
   * DELETE /api/products/:id
   * Deleta produto
   */
  delete: async (id) => {
    await api.delete(`/products/${id}`);
  },

  /**
   * GET /api/products/:id/materials
   * Lista matérias-primas do produto
   */
  getMaterials: async (productId) => {
    const response = await api.get(`/products/${productId}/materials`);
    return response.data;
  },

  /**
   * POST /api/products/:id/materials
   * Adiciona matéria-prima ao produto
   */
  addMaterial: async (productId, materialData) => {
    const response = await api.post(
      `/products/${productId}/materials`,
      materialData,
    );
    return response.data;
  },

  /**
   * PUT /api/products/:id/materials/:materialId
   * Atualiza quantidade de matéria-prima
   */
  updateMaterial: async (productId, materialId, data) => {
    const response = await api.put(
      `/products/${productId}/materials/${materialId}`,
      data,
    );
    return response.data;
  },

  /**
   * DELETE /api/products/:id/materials/:materialId
   * Remove matéria-prima do produto
   */
  removeMaterial: async (productId, materialId) => {
    await api.delete(`/products/${productId}/materials/${materialId}`);
  },
};
