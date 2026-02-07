import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { productApi } from "../../api/productApi";
import { toast } from "react-toastify";

//Thunks assíncronos (ações que fazem chamadas à API)

//Buscar todos os produtos
export const fetchProducts = createAsyncThunk(
  "products/fetchAll",
  async (_, { rejectWithValue }) => {
    try {
      return await productApi.getAll();
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || "Failed to fetch products",
      );
    }
  },
);

//Buscar produto por ID
export const fetchProductById = createAsyncThunk(
  "products/fetchById",
  async (id, { rejectWithValue }) => {
    try {
      return await productApi.getWithMaterials(id);
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || "Failed to fetch product",
      );
    }
  },
);

//Criar produto
export const createProduct = createAsyncThunk(
  "products/create",
  async (productData, { rejectWithValue }) => {
    try {
      const product = await productApi.create(productData);
      toast.success("Product created successfully!");
      return product;
    } catch (error) {
      const message =
        error.response?.data?.message || "Failed to create product";
      toast.error(message);
      return rejectWithValue(message);
    }
  },
);

//Atualizar produto
export const updateProduct = createAsyncThunk(
  "products/update",
  async ({ id, data }, { rejectWithValue }) => {
    try {
      const product = await productApi.update(id, data);
      toast.success("Product updated successfully!");
      return product;
    } catch (error) {
      const message =
        error.response?.data?.message || "Failed to update product";
      toast.error(message);
      return rejectWithValue(message);
    }
  },
);

//Deletar produto
export const deleteProduct = createAsyncThunk(
  "products/delete",
  async (id, { rejectWithValue }) => {
    try {
      await productApi.delete(id);
      toast.success("Product deleted successfully!");
      return id;
    } catch (error) {
      const message =
        error.response?.data?.message || "Failed to delete product";
      toast.error(message);
      return rejectWithValue(message);
    }
  },
);

//Adicionar matéria-prima ao produto
export const addMaterialToProduct = createAsyncThunk(
  "products/addMaterial",
  async ({ productId, materialData }, { rejectWithValue }) => {
    try {
      await productApi.addMaterial(productId, materialData);
      toast.success("Material added to product!");
      return { productId, materialData };
    } catch (error) {
      const message = error.response?.data?.message || "Failed to add material";
      toast.error(message);
      return rejectWithValue(message);
    }
  },
);

//Slice de Produtos
const productSlice = createSlice({
  name: "products",
  initialState: {
    items: [],
    currentProduct: null,
    loading: false,
    error: null,
  },
  reducers: {
    clearCurrentProduct: (state) => {
      state.currentProduct = null;
    },
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      //Fetch all products
      .addCase(fetchProducts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProducts.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchProducts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })

      //fetch product by ID
      .addCase(fetchProductById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProductById.fulfilled, (state, action) => {
        state.loading = false;
        state.currentProduct = action.payload;
      })
      .addCase(fetchProductById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })

      //create product
      .addCase(createProduct.pending, (state) => {
        state.loading = true;
      })
      .addCase(createProduct.fulfilled, (state, action) => {
        state.loading = false;
        state.items.push(action.payload);
      })
      .addCase(createProduct.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })

      //update product
      .addCase(updateProduct.pending, (state) => {
        state.loading = true;
      })
      .addCase(updateProduct.fulfilled, (state, action) => {
        state.loading = false;
        const index = state.items.findIndex((p) => p.id === action.payload.id);
        if (index !== -1) {
          state.items[index] = action.payload;
        }
      })
      .addCase(updateProduct.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })

      //Delete product
      .addCase(deleteProduct.pending, (state) => {
        state.loading = true;
      })
      .addCase(deleteProduct.fulfilled, (state, action) => {
        state.loading = false;
        state.items = state.items.filter((p) => p.id !== action.payload);
      })
      .addCase(deleteProduct.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  },
});

export const { clearCurrentProduct, clearError } = productSlice.actions;
export default productSlice.reducer;
