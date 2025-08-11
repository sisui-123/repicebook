// 分类相关API
import { get } from './http';

/**
 * 获取单个分类详情
 * @param {string} categoryId - 分类ID
 * @returns {Promise} 分类详情
 */
export function getCategoryById(categoryId) {
  return get(`/categories/${categoryId}`);
}

/**
 * 获取所有分类
 * @returns {Promise} 所有分类列表
 */
export function getAllCategories() {
  return get('/categories');
}

/**
 * 根据类型获取分类列表
 * @param {string} type - 分类类型
 * @returns {Promise} 指定类型的分类列表
 */
export function getCategoriesByType(type) {
  return get('/categories/type', { type });
}

export default {
  getCategoryById,
  getAllCategories,
  getCategoriesByType
};
