import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { rawMaterialApi } from "../../api/rawMaterialApi";
import { toast } from "react-toastify";

// Thunks
export const fetchRawMaterials = createAsyncThunk(
  "rawMaterials/fetchAll",
  async (_, { rejectWithValue }) => {
    try {
      return await rawMaterialApi.getAll();
    } catch (error) {
      return rejectWithValue(
        error.response?.data?.message || "Failed to fetch raw materials",
      );
    }
  },
);

export const createRawMaterial = createAsyncThunk(
  "rawMaterials/create",
  async (data, { rejectWithValue }) => {
    try {
      const material = await rawMaterialApi.create(data);
      toast.success("Raw material created successfully!");
      return material;
    } catch (error) {
      const message =
        error.response?.data?.message || "Failed to create raw material";
      toast.error(message);
      return rejectWithValue(message);
    }
  },
);

export const updateRawMaterial = createAsyncThunk(
  "rawMaterials/update",
  async ({ id, data }, { rejectWithValue }) => {
    try {
      const material = await rawMaterialApi.update(id, data);
      toast.success("Raw material updated successfully!");
      return material;
    } catch (error) {
      const message =
        error.response?.data?.message || "Failed to update raw material";
      toast.error(message);
      return rejectWithValue(message);
    }
  },
);

export const deleteRawMaterial = createAsyncThunk(
  "rawMaterials/delete",
  async (id, { rejectWithValue }) => {
    try {
      await rawMaterialApi.delete(id);
      toast.success("Raw material deleted successfully!");
      return id;
    } catch (error) {
      const message =
        error.response?.data?.message || "Failed to delete raw material";
      toast.error(message);
      return rejectWithValue(message);
    }
  },
);

// Slice
const rawMaterialSlice = createSlice({
  name: "rawMaterials",
  initialState: {
    items: [],
    loading: false,
    error: null,
  },
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchRawMaterials.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchRawMaterials.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchRawMaterials.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })

      .addCase(createRawMaterial.fulfilled, (state, action) => {
        state.items.push(action.payload);
      })

      .addCase(updateRawMaterial.fulfilled, (state, action) => {
        const index = state.items.findIndex((m) => m.id === action.payload.id);
        if (index !== -1) {
          state.items[index] = action.payload;
        }
      })

      .addCase(deleteRawMaterial.fulfilled, (state, action) => {
        state.items = state.items.filter((m) => m.id !== action.payload);
      });
  },
});

export const { clearError } = rawMaterialSlice.actions;
export default rawMaterialSlice.reducer;
