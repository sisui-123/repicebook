// utils/request.js 或 http.js
import axios from 'axios';
import { showToast } from 'vant';

// 创建 Axios 实例
const instance = axios.create({
  baseURL: '/api', // 由 Vite 的代理处理，例如代理到 http://localhost:8080
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
  // 不带凭证（这是 axios 默认行为，保留可读性）
  withCredentials: false
});

// 请求拦截器（无 token 处理）
instance.interceptors.request.use(
  (config) => config,
  (error) => Promise.reject(error)
);

// 响应拦截器
instance.interceptors.response.use(
  (response) => {
    const { data } = response;
    if (data.success) {
      return data;
    } else {
      showToast(data.message || '请求出错');
      return Promise.reject(new Error(data.message || '请求出错'));
    }
  },
  (error) => {
    let message = '网络异常，请稍后再试';
    if (error.response) {
      switch (error.response.status) {
        case 401:
          message = '未授权，请重新登录';
          break;
        case 403:
          message = '拒绝访问';
          break;
        case 404:
          message = '请求的资源不存在';
          break;
        case 500:
          message = '服务器内部错误';
          break;
        default:
          message = `请求错误 (${error.response.status})`;
      }
    }
    showToast(message);
    return Promise.reject(error);
  }
);

// 封装 GET 请求
export const get = (url, params) => {
  return instance.get(url, { params });
};

// 封装 POST 请求
export const post = (url, data) => {
  return instance.post(url, data);
};

// 封装 PUT 请求
export const put = (url, data) => {
  return instance.put(url, data);
};

// 封装 DELETE 请求
export const del = (url, params) => {
  return instance.delete(url, { params });
};

export default instance;
