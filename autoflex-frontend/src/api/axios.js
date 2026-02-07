import axios from "axios";

//Instância configurada do Axios
//Base URL aponta para o backend
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000, //10 segundos
});

//interceptor de request
//Pode adicionar token de autenticação aqui

api.interceptors.request.use(
  (config) => {
    // Se tiver token JWT, adiciona no header
    // const token = localStorage.getItem('token')
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`
    // }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

//Interceptor de response
//Trata erros globalmente

api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Erros comuns
    if (error.response) {
      //Servidor respondeu com status de erro
      console.error("API Error:", error.response.data);

      //Pode adicionar tratamento específico por status
      switch (error.response.status) {
        case 401:
          //Não autorizado - redirecionar para login
          //window.location.href = '/login'
          break;
        case 404:
          console.error("Resource not found");
          break;
        case 500:
          console.error("Server error");
          break;
      }
    } else if (error.request) {
      //Request foi feito mas sem resposta
      console.error("No response from server");
    } else {
      //erro ao configurar request
      console.error("Request setup error:", error.message);
    }

    return Promise.reject(error);
  },
);

export default api;
