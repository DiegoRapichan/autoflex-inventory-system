import { configureStore } from "@reduxjs/toolkit";
import productReducer from "./slices/productSlice";
import rawMaterialReducer from "./slices/rawMaterialSlice";
import productionReducer from "./slices/productionSlice";

/**
 * Redux Store centralizada
 * Gerencia todo o estado da aplicação
 */
export const store = configureStore({
  reducer: {
    products: productReducer,
    rawMaterials: rawMaterialReducer,
    production: productionReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false, // Desabilita check de serialização para simplificar
    }),
});
