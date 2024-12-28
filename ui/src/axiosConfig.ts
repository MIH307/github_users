import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api",
  withCredentials: true,
  timeout: 10000,
});


const handleLogout = () => {
  localStorage.removeItem("authToken");
  window.location.href = "/auth/login";
};

axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("authToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);


axiosInstance.interceptors.response.use(
  (response) => {
      console.log('Login response Status : OK | URL: ' + response.config.url);
    if (response.config.url === "/api/auth/logout" && response.status === 200) {
      localStorage.removeItem("authToken");
      window.location.href = "/api/auth/login";
    }
    return response;
  },
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
        console.log('error Status : ' + error.response?.status);
        handleLogout();
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
