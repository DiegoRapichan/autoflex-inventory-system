import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { productionApi } from "../../api/productionApi";
import { toast } from "react-toastify";

// Thunk para buscar sugestões
export const fetchProductionSuggestions = createAsyncThunk(
  "production/fetchSuggestions",
  async (_, { rejectWithValue }) => {
    try {
      return await productionApi.getSuggestions();
    } catch (error) {
      const message =
        error.response?.data?.message ||
        "Failed to calculate production suggestions";
      toast.error(message);
      return rejectWithValue(message);
    }
  },
);

// Slice
const productionSlice = createSlice({
  name: "production",
  initialState: {
    suggestions: [],
    totalValue: 0,
    totalProductTypes: 0,
    totalUnits: 0,
    generatedAt: null,
    warnings: [],
    loading: false,
    error: null,
  },
  reducers: {
    clearSuggestions: (state) => {
      state.suggestions = [];
      state.totalValue = 0;
      state.totalProductTypes = 0;
      state.totalUnits = 0;
      state.generatedAt = null;
      state.warnings = [];
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchProductionSuggestions.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProductionSuggestions.fulfilled, (state, action) => {
        state.loading = false;
        state.suggestions = action.payload.suggestions || [];
        state.totalValue = action.payload.totalProductionValue || 0;
        state.totalProductTypes = action.payload.totalProductTypes || 0;
        state.totalUnits = action.payload.totalUnits || 0;
        state.generatedAt = action.payload.generatedAt;
        state.warnings = action.payload.warnings || [];
      })
      .addCase(fetchProductionSuggestions.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  },
});

export const { clearSuggestions } = productionSlice.actions;
export default productionSlice.reducer;
