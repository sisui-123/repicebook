// 菜谱相关API
import { get, post, put, del } from './http';

/**
 * 获取单个菜谱详情
 * @param {string} recipeId - 菜谱ID
 * @returns {Promise} 菜谱详情，包含步骤和配料
 */
export function getRecipeById(recipeId) {
  return get(`/recipes/${recipeId}`);
}

/**
 * 获取所有菜谱
 * @returns {Promise} 菜谱列表
 */
export function getAllRecipes() {
  return get('/recipes');
}

/**
 * 获取指定用户的菜谱
 * @param {string} userId - 用户ID
 * @returns {Promise} 该用户的菜谱列表
 */
export function getUserRecipes(userId) {
  return get(`/recipes/user/${userId}`);
}

/**
 * 获取指定分类下的菜谱
 * @param {string} categoryId - 分类ID
 * @returns {Promise} 该分类下的菜谱列表
 */
export function getCategoryRecipes(categoryId) {
  return get(`/recipes/category/${categoryId}`);
}

/**
 * 获取指定烹饪类型下的菜谱
 * @param {string} cuisineTypeId - 烹饪类型ID
 * @returns {Promise} 该烹饪类型下的菜谱列表
 */
export function getCuisineTypeRecipes(cuisineTypeId) {
  return get(`/recipes/cuisine-type/${cuisineTypeId}`);
}

/**
 * 搜索菜谱
 * @param {string} keyword - 搜索关键词
 * @returns {Promise} 搜索结果菜谱列表
 */
export function searchRecipes(keyword) {
  return get('/recipes/search', { keyword });
}

/**
 * 获取最新菜谱
 * @param {number} limit - 返回数量限制，默认为10
 * @returns {Promise} 最新的菜谱列表
 */
export function getLatestRecipes(limit = 10) {
  return get('/recipes/latest', { limit });
}

/**
 * 创建新菜谱
 * @param {Object} recipeData - 菜谱数据
 * @returns {Promise} 创建的菜谱详情
 */
export function createRecipe(recipeData) {
  return post('/recipes', recipeData);
}

/**
 * 更新菜谱
 * @param {string} recipeId - 菜谱ID
 * @param {Object} recipeData - 更新的菜谱数据
 * @returns {Promise} 更新后的菜谱详情
 */
export function updateRecipe(recipeId, recipeData) {
  return put(`/recipes/${recipeId}`, recipeData);
}

/**
 * 删除菜谱
 * @param {string} recipeId - 要删除的菜谱ID
 * @returns {Promise} 删除结果
 */
export function deleteRecipe(recipeId) {
  return del(`/recipes/${recipeId}`);
}

export default {
  getRecipeById,
  getAllRecipes,
  getUserRecipes,
  getCategoryRecipes,
  getCuisineTypeRecipes,
  searchRecipes,
  getLatestRecipes,
  createRecipe,
  updateRecipe,
  deleteRecipe
};
