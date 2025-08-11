// 用户相关API
import { get, post, put } from './http';

/**
 * 用户注册
 * @param {Object} userData - 用户注册数据
 * @param {string} userData.username - 用户名
 * @param {string} userData.password - 密码
 * @param {string} userData.confirmPassword - 确认密码
 * @returns {Promise} 注册结果
 */
export function register(userData) {
  return post('/users/register', userData);
}

/**
 * 用户登录
 * @param {Object} loginData - 登录数据
 * @param {string} loginData.username - 用户名
 * @param {string} loginData.password - 密码
 * @returns {Promise} 登录结果，包含用户信息和token
 */
export function login(loginData) {
  return post('/users/login', loginData);
}

/**
 * 获取单个用户详情
 * @param {string} userId - 用户ID
 * @returns {Promise} 用户详情
 */
export function getUserById(userId) {
  return get(`/users/${userId}`);
}

/**
 * 获取所有用户
 * @returns {Promise} 用户列表
 */
export function getAllUsers() {
  return get('/users');
}

/**
 * 更新用户资料
 * @param {string} userId - 用户ID
 * @param {Object} userData - 更新的用户数据
 * @param {string} userData.bio - 个人简介
 * @param {string} userData.avatarUrl - 头像URL
 * @returns {Promise} 更新后的用户信息
 */
export function updateUser(userId, userData) {
  return put(`/users/${userId}`, userData);
}

export default {
  register,
  login,
  getUserById,
  getAllUsers,
  updateUser
};
