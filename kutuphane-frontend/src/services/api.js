import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8081/api/v1',
});

// Request Interceptor: Her isteğin Header'ına Bearer JWT Token'ı otomatik yapıştırır
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('kutuphane_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response Interceptor: 401/403 yetkisiz hatasında oturumu temizleyip Login'e atar
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      localStorage.removeItem('kutuphane_token');
      localStorage.removeItem('kutuphane_kullanici');
      if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;