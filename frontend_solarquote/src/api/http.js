import axios from 'axios';

const http = axios.create({
  baseURL: 'https://gds-solar-backend.onrender.com',
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');

  const isAuthRoute = config.url?.startsWith('/auth/');

  if (token && !isAuthRoute) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

export default http;