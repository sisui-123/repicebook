// 食材相关API
import { get } from './http';

/**
 * 获取单个食材详情
 * @param {string} ingredientId - 食材ID
 * @returns {Promise} 食材详情
 */
export function getIngredientById(ingredientId) {
  return get(`/ingredients/${ingredientId}`);
}

/**
 * 获取所有食材
 * @returns {Promise} 所有食材列表
 */
export function getAllIngredients() {
  return get('/ingredients');
}

/**
 * 根据关键词搜索食材
 * @param {string} keyword - 搜索关键词
 * @returns {Promise} 搜索结果食材列表
 */
export function searchIngredients(keyword) {
  return get('/ingredients/search', { keyword });
}

export default {
  getIngredientById,
  getAllIngredients,
  searchIngredients
};
